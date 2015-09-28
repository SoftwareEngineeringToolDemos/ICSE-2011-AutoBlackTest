/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage.paths;

import java.util.*;

import mockit.coverage.*;
import mockit.coverage.data.*;

public final class PerFilePathCoverage implements PerFileCoverage
{
   private static final long serialVersionUID = 6075064821486644269L;

   public final Map<Integer, MethodCoverageData> firstLineToMethodData =
      new LinkedHashMap<Integer, MethodCoverageData>();

   // Computed on demand, the first time the coverage percentage is requested:
   private transient int totalPaths;
   private transient int coveredPaths;

   public void addMethod(MethodCoverageData methodData)
   {
      firstLineToMethodData.put(methodData.getFirstLineInBody(), methodData);
   }

   public void registerExecution(int firstLineInMethodBody, int node)
   {
      MethodCoverageData methodData = firstLineToMethodData.get(firstLineInMethodBody);

      if (methodData != null) {
         methodData.markNodeAsReached(node);
      }
   }

   public int getTotalItems() { return totalPaths; }
   public int getCoveredItems() { return coveredPaths; }

   public int getCoveragePercentage()
   {
      if (firstLineToMethodData.isEmpty()) {
         return -1;
      }

      Collection<MethodCoverageData> methods = firstLineToMethodData.values();
      totalPaths = coveredPaths = 0;

      for (MethodCoverageData method : methods) {
         totalPaths += method.getTotalPaths();
         coveredPaths += method.getCoveredPaths();
      }

      return CoveragePercentage.calculate(coveredPaths, totalPaths);
   }

   public void reset()
   {
      for (MethodCoverageData methodData : firstLineToMethodData.values()) {
         methodData.reset();
      }

      totalPaths = coveredPaths = 0;
   }

   public void mergeInformation(PerFilePathCoverage previousCoverage)
   {
      Map<Integer, MethodCoverageData> previousInfo = previousCoverage.firstLineToMethodData;

      for (Map.Entry<Integer, MethodCoverageData> firstLineAndInfo : firstLineToMethodData.entrySet()) {
         Integer firstLine = firstLineAndInfo.getKey();
         MethodCoverageData previousPathInfo = previousInfo.get(firstLine);

         if (previousPathInfo != null) {
            MethodCoverageData pathInfo = firstLineAndInfo.getValue();
            pathInfo.addCountsFromPreviousTestRun(previousPathInfo);
         }
      }

      for (Map.Entry<Integer, MethodCoverageData> firstLineAndInfo : previousInfo.entrySet()) {
         Integer firstLine = firstLineAndInfo.getKey();

         if (!firstLineToMethodData.containsKey(firstLine)) {
            firstLineToMethodData.put(firstLine, firstLineAndInfo.getValue());
         }
      }
   }
}
