package com.github.thanospapapetrou.funcky.runtime.expressions;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptException;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndeclaredPrefixException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.expressions.literals.types.Type;

/**
 * Class representing a Funcky reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
	private static final String EMPTY_PREFIX = "Prefix must not be empty";
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_PREFIX = "Prefix must not be null";
	private static final String NULL_URI = "URI must not be null";

	private final QName name;

	/**
	 * Construct a new fully qualified reference.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param script
	 *            the URI of the script from which this reference was generated
	 * @param line
	 *            the line from which this reference was parsed or <code>-1</code> if this reference was not parsed (is built-in or generated at runtime)
	 * @param uri
	 *            the URI of this reference (the URI of the script that this reference refers to)
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI script, final int line, final URI uri, final String name) {
		this(engine, script, line, Objects.requireNonNull(uri, NULL_URI), null, name);
	}

	/**
	 * Construct a new relative reference.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param script
	 *            the URI of the script from which this reference was generated
	 * @param line
	 *            the line from which this reference was parsed or <code>-1</code> if this reference was not parsed (is built-in or generated at runtime)
	 * @param prefix
	 *            the prefix of this reference
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI script, final int line, final String prefix, final String name) {
		this(engine, script, line, null, Objects.requireNonNull(prefix, NULL_PREFIX), name);
		if (prefix.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_PREFIX);
		}
	}

	/**
	 * Construct a new reference relative to current script.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param script
	 *            the URI of the script from which this reference was generated
	 * @param line
	 *            the line from which this reference was parsed or <code>-1</code> if this reference was not parsed (is built-in or generated at runtime)
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI script, final int line, final String name) {
		this(engine, script, line, null, null, name);
	}

	private Reference(final FunckyScriptEngine engine, final URI script, final int line, final URI uri, final String prefix, final String name) {
		super(engine, script, line);
		if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		this.name = new QName((uri == null) ? null : uri.toString(), name, (prefix == null) ? XMLConstants.DEFAULT_NS_PREFIX : prefix);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Reference) {
			try {
				final Reference qualifiedThis = qualify();
				final Reference qualifiedThat = ((Reference) object).qualify();
				return Objects.equals(qualifiedThis.getUri(), qualifiedThat.getUri()) && Objects.equals(qualifiedThis.getName(), qualifiedThat.getName());
			} catch (final ScriptException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	@Override
	public <L extends Literal> L evaluate(final Class<L> clazz) throws ScriptException {
		super.evaluate(clazz);
		return resolve().evaluate(clazz);
	}

	/**
	 * Get the name of this reference.
	 * 
	 * @return the name of this reference
	 */
	public String getName() {
		return name.getLocalPart();
	}

	/**
	 * Get the URI of this reference.
	 * 
	 * @return the URI of this reference (the URI of the script that this reference refers to) or <code>null</code> if this reference has no URI (has a prefix)
	 */
	public URI getUri() {
		return name.getNamespaceURI().equals(XMLConstants.NULL_NS_URI) ? null : URI.create(name.getNamespaceURI());
	}

	/**
	 * Get the prefix of this reference.
	 * 
	 * @return the prefix of this reference or <code>null</code> if this reference has no prefix (is fully qualified or refers to the current script)
	 */
	public String getPrefix() {
		return name.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX) ? null : name.getPrefix();
	}

	@Override
	public Type getType() throws ScriptException {
		return resolve().getType();
	}

	@Override
	public int hashCode() {
		try {
			final Reference qualifiedReference = qualify();
			return Objects.hash(qualifiedReference.getUri(), qualifiedReference.getName());
		} catch (final ScriptException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		try {
			return qualify().name.toString();
		} catch (final ScriptException e) {
			throw new RuntimeException(e);
		}
	}

	private Reference qualify() throws ScriptException {
		if (getUri() != null) { // fully qualified reference
			return this;
		} else if (getPrefix() != null) { // relative reference with prefix
			final URI uri = engine.resolve(script, getPrefix());
			if (uri == null) {
				throw new UndeclaredPrefixException(this);
			}
			return new Reference(engine, script, line, uri, getName());
		} else { // relative reference without prefix
			return new Reference(engine, script, line, script, getName());
		}
	}

	private Expression resolve() throws ScriptException {
		final Reference qualified = qualify();
		if (engine.getScope(qualified.getUri()) == null) {
			engine.load(qualified);
		}
		final Expression expression = engine.resolve(qualified);
		if (expression == null) {
			throw new UndefinedSymbolException(qualified);
		}
		return expression;
	}
}
