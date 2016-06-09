package com.github.thanospapapetrou.funcky;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
	/**
	 * Add two numbers.
	 */
	public static final Function ADD = new Function(SimpleType.NUMBER, new FunctionType(SimpleType.NUMBER, SimpleType.NUMBER)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws ScriptException {
			final FunckyNumber term = (FunckyNumber) expression.eval(context);
			return new Function(SimpleType.NUMBER, SimpleType.NUMBER) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws ScriptException {
					return new FunckyNumber(null, term.getValue() + ((FunckyNumber) expression.eval(context)).getValue());
				}

				@Override
				public String toString() {
					return String.format("add %1$s", term);
				}
			};
		}

		@Override
		public String toString() {
			return "add";
		}
	};

	private final FunckyType domain;
	private final FunckyType range;

	private Function(final FunckyType domain, final FunckyType range) {
		super(null);
		this.domain = Objects.requireNonNull(domain, "Domain must not be null");
		this.range = Objects.requireNonNull(range, "Range must not be null");
	}

	/**
	 * Apply this function to the given argument in the given script context.
	 * 
	 * @param argument
	 *            the argument to apply the function to
	 * @param context
	 *            the script context in which to apply this function to the given argument
	 * @return the result of the application of this function to the given argument in the given context
	 * @throws ScriptException
	 *             if any errors occur during the evaluation
	 */
	public abstract Literal apply(final Expression argument, final ScriptContext context) throws ScriptException;

	@Override
	protected FunckyType getType() {
		return new FunctionType(domain, range);
	}
}
