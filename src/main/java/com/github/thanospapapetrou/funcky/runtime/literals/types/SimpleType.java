package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing a Funcky simple type.
 * 
 * @author thanos
 */
public class SimpleType extends FunckyType {
	private final String name;

	/**
	 * Construct a new simple type.
	 * 
	 * @param engine
	 *            the engine that generated this simple type
	 * @param script
	 *            the URI of the script from which this simple type was generated
	 * @param name
	 *            the name of this simple type
	 */
	public SimpleType(final FunckyScriptEngine engine, final URI script, final String name) {
		super(engine, script, 0);
		this.name = name;
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		super.bind(bindings);
		return this;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof SimpleType) && name.equals(((SimpleType) object).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		super.inferGenericBindings(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, FunckyType> singletonMap((TypeVariable) type, this);
		} else if (type instanceof SimpleType) {
			return equals(type) ? Collections.<TypeVariable, FunckyType> emptyMap() : null;
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
