package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.libraries.Prelude;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;

/**
 * Abstract class representing a Funcky type.
 * 
 * @author thanos
 */
public abstract class Type extends Literal {
	private static final String NULL_BINDINGS = "Bindings must not be null";
	private static final String NULL_FREE = "Free must not be null";
	private static final String NULL_TYPE = "Type must not be null";

	/**
	 * Construct a new type.
	 * 
	 * @param engine
	 *            the engine that generated this type
	 * @param script
	 *            the URI of the script from which this type was generated
	 * @param line
	 *            the line from which this type was parsed or <code>-1</code> if this type was not parsed (is built-in or generated at runtime)
	 */
	protected Type(final FunckyScriptEngine engine, final URI script, final int line) {
		super(engine, script, line);
	}

	/**
	 * Bind any type variables occurring in this type by replacing each of them with another type.
	 * 
	 * @param bindings
	 *            a map containing each type variable of this type along with the type to bind it to
	 * @return the type that occurs after binding any type variable occurring in this type
	 */
	public Type bind(final Map<TypeVariable, Type> bindings) {
		Objects.requireNonNull(bindings, NULL_BINDINGS);
		return null;
	};

	@Override
	public SimpleType getType() throws ScriptException {
		return engine.getLiteral(Prelude.class, Prelude.TYPE);
	}

	/**
	 * Free any type variables occurring in this type by replacing each of them with a new unbound type variable.
	 * 
	 * @return the type that occurs after freeing any type variable occurring in this type
	 */
	public Type free() {
		return free(new HashMap<TypeVariable, TypeVariable>());
	}

	/**
	 * Infer the generic bindings that derive when binding this type to another type.
	 * 
	 * @param type
	 *            the type to bind this type to
	 * @return a map containing each type variable of this type along with the type it should be bound with, or <code>null</code> if this type can not be bound to the given type
	 */
	public Map<TypeVariable, Type> inferGenericBindings(final Type type) {
		Objects.requireNonNull(type, NULL_TYPE);
		return null;
	}

	/**
	 * Free any type variables occurring in this type by replacing each of them with a new unbound type variable.
	 * 
	 * @param free
	 *            a map containing each already free type variable along with the unbound variable it has been replaced with
	 * @return the type that occurs after freeing any type variable occurring in this type
	 */
	protected Type free(final Map<TypeVariable, TypeVariable> free) {
		Objects.requireNonNull(free, NULL_FREE);
		return null;
	}
}
