package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.net.URI;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.Functor;
import com.github.thanospapapetrou.funcky.runtime.functors.TwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.literals.Boolean;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.Number;
import com.github.thanospapapetrou.funcky.runtime.literals.Pair;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.PairType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Class representing the Funcky prelude.
 * 
 * @author thanos
 */
public class Prelude extends Library {
	/**
	 * The URI of the prelude library.
	 */
	private static final URI PRELUDE = URI.create("funcky:prelude");

	public static final String ADD = "add";
	public static final String BOOLEAN = "boolean";
	public static final String BOTTOM = "bottom";
	public static final String CHARACTER = "character";
	public static final String COMPOSE = "compose";
	public static final String DIVIDE = "divide";
	public static final String DUPLICATE = "duplicate";
	public static final String EQUAL = "equal";
	public static final String FALSE = "false";
	public static final String FLIP = "flip";
	public static final String FUNCTION = "function";
	public static final String IDENTITY = "identity";
	public static final String IF = "if";
	public static final String INFINITY = "infinity";
	public static final String INTEGER = "integer";
	public static final String IS_NAN = "isNaN";
	public static final String LIST = "list";
	public static final String MULTIPLY = "multiply";
	public static final String NAN = "NaN";
	public static final String NUMBER = "number";
	public static final String PAIR = "pair";
	public static final String PRODUCT = "product";
	public static final String SUBTRACT = "subtract";
	public static final String TRUE = "true";
	public static final String TYPE = "type";
	public static final String TYPE_OF = "typeOf";

	/**
	 * Construct a new prelude.
	 * 
	 * @param engine
	 *            the engine that generated this prelude
	 * @throws IOException
	 * @throws ScriptException
	 */
	public Prelude(final FunckyScriptEngine engine) throws IOException, ScriptException {
		super(engine);
		// types
		final SimpleType typeType = getSimpleType(TYPE);
		addDefinition(typeType);
		final SimpleType numberType = getSimpleType(NUMBER);
		addDefinition(numberType);
		final SimpleType booleanType = getSimpleType(BOOLEAN);
		addDefinition(booleanType);
		addDefinition(getSimpleType(CHARACTER));
		// numbers
		addDefinition(getNumber(Double.POSITIVE_INFINITY));
		addDefinition(getNumber(Double.NaN));
		// booleans
		final Boolean booleanTrue = getBoolean(true);
		addDefinition(booleanTrue);
		final Boolean booleanFalse = getBoolean(false);
		addDefinition(booleanFalse);
		// functions
		addDefinition(new Function(engine, PRELUDE, BOTTOM, getFunctionType(getTypeVariable(), getTypeVariable())) { // TODO add addFunctionDefinition
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				super.apply(argument, context);
				return apply(argument, context);
			}
		});
		final TypeVariable identityType = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, IDENTITY, identityType, identityType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return arguments[0].eval(context);
			}
		});
		final TypeVariable composeType1 = getTypeVariable();
		final TypeVariable composeType2 = getTypeVariable();
		final TypeVariable composeType3 = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, COMPOSE, getFunctionType(composeType1, composeType2), getFunctionType(composeType3, composeType1), composeType3, composeType2) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return ((Function) arguments[0].eval(context)).apply(((Function) arguments[1].eval(context)).apply(arguments[2], context), context);
			}
		});
		final TypeVariable flipType1 = getTypeVariable();
		final TypeVariable flipType2 = getTypeVariable();
		final TypeVariable flipType3 = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, FLIP, getFunctionType(flipType1, getFunctionType(flipType2, flipType3)), flipType2, flipType1, flipType3) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[2], context)).apply(arguments[1], context);
			}
		});
		final TypeVariable duplicateType1 = getTypeVariable();
		final TypeVariable duplicateType2 = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, DUPLICATE, getFunctionType(duplicateType1, getFunctionType(duplicateType1, duplicateType2)), duplicateType1, duplicateType2) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[1], context)).apply(arguments[1], context);
			}
		});
		final TypeVariable equalType = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, EQUAL, equalType, equalType, booleanType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return arguments[0].eval(context).equals(arguments[1].eval(context)) ? booleanTrue : booleanFalse;
			}
		});
		final TypeVariable ifType = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, IF, booleanType, ifType, ifType, ifType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return ((Boolean) arguments[0].eval(context)).equals(booleanTrue) ? arguments[1].eval(context) : arguments[2].eval(context);
			}
		});
		addDefinition(new Function(engine, PRELUDE, TYPE_OF, getFunctionType(getTypeVariable(), typeType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				super.apply(argument, context);
				return argument.getType(context);
			}
		});
		addDefinition(new Functor(engine, PRELUDE, FUNCTION, typeType, typeType, typeType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return new FunctionType(engine, (Type) arguments[0].eval(context), (Type) arguments[1].eval(context));
			}
		});
		addDefinition(new Function(engine, PRELUDE, IS_NAN, getFunctionType(numberType, booleanType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				super.apply(argument, context);
				return Double.isNaN(((Number) argument.eval(context)).getValue()) ? booleanTrue : booleanFalse;
			}
		});
		addDefinition(new Function(engine, PRELUDE, INTEGER, getFunctionType(numberType, numberType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				super.apply(argument, context);
				final double value = ((Number) argument.eval(context)).getValue();
				return new Number(engine, (Double.isInfinite(value) || Double.isNaN(value)) ? value : (int) value);
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, ADD, numberType) {
			@Override
			protected Literal apply(final Number term1, final Number term2, final ScriptContext context) {
				super.apply(term1, term2, context);
				return new Number(engine, term1.getValue() + term2.getValue());
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, SUBTRACT, numberType) {
			@Override
			protected Literal apply(final Number minuend, final Number subtrahend, final ScriptContext context) {
				super.apply(minuend, subtrahend, context);
				return new Number(engine, minuend.getValue() - subtrahend.getValue());
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, MULTIPLY, numberType) {
			@Override
			protected Literal apply(final Number factor1, final Number factor2, final ScriptContext context) {
				super.apply(factor1, factor2, context);
				return new Number(engine, factor1.getValue() * factor2.getValue());
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, DIVIDE, numberType) {
			@Override
			protected Literal apply(final Number dividend, final Number divisor, final ScriptContext context) {
				super.apply(dividend, divisor, context);
				return new Number(engine, dividend.getValue() / divisor.getValue());
			}
		});
		final TypeVariable productType1 = getTypeVariable();
		final TypeVariable productType2 = getTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, PRODUCT, productType1, productType2, new PairType(engine, productType1, productType2)) {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return new Pair(engine, arguments[0].eval(context), arguments[1].eval(context));
			}
		});
	}

	private Boolean getBoolean(final boolean value) {
		return new Boolean(engine, PRELUDE, value);
	}

	private Number getNumber(final double value) {
		return new Number(engine, PRELUDE, value);
	}

	private SimpleType getSimpleType(final String name) { // TODO move this method to Library
		return new SimpleType(engine, PRELUDE, name);
	}
}
