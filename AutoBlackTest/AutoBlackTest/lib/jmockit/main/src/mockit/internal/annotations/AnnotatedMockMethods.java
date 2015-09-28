/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.annotations;

import java.util.*;

import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * A container for the mock methods "collected" from a mock class, separated in two sets: one with all the mock methods,
 * and another with just the subset of static methods.
 */
final class AnnotatedMockMethods
{
   private final Class<?> realClass;
   private final List<MockMethod> methods;
   private String mockClassInternalName;
   private boolean isInnerMockClass;
   private boolean withItField;
   private List<MockState> mockStates;
   Class<?> classWithMethodToSelectSubclasses;

   final class MockMethod
   {
      final String name;
      final String desc;
      final boolean isStatic;
      final boolean hasInvocationParameter;
      String mockedMethodDesc;
      private final String mockDescWithoutInvocationParameter;
      private GenericSignature mockSignature;
      private int indexForMockState;

      private MockMethod(String nameAndDesc, boolean isStatic)
      {
         int p = nameAndDesc.indexOf('(');
         name = nameAndDesc.substring(0, p);
         desc = nameAndDesc.substring(p);
         this.isStatic = isStatic;
         hasInvocationParameter = desc.startsWith("(Lmockit/Invocation;");
         mockDescWithoutInvocationParameter = hasInvocationParameter ? '(' + desc.substring(20) : desc;
         indexForMockState = -1;
      }

      boolean isMatch(String name, String desc, String signature)
      {
         if (this.name.equals(name)) {
            if (hasMatchingParameters(desc, signature)) {
               mockedMethodDesc = desc;
               return true;
            }
         }

         return false;
      }

      private boolean hasMatchingParameters(String desc, String signature)
      {
         if (signature == null || !signature.contains("TT;")) {
            return mockDescWithoutInvocationParameter.equals(desc);
         }

         if (mockSignature == null) {
            mockSignature = new GenericSignature(mockDescWithoutInvocationParameter);
         }

         return mockSignature.satisfiesGenericSignature(signature);
      }

      boolean isForGenericMethod() { return mockSignature != null; }

      Class<?> getRealClass() { return realClass; }
      String getMockNameAndDesc() { return name + desc; }
      boolean isForConstructor() { return "$init".equals(name); }
      boolean hasMatchingRealMethod() { return mockedMethodDesc != null; }
      int getIndexForMockState() { return indexForMockState; }

      boolean isReentrant() { return indexForMockState >= 0 && mockStates.get(indexForMockState).isReentrant(); }
      boolean isDynamic() { return isReentrant() || hasInvocationParameter && isForConstructor(); }

      String errorMessage(String quantifier, int numExpectedInvocations, int timesInvoked)
      {
         String nameAndDesc = getMockNameAndDesc();
         return
            "Expected " + quantifier + ' ' + numExpectedInvocations + " invocation(s) of " +
            new MethodFormatter(mockClassInternalName, nameAndDesc) + ", but was invoked " + timesInvoked + " time(s)";
      }

      @Override
      public boolean equals(Object obj)
      {
         MockMethod other = (MockMethod) obj;
         return realClass == other.getRealClass() && name.equals(other.name) && desc.equals(other.desc);
      }

      @Override
      public int hashCode()
      {
         return 31 * (31 * realClass.hashCode() + name.hashCode()) + desc.hashCode();
      }
   }

   private static final class GenericSignature
   {
      private final List<String> parameters;

