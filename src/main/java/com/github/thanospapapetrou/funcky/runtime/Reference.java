package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndeclaredPrefixException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;

/**
 * Class representing a Funcky reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
	private static final String EMPTY_PREFIX = "Prefix must not be empty";
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_NAMESPACE = "Namespace must not be null";
	private static final String NULL_PREFIX = "Prefix must not be null";

	private final QName name;

	/**
	 * Construct a new fully qualified reference.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param script
	 *            the URI of the script from which this reference was generated
	 * @param line
	 *            the line from which this reference was parsed or <code>0</code> if this reference was not parsed (is builtin or generated at runtime)
	 * @param namespace
	 *            the namespace of this reference (the URI of the script that this reference refers to)
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI script, final int line, final URI namespace, final String name) {
		this(engine, script, line, Objects.requireNonNull(namespace, NULL_NAMESPACE), null, name);
	}

	/**
	 * Construct a new relative reference.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param script
	 *            the URI of the script from which this reference was generated
	 * @param line
	 *            the line from which this reference was parsed or <code>0</code> if this reference was not parsed (is builtin or generated at runtime)
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
	 *            the line from which this reference was parsed or <code>0</code> if this reference was not parsed (is builtin or generated at runtime)
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI script, final int line, final String name) {
		this(engine, script, line, null, null, name);
	}

	private Reference(final FunckyScriptEngine engine, final URI script, final int line, final URI namespace, final String prefix, final String name) {
		super(engine, script, line);
		if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		this.name = new QName((namespace == null) ? null : namespace.toString(), name, (prefix == null) ? XMLConstants.DEFAULT_NS_PREFIX : prefix);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Reference) {
			try {
				final Reference qualifiedThis = qualify(engine.getContext());
				final Reference qualifiedThat = ((Reference) object).qualify(engine.getContext());
				return Objects.equals(qualifiedThis.getNamespace(), qualifiedThat.getNamespace()) && Objects.equals(qualifiedThis.getName(), qualifiedThat.getName());
			} catch (final UndeclaredPrefixException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	@Override
	public Literal eval(final ScriptContext context) throws ScriptException {
		return resolve(context).eval(context);
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
	 * Get the namespace of this reference.
	 * 
	 * @return the namespace of this reference (the URI of the script that this reference refers to) or <code>null</code> if this reference has no namespace (has a prefix)
	 */
	public URI getNamespace() {
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
	public Type getType(final ScriptContext context) throws ScriptException {
		super.getType(context);
		return resolve(context).getType(context);
	}

	@Override
	public int hashCode() {
		try {
			final Reference qualifiedReference = qualify(engine.getContext());
			return Objects.hash(qualifiedReference.getNamespace(), qualifiedReference.getName());
		} catch (final UndeclaredPrefixException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		try {
			return qualify(engine.getContext()).name.toString();
		} catch (final UndeclaredPrefixException e) {
			throw new RuntimeException(e);
		}
	}

	private Reference qualify(final ScriptContext context) throws UndeclaredPrefixException {
		if (getNamespace() != null) { // fully qualified reference
			return this;
		} else if (getPrefix() != null) { // relative reference with prefix
			final URI namespace = engine.resolvePrefix(context, script, getPrefix());
			if (namespace == null) {
				throw new UndeclaredPrefixException(this);
			}
			return new Reference(engine, script, line, namespace, getName());
		} else { // relative reference without prefix
			return new Reference(engine, script, line, script, getName());
		}
	}

	private Expression resolve(final ScriptContext context) throws ScriptException {
		final Reference qualified = qualify(context);
		if (engine.getScope(context, qualified.getNamespace()) == null) {
			engine.load(context, qualified.getNamespace());
		}
		final Expression expression = (Expression) context.getAttribute(qualified.getName(), engine.getScope(context, qualified.getNamespace()));
		if (expression == null) {
			throw new UndefinedSymbolException(qualified);
		}
		return expression;
	}
}
