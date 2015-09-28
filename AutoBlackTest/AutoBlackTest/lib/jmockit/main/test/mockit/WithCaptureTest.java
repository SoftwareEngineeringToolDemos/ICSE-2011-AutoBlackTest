/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.*;

public final class WithCaptureTest
{
   public static class Person {
      private String name;
      private int age;
      public Person() {}
      public Person(String name, int age) { this.name = name; this.age = age; }
      public String getName() { return name; }
      public int getAge() { return age; }
   }

   public interface DAO<T> { void create(T t); }
   static final class PersonDAO implements DAO<Person> { public void create(Person p) {} }

   @Ignore @Test
   public void captureArgumentFromSingleInvocation(final PersonDAO dao)
   {
      Person p = new Person("John", 10);
      dao.create(p);

      new Verifications() {{
         Person created;
         dao.create(created = withCapture());
         assertEquals("John", created.getName());
         assertEquals(10, created.getAge());
      }};
   }

   @Test
   public void captureArgumentFromMultipleInvocations(final PersonDAO dao)
   {
      dao.create(new Person("John", 10));
      dao.create(new Person("Jane", 20));

      new Verifications() {{
         List<Person> created = new ArrayList<Person>();

         dao.create(withCapture(created));
         assertEquals(2, created.size());

         Person first = created.get(0);
         assertEquals("John", first.getName());
         assertEquals(10, first.getAge());

         Person second = created.get(1);
         assertEquals("Jane", second.getName());
         assertEquals(20, second.getAge());
      }};
   }
}
