package com.github.thanospapapetrou.funcky.runtime;

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
	private abstract static class TwoArgumentFunction extends Function {
		private TwoArgumentFunction(final String name, final FunckyType domain1, final FunckyType domain2, final FunckyType range) {
			super(name, domain1, new FunctionType(domain2, range));
		}

		@Override
		public Literal apply(final Expression argument1, final ScriptContext context) throws UndefinedReferenceException {
			try {
				final FunctionType range = (FunctionType) super.range;
				return new Function(new Application(context, this, argument1).toString(), range.getDomain(), range.getRange()) {
					@Override
					public Literal apply(final Expression argument2, final ScriptContext context) throws UndefinedReferenceException {
						return TwoArgumentFunction.this.apply(argument1, argument2, context);
					}
				};
			} catch (final InvalidArgumentException | InvalidFunctionException e) {
				Logger.getLogger(Function.class.getName()).log(Level.WARNING, "", e); // TODO error message?
				return null;
			}
		}

		protected abstract Literal apply(final Expression argument1, final Expression argument2, final ScriptContext context) throws UndefinedReferenceException;
	}

	/**
	 * Construct a function type.
	 */
	public static final Function FUNCTION = new TwoArgumentFunction("function", SimpleType.TYPE, SimpleType.TYPE, SimpleType.TYPE) {
		@Override
		protected Literal apply(final Expression domain, final Expression range, final ScriptContext context) throws UndefinedReferenceException {
			return new FunctionType((FunckyType) domain.eval(context), (FunckyType) range.eval(context));
		}
	};

	/**
	 * Add two numbers.
	 */
	public static final Function ADD = new TwoArgumentFunction("add", SimpleType.NUMBER, SimpleType.NUMBER, SimpleType.NUMBER) {
		@Override
		protected Literal apply(final Expression term1, final Expression term2, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(null, null, 0, ((FunckyNumber) term1.eval(context)).getValue() + ((FunckyNumber) term2.eval(context)).getValue());
		}
	};

	/**
	 * Subtract two numbers.
	 */
	public static final Function SUBTRACT = new TwoArgumentFunction("subtract", SimpleType.NUMBER, SimpleType.NUMBER, SimpleType.NUMBER) {
		@Override
		protected Literal apply(final Expression minuend, final Expression subtrahend, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(null, null, 0, ((FunckyNumber) minuend.eval(context)).getValue() - ((FunckyNumber) subtrahend.eval(context)).getValue());
		}
	};

	/**
	 * Multiply two numbers.
	 */
	public static final Function MULTIPLY = new TwoArgumentFunction("multiply", SimpleType.NUMBER, SimpleType.NUMBER, SimpleType.NUMBER) {
		@Override
		protected Literal apply(final Expression factor1, final Expression factor2, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(null, null, 0, ((FunckyNumber) factor1.eval(context)).getValue() * ((FunckyNumber) factor2.eval(context)).getValue());
		}
	};

	/**
	 * Divide two numbers.
	 */
	public static final Function DIVIDE = new TwoArgumentFunction("divide", SimpleType.NUMBER, SimpleType.NUMBER, SimpleType.NUMBER) {
		@Override
		protected Literal apply(final Expression dividend, final Expression divisor, final ScriptContext context) throws UndefinedReferenceException {
			return new FunckyNumber(null, null, 0, ((FunckyNumber) dividend.eval(context)).getValue() / ((FunckyNumber) divisor.eval(context)).getValue());
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
