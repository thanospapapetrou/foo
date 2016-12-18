package com.github.thanospapapetrou.funcky.runtime.functors;

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
	 * @param arguments
	 *            the arguments to apply this functor to
	 * @return the literal result of applying this functor to the given arguments
	 * @throws ScriptException
	 *             if any errors occur while applying this functor to the given arguments
	 */
	public Literal apply(final Expression... arguments) throws ScriptException;
}
