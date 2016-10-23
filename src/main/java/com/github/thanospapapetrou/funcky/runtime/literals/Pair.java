package com.github.thanospapapetrou.funcky.runtime.literals;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.literals.types.PairType;

/**
 * Class representing a Funcky pair.
 * 
 * @author thanos
 */
public class Pair extends Literal {
	private static final String NULL_FIRST = "First must not be null";
	private static final String NULL_SECOND = "Second must not be null";
	private static final String PAIR = "<%1$s, %2$s>";

	private final Literal first;
	private final Literal second;

	/**
	 * Construct a new pair.
	 * 
	 * @param engine
	 *            the engine that generated this pair
	 * @param first
	 *            the first value of this pair
	 * @param second
	 *            the second value of this pair
	 */
	public Pair(final FunckyScriptEngine engine, final Literal first, final Literal second) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
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
	public Literal getFirst() {
		return first;
	}

	/**
	 * Get the second value of this pair.
	 * 
	 * @return the second value of this pair
	 */
	public Literal getSecond() {
		return second;
	}

	@Override
	public PairType getType(final ScriptContext context) throws ScriptException {
		super.getType(context);
		return new PairType(engine, first.getType(context), second.getType(context));
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public String toString() {
		return String.format(PAIR, first, second);
	}
}
