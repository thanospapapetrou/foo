package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Class representing a Funcky simple type.
 * 
 * @author thanos
 */
public class SimpleType extends Type {
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
	public Type bind(final Map<TypeVariable, Type> bindings) {
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
	public Map<TypeVariable, Type> inferGenericBindings(final Type type) {
		super.inferGenericBindings(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, Type> singletonMap((TypeVariable) type, this);
		} else if (type instanceof SimpleType) {
			return equals(type) ? Collections.<TypeVariable, Type> emptyMap() : null;
		}
		return null;
	}
	
	@Override
	public Reference toExpression() {
		return new Reference(engine, script, name);
	}

	@Override
	protected SimpleType free(final Map<TypeVariable, TypeVariable> free) {
		super.free(free);
		return this;
	}
}
