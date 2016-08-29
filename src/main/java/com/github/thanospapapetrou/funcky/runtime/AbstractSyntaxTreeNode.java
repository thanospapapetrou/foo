package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing an abstract syntax tree node.
 * 
 * @author thanos
 */
public abstract class AbstractSyntaxTreeNode extends CompiledScript {
	private static final String NEGATIVE_LINE_NUMBER = "Line number must be non-negative";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";

	protected final FunckyScriptEngine engine;
	protected final URI script;
	protected final int lineNumber;

	/**
	 * Construct a new abstract syntax tree node.
	 * 
	 * @param engine
	 *            the engine that parsed this abstract syntax tree node
	 * @param script
	 *            the URI of the script from which this abstract syntax tree node was generated
	 * @param lineNumber
	 *            the number of the line from which this abstract syntax tree node was parsed or <code>0</code> if this abstract syntax tree node was not parsed from any line (is a builtin)
	 */
	protected AbstractSyntaxTreeNode(final FunckyScriptEngine engine, final URI script, final int lineNumber) {
		this.engine = Objects.requireNonNull(engine, NULL_ENGINE);
		this.script = Objects.requireNonNull(script, NULL_SCRIPT);
		if (lineNumber < 0) {
			throw new IllegalArgumentException(NEGATIVE_LINE_NUMBER);
		}
		this.lineNumber = lineNumber;
	}

	@Override
	public Object eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		Objects.requireNonNull(context, NULL_CONTEXT);
		return null;
	}

	@Override
	public FunckyScriptEngine getEngine() {
		return engine;
	}

	/**
	 * Get the script.
	 * 
	 * @return the URI of the script from which this abstract syntax tree node was generated
	 */
	public URI getScript() {
		return script;
	}

	/**
	 * Get the line number.
	 * 
	 * @return the number of the line from which this abstract syntax tree node was parsed or <code>null</code> if this abstract syntax tree node was not parsed from any line (is a builtin)
	 */
	public int getLineNumber() {
		return lineNumber;
	}
}
