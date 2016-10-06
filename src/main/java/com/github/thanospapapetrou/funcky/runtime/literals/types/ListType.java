package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.libraries.Prelude;

/**
 * Class representing a Funcky list type.
 * 
 * @author thanos
 */
public class ListType extends Type {
	private static final String LIST = "List";
	private static final String NULL_ELEMENT = "Element must not be null";

	private final Type element;

	/**
	 * Construct a new list type.
	 * 
	 * @param engine
	 *            the engine that generated this list type
	 * @param element
	 *            the type of the element of this list type
	 */
	public ListType(final FunckyScriptEngine engine, final Type element) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
		this.element = Objects.requireNonNull(element, NULL_ELEMENT);
	}

	@Override
	public Type bind(final Map<TypeVariable, Type> bindings) {
		super.bind(bindings);
		return new ListType(engine, element.bind(bindings));
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof ListType) && element.equals(((ListType) object).element);
	}

	/**
	 * Get the element type of this list type.
	 * 
	 * @return the element type
	 */
	public Type getElement() {
		return element;
	}

	@Override
	public int hashCode() {
		return element.hashCode();
	}

	@Override
	public Map<TypeVariable, Type> inferGenericBindings(final Type type) {
		super.inferGenericBindings(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, Type> singletonMap((TypeVariable) type, this);
		} else if (type instanceof ListType) {
			return element.inferGenericBindings(((ListType) type).element);
		}
		return null;
	}

	@Override
	public Application toExpression() {
		return new Application(engine, new Reference(engine, Prelude.PRELUDE, LIST), element); // TODO use Prelude.LIST
	}

	@Override
	protected ListType free(final Map<TypeVariable, TypeVariable> free) {
		super.free(free);
		return new ListType(engine, element.free(free));
	}
}
