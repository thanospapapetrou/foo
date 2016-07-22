package com.github.thanospapapetrou.funcky.runtime;

import java.util.Arrays;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Abstract class representing a Funcky function.
 * 
 * @author thanos
 */
public abstract class Function extends Literal {
	private abstract static class Functor extends Function {
		private static final int FUNCTION_TYPES = 2;
		private static final String ANONYMOUS = "_anonymous_";

		private final FunckyType[] types;

		private Functor(final String name, final FunckyType... types) {
			super(name, types[0], getFunctionType(Arrays.copyOfRange(types, 1, types.length)));
			this.types = types;
		}

		private static FunckyType getFunctionType(final FunckyType... types) {
			return (types.length == 1) ? types[0] : new FunctionType(types[0], getFunctionType(Arrays.copyOfRange(types, 1, types.length)));
		}

		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException {
			Objects.requireNonNull(argument, NULL_ARGUMENT);
			Objects.requireNonNull(context, NULL_CONTEXT);
			final Functor that = this;
			final FunckyType[] newTypes = new FunckyType[types.length - 1];
			for (int i = 0; i < newTypes.length; i++) {
				newTypes[i] = types[i + 1].bind(types[0].inferGenericBindings(argument.getType(context))); // TODO unbind unbound type variables
			}
			return (types.length == FUNCTION_TYPES) ? apply(context, argument) : new Functor(ANONYMOUS, newTypes) {
				@Override
				public Expression toExpression() {
					return new Application(that, argument);
				}

				@Override
				public String toString() {
					return toExpression().toString();
				}

				@Override
				protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
					final Expression[] newArguments = new Expression[arguments.length + 1];
					newArguments[0] = argument;
					System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
					return that.apply(context, newArguments);
				}
			};
		}

		protected abstract Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException;
	}

	private abstract static class TwoArgumentArithmeticOperator extends Functor {
		private TwoArgumentArithmeticOperator(final String name) {
			super(name, SimpleType.NUMBER, SimpleType.NUMBER, SimpleType.NUMBER);
		}

		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return apply((FunckyNumber) arguments[0].eval(context), (FunckyNumber) arguments[1].eval(context), context);
		}

		protected abstract Literal apply(final FunckyNumber argument1, final FunckyNumber argument2, final ScriptContext context) throws UndefinedSymbolException;
	}

	private static final TypeVariable IDENTITY_TYPE = new TypeVariable("type");

	/**
	 * Identity function.
	 */
	public static Function IDENTITY = new Function("identity", IDENTITY_TYPE, IDENTITY_TYPE) {
		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException {
			return Objects.requireNonNull(argument, NULL_ARGUMENT).eval(context);
		}
	};

	private static final TypeVariable COMPOSE_TYPE_1 = new TypeVariable("type1");
	private static final TypeVariable COMPOSE_TYPE_2 = new TypeVariable("type2");
	private static final TypeVariable COMPOSE_TYPE_3 = new TypeVariable("type3");

	/**
	 * Compose two functions.
	 */
	public static final Function COMPOSE = new Functor("compose", new FunctionType(COMPOSE_TYPE_1, COMPOSE_TYPE_2), new FunctionType(COMPOSE_TYPE_3, COMPOSE_TYPE_1), COMPOSE_TYPE_3, COMPOSE_TYPE_2) {
		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return ((Function) arguments[0].eval(context)).apply(((Function) arguments[1].eval(context)).apply(arguments[2], context), context);
		}
	};

	private static final TypeVariable FLIP_TYPE_1 = new TypeVariable("type1");
	private static final TypeVariable FLIP_TYPE_2 = new TypeVariable("type2");
	private static final TypeVariable FLIP_TYPE_3 = new TypeVariable("type3");

	/**
	 * Flip the arguments of a function.
	 */
	public static final Function FLIP = new Functor("flip", new FunctionType(FLIP_TYPE_1, new FunctionType(FLIP_TYPE_2, FLIP_TYPE_3)), FLIP_TYPE_2, FLIP_TYPE_1, FLIP_TYPE_3) {
		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[2], context)).apply(arguments[1], context);
		}
	};

	/**
	 * Construct a function type.
	 */
	public static final Function FUNCTION = new Functor("function", SimpleType.TYPE, SimpleType.TYPE, SimpleType.TYPE) {
		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return new FunctionType((FunckyType) arguments[0].eval(context), (FunckyType) arguments[1].eval(context));
		}
	};

	private static final TypeVariable TYPE_OF_TYPE = new TypeVariable("type");

	/**
	 * Get the type of an expression.
	 */
	public static final Function TYPE_OF = new Function("typeOf", TYPE_OF_TYPE, SimpleType.TYPE) {
		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException {
			return Objects.requireNonNull(argument, NULL_ARGUMENT).getType(Objects.requireNonNull(context, NULL_CONTEXT));
		}
	};

	private static final TypeVariable IF_TYPE = new TypeVariable("type");

	/**
	 * Ternary operator.
	 */
	public static final Function IF = new Functor("if", SimpleType.BOOLEAN, IF_TYPE, IF_TYPE, IF_TYPE) {
		@Override
		public Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return ((FunckyBoolean) arguments[0].eval(context)).equals(FunckyBoolean.TRUE) ? arguments[1].eval(context) : arguments[2].eval(context);
		}
	};

	/**
	 * Add two numbers.
	 */
	public static final Function ADD = new TwoArgumentArithmeticOperator("add") {
		@Override
		protected Literal apply(final FunckyNumber term1, final FunckyNumber term2, final ScriptContext context) throws UndefinedSymbolException {
			return new FunckyNumber(term1.getValue() + term2.getValue());
		}
	};

	/**
	 * Subtract two numbers.
	 */
	public static final Function SUBTRACT = new TwoArgumentArithmeticOperator("subtract") {
		@Override
		protected Literal apply(final FunckyNumber minuend, final FunckyNumber subtrahend, final ScriptContext context) throws UndefinedSymbolException {
			return new FunckyNumber(minuend.getValue() - subtrahend.getValue());
		}
	};

	/**
	 * Multiply two numbers.
	 */
	public static final Function MULTIPLY = new TwoArgumentArithmeticOperator("multiply") {
		@Override
		protected Literal apply(final FunckyNumber factor1, final FunckyNumber factor2, final ScriptContext context) throws UndefinedSymbolException {
			return new FunckyNumber(factor1.getValue() * factor2.getValue());
		}
	};

	/**
	 * Divide two numbers.
	 */
	public static final Function DIVIDE = new TwoArgumentArithmeticOperator("divide") {
		@Override
		protected Literal apply(final FunckyNumber dividend, final FunckyNumber divisor, final ScriptContext context) throws UndefinedSymbolException {
			return new FunckyNumber(dividend.getValue() / divisor.getValue());
		}
	};

	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";

	private final String name;
	private final FunckyType domain;
	private final FunckyType range;

	private Function(final String name, final FunckyType domain, final FunckyType range) {
		super(null, null, 0);
		this.name = name;
		this.domain = domain;
		this.range = range;
	}

	/**
	 * Apply this function to an argument.
	 * 
	 * @param argument
	 *            the argument to apply this function to
	 * @param context
	 *            the context in which to evaluate the application
	 * @return the literal result of applying this function to the given argument
	 * @throws UndefinedSymbolException
	 *             if any reference to an undefined symbol is encountered
	 */
	public abstract Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException;

	@Override
	public FunckyType getType() {
		return new FunctionType(domain, range);
	}

	@Override
	public Expression toExpression() {
		return new Reference(name);
	}

	@Override
	public String toString() {
		return name;
	}
}
