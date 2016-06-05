package com.github.thanospapapetrou.foo.runtime;

/**
 * Class representing a Foo boolean
 * 
 * @author thanos
 */
public class FooBoolean extends Literal {
	/**
	 * Foo boolean representing true.
	 */
	public static final FooBoolean TRUE = new FooBoolean(true);

	/**
	 * Foo boolean representing false.
	 */
	public static final FooBoolean FALSE = new FooBoolean(false);

	private final boolean value;

	private FooBoolean(final boolean value) {
		super(null);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FooBoolean) && (value == ((FooBoolean) object).value);
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		return Boolean.valueOf(value).toString();
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.BOOLEAN;
	}
}
