package com.github.thanospapapetrou.funcky.runtime.expressions.literals;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.ListType;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.libraries.Characters;
import com.github.thanospapapetrou.funcky.runtime.libraries.Strings;

/**
 * Class representing a Funcky list.
 * 
 * @author thanos
 */
public class List extends Literal {
	private static final String LEFT_SQUARE_BRACKET = "[";
	private static final String LIST_DELIMITER = ", ";
	private static final String NULL_HEAD = "Head must not be null";
	private static final String NULL_STRING = "String must not be null";
	private static final String NULL_TAIL = "Tail must not be null";
	private static final String NULL_TYPE = "Type must not be null";
	private static final String QUOTATION_MARK = "\"";
	private static final String RIGHT_SQUARE_BRACKET = "]";

	private final ListType type;
	private final Literal head;
	private final List tail;

	/**
	 * Construct a new list.
	 * 
	 * @param engine
	 *            the engine that generated this list
	 * @param script
	 *            the URI of the script from which this list was generated
	 * @param line
	 *            the line from which this list was parsed or <code>-1</code> if this list was not parsed (is built-in or generated at runtime)
	 * @param head
	 *            the head of this list
	 * @param tail
	 *            the tail of this list
	 * @throws ScriptException
	 *             if any errors occur while constructing this list
	 */
	public List(final FunckyScriptEngine engine, final URI script, final int line, final Literal head, final List tail) throws ScriptException {
		super(engine, script, line);
		type = engine.getListType(head.getType());
		this.head = Objects.requireNonNull(head, NULL_HEAD);
		this.tail = Objects.requireNonNull(tail, NULL_TAIL);
	}

	/**
	 * Construct a new empty list.
	 * 
	 * @param engine
	 *            the engine that generated this empty list
	 * @param script
	 *            the URI of the script from which this list was generated
	 * @param line
	 *            the line from which this list was parsed or <code>-1</code> if this list was not parsed (is built-in or generated at runtime)
	 * @param type
	 *            the type of this list
	 */
	public List(final FunckyScriptEngine engine, final URI script, final int line, final ListType type) {
		super(engine, script, line);
		this.type = Objects.requireNonNull(type, NULL_TYPE);
		this.head = null;
		this.tail = null;
	}

	/**
	 * Construct a new string (list of characters).
	 * 
	 * @param engine
	 *            the engine that generated this string
	 * @param script
	 *            the URI of the script from which this string was generated
	 * @param line
	 *            the line from which this string was parsed or <code>-1</code> if this string was not parsed (is built-in or generated at runtime)
	 * @param string
	 *            the value of this string
	 * @throws ScriptException
	 *             if any errors occur while constructing this string
	 */
	public List(final FunckyScriptEngine engine, final URI script, final int line, final String string) throws ScriptException {
		super(engine, script, line);
		this.type = engine.getListType((SimpleType) engine.getReference(Characters.class, Characters.CHARACTER).eval()); // TODO no casting
		this.head = Objects.requireNonNull(string, NULL_STRING).isEmpty() ? null : new Character(engine, script, line, string.charAt(0));
		this.tail = string.isEmpty() ? null : new List(engine, script, line, string.substring(1));
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof List) {
			final List list = (List) object;
			return Objects.equals(head, list.head) && Objects.equals(tail, list.tail);
		}
		return false;
	}

	/**
	 * Get the head of this list.
	 * 
	 * @return the head of this list
	 */
	public Literal getHead() {
		return head;
	}

	/**
	 * Get the tail of this list.
	 * 
	 * @return the tail of this list
	 */
	public List getTail() {
		return tail;
	}

	@Override
	public ListType getType() throws ScriptException {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(head, tail);
	}

	@Override
	public String toString() {
		try {
			final boolean isString = type.equals(engine.getReference(Strings.class, Strings.STRING).eval());
			final StringBuilder string = new StringBuilder(isString ? QUOTATION_MARK : LEFT_SQUARE_BRACKET);
			for (List list = this; list.head != null; list = list.tail) {
				string.append(isString ? ((Character) list.head).getValue() : list.head.toString()).append(isString ? "" : LIST_DELIMITER); // TODO no casting
			}
			string.setLength(string.length() - ((head == null) || isString ? 0 : LIST_DELIMITER.length()));
			return string.append(isString ? QUOTATION_MARK : RIGHT_SQUARE_BRACKET).toString();
		} catch (final ScriptException e) {
			return null; // TODO
		}
	}
}
