package com.github.thanospapapetrou.funcky;

/**
 * Class representing a Funcky number.
 * 
 * @author thanos
 */
public class FunckyNumber extends Literal {
	/**
	 * Funcky number representing π.
	 */
	public static final FunckyNumber PI = new FunckyNumber(null, Math.PI);

	/**
	 * Funcky number representing e.
	 */
	public static final FunckyNumber E = new FunckyNumber(null, Math.E);

	/**
	 * Funcky number representing ∞.
	 */
	public static final FunckyNumber INFINITY = new FunckyNumber(null, Double.POSITIVE_INFINITY);

	/**
	 * Funcky number representing NaN.
	 */
	public static final FunckyNumber NAN = new FunckyNumber(null, Double.NaN);
	private static final String POSITIVE_INFINITY_STRING = "infinity";
	private static final String NEGATIVE_INFINITY_STRING = "minus identify";
	private static final String NAN_STRING = "nan";

	private final double value;

	FunckyNumber(final FunckyScriptEngine engine, final double value) {
		super(engine);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FunckyNumber) && (value == ((FunckyNumber) object).value);
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.NUMBER;
	}

	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		if (value == Double.POSITIVE_INFINITY) {
			return POSITIVE_INFINITY_STRING;
		} else if (value == Double.NEGATIVE_INFINITY) {
			return NEGATIVE_INFINITY_STRING;
		} else if (value == Double.NaN) {
			return NAN_STRING;
		}
		return Double.toString(value);
	}

	double getValue() {
		return value;
	}
}
