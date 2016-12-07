package com.github.thanospapapetrou.funcky.helper;

public class Application implements Expression {
	private final Expression function;
	private final Expression argument;

	public Application(final Expression function, final Expression argument) {
		this.function = function;
		this.argument = argument;
	}

	public Expression getFunction() {
		return function;
	}

	public Expression getArgument() {
		return argument;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Application) && function.equals(((Application) object).function) && argument.equals(((Application) object).argument);
	}

	@Override
	public int hashCode() {
		return function.hashCode() + argument.hashCode();
	}

	@Override
	public String toString() {
		return String.format("(%1$s %2$s)", function, argument);
	}
}
