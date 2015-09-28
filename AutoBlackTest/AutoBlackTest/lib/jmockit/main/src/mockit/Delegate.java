/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

/**
 * An empty interface to be used with the {@link Expectations#result} field or the {@link Invocations#with(Delegate)}
 * method, allowing test code to define invocation results (return values or thrown exceptions) or argument matching
 * rules based on test-specific logic.
 * <p/>
 * When used with the {@code result} field, the following rules apply to the implementation class:
 * <ol>
 * <li>A method matching the signature of the recorded method/constructor call must be defined.
 * That is, they should have the same name and parameters. In the case of delegating a constructor, a delegate
 * <em>method</em> should still be used, with name "$init".</li>
 * <li>Alternatively, for the common case where the delegate implementation class defines a <em>single</em> method, the
 * name is allowed to be different from the recorded method.
 * The parameters should still match, though. Besides giving more flexibility, this ability also prevents the test from
 * breaking in case the recorded method is renamed.
 * If the delegate class defines two or more methods (including {@code private} ones, if any), then exactly one
 * of them <em>must</em> match both the name and the parameters of the recorded method.</li>
 * <li>The result of a delegate method execution can be any return value compatible with the recorded method's return
 * type, or a thrown error/exception.</li>
 * </ol>
 * When used with the {@code with} method, the implementing class must have a single non-{@code private} method which
 * returns a {@code boolean}, being {@code true} for a succesfully matched argument or {@code false} otherwise.
 * <p/>
 * At replay time, when the mocked method/constructor is called the corresponding "delegate" method will be called.
 * The arguments passed to the delegate method will be the same as those received by the recorded invocation during
 * replay.
 * Even {@code static} methods in the mocked type can have delegates, which in turn can be static or not.
 * The same is true for {@code private}, {@code final}, and {@code native} methods.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/BehaviorBasedTesting.html#delegates">In the Tutorial</a>
 * <p/>
 * Sample tests:
 * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/easymock/test/org/easymock/samples/DocumentManager_JMockit_Test.java">DocumentManager_JMockit_Test</a>
 *
 * @param <T> the type of the argument to be matched, when used with the {@code with(Delegate&lt;T>)} method
 */
public interface Delegate<T>
{
}
