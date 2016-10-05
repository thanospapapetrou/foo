package com.github.thanospapapetrou.funcky.runtime.literals;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;

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

	private static String escape(final char character) {
		return ESCAPES.containsKey(character) ? ESCAPES.get(character) : (CONTROL.matcher(java.lang.Character.toString(character)).matches() ? String.format(OCTAL, (int) character) : java.lang.Character.toString(character));
	}

	/**
	 * Construct a new character.
	 * 
	 * @param engine
	 *            the engine that generated this character
	 * @param script
	 *            the URI of the script from which this character was generated
	 * @param lineNumber
	 *            the number of the line from which this character was parsed or <code>0</code> if this number was not parsed (is builtin or generated at runtime)
	 * @param value
	 *            the value of this character
	 */
	public Character(final FunckyScriptEngine engine, final URI script, final int lineNumber, final char value) {
		super(engine, script, lineNumber);
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Character) && (value == ((Character) object).value);
	}

	@Override
	public SimpleType getType(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
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
		return java.lang.Character.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		return String.format(CHARACTER, escape(value));
	}
}
