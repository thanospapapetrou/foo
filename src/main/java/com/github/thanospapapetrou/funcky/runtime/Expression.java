package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunckyType;

/**
 * Abstract class representing a Funcky expression.
 * 
 * @author thanos
 */
public abstract class Expression extends AbstractSyntaxTreeNode {
	private static final String NULL_CONTEXT = "Context must not be null";

	/**
	 * Construct a new expression.
	 * 
	 * @param engine
	 *            the engine that generated this expression
	 * @param script
	 *            the URI of the script from which this expression was generated
	 * @param lineNumber
	 *            the number of the line from which this expression was parsed or <code>0</code> if this expression was not parsed (is builtin or generated at runtime)
	 */
	protected Expression(final FunckyScriptEngine engine, final URI script, final int lineNumber) {
		super(engine, script, lineNumber);
	}

	@Override
	public Literal eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.eval(context);
		return null;
	}

	/**
	 * Get the type of this expression.
	 * 
	 * @param context
	 *            the context in which to evaluate the type
	 * @return the type of this expression as evaluated in the given context
	 * @throws InvalidArgumentException
	 *             if the type of the argument does not match the domain of the function
	 * @throws InvalidFunctionException
	 *             if function is not actually a function
	 * @throws UndefinedSymbolException
	 *             if any reference to an undefined symbol is encountered
	 */
	public FunckyType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		Objects.requireNonNull(context, NULL_CONTEXT);
		return null;
	}
}
