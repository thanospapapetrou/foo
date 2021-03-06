package com.github.thanospapapetrou.funcky.runtime.expressions.literals.types;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing a Funcky type variable.
 * 
 * @author thanos
 */
public class TypeVariable extends Type {
	private static final String TYPE_VARIABLE = "$%1$s";

	private final String name;

	/**
	 * Construct a new type variable.
	 * 
	 * @param engine
	 *            the engine that generated this type variable
	 * @param script
	 *            the URI of the script from which this type variable was generated
	 * @param line
	 *            the line from which this type variable was parsed or <code>-1</code> if this type was not parsed (is built-in or generated at runtime)
	 * @param name
	 *            the name of this type variable
	 */
	public TypeVariable(final FunckyScriptEngine engine, final URI script, final int line, final String name) {
		super(engine, script, line);
		this.name = name;
	}

	@Override
	public Type bind(final Map<TypeVariable, Type> bindings) {
		super.bind(bindings);
		return bindings.containsKey(this) ? bindings.get(this) : this;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof TypeVariable) && name.equals(((TypeVariable) object).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public Map<TypeVariable, Type> infer(final Type type) {
		super.infer(type);
		return Collections.singletonMap(this, type);
	}

	@Override
	public String toString() {
		return String.format(TYPE_VARIABLE, name);
	}

	@Override
	protected TypeVariable free(final Map<TypeVariable, TypeVariable> free) {
		super.free(free);
		if (!free.containsKey(this)) {
			free.put(this, engine.getTypeVariable());
		}
		return free.get(this);
	}
}
