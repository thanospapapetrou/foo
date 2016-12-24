package com.github.thanospapapetrou.funcky.runtime.expressions.literals;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.libraries.Characters;

/**
 * Class representing a Funcky character.
 * 
 * @author thanos
 */
public class Character extends Literal {
	private static final String CHARACTER = "'%1$s'";
	private static final Pattern CONTROL = Pattern.compile("\\p{C}");
	private static final Map<java.lang.Character, String> ESCAPES = new HashMap<>();
	private static final String OCTAL = "\\%1$o";

	private final char value;

	static {
		ESCAPES.put((char) 0x7, "\\a");
		ESCAPES.put('\b', "\\b");
		ESCAPES.put((char) 0xC, "\\f");
		ESCAPES.put('\n', "\\n");
		ESCAPES.put('\r', "\\r");
		ESCAPES.put('\t', "\\t");
		ESCAPES.put((char) 0xB, "\\v");
		ESCAPES.put('\\', "\\\\");
	}

	/**
	 * Construct a new character.
	 * 
	 * @param engine
	 *            the engine that generated this character
	 * @param script
	 *            the URI of the script from which this character was generated
	 * @param line
	 *            the line from which this character was parsed or <code>-1</code> if this character was not parsed (is built-in or generated at runtime)
	 * @param value
	 *            the value of this character
	 */
	public Character(final FunckyScriptEngine engine, final URI script, final int line, final char value) {
		super(engine, script, line);
		this.value = value;
	}

	private static String escape(final char character) {
		return ESCAPES.containsKey(character) ? ESCAPES.get(character) : (CONTROL.matcher(java.lang.Character.toString(character)).matches() ? String.format(OCTAL, (int) character) : java.lang.Character.toString(character));
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Character) && (value == ((Character) object).value);
	}

	@Override
	public SimpleType getType() throws ScriptException {
		return engine.getLiteral(Characters.class, Characters.CHARACTER);
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
		return java.lang.Character.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		return String.format(CHARACTER, escape(value));
	}
}
