package com.github.thanospapapetrou.funcky.runtime;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Funcky type variable.
 * 
 * @author thanos
 */
public class TypeVariable extends FunckyType {
	private static final String TYPE_VARIABLE = "<%1$s>";
	private static final String NULL_TYPE = "Type must not be null";

	private final String name;

	TypeVariable(final String name) {
		super();
		this.name = name;
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		return bindings.containsKey(this) ? bindings.get(this).bind(bindings) : this;
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		return Collections.singletonMap(this, Objects.requireNonNull(type, NULL_TYPE));
	}

	@Override
	public Expression toExpression() {
		return this;
	}

	@Override
	public String toString() {
		return String.format(TYPE_VARIABLE, name);
	}
}
