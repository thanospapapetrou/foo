package com.github.thanospapapetrou.foo.runtime;

/**
 * Class representing a Foo simple type.
 * 
 * @author thanos
 */
public class SimpleType extends FooType {
	/**
	 * Simple type representing a type.
	 */
	public static final SimpleType TYPE = new SimpleType("Type");

	/**
	 * Simple type representing a number.
	 */
	public static final SimpleType NUMBER = new SimpleType("Number");

	/**
	 * Simple type representing a boolean.
	 */
	public static final SimpleType BOOLEAN = new SimpleType("Boolean");

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
	public String toString() {
		return name;
	}
}
