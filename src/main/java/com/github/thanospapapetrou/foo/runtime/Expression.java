package com.github.thanospapapetrou.foo.runtime;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.foo.FooScriptEngine;

/**
 * Abstract class representing a Foo expression.
 * 
 * @author thanos
 */
public abstract class Expression extends CompiledScript {
	private final FooScriptEngine engine;

	/**
	 * Construct a new expression.
	 * 
	 * @param engine
	 *            the Foo script engine that created this expression or <code>null</code> if this expression was not created by any Foo script engine
	 */
	public Expression(final FooScriptEngine engine) {
		this.engine = engine;
	}

	/**
	 * Evaluate this expression.
	 * 
	 * @param context
	 *            the script context in which to evaluate this expression
	 * @return the result of the evaluation
	 * @throws ScriptException
	 *             if any errors occur during the evaluation
	 */
	@Override
	public abstract Literal eval(final ScriptContext context) throws ScriptException;

	@Override
	public FooScriptEngine getEngine() {
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
	public abstract FooType getType(final ScriptContext context) throws ScriptException;
}
