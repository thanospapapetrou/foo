package com.github.thanospapapetrou.funcky.runtime;

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

	private final String name;

	private SimpleType(final String name) {
		this.name = name;
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
	public Expression toExpression() {
		return new Reference(toString());
	}

	@Override
	public String toString() {
		return name;
	}
}
