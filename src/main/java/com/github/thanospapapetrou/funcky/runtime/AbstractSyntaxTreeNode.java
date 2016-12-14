package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing an abstract syntax tree node.
 * 
 * @author thanos
 */
public abstract class AbstractSyntaxTreeNode extends CompiledScript {
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";

	/**
	 * The engine that generated this node.
	 */
	protected final FunckyScriptEngine engine;

	/**
	 * The URI of the script from which this node was generated.
	 */
	protected final URI script;

	/**
	 * The line of the script from which this node was generated.
	 */
	protected final int line;

	/**
	 * Construct a new node.
	 * 
	 * @param engine
	 *            the engine that generated this node
	 * @param script
	 *            the URI of the script from which this node was generated
	 * @param line
	 *            the line from which this node was parsed or <code>-1</code> if this node was not parsed (is built-in or generated at runtime)
	 */
	protected AbstractSyntaxTreeNode(final FunckyScriptEngine engine, final URI script, final int line) {
		this.engine = Objects.requireNonNull(engine, NULL_ENGINE);
		this.script = Objects.requireNonNull(script, NULL_SCRIPT);
		this.line = line;
	}

	@Override
	public Object eval(final ScriptContext context) throws ScriptException {
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
	 * @return the line from which this abstract syntax tree node was parsed or <code>-1</code> if this abstract syntax tree node was not parsed from any line (is a built-in)
	 */
	public int getLineNumber() {
		return line;
	}
}
