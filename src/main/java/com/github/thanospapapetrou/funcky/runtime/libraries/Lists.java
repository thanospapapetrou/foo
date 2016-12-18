package com.github.thanospapapetrou.funcky.runtime.libraries;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.literals.List;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.ListType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

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
		final SimpleType type = engine.getLiteral(Prelude.class, Prelude.TYPE);
		addFunctionDefinition(LIST, type, type, new ApplicableFunction() {
			@Override
			public ListType apply(final Expression argument) throws ScriptException {
				return engine.getListType((Type) argument.eval());
			}
		});
		addFunctionDefinition(ELEMENT, type, type, new ApplicableFunction() {
			@Override
			public Type apply(final Expression argument) throws ScriptException {
				return ((ListType) argument.eval()).getElement(); // TODO validate that argument is list type
			}
		});
		final TypeVariable headElementType = engine.getTypeVariable();
		final ListType headListType = engine.getListType(headElementType);
		addDefinition(EMPTY, engine.getList());
		addFunctionDefinition(HEAD, headListType, headElementType, new ApplicableFunction() {
			@Override
			public Literal apply(final Expression argument) throws ScriptException {
				return ((List) argument.eval()).getHead(); // TODO validate that argument is not empty
			}
		});
		final ListType tailType = engine.getListType(engine.getTypeVariable());
		addFunctionDefinition(TAIL, tailType, tailType, new ApplicableFunction() {
			@Override
			public List apply(final Expression argument) throws ScriptException {
				return ((List) argument.eval()).getTail(); // TODO validate that argument is not empty
			}
		});
		final TypeVariable prependElementType = engine.getTypeVariable();
		final ListType prependListType = engine.getListType(prependElementType);
		addFunctorDefinition(PREPEND, new ApplicableFunctor() {
			@Override
			public List apply(final Expression... arguments) throws ScriptException {
				return engine.getList(arguments[0].eval(), (List) arguments[1].eval());
			}
		}, prependElementType, prependListType, prependListType);
		// TODO remove append and define it in funcky
		final TypeVariable appendElementType = engine.getTypeVariable();
		final ListType appendListType = engine.getListType(appendElementType);
		addFunctorDefinition(APPEND, new ApplicableFunctor() {
			@Override
			public List apply(final Expression... arguments) throws ScriptException {
				final List list = (List) arguments[0].eval();
				final Literal element = arguments[1].eval();
				final List empty = engine.getLiteral(Lists.class, EMPTY);
				return list.equals(empty) ? engine.getList(element, empty) : engine.getList(list.getHead(), apply(list.getTail(), element));
			}
		}, appendListType, appendElementType, appendListType);
	}
}
