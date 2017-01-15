package com.github.thanospapapetrou.funcky.runtime.expressions;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.AbstractSyntaxTreeNode;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.Type;

/**
 * Abstract class representing a Funcky expression.
 * 
 * @author thanos
 */
public abstract class Expression extends AbstractSyntaxTreeNode {
	private static final String NULL_CLASS = "Class must not be null";

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
	public Literal eval() throws ScriptException {
		return evaluate(Literal.class);
	}

	/**
	 * Evaluate this expression.
	 * 
	 * @param <L>
	 *            the class of the literal to return
	 * @param clazz
	 *            the class of the literal to return
	 * @return the literal that this expression evaluates to
	 * @throws ScriptException
	 *             if any errors occur during the evaluation
	 */
	public <L extends Literal> L evaluate(final Class<L> clazz) throws ScriptException {
		Objects.requireNonNull(clazz, NULL_CLASS);
		return null;
	}

	/**
	 * Get the type of this expression.
	 * 
	 * @return the type of this expression
	 * @throws ScriptException
	 *             if any errors occur while evaluating the type of this expression
	 */
	public abstract Type getType() throws ScriptException;
}
