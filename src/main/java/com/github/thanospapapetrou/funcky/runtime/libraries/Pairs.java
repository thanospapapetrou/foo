package com.github.thanospapapetrou.funcky.runtime.libraries;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Pair;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.PairType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.TypeVariable;

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
		final SimpleType type = engine.getReference(Prelude.class, Prelude.TYPE).evaluate(SimpleType.class);
		addFunctorDefinition(PAIR, new ApplicableFunctor() {
			@Override
			public PairType apply(final Expression... arguments) throws ScriptException {
				return engine.getPairType(arguments[0].evaluate(Type.class), arguments[1].evaluate(Type.class));
			}
		}, type, type, type);
		addFunctionDefinition(FIRST_TYPE, type, type, new ApplicableFunction() {
			@Override
			public Type apply(final Expression argument) throws ScriptException { // TODO validate that argument is pair type
				return ((PairType) argument.eval()).getFirst();
			}
		});
		addFunctionDefinition(SECOND_TYPE, type, type, new ApplicableFunction() {
			@Override
			public Type apply(final Expression argument) throws ScriptException { // TODO validate that argument is pair type
				return ((PairType) argument.eval()).getSecond();
			}
		});
		final TypeVariable combineType1 = engine.getTypeVariable();
		final TypeVariable combineType2 = engine.getTypeVariable();
		addFunctorDefinition(COMBINE, new ApplicableFunctor() {
			@Override
			public Pair apply(final Expression... arguments) throws ScriptException {
				return engine.getPair(arguments[0].eval(), arguments[1].eval());
			}
		}, combineType1, combineType2, engine.getPairType(combineType1, combineType2));
		final TypeVariable firstType = engine.getTypeVariable();
		addFunctionDefinition(FIRST, engine.getPairType(firstType, engine.getTypeVariable()), firstType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return argument.evaluate(Pair.class).getFirst();
			}
		});
		final TypeVariable secondType = engine.getTypeVariable();
		addFunctionDefinition(SECOND, engine.getPairType(engine.getTypeVariable(), secondType), secondType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return argument.evaluate(Pair.class).getSecond();
			}
		});
	}
}
