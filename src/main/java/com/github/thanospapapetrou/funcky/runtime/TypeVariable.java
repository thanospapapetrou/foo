package com.github.thanospapapetrou.funcky.runtime;

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
	private static final String RANDOM_NAME = "type$%1$s%2$s";
	private static final String TYPE_VARIABLE = "<%1$s>";

	private final String name;

	private static final String generateRandomName() {
		final UUID uuid = UUID.randomUUID();
		return String.format(RANDOM_NAME, Long.toHexString(uuid.getMostSignificantBits()), Long.toHexString(uuid.getLeastSignificantBits()));
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
	 * Construct a new type variable with a random name.
	 * 
	 * @param engine
	 *            the engine that parsed this type variable
	 * @param script
	 *            the URI of the script from which this type variable was parsed
	 * @param lineNumber
	 *            the number of the line from which this type variable was parsed or <code>0</code> if this type was not parsed from any line (it is builtin)
	 */
	public TypeVariable(final FunckyScriptEngine engine, final URI script, final int lineNumber) {
		this(engine, script, lineNumber, generateRandomName());
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		super.bind(bindings);
		return bindings.containsKey(this) ? bindings.get(this) : this;
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		super.inferGenericBindings(type);
		return Collections.singletonMap(this, type.bind(Collections.<TypeVariable, FunckyType> singletonMap(this, new TypeVariable(engine, script, lineNumber))));
	}

	@Override
	public Expression toExpression() {
		return this;
	}

	@Override
	public String toString() {
		return String.format(TYPE_VARIABLE, name);
	}
}
