package com.github.thanospapapetrou.funcky.runtime;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Funcky simple type.
 * 
 * @author thanos
 */
public class SimpleType extends FunckyType {
	/**
	 * Simple type representing a type.
	 */
	public static final SimpleType TYPE = new SimpleType("type");

	/**
	 * Simple type representing a number.
	 */
	public static final SimpleType NUMBER = new SimpleType("number");

	/**
	 * Simple type representing a boolean.
	 */
	public static final SimpleType BOOLEAN = new SimpleType("boolean");

	private static final String NULL_TYPE = "Type must not be null";

	private final String name;

	private SimpleType(final String name) {
		this.name = name;
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		return this;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof SimpleType) && name.endsWith(((SimpleType) object).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		return equals(Objects.requireNonNull(type, NULL_TYPE)) ? Collections.<TypeVariable, FunckyType> emptyMap() : null;
	}

	@Override
	public Expression toExpression() {
		return new Reference(toString());
	}

	@Override
	public String toString() {
		return name;
	}
}
