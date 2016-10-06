package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

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
	 *            the engine that generated this literal
	 * @param script
	 *            the URI of the script from which this literal was generated
	 * @param line
	 *            the line from which this literal was parsed or <code>0</code> if this literal was not parsed (is builtin or generated at runtime)
	 */
	protected Literal(final FunckyScriptEngine engine, final URI script, final int line) {
		super(engine, script, line);
	}

	@Override
	public Literal eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super.eval(context);
		return this;
	}

	/**
	 * Get the expression used to display this literal.
	 * 
	 * @return the expression used to display this literal
	 */
	public Expression toExpression() {
		return this;
	}

	@Override
	public String toString() {
		return toExpression().toString();
	}
}
