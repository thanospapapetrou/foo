package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Class representing a Funcky list type.
 * 
 * @author thanos
 */
public class ListType extends FunckyType {
	private static final String LIST = "List";
	private static final String NULL_ELEMENT = "Element must not be null";

	private final FunckyType element;

	/**
	 * Construct a new list type.
	 * 
	 * @param engine
	 *            the engine that generated this list type
	 * @param element
	 *            the type of the element of this list type
	 */
	public ListType(final FunckyScriptEngine engine, final FunckyType element) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
		this.element = Objects.requireNonNull(element, NULL_ELEMENT);
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
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
	public FunckyType getElement() {
		return element;
	}

	@Override
	public int hashCode() {
		return element.hashCode();
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		super.inferGenericBindings(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, FunckyType> singletonMap((TypeVariable) type, this);
		} else if (type instanceof ListType) {
			return element.inferGenericBindings(((ListType) type).element);
		}
		return null;
	}

	@Override
	public Expression toExpression() {
		return new Application(engine, new Reference(engine, LIST), element);
	}

	@Override
	public String toString() {
		return toExpression().toString();
	}
}
