package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
	/**
	 * Construct a function type.
	 */
	public static final Function FUNCTION = new Function(SimpleType.TYPE, new FunctionType(SimpleType.TYPE, SimpleType.TYPE)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
			final FunckyType domain = (FunckyType) expression.eval(context);
			return new Function(SimpleType.TYPE, SimpleType.TYPE) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
					return new FunctionType(domain, (FunckyType) expression.eval(context));
				}

				@Override
				public String toString() {
					return String.format("%1$s %2$s", FUNCTION, domain);
				}
			};
		}

		@Override
		public String toString() {
			return "function";
		}
	};

	/**
	 * Add two numbers.
	 */
	public static final Function ADD = new Function(SimpleType.NUMBER, new FunctionType(SimpleType.NUMBER, SimpleType.NUMBER)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
			final FunckyNumber term = (FunckyNumber) expression.eval(context);
			return new Function(SimpleType.NUMBER, SimpleType.NUMBER) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
					return new FunckyNumber(null, null, 0, term.getValue() + ((FunckyNumber) expression.eval(context)).getValue());
				}

				@Override
				public String toString() {
					return String.format("%1$s %2$s", ADD, term);
				}
			};
		}

		@Override
		public String toString() {
			return "add";
		}
	};

	/**
	 * Subtract two numbers.
	 */
	public static final Function SUBTRACT = new Function(SimpleType.NUMBER, new FunctionType(SimpleType.NUMBER, SimpleType.NUMBER)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
			final FunckyNumber minuend = (FunckyNumber) expression.eval(context);
			return new Function(SimpleType.NUMBER, SimpleType.NUMBER) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
					return new FunckyNumber(null, null, 0, minuend.getValue() - ((FunckyNumber) expression.eval(context)).getValue());
				}

				@Override
				public String toString() {
					return String.format("%1$s %2$s", SUBTRACT, minuend);
				}
			};
		}

		@Override
		public String toString() {
			return "subtract";
		}
	};

	/**
	 * Multiply two numbers.
	 */
	public static final Function MULTIPLY = new Function(SimpleType.NUMBER, new FunctionType(SimpleType.NUMBER, SimpleType.NUMBER)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
			final FunckyNumber factor = (FunckyNumber) expression.eval(context);
			return new Function(SimpleType.NUMBER, SimpleType.NUMBER) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
					return new FunckyNumber(null, null, 0, factor.getValue() * ((FunckyNumber) expression.eval(context)).getValue());
				}

				@Override
				public String toString() {
					return String.format("%1$s %2$s", MULTIPLY, factor);
				}
			};
		}

		@Override
		public String toString() {
			return "multiply";
		}
	};

	/**
	 * Divide two numbers.
	 */
	public static final Function DIVIDE = new Function(SimpleType.NUMBER, new FunctionType(SimpleType.NUMBER, SimpleType.NUMBER)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
			final FunckyNumber dividend = (FunckyNumber) expression.eval(context);
			return new Function(SimpleType.NUMBER, SimpleType.NUMBER) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws UndefinedReferenceException {
					return new FunckyNumber(null, null, 0, dividend.getValue() / ((FunckyNumber) expression.eval(context)).getValue());
				}

				@Override
				public String toString() {
					return String.format("%1$s %2$s", DIVIDE, dividend);
				}
			};
		}

		@Override
		public String toString() {
			return "divide";
		}
	};

	private final FunckyType domain;
	private final FunckyType range;

	private Function(final FunckyType domain, final FunckyType range) {
		super(null, null, 0);
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
	 * @throws UndefinedReferenceException
	 *             if any errors occur during the evaluation of the application
	 */
	public abstract Literal apply(final Expression argument, final ScriptContext context) throws UndefinedReferenceException;

	@Override
	protected FunckyType getType() {
		return new FunctionType(domain, range);
	}
}
