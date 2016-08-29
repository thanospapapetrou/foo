package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Abstract class representing a Funcky type.
 * 
 * @author thanos
 */
public abstract class FunckyType extends Literal {
	private static final String NULL_BINDINGS = "Bindings must not be null";
	private static final String NULL_TYPE = "Type must not be null";
	private static final String TYPE = "type";

	/**
	 * Construct a new type.
	 * 
	 * @param engine
	 *            the engine that parsed this type
	 * @param script
	 *            the URI of the script from which this type was parsed
	 * @param lineNumber
	 *            the number of the line from which this type was parsed or <code>0</code> if this type was not parsed from any line (it is builtin)
	 */
	protected FunckyType(final FunckyScriptEngine engine, final URI script, final int lineNumber) {
		super(engine, script, lineNumber);
	}

	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		Objects.requireNonNull(bindings, NULL_BINDINGS);
		return null;
	};

	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		Objects.requireNonNull(type, NULL_TYPE);
		return null;
	}

	@Override
	public SimpleType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return (SimpleType) new Reference(engine, script, lineNumber, TYPE).eval(context);
	}
}
