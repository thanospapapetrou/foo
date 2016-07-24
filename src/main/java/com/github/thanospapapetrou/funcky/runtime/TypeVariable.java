package com.github.thanospapapetrou.funcky.runtime;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Class representing a Funcky type variable.
 * 
 * @author thanos
 */
public class TypeVariable extends FunckyType {
	private static final String RANDOM_NAME = "type$%1$s%2$s";
	private static final String TYPE_VARIABLE = "<%1$s>";
	private static final String NULL_TYPE = "Type must not be null";

	private final String name;

	private static final String generateRandomName() {
		final UUID uuid = UUID.randomUUID();
		return String.format(RANDOM_NAME, Long.toHexString(uuid.getMostSignificantBits()), Long.toHexString(uuid.getLeastSignificantBits()));
	}

	TypeVariable(final String name) {
		super();
		this.name = name;
	}

	TypeVariable() {
		this(generateRandomName());
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		return bindings.containsKey(this) ? bindings.get(this) : this;
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		return Collections.singletonMap(this, Objects.requireNonNull(type, NULL_TYPE).bind(Collections.<TypeVariable, FunckyType> singletonMap(this, new TypeVariable())));
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
