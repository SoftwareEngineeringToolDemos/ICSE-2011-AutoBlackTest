/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage.standalone;

import java.lang.instrument.*;

import mockit.coverage.*;

public final class Startup
{
   private static Instrumentation instrumentation;
   private static boolean standalone;

   public static void premain(String agentArgs, Instrumentation inst)
   {
      standalone = true;
      instrumentation = inst;

      CoverageControl.create();
      inst.addTransformer(CodeCoverage.create());

      System.out.println();
      System.out.println("JMockit Coverage tool loaded; connect with a JMX client to control operation");
      System.out.println();
   }

   public static Instrumentation instrumentation()
   {
      if (instrumentation == null) {
         instrumentation = mockit.internal.startup.Startup.instrumentation();
      }

      return instrumentation;
   }

   public static boolean isStandalone() { return standalone; }
}
