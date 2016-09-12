package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.types.PairType;

/**
 * Class representing a Funcky pair.
 * 
 * @author thanos
 */
public class Pair extends Literal {
	private static final String NULL_FIRST = "First must not be null";
	private static final String NULL_SECOND = "Second must not be null";
	private static final String PAIR = "{%1$s, %2$s}";

	private final Expression first;
	private final Expression second;

	/**
	 * Construct a new pair.
	 * 
	 * @param engine
	 *            the engine that generated this pair
	 * @param script
	 *            the URI of the script from which this pair was generated
	 * @param lineNumber
	 *            the number of the line from which this pair was parsed or <code>0</code> if this pair was not parsed (is builtin or generated at runtime)
	 * @param first
	 *            the first value of this pair
	 * @param second
	 *            the second value of this pair
	 */
	public Pair(final FunckyScriptEngine engine, final URI script, final int lineNumber, final Expression first, final Expression second) {
		super(engine, script, lineNumber);
		this.first = Objects.requireNonNull(first, NULL_FIRST);
		this.second = Objects.requireNonNull(second, NULL_SECOND);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Pair) {
			final Pair pair = (Pair) object;
			return first.equals(pair.first) && second.equals(pair.second);
		}
		return false;
	}

	/**
	 * Get the first value of this pair.
	 * 
	 * @return the first value of this pair
	 */
	public Expression getFirst() {
		return first;
	}

	/**
	 * Get the second value of this pair.
	 * 
	 * @return the second value of this pair
	 */
	public Expression getSecond() {
		return second;
	}

	@Override
	public PairType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return new PairType(engine, first.getType(context), second.getType(context));
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public Expression toExpression() {
		return this;
	}

	@Override
	public String toString() {
		return String.format(PAIR, first, second);
	}
}
