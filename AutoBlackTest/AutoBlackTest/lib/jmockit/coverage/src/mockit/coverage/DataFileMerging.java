/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage;

import java.io.*;
import java.util.*;

import mockit.coverage.data.*;

final class DataFileMerging
{
   private final List<File> inputFiles;

   DataFileMerging(String[] inputPaths)
   {
      inputFiles = new ArrayList<File>(inputPaths.length);

      for (String path : inputPaths) {
         addInputFileToList(path.trim());
      }
   }

   private void addInputFileToList(String path)
   {
      if (path.length() > 0) {
         File inputFile = new File(path);

         if (inputFile.isDirectory()) {
            inputFile = new File(inputFile, "coverage.ser");
         }

         inputFiles.add(inputFile);
      }
   }

   CoverageData merge() throws ClassNotFoundException, IOException
   {
      CoverageData mergedData = null;

      for (File inputFile : inputFiles) {
         if (inputFile.exists()) {
            CoverageData existingData = CoverageData.readDataFromFile(inputFile);

            if (mergedData == null) {
               mergedData = existingData;
            }
            else {
               mergedData.merge(existingData);
            }
         }
      }

      if (mergedData == null) {
         throw new IllegalArgumentException("No input \"coverage.ser\" files found");
      }

      return mergedData;
   }
}