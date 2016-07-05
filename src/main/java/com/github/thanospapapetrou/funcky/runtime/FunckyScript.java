package com.github.thanospapapetrou.funcky.runtime;

import java.util.List;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Class representing a Funcky script.
 * 
 * @author thanos
 */
public class FunckyScript extends AbstractSyntaxTreeNode {
	private static final String NULL_DEFINITIONS = "Definitions must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";

	private final List<Definition> definitions;

	/**
	 * Construct a new script.
	 * 
	 * @param engine
	 *            the engine that parsed this script
	 * @param fileName
	 *            the name of the file from which this script was parsed
	 * @param lineNumber
	 *            the number of the line from which this script was parsed
	 * @param definitions
	 *            the definitions of this script
	 */
	public FunckyScript(final FunckyScriptEngine engine, final String fileName, final int lineNumber, final List<Definition> definitions) {
		super(requireNonNullEngine(engine), requireValidFileName(fileName), requirePositiveLineNumber(lineNumber));
		this.definitions = Objects.requireNonNull(definitions, NULL_DEFINITIONS);
	}

	@Override
	public Void eval(final ScriptContext context) throws UndefinedReferenceException {
		for (final Definition definition : definitions) {
			definition.eval(Objects.requireNonNull(context, NULL_CONTEXT));
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
