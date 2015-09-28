/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package jmockit.tutorial.domain;

import java.util.*;
import static java.util.Arrays.*;

import org.apache.commons.mail.*;

import org.junit.*;

import mockit.*;

import jmockit.tutorial.persistence.*;

public final class MyBusinessService_ExpectationsAPI2_Test
{
   @Tested MyBusinessService service;

   @Mocked(stubOutClassInitialization = true) final Database unused = null;
   @Mocked SimpleEmail email;

   final EntityX data = new EntityX(5, "abc", "someone@somewhere.com");

   @Test
   public void doBusinessOperationXyz() throws Exception
   {
      new Expectations() {
         // Any mocked method returning the input field type will return the given field value:
         @Input final List<EntityX> items = asList(new EntityX(1, "AX5", "someone@somewhere.com"));
      };

      service.doBusinessOperationXyz(data);

      new Verifications() {{ Database.persist(data); }};
      new Verifications() {{ email.send(); }};
   }

   @Test(expected = EmailException.class)
   public void doBusinessOperationXyzWithInvalidEmailAddress() throws Exception
   {
      new NonStrictExpectations() {
         // Any mocked method/constructor with this exception in the throws clause will throw it:
         @Input EmailException onInvalidEmail;

         { email.send(); times = 0; }
      };

      service.doBusinessOperationXyz(data);
   }
}
