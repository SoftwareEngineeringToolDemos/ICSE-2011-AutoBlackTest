/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package integrationTests;

import java.lang.reflect.*;
import java.util.*;

import static java.lang.reflect.Modifier.*;
import org.junit.*;

import mockit.coverage.data.*;
import mockit.coverage.dataItems.*;
import mockit.coverage.lines.*;
import mockit.coverage.paths.*;

public class CoverageTest extends Assert
{
   static final Map<String, FileCoverageData> data = CoverageData.instance().getFileToFileDataMap();
   protected static FileCoverageData fileData;
   protected MethodCoverageData methodData;
   private int currentPathIndex = -1;
   private static String testedClassSimpleName;

   @Before
   public void findCoverageData() throws Exception
   {
      Field testedField = getClass().getDeclaredField("tested");
      Class<?> testedClass = testedField.getType();

      String classFilePath = testedClass.getName().replace('.', '/') + ".java";
      fileData = data.get(classFilePath);
      assertNotNull("FileCoverageData not found for " + classFilePath);

      if (!testedClass.isEnum() && !isAbstract(testedClass.getModifiers()) && !isFinal(testedField.getModifiers())) {
         testedField.setAccessible(true);

         if (testedField.get(this) == null) {
            //noinspection ClassNewInstance
            Object newTestedInstance = testedClass.newInstance();

            testedField.set(this, newTestedInstance);
         }
      }

      testedClassSimpleName = testedClass.getSimpleName();
   }

   protected final void assertLines(int startingLine, int endingLine, int expectedLinesExecuted)
   {
      SortedMap<Integer, LineCoverageData> lineToLineData = fileData.lineCoverageInfo.lineToLineData;
      assertTrue("Starting line not found", lineToLineData.containsKey(startingLine));
      assertTrue("Ending line not found", lineToLineData.containsKey(endingLine));

      int linesExecuted = 0;

      for (int line = startingLine; line <= endingLine; line++) {
         LineCoverageData lineData = lineToLineData.get(line);

         if (lineData != null && lineData.getExecutionCount() > 0) {
            linesExecuted++;
         }
      }

      assertEquals("Unexpected number of lines executed:", expectedLinesExecuted, linesExecuted);
   }

   protected final void assertLine(
      int line, int expectedSegments, int expectedCoveredSegments, int expectedExecutionCount)
   {
      LineCoverageData lineData = fileData.lineCoverageInfo.lineToLineData.get(line);
      assertNotNull("Not an executable line", lineData);
      assertEquals("Segments:", expectedSegments, lineData.getNumberOfSegments());
      assertEquals("Covered segments:", expectedCoveredSegments, lineData.getNumberOfCoveredSegments());
      assertEquals("Execution count:", expectedExecutionCount, lineData.getExecutionCount());
   }

   protected final void findMethodData(int firstLineOfMethodBody, String methodName)
   {
      methodData = fileData.pathCoverageInfo.firstLineToMethodData.get(firstLineOfMethodBody);
      assertNotNull("Method not found with first line " + firstLineOfMethodBody, methodData);
      assertEquals(
         "No method with name \"" + methodName + "\" found with first line at " +
         firstLineOfMethodBody, methodName, methodData.methodName);
   }

   protected final void assertPaths(int expectedPaths, int expectedCoveredPaths, int expectedExecutionCount)
   {
      assertEquals("Number of paths:", expectedPaths, methodData.getTotalPaths());
      assertEquals("Number of covered paths:", expectedCoveredPaths, methodData.getCoveredPaths());
      assertEquals("Execution count for all paths:", expectedExecutionCount, methodData.getExecutionCount());
   }

   protected final void assertMethodLines(int startingLine, int endingLine)
   {
      assertEquals(startingLine, methodData.getFirstLineInBody());
      assertEquals(endingLine, methodData.getLastLineInBody());
   }

