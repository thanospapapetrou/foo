package com.github.thanospapapetrou.funcky.runtime;

/**
 * Class representing a Funcky type variable.
 * 
 * @author thanos
 */
public class TypeVariable extends FunckyType {
	private static final String TYPE_VARIABLE = "<%1$s>";

	private final String name;

	TypeVariable(final String name) {
		super();
		this.name = name;
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
