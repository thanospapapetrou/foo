package com.github.thanospapapetrou.funcky.runtime.functors;

import com.github.thanospapapetrou.funcky.runtime.literals.Number;

/**
 * Interface representing a two argument arithmetic operator.
 * 
 * @author thanos
 */
public interface ApplicableTwoArgumentArithmeticOperator {
	/**
	 * Apply this operator to the given arguments.
	 * 
	 * @param argument1
	 *            the first argument to apply this operator to
	 * @param argument2
	 *            the second argument to apply this operator to
	 * @return the result of applying this operator to the given arguments
	 */
	public Number apply(final Number argument1, final Number argument2);
}
