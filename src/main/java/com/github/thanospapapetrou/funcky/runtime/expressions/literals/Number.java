package com.github.thanospapapetrou.funcky.runtime.expressions.literals;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.libraries.Numbers;

/**
 * Class representing a Funcky number.
 * 
 * @author thanos
 */
public class Number extends Literal {
	private final double value;

	/**
	 * Construct a new number.
	 * 
	 * @param engine
	 *            the engine that generated this number
	 * @param script
	 *            the URI of the script from which this number was generated
	 * @param line
	 *            the line from which this number was parsed or <code>-1</code> if this number was not parsed (is built-in or generated at runtime)
	 * @param value
	 *            the value of this number
	 */
	public Number(final FunckyScriptEngine engine, final URI script, final int line, final double value) {
		super(engine, script, line);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Number) && (value == ((Number) object).value);
	}

	@Override
	public SimpleType getType() throws ScriptException {
		return engine.getReference(Numbers.class, Numbers.NUMBER).evaluate(SimpleType.class);
	}

	/**
	 * Get the value of this number.
	 * 
	 * @return the value of this number
	 */
	public double getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}

	@Override
	public Expression toExpression() {
		if (value == Double.POSITIVE_INFINITY) {
			return engine.getReference(Numbers.class, Numbers.INFINITY);
		} else if (value == Double.NEGATIVE_INFINITY) {
			return engine.getApplication(engine.getReference(Numbers.class, Numbers.MINUS), engine.getReference(Numbers.class, Numbers.INFINITY));
		} else if (Double.isNaN(value)) {
			return engine.getReference(Numbers.class, Numbers.NAN);
		}
		return this;
	}

	@Override
	public String toString() {
		return ((value == Double.POSITIVE_INFINITY) || (value == Double.NEGATIVE_INFINITY) || (value == Double.NaN)) ? super.toString() : Double.toString(value);
	}
}
