package com.github.thanospapapetrou.funcky.runtime.functors;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;

/**
 * Interface representing a functor.
 * 
 * @author thanos
 */
public interface ApplicableFunctor {
	/**
	 * Apply this functor to the given arguments.
	 * 
	 * @param context
	 *            the context in which to evaluate the application
	 * @param arguments
	 *            the arguments to apply this functor to
	 * @return the literal result of applying this functor to the given arguments
	 * @throws ScriptException
	 *             if any errors occur while applying this functor to the given arguments
	 */
	public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException;
}
