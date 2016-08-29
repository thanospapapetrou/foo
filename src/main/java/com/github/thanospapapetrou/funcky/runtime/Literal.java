package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Abstract class representing a Funcky literal. A literal can not be evaluated further, however it may not be directly representable, so it has to be converted to an expression to be displayed.
 * 
 * @author thanos
 */
public abstract class Literal extends Expression {
	/**
	 * Construct a new literal.
	 * 
	 * @param engine
	 *            the Funcky script engine that parsed this literal
	 * @param script
	 *            the URI of the script from which this literal was parsed
	 * @param lineNumber
	 *            the number of the line from which this literal was parsed or <code>0</code> if this literal was not parsed from any line (is a builtin)
	 */
	protected Literal(final FunckyScriptEngine engine, final URI script, final int lineNumber) {
		super(engine, script, lineNumber);
	}

	@Override
	public Literal eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.eval(context);
		return this;
	}

	/**
	 * Get the expression used to display this literal.
	 * 
	 * @return the expression used to display this literal
	 */
	public abstract Expression toExpression();
}
