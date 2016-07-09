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
		private static final int MIN_TYPES = 2;
		private static final String ANONYMOUS = "_anonymous_";
		private static final String NULL_TYPES = "Types must not be null";
		private static final String TYPES_CONTAINS_LESS_THAN_TWO_ELEMENTS = "Types must contain at least two elements";
		private static final String NULL_TYPE = "Types must not be null";
		private static final String EMPTY_TYPES = "Types must not be empty";
		private static final String NULL_ARGUMENT = "Argument must not be null";
		private static final String NULL_CONTEXT = "Context must not be null";
		private static final String NULL_ARGUMENTS = "Arguments must not be null";

		private final FunckyType[] types;

		private Functor(final String name, final FunckyType... types) {
			super(name, requireValidTypes(types, MIN_TYPES, TYPES_CONTAINS_LESS_THAN_TWO_ELEMENTS)[0], getFunctionType(Arrays.copyOfRange(types, 1, types.length)));
			this.types = types;
		}

		private static FunckyType[] requireValidTypes(final FunckyType[] types, final int minTypes, final String lessThanMinMessage) {
			if (Objects.requireNonNull(types, NULL_TYPES).length < minTypes) {
				throw new IllegalArgumentException(lessThanMinMessage);
			}
			for (final FunckyType type : types) {
				Objects.requireNonNull(type, NULL_TYPE);
			}
			return types;
		}

		private static FunckyType getFunctionType(final FunckyType... types) {
			return (requireValidTypes(types, 1, EMPTY_TYPES).length == 1) ? types[0] : new FunctionType(types[0], getFunctionType(Arrays.copyOfRange(types, 1, types.length)));
		}

		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException {
			Objects.requireNonNull(argument, NULL_ARGUMENT);
			Objects.requireNonNull(context, NULL_CONTEXT);
			final Functor that = this;
			return (types.length == MIN_TYPES) ? apply(context, argument) : new Functor(ANONYMOUS, Arrays.copyOfRange(types, 1, types.length)) {
				@Override
				protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
					final Expression[] newArguments = new Expression[Objects.requireNonNull(arguments, NULL_ARGUMENTS).length + 1];
					newArguments[0] = argument;
					System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
					return that.apply(Objects.requireNonNull(context, NULL_CONTEXT), newArguments);
				}

				@Override
				public String toString() {
					return toExpression().toString();
				}
				
				@Override
				Expression toExpression() {
					return new Application(that, argument);
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

	/**
	 * Construct a function type.
	 */
	public static final Function FUNCTION = new Functor("function", SimpleType.TYPE, SimpleType.TYPE, SimpleType.TYPE) {
		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return new FunctionType((FunckyType) arguments[0].eval(context), (FunckyType) arguments[1].eval(context));
		}
	};

	private static final TypeVariable EXPRESSION = new TypeVariable("expression");

	/**
	 * Get the type of an expression.
	 */
	public static final Function TYPE_OF = new Function("typeOf", EXPRESSION, SimpleType.TYPE) {
		@Override
		public Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException {
			return argument.getType(context);
		}
	};

	private static final TypeVariable DOMAIN1 = new TypeVariable("domain1");
	private static final TypeVariable DOMAIN2 = new TypeVariable("domain2");
	private static final TypeVariable RANGE = new TypeVariable("range");

	/**
	 * Flip the arguments of a function.
	 */
	public static final Function FLIP = new Functor("flip", new FunctionType(DOMAIN1, new FunctionType(DOMAIN2, RANGE)), DOMAIN2, DOMAIN1) {
		@Override
		protected Literal apply(final ScriptContext context, final Expression... arguments) throws UndefinedSymbolException {
			return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[2], context)).apply(arguments[1], context);
		}
	};

	/**
	 * Ternary operator.
	 */
	public static final Function IF = new Functor("if", SimpleType.BOOLEAN, EXPRESSION, EXPRESSION, EXPRESSION) {
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

	private static final String NULL_NAME = "Name must not be null";
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_DOMAIN = "Domain must not be null";
	private static final String NULL_RANGE = "Range must not be null";

	private final String name;
	private final FunckyType domain;
	private final FunckyType range;

	private Function(final String name, final FunckyType domain, final FunckyType range) {
		super(null, null, 0);
		this.name = requireValidName(name);
		this.domain = Objects.requireNonNull(domain, NULL_DOMAIN);
		this.range = Objects.requireNonNull(range, NULL_RANGE);
	}

	private static String requireValidName(final String name) {
		Objects.requireNonNull(name, NULL_NAME);
		if (name.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		return name;
	}

	/**
	 * Apply this function to the given argument in the given script context.
	 * 
	 * @param argument
	 *            the argument to apply the function to
	 * @param context
	 *            the script context in which to apply this function to the given argument
	 * @return the result of the application of this function to the given argument in the given context
	 * @throws UndefinedSymbolException
	 *             if any errors occur during the evaluation of the application
	 */
	public abstract Literal apply(final Expression argument, final ScriptContext context) throws UndefinedSymbolException;

	@Override
	public String toString() {
		return name;
	}

	@Override
	protected FunckyType getType() {
		return new FunctionType(domain, range);
	}

	@Override
	Expression toExpression() {
		return new Reference(name);
	}
}
