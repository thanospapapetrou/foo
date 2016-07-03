package com.github.thanospapapetrou.funcky.runtime;

import javax.script.CompiledScript;
import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Class representing an abstract syntax tree node.
 * 
 * @author thanos
 */
public abstract class AbstractSyntaxTreeNode extends CompiledScript {
	private final FunckyScriptEngine engine;
	private final String fileName;
	private final int lineNumber;

	/**
	 * Construct a new abstract syntax tree node.
	 * 
	 * @param engine
	 *            the engine that parsed this abstract syntax tree node or <code>null</code> if this abstract syntax tree node was not parsed by any engine
	 * @param fileName
	 *            the name of the file from which this abstract syntax tree node was parsed or <code>null</code> if this abstract syntax tree node was not parsed from any file
	 * @param lineNumber
	 *            the number of the line from which this abstract syntax tree node was parsed or <code>0</code> if this abstract syntax tree node was not parsed from any line
	 */
	protected AbstractSyntaxTreeNode(final FunckyScriptEngine engine, final String fileName, final int lineNumber) {
		this.engine = engine;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}

	/**
	 * Check that a line number is positive.
	 * 
	 * @param lineNumber
	 *            the line number to check
	 * @return the given line number, if positive
	 */
	public static int requirePositiveLineNumber(final int lineNumber) {
		if (lineNumber <= 0) {
			throw new IllegalArgumentException("Line number must be positive");
		}
		return lineNumber;
	}

	@Override
	public abstract Object eval(final ScriptContext context) throws UndefinedReferenceException;

	@Override
	public FunckyScriptEngine getEngine() {
		return engine;
	}

	/**
	 * Get the file name.
	 * 
	 * @return the name of the file from which this abstract syntax tree node was parsed or <code>null</code> if this abstract syntax tree node was not parsed from any file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Get the line number.
	 * 
	 * @return the number of the line from which this abstract syntax tree node was parsed or <code>null</code> if this abstract syntax tree node was not parsed from any line
	 */
	public int getLineNumber() {
		return lineNumber;
	}
}
