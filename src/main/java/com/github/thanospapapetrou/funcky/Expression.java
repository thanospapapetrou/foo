package com.github.thanospapapetrou.funcky;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * Abstract class representing a Funcky expression.
 * 
 * @author thanos
 */
public abstract class Expression extends CompiledScript {
	private final FunckyScriptEngine engine;

	/**
	 * Construct a new expression.
	 * 
	 * @param engine
	 *            the Funcky script engine that created this expression or <code>null</code> if this expression was not created by any Funcky script engine
	 */
	protected Expression(final FunckyScriptEngine engine) {
		this.engine = engine;
	}

	@Override
	public abstract Literal eval(final ScriptContext context) throws ScriptException;

	@Override
	public FunckyScriptEngine getEngine() {
		return engine;
	}

	/**
	 * Get the type of this expression.
	 * 
	 * @param context
	 *            the script context in which to evaluate the type of this expression
	 * @return the type of this expression
	 * @throws ScriptException
	 *             if any errors occur while evaluating the type
	 */
	public abstract FunckyType getType(final ScriptContext context) throws ScriptException;
}
