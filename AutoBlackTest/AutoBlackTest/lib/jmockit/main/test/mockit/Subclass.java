/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

@SuppressWarnings({"UnusedDeclaration"})
final class Subclass extends BaseClass
{
   final int INITIAL_VALUE = new Random().nextInt();
   final int initialValue = -1;

   private static final Integer constantField = 123;
   private static final String compileTimeConstantField = "test";
   static final boolean FLAG = false;

   private static StringBuilder buffer;
   private static char static1;
   private static char static2;

   static StringBuilder getBuffer() { return buffer; }
   static void setBuffer(StringBuilder buffer) { Subclass.buffer = buffer; }

   private String stringField;
   private int intField;
   private int intField2;
   private List<String> listField;

   Subclass() { intField = -1; }
   Subclass(int a, String b) { intField = a; stringField = b; }
   Subclass(String... args) { listField = Arrays.asList(args); }
   Subclass(List<String> list) { listField = list; }

   private static Boolean anStaticMethod() { return true; }
   private static void staticMethod(short s, String str, Boolean b) {}
   private static String staticMethod(short s, StringBuilder str, boolean b) { return String.valueOf(str); }

   private long aMethod() { return 567L; }
   private void instanceMethod(short s, String str, Boolean b) {}
   private String instanceMethod(short s, StringBuilder str, boolean b) { return String.valueOf(str); }

   int getIntField() { return intField; }
   void setIntField(int intField) { this.intField = intField; }

   int getIntField2() { return intField2; }
   void setIntField2(int intField2) { this.intField2 = intField2; }

   String getStringField() { return stringField; }
   void setStringField(String stringField) { this.stringField = stringField; }

   List<String> getListField() { return listField; }
   void setListField(List<String> listField) { this.listField = listField; }

   private final class InnerClass
   {
      private InnerClass() {}
      private InnerClass(boolean b, Long l, String s) {}
      private InnerClass(List<String> list) {}
   }
}
