package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.net.URI;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.TypeVariable;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.functors.Functor;
import com.github.thanospapapetrou.funcky.runtime.functors.TwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.literals.FunckyBoolean;
import com.github.thanospapapetrou.funcky.runtime.literals.FunckyNumber;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.Pair;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunckyType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.PairType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;

/**
 * Class representing a Funcky prelude.
 * 
 * @author thanos
 */
public class Prelude extends Library {
	private static final String ADD = "add";
	private static final String BOOLEAN = "boolean";
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
	private static final URI PRELUDE = URI.create("funcky:prelude");
	private static final String PRODUCT = "product";
	private static final String RESOURCE = "/Prelude.funcky";
	private static final String SUBTRACT = "subtract";
	private static final String TYPE = "type";
	private static final String TYPE_1 = "type1";
	private static final String TYPE_2 = "type2";
	private static final String TYPE_3 = "type3";
	private static final String TYPE_OF = "typeOf";

	private final SimpleType typeType;
	private final SimpleType numberType;
	private final SimpleType booleanType;
	private final SimpleType characterType;
	private final FunckyBoolean booleanTrue;
	private final FunckyBoolean booleanFalse;

	/**
	 * Construct a new prelude.
	 * 
	 * @param engine
	 *            the engine that generated this prelude
	 * @throws IOException
	 * @throws ScriptException
	 */
	public Prelude(final FunckyScriptEngine engine) throws IOException, ScriptException {
		super(engine, PRELUDE, RESOURCE);
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
		final TypeVariable identityType = generateTypeVariable(TYPE);
		addDefinition(new Functor(engine, PRELUDE, IDENTITY, identityType, identityType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return arguments[0].eval(context);
			}
		});
		final TypeVariable composeType1 = generateTypeVariable(TYPE_1);
		final TypeVariable composeType2 = generateTypeVariable(TYPE_2);
		final TypeVariable composeType3 = generateTypeVariable(TYPE_3);
		addDefinition(new Functor(engine, PRELUDE, COMPOSE, generateFunctionType(composeType1, composeType2), generateFunctionType(composeType3, composeType1), composeType3, composeType2) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return ((Function) arguments[0].eval(context)).apply(((Function) arguments[1].eval(context)).apply(arguments[2], context), context);
			}
		});
		final TypeVariable flipType1 = generateTypeVariable(TYPE_1);
		final TypeVariable flipType2 = generateTypeVariable(TYPE_2);
		final TypeVariable flipType3 = generateTypeVariable(TYPE_3);
		addDefinition(new Functor(engine, PRELUDE, FLIP, generateFunctionType(flipType1, generateFunctionType(flipType2, flipType3)), flipType2, flipType1, flipType3) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[2], context)).apply(arguments[1], context);
			}
		});
		final TypeVariable duplicateType1 = generateTypeVariable(TYPE_1);
		final TypeVariable duplicateType2 = generateTypeVariable(TYPE_2);
		addDefinition(new Functor(engine, PRELUDE, DUPLICATE, generateFunctionType(duplicateType1, generateFunctionType(duplicateType1, duplicateType2)), duplicateType1, duplicateType2) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return ((Function) ((Function) arguments[0].eval(context)).apply(arguments[1], context)).apply(arguments[1], context);
			}
		});
		final TypeVariable equalType = generateTypeVariable(TYPE);
		addDefinition(new Functor(engine, PRELUDE, EQUAL, equalType, equalType, booleanType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return arguments[0].eval(context).equals(arguments[1].eval(context)) ? booleanTrue : booleanFalse;
			}
		});
		final TypeVariable ifType = generateTypeVariable(TYPE);
		addDefinition(new Functor(engine, PRELUDE, IF, booleanType, ifType, ifType, ifType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return ((FunckyBoolean) arguments[0].eval(context)).equals(booleanTrue) ? arguments[1].eval(context) : arguments[2].eval(context);
			}
		});
		final TypeVariable typeOfType = generateTypeVariable(TYPE);
		addDefinition(new Function(engine, PRELUDE, TYPE_OF, generateFunctionType(typeOfType, typeType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException, AlreadyDefinedSymbolException {
				super.apply(argument, context);
				return argument.getType(context); // TODO use special constructor for this
			}
		});
		addDefinition(new Functor(engine, PRELUDE, FUNCTION, typeType, typeType, typeType) {
			@Override
			protected Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(context, arguments);
				return new FunctionType(engine, (FunckyType) arguments[0].eval(context), (FunckyType) arguments[1].eval(context));
			}
		});
		addDefinition(new Function(engine, PRELUDE, IS_NAN, generateFunctionType(numberType, booleanType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(argument, context);
				return Double.isNaN(((FunckyNumber) argument.eval(context)).getValue()) ? booleanTrue : booleanFalse;
			}
		});
		addDefinition(new Function(engine, PRELUDE, INTEGER, generateFunctionType(numberType, numberType)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
				super.apply(argument, context);
				final double value = ((FunckyNumber) argument.eval(context)).getValue();
				return new FunckyNumber(engine, (Double.isInfinite(value) || Double.isNaN(value)) ? value : (int) value);
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, ADD, numberType) {
			@Override
			protected Literal apply(final FunckyNumber term1, final FunckyNumber term2, final ScriptContext context) {
				super.apply(term1, term2, context);
				return new FunckyNumber(engine, term1.getValue() + term2.getValue());
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, SUBTRACT, numberType) {
			@Override
			protected Literal apply(final FunckyNumber minuend, final FunckyNumber subtrahend, final ScriptContext context) {
				super.apply(minuend, subtrahend, context);
				return new FunckyNumber(engine, minuend.getValue() - subtrahend.getValue());
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, MULTIPLY, numberType) {
			@Override
			protected Literal apply(final FunckyNumber factor1, final FunckyNumber factor2, final ScriptContext context) {
				super.apply(factor1, factor2, context);
				return new FunckyNumber(engine, factor1.getValue() * factor2.getValue());
			}
		});
		addDefinition(new TwoArgumentArithmeticOperator(engine, PRELUDE, DIVIDE, numberType) {
			@Override
			protected Literal apply(final FunckyNumber dividend, final FunckyNumber divisor, final ScriptContext context) {
				super.apply(dividend, divisor, context);
				return new FunckyNumber(engine, dividend.getValue() / divisor.getValue());
			}
		});
		final TypeVariable productType1 = generateTypeVariable(TYPE_1);
		final TypeVariable productType2 = generateTypeVariable(TYPE_2);
		addDefinition(new Functor(engine, PRELUDE, PRODUCT, productType1, productType2, new PairType(engine, productType1, productType2)) {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
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
	public FunckyBoolean getFalse() {
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
	public FunckyBoolean getTrue() {
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

	private FunckyBoolean generateBoolean(final boolean value) {
		return new FunckyBoolean(engine, PRELUDE, value);
	}

	private FunckyNumber generateNumber(final double value) {
		return new FunckyNumber(engine, PRELUDE, value);
	}

	private SimpleType generateSimpleType(final String name) {
		return new SimpleType(engine, PRELUDE, name);
	}
}
