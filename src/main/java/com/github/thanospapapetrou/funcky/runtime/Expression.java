package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;
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
	private static final String NULL_CONTEXT = "Context must not be null";

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
	public Literal eval(final ScriptContext context) throws ScriptException {
		super.eval(context);
		return null;
	}

	/**
	 * Get the type of this expression.
	 * 
	 * @param context
	 *            the context in which to evaluate the type
	 * @return the type of this expression as evaluated in the given context
	 * @throws ScriptException
	 *             if any errors occur while evaluating the type of this expression in the given context
	 */
	public Type getType(final ScriptContext context) throws ScriptException {
		Objects.requireNonNull(context, NULL_CONTEXT);
		return null;
	}
}
