package com.github.thanospapapetrou.funcky.runtime;

/**
 * Class representing a Funcky boolean.
 * 
 * @author thanos
 */
public class FunckyBoolean extends Literal {
	/**
	 * Funcky boolean representing true.
	 */
	public static final FunckyBoolean TRUE = new FunckyBoolean(true);

	/**
	 * Funcky boolean representing false.
	 */
	public static final FunckyBoolean FALSE = new FunckyBoolean(false);

	private final boolean value;

	private FunckyBoolean(final boolean value) {
		super(null, null, 0);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FunckyBoolean) && (value == ((FunckyBoolean) object).value);
	}

	@Override
	public SimpleType getType() {
		return SimpleType.BOOLEAN;
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	@Override
	public Expression toExpression() {
		return new Reference(toString());
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}
}
