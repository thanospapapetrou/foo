package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.net.URI;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyException;
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
	public static final URI PRELUDE = URI.create("funcky:prelude");

	private static final String ADD = "add";
	private static final String BOOLEAN = "boolean";
	private static final String BOTTOM = "bottom";
	private static final String CHARACTER = "character";
	private static final String COMPOSE = "compose";
	private static final String DIVIDE = "divide";
	private static final String DUPLICATE = "duplicate";
	private static final String EQUAL = "equal";
	private static final String FUNCTION = "function";
	private static final String IDENTITY = "identity";
	private static final String IF = "if";
	private static final String INTEGER = "integer";
	private static final String IS_NAN = "isNaN";
	private static final String FLIP = "flip";
	private static final String MULTIPLY = "multiply";
	private static final String NUMBER = "number";
	private static final String PRODUCT = "product";
	private static final String SUBTRACT = "subtract";
	private static final String TYPE = "type";
	private static final String TYPE_OF = "typeOf";

	private final SimpleType typeType;
	private final SimpleType numberType;
	private final SimpleType booleanType;
	private final SimpleType characterType;
	private final Boolean booleanTrue;
	private final Boolean booleanFalse;

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
		typeType = generateSimpleType(TYPE);
		addDefinition(typeType);
		numberType = generateSimpleType(NUMBER);
		addDefinition(numberType);
		booleanType = generateSimpleType(BOOLEAN);
		addDefinition(booleanType);
		characterType = generateSimpleType(CHARACTER);
		addDefinition(characterType);
		// numbers
		addDefinition(generateNumber(Double.POSITIVE_INFINITY));
		addDefinition(generateNumber(Double.NaN));
		// booleans
		booleanTrue = generateBoolean(true);
		addDefinition(booleanTrue);
		booleanFalse = generateBoolean(false);
		addDefinition(booleanFalse);
		// functions
		final TypeVariable bottomType1 = generateTypeVariable();
		final TypeVariable bottomType2 = generateTypeVariable();
		addDefinition(new Function(engine, PRELUDE, BOTTOM, generateFunctionType(bottomType1, bottomType2)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws FunckyException {
				super.apply(argument, context);
				return apply(argument, context);
			}
		});
		final TypeVariable identityType = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, IDENTITY, identityType, identityType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return arguments[0].eval(context);
			}
		});
		final TypeVariable composeType1 = generateTypeVariable();
		final TypeVariable composeType2 = generateTypeVariable();
		final TypeVariable composeType3 = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, COMPOSE, generateFunctionType(composeType1, composeType2), generateFunctionType(composeType3, composeType1), composeType3, composeType2) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return ((Function) arguments[0].eval(context)).apply(((Function) arguments[1].eval(context)).apply(arguments[2], context), context);
			}
		});
		final TypeVariable flipType1 = generateTypeVariable();
		final TypeVariable flipType2 = generateTypeVariable();
		final TypeVariable flipType3 = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, FLIP, generateFunctionType(flipType1, generateFunctionType(flipType2, flipType3)), flipType2, flipType1, flipType3) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[2], context)).apply(arguments[1], context);
			}
		});
		final TypeVariable duplicateType1 = generateTypeVariable();
		final TypeVariable duplicateType2 = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, DUPLICATE, generateFunctionType(duplicateType1, generateFunctionType(duplicateType1, duplicateType2)), duplicateType1, duplicateType2) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[1], context)).apply(arguments[1], context);
			}
		});
		final TypeVariable equalType = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, EQUAL, equalType, equalType, booleanType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return arguments[0].eval(context).equals(arguments[1].eval(context)) ? booleanTrue : booleanFalse;
			}
		});
		final TypeVariable ifType = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, IF, booleanType, ifType, ifType, ifType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return ((Boolean) arguments[0].eval(context)).equals(booleanTrue) ? arguments[1].eval(context) : arguments[2].eval(context);
			}
		});
		final TypeVariable typeOfType = generateTypeVariable();
		addDefinition(new Function(engine, PRELUDE, TYPE_OF, generateFunctionType(typeOfType, typeType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws FunckyException {
				super.apply(argument, context);
				return argument.getType(context);
			}
		});
		addDefinition(new Functor(engine, PRELUDE, FUNCTION, typeType, typeType, typeType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return new FunctionType(engine, (Type) arguments[0].eval(context), (Type) arguments[1].eval(context));
			}
		});
		addDefinition(new Function(engine, PRELUDE, IS_NAN, generateFunctionType(numberType, booleanType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws FunckyException {
				super.apply(argument, context);
				return Double.isNaN(((Number) argument.eval(context)).getValue()) ? booleanTrue : booleanFalse;
			}
		});
		addDefinition(new Function(engine, PRELUDE, INTEGER, generateFunctionType(numberType, numberType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws FunckyException {
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
		final TypeVariable productType1 = generateTypeVariable();
		final TypeVariable productType2 = generateTypeVariable();
		addDefinition(new Functor(engine, PRELUDE, PRODUCT, productType1, productType2, new PairType(engine, productType1, productType2)) {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws FunckyException {
				super.apply(context, arguments);
				return new Pair(engine, arguments[0].eval(context), arguments[1].eval(context));
			}
		});
	}

	/**
	 * Get the boolean type defined by this prelude.
	 * 
	 * @return the boolean type
	 */
	public SimpleType getBoolean() {
		return booleanType;
	}

	/**
	 * Get the character type defined by this prelude.
	 * 
	 * @return the character type
	 */
	public SimpleType getCharacter() {
		return characterType;
	}

	/**
	 * Get the boolean false defined by this prelude.
	 * 
	 * @return the boolean false
	 */
	public Boolean getFalse() {
		return booleanFalse;
	}

	/**
	 * Get the number type defined by this prelude.
	 * 
	 * @return the number type;
	 */
	public SimpleType getNumber() {
		return numberType;
	}

	/**
	 * Get the boolean true defined by this prelude.
	 * 
	 * @return the boolean true
	 */
	public Boolean getTrue() {
		return booleanTrue;
	}

	/**
	 * Get the type type defined by this prelude.
	 * 
	 * @return the type type
	 */
	public SimpleType getType() {
		return typeType;
	}

	private Boolean generateBoolean(final boolean value) {
		return new Boolean(engine, PRELUDE, value);
	}

	private Number generateNumber(final double value) {
		return new Number(engine, PRELUDE, value);
	}

	private SimpleType generateSimpleType(final String name) {
		return new SimpleType(engine, PRELUDE, name);
	}
}
