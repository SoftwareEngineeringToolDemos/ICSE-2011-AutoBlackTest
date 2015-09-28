/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage.dataItems;

import java.io.*;

public abstract class FieldData implements Serializable
{
   private static final long serialVersionUID = 8565599590976858508L;

   int readCount;
   int writeCount;
   Boolean covered;

   private void writeObject(ObjectOutputStream out) throws IOException
   {
      isCovered();
      out.defaultWriteObject();
   }

   public final int getReadCount() { return readCount; }
   public final int getWriteCount() { return writeCount; }

   public final boolean isCovered()
   {
      if (covered == null) {
         covered = false;
         markAsCoveredIfNoUnreadValuesAreLeft();
      }

      return covered;
   }

   abstract void markAsCoveredIfNoUnreadValuesAreLeft();

   final void addCountsFromPreviousTestRun(FieldData previousInfo)
   {
      readCount += previousInfo.readCount;
      writeCount += previousInfo.writeCount;
      covered = isCovered() || previousInfo.covered;
   }
}
