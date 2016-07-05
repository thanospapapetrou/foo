package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.FunckyType;

/**
 * Exception thrown when encountering an invalid argument.
 * 
 * @author thanos
 */
public class InvalidArgumentException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String INVALID_ARGUMENT = "Function %1$s expects an argument of type %2$s but %3$s has type %4$s";
	private static final String NULL_FUNCTION = "Function must not be null";
	private static final String NULL_DOMAIN = "Domain must not be null";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_ARGUMENT_TYPE = "Argument type must not be null";

	/**
	 * Construct a new invalid argument exception.
	 * 
	 * @param function
	 *            the function expression
	 * @param domain
	 *            the domain type of the function expression
	 * @param argument
	 *            the invalid argument expression
	 * @param argumentType
	 *            the type of the invalid argument expression
	 */
	public InvalidArgumentException(final Expression function, final FunckyType domain, final Expression argument, final FunckyType argumentType) {
		super(String.format(INVALID_ARGUMENT, Objects.requireNonNull(function, NULL_FUNCTION), Objects.requireNonNull(domain, NULL_DOMAIN), Objects.requireNonNull(argument, NULL_ARGUMENT), Objects.requireNonNull(argumentType, NULL_ARGUMENT_TYPE)), argument.getFileName(), argument.getLineNumber());
	}
}
