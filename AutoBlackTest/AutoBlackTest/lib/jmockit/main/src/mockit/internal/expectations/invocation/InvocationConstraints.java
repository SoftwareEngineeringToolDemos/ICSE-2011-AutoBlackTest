/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

public final class InvocationConstraints
{
   public int minInvocations;
   int maxInvocations;
   public int invocationCount;

   public InvocationConstraints(boolean nonStrictInvocation)
   {
      setDefaultLimits(nonStrictInvocation);
   }

   public InvocationConstraints(InvocationConstraints other)
   {
      setLimits(other.minInvocations, other.maxInvocations);
      invocationCount = other.invocationCount;
   }

   public void setDefaultLimits(boolean nonStrictInvocation)
   {
      setLimits(nonStrictInvocation ? 0 : 1, nonStrictInvocation ? -1 : 1);
   }

   public void setLimits(int minInvocations, int maxInvocations)
   {
      this.minInvocations = minInvocations;
      this.maxInvocations = maxInvocations;
   }

   void adjustMaxInvocations(int expectedInvocationCount)
   {
      if (maxInvocations > 0 && maxInvocations < expectedInvocationCount) {
         maxInvocations = expectedInvocationCount;
      }
   }

   void setUnlimitedMaxInvocations()
   {
      maxInvocations = -1;
   }

   public boolean incrementInvocationCount()
   {
      invocationCount++;
      return invocationCount == maxInvocations;
   }

   public void addInvocationCount(InvocationConstraints other)
   {
      invocationCount += other.invocationCount;
   }

   public boolean isInvocationCountLessThanMinimumExpected()
   {
      return invocationCount < minInvocations;
   }

   public boolean isInvocationCountMoreThanMaximumExpected()
   {
      return maxInvocations >= 0 && invocationCount > maxInvocations;
   }

   public boolean isInvocationCountInExpectedRange()
   {
      return minInvocations <= invocationCount && (invocationCount <= maxInvocations || maxInvocations < 0);
   }

   public Error verify(ExpectedInvocation invocation, int minInvocations, int maxInvocations)
   {
      Error error = verifyLowerLimit(invocation, minInvocations);

      return error != null ? error : verifyUpperLimit(invocation, maxInvocations);
   }

   private Error verifyLowerLimit(ExpectedInvocation invocation, int minInvocations)
   {
      return invocationCount < minInvocations ? errorForMissingExpectations(invocation, minInvocations) : null;
   }

   private Error errorForMissingExpectations(ExpectedInvocation invocation, int minInvocations)
   {
      return invocation.errorForMissingInvocations(minInvocations - invocationCount) ;
   }

   public Error errorForMissingExpectations(ExpectedInvocation invocation)
   {
      return invocation.errorForMissingInvocations(minInvocations - invocationCount) ;
   }

   private Error verifyUpperLimit(ExpectedInvocation invocation, int maxInvocations)
   {
      if (maxInvocations >= 0) {
         int n = invocationCount - maxInvocations;

         if (n > 0) {
            return invocation.errorForUnexpectedInvocations(n);
         }
      }

      return null;
   }
}
