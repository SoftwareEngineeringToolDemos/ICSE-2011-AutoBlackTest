/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples;

import java.util.*;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;
import powermock.examples.dependencymanagement.*;
import powermock.examples.domain.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/AbstractFactory/src/test/java/powermock/examples/MyServiceUserTest.java">PowerMock version</a>
 */
public final class MyServiceUser_JMockit_Test
{
   MyServiceUser tested;

   @Cascading DependencyManager dependencyManagerMock;

   @Before
   public void setUp()
   {
      tested = new MyServiceUser();
   }

   @Test
   public void testGetNumberOfPersons()
   {
      final Set<Person> persons = new HashSet<Person>();
      persons.add(new Person("Rogério", "Liesenfeld", "MockStreet"));
      persons.add(new Person("John", "Doe", "MockStreet2"));

      new Expectations()
      {
         {
            DependencyManager.getInstance().getMyService().getAllPersons(); result = persons;
         }
      };

      int numberOfPersons = tested.getNumberOfPersons();

      assertEquals(2, numberOfPersons);
   }
}
