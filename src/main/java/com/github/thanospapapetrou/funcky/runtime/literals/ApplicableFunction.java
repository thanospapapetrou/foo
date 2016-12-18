package com.github.thanospapapetrou.funcky.runtime.literals;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.runtime.Expression;

/**
 * Interface representing a function.
 * 
 * @author thanos
 */
public interface ApplicableFunction {
	/**
	 * Apply this function to an argument.
	 * 
	 * @param argument
	 *            the argument to apply this function to
	 * @return the literal result of applying this function to the given argument
	 * @throws ScriptException
	 *             if any errors occur while applying this function to the given argument
	 */
	public Literal apply(final Expression argument) throws ScriptException;
}
