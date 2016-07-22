package com.github.thanospapapetrou.funcky.runtime;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Abstract class representing a Funcky expression.
 * 
 * @author thanos
 */
public abstract class Expression extends AbstractSyntaxTreeNode {
	/**
	 * Construct a new expression.
	 * 
	 * @param engine
	 *            the Funcky script engine that parsed this expression or <code>null</code> if this expression was not parsed by any engine
	 * @param fileName
	 *            the name of the file from which this expression was parsed or <code>null</code> if this expression was not parsed from any file
	 * @param lineNumber
	 *            the number of the line from which this expression was parsed or <code>null</code> if this expression was not parsed from any line
	 */
	protected Expression(final FunckyScriptEngine engine, final String fileName, final int lineNumber) {
		super(engine, fileName, lineNumber);
	}

	@Override
	public abstract Literal eval(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException;

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
	public abstract FunckyType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException;
}
