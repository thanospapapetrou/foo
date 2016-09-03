package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky script.
 * 
 * @author thanos
 */
public class FunckyScript extends AbstractSyntaxTreeNode {
	private static final String NULL_DEFINITIONS = "Definitions must not be null";

	/**
	 * The definitions of this script.
	 */
	protected final List<Definition> definitions;

	/**
	 * Construct a new script.
	 * 
	 * @param engine
	 *            the engine that generated this script
	 * @param script
	 *            the URI of this script
	 * @param lineNumber
	 *            the number of the line from which this script was parsed
	 * @param definitions
	 *            the definitions of this script
	 */
	public FunckyScript(final FunckyScriptEngine engine, final URI script, final int lineNumber, final List<Definition> definitions) {
		super(engine, script, lineNumber);
		this.definitions = Objects.requireNonNull(definitions, NULL_DEFINITIONS);
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.eval(context);
		for (final Definition definition : definitions) {
			definition.eval(context);
		}
		return null;
	}

	/**
	 * Get the definitions.
	 * 
	 * @return the definitions of this script
	 */
	public List<Definition> getDefinitions() {
		return definitions;
	}
}
