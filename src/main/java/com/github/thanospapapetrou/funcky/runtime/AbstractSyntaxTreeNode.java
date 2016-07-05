package com.github.thanospapapetrou.funcky.runtime;

import java.util.Objects;

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
	private static final String NON_POSITIVE_LINE_NUMBER = "Line number must be positive";
	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_FILE_NAME = "File name must not be null";
	private static final String EMPTY_FILE_NAME = "File name must not be empty";

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
			throw new IllegalArgumentException(NON_POSITIVE_LINE_NUMBER);
		}
		return lineNumber;
	}

	/**
	 * Check that a Funcky script engine is not null.
	 * 
	 * @param engine
	 *            the engine to check
	 * @return the given engine, if non <code>null</code>
	 */
	protected static FunckyScriptEngine requireNonNullEngine(final FunckyScriptEngine engine) {
		return Objects.requireNonNull(engine, NULL_ENGINE);
	}

	/**
	 * Check that a file name is valid.
	 * 
	 * @param fileName
	 *            the file name to check
	 * @return the given file name, if non <code>null</code> and non empty
	 */
	protected static String requireValidFileName(final String fileName) {
		Objects.requireNonNull(fileName, NULL_FILE_NAME);
		if (fileName.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_FILE_NAME);
		}
		return fileName;
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
