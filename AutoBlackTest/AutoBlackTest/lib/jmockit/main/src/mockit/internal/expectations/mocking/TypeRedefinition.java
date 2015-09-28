/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import mockit.external.asm4.*;
import mockit.internal.util.*;

final class TypeRedefinition extends BaseTypeRedefinition
{
   private final Object parentObject;

   TypeRedefinition(Object parentObject, MockedType typeMetadata)
   {
      super(typeMetadata.getClassType());
      this.parentObject = parentObject;
      this.typeMetadata = typeMetadata;
   }

   void redefineTypeForFinalField()
   {
      adjustTargetClassIfRealClassNameSpecified();

      if (targetClass == null || !typeMetadata.injectable && targetClass.isInterface()) {
         throw new IllegalArgumentException(
            "Final mock field must be of a class type, or otherwise the real class must be " +
            "specified through the @Mocked annotation:\n" + typeMetadata.mockId);
      }

      Integer mockedClassId = redefineClassesFromCache();

      if (mockedClassId != null) {
         typeMetadata.buildMockingConfiguration();
         redefineMethodsAndConstructorsInTargetType();
         storeRedefinedClassesInCache(mockedClassId);
      }
   }

   InstanceFactory redefineType()
   {
      adjustTargetClassIfRealClassNameSpecified();
      typeMetadata.buildMockingConfiguration();

      return redefineType(typeMetadata.declaredType);
   }

   private void adjustTargetClassIfRealClassNameSpecified()
   {
      String realClassName = typeMetadata.getRealClassName();

      if (realClassName.length() > 0) {
         targetClass = Utilities.loadClass(realClassName);
      }
   }

   @Override
   ExpectationsModifier createModifier(Class<?> realClass, ClassReader classReader)
   {
      ExpectationsModifier modifier = new ExpectationsModifier(realClass.getClassLoader(), classReader, typeMetadata);

      if (typeMetadata.injectable) {
         modifier.useDynamicMockingForInstanceMethods(typeMetadata);
      }

      return modifier;
   }

   @Override
   String getNameForConcreteSubclassToCreate()
   {
      Package testPackage = parentObject.getClass().getPackage();
      String prefix = testPackage == null ? "" : testPackage.getName() + '.';

      return prefix + Utilities.GENERATED_SUBCLASS_PREFIX + typeMetadata.mockId;
   }
}
