package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;

/**
 * Abstract class representing a Funcky expression.
 * 
 * @author thanos
 */
public abstract class Expression extends AbstractSyntaxTreeNode {
	/**
	 * Construct a new expression.
	 * 
	 * @param engine
	 *            the engine that generated this expression
	 * @param script
	 *            the URI of the script from which this expression was generated
	 * @param line
	 *            the line from which this expression was parsed or <code>-1</code> if this expression was not parsed (is built-in or generated at runtime)
	 */
	protected Expression(final FunckyScriptEngine engine, final URI script, final int line) {
		super(engine, script, line);
	}

	@Override
	public abstract Literal eval() throws ScriptException;

	/**
	 * Get the type of this expression.
	 * 
	 * @return the type of this expression
	 * @throws ScriptException
	 *             if any errors occur while evaluating the type of this expression
	 */
	public abstract Type getType() throws ScriptException;
}
