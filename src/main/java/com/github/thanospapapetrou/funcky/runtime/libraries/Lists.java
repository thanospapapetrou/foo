package com.github.thanospapapetrou.funcky.runtime.libraries;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Expression;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.List;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.ListType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.TypeVariable;

/**
 * Lists related library.
 * 
 * @author thanos
 */
public class Lists extends Library {
	/**
	 * Append an element to a list.
	 */
	public static final String APPEND = "append";

	/**
	 * Get the element type of a list type.
	 */
	public static final String ELEMENT = "element";

	/**
	 * Empty list.
	 */
	public static final String EMPTY = "empty";

	/**
	 * Get the head of a list.
	 */
	public static final String HEAD = "head";

	/**
	 * Construct a new list type.
	 */
	public static final String LIST = "list";

	/**
	 * Prepend an element to a list.
	 */
	public static final String PREPEND = "prepend";

	/**
	 * Get the tail of a list.
	 */
	public static final String TAIL = "tail";

	/**
	 * Construct a new lists library.
	 * 
	 * @param engine
	 *            the engine constructing this lists library
	 * @throws ScriptException
	 *             if any errors occur while constructing this lists library
	 */
	public Lists(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		final SimpleType type = engine.getReference(Prelude.class, Prelude.TYPE).evaluate(SimpleType.class);
		addFunctionDefinition(LIST, type, type, new ApplicableFunction() {
			@Override
			public ListType apply(final Expression argument) throws ScriptException {
				return engine.getListType(argument.evaluate(Type.class));
			}
		});
		addFunctionDefinition(ELEMENT, type, type, new ApplicableFunction() {
			@Override
			public Type apply(final Expression argument) throws ScriptException {
				return ((ListType) argument.eval()).getElement(); // TODO validate that argument is list type
			}
		});
		addFunctionDefinition(EMPTY, type, engine.getListType(engine.getTypeVariable()), new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return engine.getList(argument.evaluate(Type.class));
			}
		});
		final TypeVariable headElementType = engine.getTypeVariable();
		final ListType headListType = engine.getListType(headElementType);
		addFunctionDefinition(HEAD, headListType, headElementType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return argument.evaluate(List.class).getHead(); // TODO validate that argument is not empty
			}
		});
		final ListType tailType = engine.getListType(engine.getTypeVariable());
		addFunctionDefinition(TAIL, tailType, tailType, new ApplicableFunction() {
			@Override
			public List apply(final Expression argument) throws ScriptException {
				return argument.evaluate(List.class).getTail(); // TODO validate that argument is not empty
			}
		});
		final TypeVariable prependElementType = engine.getTypeVariable();
		final ListType prependListType = engine.getListType(prependElementType);
		addFunctorDefinition(PREPEND, new ApplicableFunctor() {
			@Override
			public List apply(final Expression... arguments) throws ScriptException {
				return engine.getList(arguments[0].eval(), arguments[1].evaluate(List.class));
			}
		}, prependElementType, prependListType, prependListType);
		// TODO remove append and define it in funcky
		final TypeVariable appendElementType = engine.getTypeVariable();
		final ListType appendListType = engine.getListType(appendElementType);
		addFunctorDefinition(APPEND, new ApplicableFunctor() {
			@Override
			public List apply(final Expression... arguments) throws ScriptException {
				final List list = arguments[0].evaluate(List.class);
				final Literal element = arguments[1].eval();
				final List empty = engine.getApplication(engine.getReference(Lists.class, EMPTY), engine.getTypeVariable()).evaluate(List.class);
				return list.equals(empty) ? engine.getList(element, empty) : engine.getList(list.getHead(), apply(list.getTail(), element));
			}
		}, appendListType, appendElementType, appendListType);
	}
}
