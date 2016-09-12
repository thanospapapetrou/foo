package com.github.thanospapapetrou.funcky.runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing a Funcky pair type.
 * 
 * @author thanos
 */
public class PairType extends FunckyType {
	private static final String NULL_FIRST = "First must not be null";
	private static final String NULL_SECOND = "Second must not be null";
	private static final String PAIR = "pair";

	private final FunckyType first;
	private final FunckyType second;

	/**
	 * Construct a new pair type.
	 * 
	 * @param engine
	 *            the engine that generated this pair type
	 * @param first
	 *            the first type of this pair type
	 * @param second
	 *            the second type of this pair type
	 */
	public PairType(final FunckyScriptEngine engine, final FunckyType first, final FunckyType second) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
		this.first = Objects.requireNonNull(first, NULL_FIRST);
		this.second = Objects.requireNonNull(second, NULL_SECOND);
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		super.bind(bindings);
		return new PairType(engine, first.bind(bindings), second.bind(bindings));
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof PairType) {
			final PairType pairType = (PairType) object;
			return first.equals(pairType.first) && second.equals(pairType.second);
		}
		return false;
	}

	/**
	 * Get the first type of this pair type.
	 * 
	 * @return the first type
	 */
	public FunckyType getFirst() {
		return first;
	}

	/**
	 * Get the second type of this pair type.
	 * 
	 * @return the second type
	 */
	public FunckyType getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		super.inferGenericBindings(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, FunckyType> singletonMap((TypeVariable) type, this);
		} else if (type instanceof PairType) {
			final PairType pairType = (PairType) type;
			final Map<TypeVariable, FunckyType> firstBindings = first.inferGenericBindings(pairType.first);
			final Map<TypeVariable, FunckyType> secondBindings = second.inferGenericBindings(pairType.second);
			return ((firstBindings == null) || (secondBindings == null)) ? null : new HashMap<TypeVariable, FunckyType>(firstBindings) {
				private static final long serialVersionUID = 1L;

				{
					putAll(secondBindings);
					putAll(firstBindings);
				}
			};
		}
		return null;
	}

	@Override
	public Expression toExpression() {
		return new Application(engine, new Application(engine, new Reference(engine, PAIR), first), second);
	}

	@Override
	public String toString() {
		return toExpression().toString();
	}
}
