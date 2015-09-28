/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Indicates a class to be tested in isolation from selected dependencies, with optional automatic instantiation and/or
 * automatic injection of dependencies.
 * This annotation is only applicable to instance fields of a test class.
 * <p/>
 * If the tested field is not {@code final} and its value remains {@code null} at the time a test method is about to be
 * executed, then a suitable instance of the tested class is created and assigned to the field.
 * The instantiation will only be attempted if the tested class has at least one constructor with the same accessibility
 * as the class: i.e. either a <em>public</em> constructor in a public class, or a <em>non-private</em> constructor in a
 * <em>non-public</em> class.
 * Constructor injection will take place, provided all of the constructor parameters (if any) can be satisfied with the
 * values of available {@linkplain Injectable injectable} fields and/or injectable test method parameters.
 * If the tested class has more than one satisfiable constructor, the one with most parameters is chosen.
 * The matching between <em>injectable</em> fields/parameters and <em>constructor</em> parameters is done by
 * <em>type</em> when there is only one parameter of a given type; otherwise, by type <em>and name</em>.
 * <p/>
 * Whenever the tested object is created automatically, <em>field injection</em> is also performed.
 * Only non-<code>final</code> instance fields which remain uninitialized at this time are considered, between those
 * declared in the tested class itself or in one of its super-classes.
 * For each such <em>target</em> field, the value of an still unused {@linkplain Injectable injectable} field or test
 * method parameter of the <em>same</em> type is assigned.
 * Multiple target fields of the same type can be injected from separate injectables, provided each target field has the
 * same name as an available injectable field/parameter of that type.
 * Finally, if there is no matching and available injectable value for a given target field, it is left unassigned.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tested
{
}