   protected final Path assertPath(int expectedNodeCount, int expectedExecutionCount)
   {
      int i = currentPathIndex + 1;
      currentPathIndex = -1;

      Path path = methodData.paths.get(i);
      assertEquals("Path node count:", expectedNodeCount, path.getNodes().size());
      assertEquals("Path execution count:", expectedExecutionCount, path.getExecutionCount());

      currentPathIndex = i;
      return path;
   }

   protected final void assertRegularPath(int expectedNodeCount, int expectedExecutionCount)
   {
      Path path = assertPath(expectedNodeCount, expectedExecutionCount);
      assertFalse("Path is shadowed", path.isShadowed());
   }

   protected final void assertShadowedPath(int expectedNodeCount, int expectedExecutionCount)
   {
      Path path = assertPath(expectedNodeCount, expectedExecutionCount);
      assertTrue("Path is not shadowed", path.isShadowed());
   }

   @After
   public final void verifyThatAllPathsWereAccountedFor()
   {
      int nextPathIndex = currentPathIndex + 1;

      if (methodData != null && nextPathIndex > 0) {
         assertEquals("Path " + nextPathIndex + " was not verified;", nextPathIndex, methodData.paths.size());
      }
   }

   protected final void assertFieldIgnored(String fieldName)
   {
      String fieldId = testedClassSimpleName + '.' + fieldName;
      assertFalse(
         "Field " + fieldName + " should not have static coverage data",
         fileData.dataCoverageInfo.staticFieldsData.containsKey(fieldId));
      assertFalse(
         "Field " + fieldName + " should not have instance coverage data", 
         fileData.dataCoverageInfo.instanceFieldsData.containsKey(fieldId));
   }

   protected static void assertStaticFieldCovered(String fieldName)
   {
      assertTrue("Static field " + fieldName + " should be covered", isStaticFieldCovered(fieldName));
   }

   private static boolean isStaticFieldCovered(String fieldName)
   {
      String classAndFieldNames = testedClassSimpleName + '.' + fieldName;
      StaticFieldData staticFieldData = fileData.dataCoverageInfo.staticFieldsData.get(classAndFieldNames);

      return staticFieldData.isCovered();
   }

   protected static void assertStaticFieldUncovered(String fieldName)
   {
      assertFalse("Static field " + fieldName + " should not be covered", isStaticFieldCovered(fieldName));
   }

   protected static void assertInstanceFieldCovered(String fieldName)
   {
      assertTrue("Instance field " + fieldName + " should be covered", isInstanceFieldCovered(fieldName));
   }

   private static boolean isInstanceFieldCovered(String fieldName)
   {
      return getInstanceFieldData(fieldName).isCovered();
   }

   private static InstanceFieldData getInstanceFieldData(String fieldName)
   {
      String classAndFieldNames = testedClassSimpleName + '.' + fieldName;
      return fileData.dataCoverageInfo.instanceFieldsData.get(classAndFieldNames);
   }

   protected static void assertInstanceFieldUncovered(String fieldName)
   {
      assertFalse("Instance field " + fieldName + " should not be covered", isInstanceFieldCovered(fieldName));
   }

   protected static void assertInstanceFieldUncovered(String fieldName, Object... uncoveredInstances)
   {
      String msg = "Instance field " + fieldName + " should not be covered";
      InstanceFieldData fieldData = getInstanceFieldData(fieldName);
      List<Integer> ownerInstances = fieldData.getOwnerInstancesWithUnreadAssignments();

      assertEquals(msg, uncoveredInstances.length, ownerInstances.size());

      for (Object uncoveredInstance : uncoveredInstances) {
         Integer instanceId = System.identityHashCode(uncoveredInstance);
         assertTrue(msg, ownerInstances.contains(instanceId));
      }
   }

   protected static void verifyDataCoverage(int expectedItems, int expectedCoveredItems, int expectedCoverage)
   {
      assertEquals("Total data items:", expectedItems, fileData.dataCoverageInfo.getTotalItems());
      assertEquals("Covered data items:", expectedCoveredItems, fileData.dataCoverageInfo.getCoveredItems());
      assertEquals("Data coverage:", expectedCoverage, fileData.dataCoverageInfo.getCoveragePercentage());
   }
}