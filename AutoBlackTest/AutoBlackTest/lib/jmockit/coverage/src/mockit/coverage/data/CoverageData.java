/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage.data;

import java.io.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Coverage data captured for all source files exercised during a test run.
 */
public final class CoverageData implements Serializable
{
   private static final long serialVersionUID = -4860004226098360259L;
   private static final CoverageData instance = new CoverageData();

   public static CoverageData instance() { return instance; }

   private boolean withCallPoints;
   private final Map<String, FileCoverageData> fileToFileData = new ConcurrentHashMap<String, FileCoverageData>();

   public boolean isWithCallPoints() { return withCallPoints; }
   public void setWithCallPoints(boolean withCallPoints) { this.withCallPoints = withCallPoints; }

   public Map<String, FileCoverageData> getFileToFileDataMap()
   {
      return Collections.unmodifiableMap(fileToFileData);
   }

   public FileCoverageData addFile(String file)
   {
      FileCoverageData fileData = getFileData(file);

      // For a class with nested/inner classes, a previous class in the same source file may already have been added.
      if (fileData == null) {
         fileData = new FileCoverageData();
         fileToFileData.put(file, fileData);
      }

      return fileData;
   }

   public FileCoverageData getFileData(String file) { return fileToFileData.get(file); }
   public boolean isEmpty() { return fileToFileData.isEmpty(); }
   public void clear() { fileToFileData.clear(); }

   public void reset()
   {
      for (FileCoverageData fileCoverageData : fileToFileData.values()) {
         fileCoverageData.reset();
      }
   }

   public void fillLastModifiedTimesForAllClassFiles()
   {
      for (Iterator<Map.Entry<String, FileCoverageData>> itr = fileToFileData.entrySet().iterator(); itr.hasNext(); ) {
         Map.Entry<String, FileCoverageData> fileAndFileData = itr.next();

         try {
            File coveredClassFile = getClassFile(fileAndFileData.getKey());
            fileAndFileData.getValue().lastModified = coveredClassFile.lastModified();
         }
         catch (ClassNotFoundException ignored) { itr.remove(); }
         catch (NoClassDefFoundError ignored) { itr.remove(); }
      }
   }

   private File getClassFile(String sourceFilePath) throws ClassNotFoundException
   {
      String sourceFilePathNoExt = sourceFilePath.substring(0, sourceFilePath.lastIndexOf('.'));
      String className = sourceFilePathNoExt.replace('/', '.');
      Class<?> coveredClass = Class.forName(className, false, getClass().getClassLoader());
      CodeSource codeSource = coveredClass.getProtectionDomain().getCodeSource();
      String pathToClassFile = codeSource.getLocation().getPath() + sourceFilePathNoExt + ".class";

      return new File(pathToClassFile);
   }

   public static CoverageData readDataFromFile(File dataFile) throws IOException, ClassNotFoundException
   {
      ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)));

      try {
         return (CoverageData) input.readObject();
      }
      finally {
         input.close();
      }
   }

   public void writeDataToFile(File dataFile) throws IOException
   {
      ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));

      try {
         output.writeObject(this);
      }
      finally {
         output.close();
      }
   }

   public void merge(CoverageData previousData)
   {
      withCallPoints |= previousData.withCallPoints;

      for (Map.Entry<String, FileCoverageData> previousFileAndFileData : previousData.fileToFileData.entrySet()) {
         String previousFile = previousFileAndFileData.getKey();
         FileCoverageData previousFileData = previousFileAndFileData.getValue();
         FileCoverageData fileData = fileToFileData.get(previousFile);

         if (fileData == null) {
            fileToFileData.put(previousFile, previousFileData);
         }
         else if (previousFileData.lastModified == fileData.lastModified) {
            fileData.mergeWithDataFromPreviousTestRun(previousFileData);
         }
      }
   }
}
