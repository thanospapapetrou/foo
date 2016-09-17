package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing a Funcky type variable.
 * 
 * @author thanos
 */
public class TypeVariable extends FunckyType {
	private static final String NAME = "type$%1$s%2$s";
	private static final String TYPE_VARIABLE = "<%1$s>";

	private final String name;

	private static final String generateRandomName() {
		final UUID uuid = UUID.randomUUID();
		return String.format(NAME, Long.toHexString(uuid.getMostSignificantBits()), Long.toHexString(uuid.getLeastSignificantBits()));
	}

	/**
	 * Construct a new type variable.
	 * 
	 * @param engine
	 *            the engine that generated this type variable
	 * @param script
	 *            the URI of the script from which this type variable was generated
	 * @param lineNumber
	 *            the number of the line from which this type variable was parsed or <code>0</code> if this type was not parsed (is builtin or generated at runtime)
	 * @param name
	 *            the name of this type variable
	 */
	public TypeVariable(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String name) {
		super(engine, script, lineNumber);
		this.name = name;
	}

	/**
	 * Construct a new type variable with a random name generated at runtime.
	 * 
	 * @param engine
	 *            the engine that parsed this type variable
	 */
	public TypeVariable(final FunckyScriptEngine engine) {
		this(engine, FunckyScriptEngine.RUNTIME, 0, generateRandomName());
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
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
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		super.inferGenericBindings(type);
		return Collections.singletonMap(this, type.bind(Collections.<TypeVariable, FunckyType> singletonMap(this, new TypeVariable(engine))));
	}

	@Override
	public String toString() {
		return String.format(TYPE_VARIABLE, name);
	}
}
