/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

public final class NullityMatcher implements ArgumentMatcher
{
   public static final ArgumentMatcher INSTANCE = new NullityMatcher();

   private NullityMatcher() {}
   public boolean matches(Object argValue) { return argValue == null; }
   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch) { argumentMismatch.append("null"); }
}

