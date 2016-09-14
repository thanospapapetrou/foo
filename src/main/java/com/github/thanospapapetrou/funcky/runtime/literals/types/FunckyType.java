package com.github.thanospapapetrou.funcky.runtime.literals.types;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;

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
	 *            the engine that generated this type
	 * @param script
	 *            the URI of the script from which this type was generated
	 * @param lineNumber
	 *            the number of the line from which this type was parsed or <code>0</code> if this type was not parsed (is builtin or generated at runtime)
	 */
	protected FunckyType(final FunckyScriptEngine engine, final URI script, final int lineNumber) {
		super(engine, script, lineNumber);
	}

	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		Objects.requireNonNull(bindings, NULL_BINDINGS);
		return null;
	};

	@Override
	public SimpleType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return (SimpleType) new Reference(engine, TYPE).eval(context);
	}

	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		Objects.requireNonNull(type, NULL_TYPE);
		return null;
	}
}
