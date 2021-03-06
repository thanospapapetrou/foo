package com.github.thanospapapetrou.funcky.runtime.expressions.literals.types;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Application;
import com.github.thanospapapetrou.funcky.runtime.libraries.Pairs;

/**
 * Class representing a Funcky pair type.
 * 
 * @author thanos
 */
public class PairType extends Type {
	private static final String NULL_FIRST = "First must not be null";
	private static final String NULL_SECOND = "Second must not be null";

	private final Type first;
	private final Type second;

	/**
	 * Construct a new pair type.
	 * 
	 * @param engine
	 *            the engine that generated this pair type
	 * @param script
	 *            the URI of the script from which this pair type was generated
	 * @param first
	 *            the first type of this pair type
	 * @param second
	 *            the second type of this pair type
	 */
	public PairType(final FunckyScriptEngine engine, final URI script, final Type first, final Type second) {
		super(engine, script, -1);
		this.first = Objects.requireNonNull(first, NULL_FIRST);
		this.second = Objects.requireNonNull(second, NULL_SECOND);
	}

	@Override
	public PairType bind(final Map<TypeVariable, Type> bindings) {
		super.bind(bindings);
		return engine.getPairType(first.bind(bindings), second.bind(bindings));
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
	public Type getFirst() {
		return first;
	}

	/**
	 * Get the second type of this pair type.
	 * 
	 * @return the second type
	 */
	public Type getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public Map<TypeVariable, Type> infer(final Type type) {
		super.infer(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, Type> singletonMap((TypeVariable) type, this);
		} else if (type instanceof PairType) {
			final PairType pairType = (PairType) type;
			final Map<TypeVariable, Type> firstBindings = first.infer(pairType.first);
			if (firstBindings == null) {
				return null;
			}
			final Map<TypeVariable, Type> secondBindings = second.infer(pairType.second);
			if (secondBindings == null) {
				return null;
			}
			final Map<TypeVariable, Type> bindings = new HashMap<>();
			bindings.putAll(firstBindings);
			bindings.putAll(secondBindings);
			return bindings;
		}
		return null;
	}

	@Override
	public Application toExpression() {
		return engine.getApplication(engine.getApplication(engine.getReference(Pairs.class, Pairs.PAIR), first), second);
	}

	@Override
	protected PairType free(final Map<TypeVariable, TypeVariable> free) {
		super.free(free);
		return engine.getPairType(first.free(free), second.free(free));
	}
}
