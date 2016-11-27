package com.github.thanospapapetrou.funcky.runtime.libraries;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.Pair;
import com.github.thanospapapetrou.funcky.runtime.literals.types.PairType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Pairs related library.
 * 
 * @author thanos
 */
public class Pairs extends Library {
	/**
	 * Construct a new pair.
	 */
	public static final String COMBINE = "combine";

	/**
	 * Get the first component of a pair.
	 */
	public static final String FIRST = "first";

	/**
	 * Get the first type of a pair type.
	 */
	public static final String FIRST_TYPE = "firstType";

	/**
	 * Get the second component of a pair.
	 */
	public static final String SECOND = "second";

	/**
	 * Get the second type of a pair type.
	 */
	public static final String SECOND_TYPE = "secondType";

	/**
	 * Construct a new pair type.
	 */
	public static final String PAIR = "pair";

	/**
	 * Construct a new pairs library.
	 * 
	 * @param engine
	 *            the engine constructing this pairs library
	 * @throws ScriptException
	 *             if any errors occur while constructing a pairs library
	 */
	public Pairs(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		final SimpleType type = (SimpleType) engine.getReference(Prelude.class, Prelude.TYPE).eval();
		addFunctorDefinition(PAIR, new ApplicableFunctor() {
			@Override
			public PairType apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return new PairType(engine, ((Type) arguments[0].eval(context)), ((Type) arguments[1].eval(context)));
			}
		}, type, type, type);
		addFunctionDefinition(FIRST_TYPE, type, type, new ApplicableFunction() {
			@Override
			public Type apply(final Expression argument, final ScriptContext context) throws ScriptException { // TODO validate that argument is pair type
				return ((PairType) argument.eval(context)).getFirst();
			}
		});
		addFunctionDefinition(SECOND_TYPE, type, type, new ApplicableFunction() {
			@Override
			public Type apply(final Expression argument, final ScriptContext context) throws ScriptException { // TODO validate that argument is pair type
				return ((PairType) argument.eval(context)).getSecond();
			}
		});
		final TypeVariable combineType1 = getTypeVariable();
		final TypeVariable combineType2 = getTypeVariable();
		addFunctorDefinition(COMBINE, new ApplicableFunctor() {
			@Override
			public Pair apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return new Pair(engine, arguments[0].eval(context), arguments[1].eval(context));
			}
		}, combineType1, combineType2, new PairType(engine, combineType1, combineType2));
		final TypeVariable firstType = getTypeVariable();
		addFunctionDefinition(FIRST, new PairType(engine, firstType, getTypeVariable()), firstType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return ((Pair) argument.eval(context)).getFirst();
			}
		});
		final TypeVariable secondType = getTypeVariable();
		addFunctionDefinition(SECOND, new PairType(engine, getTypeVariable(), secondType), secondType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				return ((Pair) argument.eval(context)).getSecond();
			}
		});
	}
}
