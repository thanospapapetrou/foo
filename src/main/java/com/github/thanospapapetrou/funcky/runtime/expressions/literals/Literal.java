package com.github.thanospapapetrou.funcky.runtime.expressions.literals;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;

/**
 * Abstract class representing a Funcky literal. A literal can not be evaluated further, however it may not be directly representable, so it has to be converted to an expression to be displayed.
 * 
 * @author thanos
 */
public abstract class Literal extends Expression {
	private static final String LITERAL_CAST_ERROR = "Literal %1$s can not be cast to %2$s";

	/**
	 * Construct a new literal.
	 * 
	 * @param engine
	 *            the engine that generated this literal
	 * @param script
	 *            the URI of the script from which this literal was generated
	 * @param line
	 *            the line from which this literal was parsed or <code>-1</code> if this literal was not parsed (is built-in or generated at runtime)
	 */
	protected Literal(final FunckyScriptEngine engine, final URI script, final int line) {
		super(engine, script, line);
	}

	@Override
	public <L extends Literal> L evaluate(final Class<L> clazz) throws ScriptException {
		super.evaluate(clazz);
		if (clazz.isAssignableFrom(this.getClass())) {
			return clazz.cast(this);
		}
		throw new ScriptException(new IllegalStateException(String.format(LITERAL_CAST_ERROR, this, clazz.getName())));
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
