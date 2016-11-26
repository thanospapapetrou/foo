package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.net.URI;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
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

	/**
	 * Bottom function.
	 */
	public static final String BOTTOM = "bottom";

	/**
	 * Compose two functions.
	 */
	public static final String COMPOSE = "compose";

	/**
	 * Duplicate the argument of a function.
	 */
	public static final String DUPLICATE = "duplicate";

	/**
	 * Flip the arguments of a function.
	 */
	public static final String FLIP = "flip";

	/**
	 * Construct a function type.
	 */
	public static final String FUNCTION = "function";

	/**
	 * Identity function.
	 */
	public static final String IDENTITY = "identity";
	public static final String LIST = "list";
	public static final String PAIR = "pair";
	public static final String PRODUCT = "product";

	/**
	 * Type of types.
	 */
	public static final String TYPE = "type";

	/**
	 * Get the type of an expression.
	 */
	public static final String TYPE_OF = "typeOf";

	/**
	 * Construct a new prelude library.
	 * 
	 * @param engine
	 *            the engine constructing this prelude library
	 * @throws ScriptException
	 *             if any errors occur while constructing this prelude library
	 */
	public Prelude(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		final SimpleType typeType = getSimpleType(PRELUDE, TYPE);
		addDefinition(typeType);
		addFunctionDefinition(BOTTOM, getTypeVariable(), getTypeVariable(), new ApplicableFunction() {
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
		addFunctionDefinition(TYPE_OF, getTypeVariable(), typeType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return argument.getType(context);
			}
		});
		addFunctorDefinition(FUNCTION, new ApplicableFunctor() {
			@Override
			public FunctionType apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return new FunctionType(engine, (Type) arguments[0].eval(context), (Type) arguments[1].eval(context));
			}
		}, typeType, typeType, typeType);
		final TypeVariable productType1 = getTypeVariable();
		final TypeVariable productType2 = getTypeVariable();
		addFunctorDefinition(PRODUCT, new ApplicableFunctor() {
			@Override
			public Pair apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return new Pair(engine, arguments[0].eval(context), arguments[1].eval(context));
			}
		}, productType1, productType2, new PairType(engine, productType1, productType2));
	}
}
