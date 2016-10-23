package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.net.URI;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableTwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
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
		addFunctionDefinition(BOTTOM, getTypeVariable(), getTypeVariable(), new ApplicableFunction() { // TODO add addFunctionDefinition
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return apply(argument, context);
			}
		});
		final TypeVariable identityType = getTypeVariable();
		addFunctorDefinition(IDENTITY, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return arguments[0].eval(context);
			}
		}, identityType, identityType);
		final TypeVariable composeType1 = getTypeVariable();
		final TypeVariable composeType2 = getTypeVariable();
		final TypeVariable composeType3 = getTypeVariable();
		addFunctorDefinition(COMPOSE, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return ((Function) arguments[0].eval(context)).apply(((Function) arguments[1].eval(context)).apply(arguments[2], context), context);
			}
		}, getFunctionType(composeType1, composeType2), getFunctionType(composeType3, composeType1), composeType3, composeType2);
		final TypeVariable flipType1 = getTypeVariable();
		final TypeVariable flipType2 = getTypeVariable();
		final TypeVariable flipType3 = getTypeVariable();
		addFunctorDefinition(FLIP, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[2], context)).apply(arguments[1], context);
			}
		}, getFunctionType(flipType1, getFunctionType(flipType2, flipType3)), flipType2, flipType1, flipType3);
		final TypeVariable duplicateType1 = getTypeVariable();
		final TypeVariable duplicateType2 = getTypeVariable();
		addFunctorDefinition(DUPLICATE, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[1], context)).apply(arguments[1], context);
			}
		}, getFunctionType(duplicateType1, getFunctionType(duplicateType1, duplicateType2)), duplicateType1, duplicateType2);
		final TypeVariable equalType = getTypeVariable();
		addFunctorDefinition(EQUAL, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return arguments[0].eval(context).equals(arguments[1].eval(context)) ? booleanTrue : booleanFalse;
			}
		}, equalType, equalType, booleanType);
		final TypeVariable ifType = getTypeVariable();
		addFunctorDefinition(IF, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return ((Boolean) arguments[0].eval(context)).equals(booleanTrue) ? arguments[1].eval(context) : arguments[2].eval(context);
			}
		}, booleanType, ifType, ifType, ifType);

		addFunctionDefinition(TYPE_OF, getTypeVariable(), typeType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return argument.getType(context);
			}
		});
		addFunctorDefinition(FUNCTION, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return new FunctionType(engine, (Type) arguments[0].eval(context), (Type) arguments[1].eval(context));
			}
		}, typeType, typeType, typeType);

		addFunctionDefinition(IS_NAN, numberType, booleanType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return Double.isNaN(((Number) argument.eval(context)).getValue()) ? booleanTrue : booleanFalse;
			}
		});
		addFunctionDefinition(INTEGER, numberType, numberType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				final double value = ((Number) argument.eval(context)).getValue();
				return new Number(engine, (Double.isInfinite(value) || Double.isNaN(value)) ? value : (int) value);
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(ADD, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number term1, final Number term2, final ScriptContext context) {
				return new Number(engine, term1.getValue() + term2.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(SUBTRACT, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number minuend, final Number subtrahend, final ScriptContext context) {
				return new Number(engine, minuend.getValue() - subtrahend.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(MULTIPLY, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number factor1, final Number factor2, final ScriptContext context) {
				return new Number(engine, factor1.getValue() * factor2.getValue());
			}
		});
		addTwoArgumentArithmeticOperatorDefinition(DIVIDE, numberType, new ApplicableTwoArgumentArithmeticOperator() {
			@Override
			public Literal apply(final Number dividend, final Number divisor, final ScriptContext context) {
				return new Number(engine, dividend.getValue() / divisor.getValue());
			}
		});
		final TypeVariable productType1 = getTypeVariable();
		final TypeVariable productType2 = getTypeVariable();
		addFunctorDefinition(PRODUCT, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return new Pair(engine, arguments[0].eval(context), arguments[1].eval(context));
			}
		}, productType1, productType2, new PairType(engine, productType1, productType2));
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