      GenericSignature(String signature)
      {
         int p = signature.indexOf('(');
         int q = signature.indexOf(')');
         String semicolonSeparatedParameters = signature.substring(p + 1, q);
         parameters = new ArrayList<String>(4);

         for (int i = 0; i < semicolonSeparatedParameters.length(); i++) {
            char c = semicolonSeparatedParameters.charAt(i);
            String parameter;

            if (c == 'L' || c == 'T' || c == '[') {
               int j = i + 1;

               do {
                  c = semicolonSeparatedParameters.charAt(j);
                  j++;
               } while (c != ';' && c != '<');

               parameter = semicolonSeparatedParameters.substring(i, j - 1);

               if (c == '<') {
                  int angleBracketDepth = 1;

                  do {
                     c = semicolonSeparatedParameters.charAt(j);
                     if (c == '>') angleBracketDepth--; else if (c == '<') angleBracketDepth++;
                     j++;
                  } while (angleBracketDepth > 0);
               }

               i = j;
            }
            else {
               parameter = String.valueOf(c);
            }

            parameters.add(parameter);
         }
      }

      boolean satisfiesGenericSignature(String signature)
      {
         GenericSignature genericSignature = new GenericSignature(signature);
         int n = parameters.size();

         if (n != genericSignature.parameters.size()) {
            return false;
         }

         for (int i = 0; i < n; i++) {
            String p1 = genericSignature.parameters.get(i);
            String p2 = parameters.get(i);

            if (p1.charAt(0) != 'T' && !p1.equals(p2)) {
               return false;
            }
         }

         return true;
      }
   }

   AnnotatedMockMethods(Class<?> realClass)
   {
      this.realClass = realClass;
      methods = new ArrayList<MockMethod>();
   }

   MockMethod addMethod(boolean fromSuperClass, String name, String desc, boolean isStatic)
   {
      if (fromSuperClass && isMethodAlreadyAdded(name, desc)) {
         return null;
      }

      String nameAndDesc = name + desc;
      MockMethod mockMethod = new MockMethod(nameAndDesc, isStatic);
      methods.add(mockMethod);
      return mockMethod;
   }

   private boolean isMethodAlreadyAdded(String name, String desc)
   {
      int p = desc.lastIndexOf(')');
      String params = desc.substring(0, p + 1);

      for (MockMethod mockMethod : methods) {
         if (mockMethod.name.equals(name) && mockMethod.desc.startsWith(params)) {
            return true;
         }
      }

      return false;
   }

   void addMockState(MockState mockState)
   {
      if (mockStates == null) {
         mockStates = new ArrayList<MockState>(4);
      }

      mockState.mockMethod.indexForMockState = mockStates.size();
      mockStates.add(mockState);
   }

   /**
    * Verifies if a mock method with the same signature of a given real method was previously collected from the mock
    * class.
    * This operation can be performed only once for any given mock method in this container, so that after the last real
    * method is processed there should be no mock methods left unused in the container.
    */
   MockMethod containsMethod(String name, String desc, String signature)
   {
      for (MockMethod mockMethod : methods) {
         if (mockMethod.isMatch(name, desc, signature)) {
            return mockMethod;
         }
      }

      return null;
   }

   String getMockClassInternalName() { return mockClassInternalName; }
   void setMockClassInternalName(String mockClassInternalName) { this.mockClassInternalName = mockClassInternalName; }

   boolean isInnerMockClass() { return isInnerMockClass; }
   void setInnerMockClass(boolean innerMockClass) { isInnerMockClass = innerMockClass; }

   boolean supportsItField(Class<?> mockedClass) { return withItField && mockedClass == realClass; }
   void setWithItField(boolean withItField) { this.withItField = withItField; }

   boolean hasUnusedMocks()
   {
      for (MockMethod method : methods) {
         if (!method.hasMatchingRealMethod()) {
            return true;
         }
      }

      return false;
   }

   List<String> getUnusedMockSignatures()
   {
      List<String> signatures = new ArrayList<String>(methods.size());

      for (MockMethod mockMethod : methods) {
         if (!mockMethod.hasMatchingRealMethod()) {
            signatures.add(mockMethod.getMockNameAndDesc());
         }
      }

      return signatures;
   }

   void registerMockStates()
   {
      if (mockStates != null) {
         AnnotatedMockStates annotatedMockStates = TestRun.getMockClasses().getMockStates();
         annotatedMockStates.addMockClassAndStates(mockClassInternalName, mockStates);
      }
   }
}
