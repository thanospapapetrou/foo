package com.github.thanospapapetrou.funcky.runtime;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
	private abstract static class Functor extends Function {
		private final FunckyType[] types;

		private Functor(final String name, final FunckyType... types) {
			super(name, types[0], getFunctionType(Arrays.copyOfRange(types, 1, types.length)));
			this.types = types;
		}

		private static FunckyType getFunctionType(final FunckyType... types) {
			return (types.length == 1) ? types[0] : new FunctionType(types[0], getFunctionType(Arrays.copyOfRange(types, 1, types.length)));
		}

		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedReferenceException {
			final Functor that = this;
			try {
				return (types.length == 2) ? apply(context, argument) : new Functor(new Application(context, that, argument).toString(), Arrays.copyOfRange(types, 1, types.length)) {
					@Override
					protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedReferenceException {
						final Expression[] newArguments = new Expression[arguments.length + 1];
						newArguments[0] = argument;
						System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
						return that.apply(context, newArguments);
					}
				};
			} catch (final InvalidArgumentException | InvalidFunctionException e) {
				Logger.getLogger(Function.class.getName()).log(Level.WARNING, String.format("Error defining return function of functor %1$s", this), e);
				return null;
			}
		}

		protected abstract Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedReferenceException;
	}

	private abstract static class TwoArgumentArithmeticOperator extends Functor {
		private TwoArgumentArithmeticOperator(final String name) {
			super(name, SimpleType.NUMBER, SimpleType.NUMBER, SimpleType.NUMBER);
		}

		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedReferenceException {
			return apply((FunckyNumber) arguments[0].eval(context), (FunckyNumber) arguments[1].eval(context), context);
		}

		protected abstract Literal apply(final FunckyNumber argument1, final FunckyNumber argument2, final ScriptContext context) throws UndefinedReferenceException;
	}

	/**
	 * Construct a function type.
	 */
	public static final Function FUNCTION = new Functor("function", SimpleType.TYPE, SimpleType.TYPE, SimpleType.TYPE) {
		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedReferenceException {
			return new FunctionType((FunckyType) arguments[0].eval(context), (FunckyType) arguments[1].eval(context));
		}
	};

	private static final TypeVariable TYPE = new TypeVariable("type");
	
	/**
	 * Get the type of an expression.
	 */
	public static final Function TYPE_OF = new Function("typeOf", TYPE, SimpleType.TYPE) {
		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedReferenceException {
			return argument.getType(context);
		}
	};

	/**
	 * Ternary operator.
	 */
	public static final Function IF = new Functor("if", SimpleType.BOOLEAN, TYPE, TYPE, TYPE) {
		@Override
		public Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedReferenceException {
			return ((FunckyBoolean) arguments[0].eval(context)).equals(FunckyBoolean.TRUE) ? arguments[1].eval(context) : arguments[2].eval(context);
		}
	};

	/**
	 * Add two numbers.
	 */
	public static final Function ADD = new TwoArgumentArithmeticOperator("add") {
		@Override
		protected Literal apply(final FunckyNumber term1, final FunckyNumber term2, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(term1.getValue() + term2.getValue());
		}
	};

	/**
	 * Subtract two numbers.
	 */
	public static final Function SUBTRACT = new TwoArgumentArithmeticOperator("subtract") {
		@Override
		protected Literal apply(final FunckyNumber minuend, final FunckyNumber subtrahend, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(minuend.getValue() - subtrahend.getValue());
		}
	};

	/**
	 * Multiply two numbers.
	 */
	public static final Function MULTIPLY = new TwoArgumentArithmeticOperator("multiply") {
		@Override
		protected Literal apply(final FunckyNumber factor1, final FunckyNumber factor2, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(factor1.getValue() * factor2.getValue());
		}
	};

	/**
	 * Divide two numbers.
	 */
	public static final Function DIVIDE = new TwoArgumentArithmeticOperator("divide") {
		@Override
		protected Literal apply(final FunckyNumber dividend, final FunckyNumber divisor, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(dividend.getValue() / divisor.getValue());
		}
	};

	private final String name;
	private final FunckyType domain;
	private final FunckyType range;

	private Function(final String name, final FunckyType domain, final FunckyType range) {
		super(null, null, 0);
		this.name = Objects.requireNonNull(name, "Name must not be null");
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

	@Override
	public String toString() {
		return name;
	}
}
