package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

/**
 * Class representing a Funcky character.
 * @author thanos
 *
 */
public class FunckyCharacter extends Literal {
	private final char value;

	/**
	 * Construct a new character.
	 * @param engine the engine that generated this character 
	 * @param script the URI of the script from which this character was generated
	 * @param lineNumber the number of the line from which this character was parsed or <code>0</code> if this number was not parsed (is builtin or generated at runtime)
	 * @param value the value of this character
	 */
	public FunckyCharacter(final FunckyScriptEngine engine, final URI script, final int lineNumber, final char value) {
		super(engine, script, lineNumber);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof FunckyCharacter) && (value == ((FunckyCharacter) object).value);
	}

	@Override
	public SimpleType getType(final ScriptContext context) throws InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.getType(context);
		return engine.getPrelude().getCharacter();
	}

	/**
	 * Get the value of this character.
	 * 
	 * @return the value of this character
	 */
	public char getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Character.valueOf(value).hashCode();
	}

	@Override
	public Expression toExpression() {
		return this;
	}

	@Override
	public String toString() {
		return Character.toString(value);
	}
}
