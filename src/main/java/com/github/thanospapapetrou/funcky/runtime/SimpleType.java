package com.github.thanospapapetrou.funcky.runtime;

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

	private static final String NULL_NAME = "Name must not be null";
	private static final String EMPTY_NAME = "Name must not be empty";

	private final String name;

	private SimpleType(final String name) {
		this.name = requireValidName(name);
	}

	private static final String requireValidName(final String name) {
		Objects.requireNonNull(name, NULL_NAME);
		if (name.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		return name;
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
	public String toString() {
		return name;
	}

	@Override
	Expression toExpression() {
		return new Reference(toString());
	}
}
