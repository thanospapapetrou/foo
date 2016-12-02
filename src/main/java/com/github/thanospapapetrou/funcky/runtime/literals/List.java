package com.github.thanospapapetrou.funcky.runtime.literals;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.literals.types.ListType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Class representing a Funcky list.
 * 
 * @author thanos
 */
public class List extends Literal {
	private static final String EMPTY_STRING = "String must not be empty";
	private static final String LIST_DELIMITER = ", ";
	private static final String LIST_END = "]";
	private static final String LIST_START = "[";
	private static final String NULL_HEAD = "Head must not be null";
	private static final String NULL_STRING = "String must not be null";
	private static final String NULL_TAIL = "Tail must not be null";

	private final Literal head;
	private final List tail;

	private static String requireValidString(final String string) {
		if (Objects.requireNonNull(string, NULL_STRING).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_STRING);
		}
		return string;
	}

	/**
	 * Construct a new list.
	 * 
	 * @param engine
	 *            the engine that generated this list
	 * @param head
	 *            the head of this list
	 * @param tail
	 *            the tail of this list
	 */
	public List(final FunckyScriptEngine engine, final Literal head, final List tail) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
		this.head = Objects.requireNonNull(head, NULL_HEAD);
		this.tail = Objects.requireNonNull(tail, NULL_TAIL);
	}

	/**
	 * Construct a new empty list.
	 * 
	 * @param engine
	 *            the engine that generated this empty list
	 */
	public List(final FunckyScriptEngine engine) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
		this.head = null;
		this.tail = null;
	}

	/**
	 * Construct a new string (list of characters).
	 * 
	 * @param engine
	 *            the engine that generated this string
	 * @param string
	 *            the value of this string
	 */
	public List(final FunckyScriptEngine engine, final String string) {
		this(engine, new Character(engine, requireValidString(string).charAt(0)), string.substring(1).isEmpty() ? new List(engine) : new List(engine, string.substring(1)));
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
	public ListType getType(final ScriptContext context) throws ScriptException {
		super.getType(context);
		return new ListType(engine, (head == null) ? new TypeVariable(engine) : head.getType(context));
	}

	@Override
	public int hashCode() {
		return Objects.hash(head, tail);
	}

	@Override
	public String toString() {
		final StringBuilder string = new StringBuilder(LIST_START);
		if (head != null) {
			string.append(head.toString());
			for (List list = tail; list.head != null; list = list.tail) {
				string.append(LIST_DELIMITER).append(list.head.toString());
			}
		}
		return string.append(LIST_END).toString();
	}
}
