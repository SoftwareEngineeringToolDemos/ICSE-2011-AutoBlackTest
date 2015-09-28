/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package integrationTests.textFile;

import java.io.*;
import java.util.*;

import org.junit.*;

import mockit.*;

import integrationTests.textFile.TextFile.*;
import static org.junit.Assert.*;

public final class TextFileUsingExpectationsTest
{
   @Test
   public void createTextFile() throws Exception
   {
      new Expectations()
      {
         DefaultTextReader reader;

         // Records TextFile#TextFile(String, int):
         {
            new DefaultTextReader("file");
         }
      };

      new TextFile("file", 0);
   }

   @Test
   public void createTextFileByStubbingOutTheTextReaderClass() throws Exception
   {
      Mockit.stubOutClass(DefaultTextReader.class.getName());

      new TextFile("file", 0);
   }

   @Test
   public void createTextFileByCapturingTheTextReaderClassThroughItsBaseType() throws Exception
   {
      new Expectations()
      {
         @Capturing
         TextReader reader;
      };

      new TextFile("file", 0);
   }

   @Test
   public void createTextFileByMockingTheTextReaderClassThroughItsName() throws Exception
   {
      new Expectations()
      {
         @Mocked(realClassName = "integrationTests.textFile.TextFile$DefaultTextReader")
         final Object reader = null;
      };

      new TextFile("file", 0);
   }

   @Test
   public void createTextFileByRecordingTheConstructorInvocationThroughReflection() throws Exception
   {
      new Expectations()
      {
         @Mocked(realClassName = "integrationTests.textFile.TextFile$DefaultTextReader")
         final Object reader =
            newInstance("integrationTests.textFile.TextFile$DefaultTextReader", "file");

         {
            invoke(reader, "close");
         }
      };

      new TextFile("file", 0).closeReader();
   }

   @Test
   public void createTextFileByRecordingNonStrictInvocationsThroughReflection() throws Exception
   {
      new Expectations()
      {
         @NonStrict @Mocked(realClassName = "integrationTests.textFile.TextFile$DefaultTextReader")
         Object reader;

         {
            invoke(reader, "close"); times = 1;
         }
      };

      new TextFile("file", 0).closeReader();
   }

   @Test
   public void parseTextFileUsingConcreteClass() throws Exception
   {
      new Expectations()
      {
         final DefaultTextReader reader = new DefaultTextReader("file");

         // Records TextFile#parse():
         {
            reader.skip(200); result = 200L;
            reader.readLine(); returns("line1", "another,line", null);
            reader.close();
         }
      };

      TextFile textFile = new TextFile("file", 200);
      List<String[]> result = textFile.parse();

      assertResultFromTextFileParsing(result);
   }

   private void assertResultFromTextFileParsing(List<String[]> result)
   {
      assertEquals(2, result.size());
      String[] line1 = result.get(0);
      assertEquals(1, line1.length);
      assertEquals("line1", line1[0]);
      String[] line2 = result.get(1);
      assertEquals(2, line2.length);
      assertEquals("another", line2[0]);
      assertEquals("line", line2[1]);
   }

   @Test
   public void parseTextFileUsingInterface(final TextReader reader) throws Exception
   {
      new Expectations()
      {
         // Records TextFile#parse():
         {
            reader.skip(200); result = 200L;
            reader.readLine(); returns("line1", "another,line", null);
            reader.close();
         }
      };

      // Replays recorded invocations while verifying expectations:
      TextFile textFile = new TextFile(reader, 200);
      List<String[]> result = textFile.parse();

      // Verifies result:
      assertResultFromTextFileParsing(result);
   }

   @Test
   public void parseTextFileUsingBufferedReader() throws Exception
   {
      new Expectations()
      {
         final FileReader fileReader = null;
         BufferedReader reader;

         // Records TextFile#TextFile(String):
         {
            new BufferedReader(new FileReader("file"));
         }

         // Records TextFile#parse():
         {
            reader.skip(0); result = 0L;
            reader.readLine(); result = "line1"; result = "another,line"; result = null;
            reader.close();
         }
      };

      TextFile textFile = new TextFile("file");
      List<String[]> result = textFile.parse();

      assertResultFromTextFileParsing(result);
   }
}
