package com.github.thanospapapetrou.funcky.runtime.exceptions;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.FunctionType;

/**
 * Exception thrown when encountering an invalid argument.
 * 
 * @author thanos
 */
public class InvalidArgumentException extends FunckyException {
	private static final long serialVersionUID = 1L;
	private static final String INVALID_ARGUMENT = "Function %1$s expects an argument of type %2$s but %3$s has type %4$s";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_APPLICATION = "Application must not be null";

	/**
	 * Construct a new invalid argument exception.
	 * 
	 * @param context
	 *            the context in which to evaluate the types of the function and the argument
	 * @param application
	 *            the application that caused this invalid argument exception
	 * @throws InvalidArgumentException
	 *             if any invalid argument is encountered while evaluating types
	 * @throws InvalidFunctionException
	 *             if any invalid function is encountered while evaluating types
	 * @throws UndefinedSymbolException
	 *             if any reference to an undefined symbol is encountered while evaluating types
	 */
	public InvalidArgumentException(final ScriptContext context, final Application application) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super(String.format(INVALID_ARGUMENT, Objects.requireNonNull(application, NULL_APPLICATION).getFunction(), ((FunctionType) application.getFunction().getType(Objects.requireNonNull(context, NULL_CONTEXT))).getDomain(), application.getArgument(), application.getArgument().getType(context)), application.getScript(), application.getLineNumber());
	}
}
