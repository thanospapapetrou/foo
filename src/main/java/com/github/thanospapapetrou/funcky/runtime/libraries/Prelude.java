package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.TypeVariable;

/**
 * Class representing the Funcky prelude.
 * 
 * @author thanos
 */
public class Prelude extends Library {
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

	/**
	 * Type of types.
	 */
	public static final String TYPE = "type";

	/**
	 * Get the type of an expression.
	 */
	public static final String TYPE_OF = "typeOf";

	private static final URI PRELUDE = URI.create("funcky:prelude");

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
		final SimpleType typeType = new SimpleType(engine, PRELUDE, TYPE);
		addDefinition(typeType);
		addFunctionDefinition(BOTTOM, engine.getTypeVariable(), engine.getTypeVariable(), new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return apply(argument);
			}
		});
		final TypeVariable identityType = engine.getTypeVariable();
		addFunctorDefinition(IDENTITY, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return arguments[0].eval();
			}
		}, identityType, identityType);
		final TypeVariable composeType1 = engine.getTypeVariable();
		final TypeVariable composeType2 = engine.getTypeVariable();
		final TypeVariable composeType3 = engine.getTypeVariable();
		addFunctorDefinition(COMPOSE, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return ((Function) arguments[0].eval()).apply(((Function) arguments[1].eval()).apply(arguments[2]));
			}
		}, engine.getFunctionType(composeType1, composeType2), engine.getFunctionType(composeType3, composeType1), composeType3, composeType2);
		final TypeVariable flipType1 = engine.getTypeVariable();
		final TypeVariable flipType2 = engine.getTypeVariable();
		final TypeVariable flipType3 = engine.getTypeVariable();
		addFunctorDefinition(FLIP, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return ((Function) ((Function) arguments[0].eval()).apply(arguments[2])).apply(arguments[1]);
			}
		}, engine.getFunctionType(flipType1, engine.getFunctionType(flipType2, flipType3)), flipType2, flipType1, flipType3);
		final TypeVariable duplicateType1 = engine.getTypeVariable();
		final TypeVariable duplicateType2 = engine.getTypeVariable();
		addFunctorDefinition(DUPLICATE, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return ((Function) ((Function) arguments[0].eval()).apply(arguments[1])).apply(arguments[1]);
			}
		}, engine.getFunctionType(duplicateType1, engine.getFunctionType(duplicateType1, duplicateType2)), duplicateType1, duplicateType2);
		addFunctionDefinition(TYPE_OF, engine.getTypeVariable(), typeType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return argument.getType();
			}
		});
		addFunctorDefinition(FUNCTION, new ApplicableFunctor() {
			@Override
			public FunctionType apply(final Expression... arguments) throws ScriptException {
				return engine.getFunctionType((Type) arguments[0].eval(), (Type) arguments[1].eval());
			}
		}, typeType, typeType, typeType);
	}
}
