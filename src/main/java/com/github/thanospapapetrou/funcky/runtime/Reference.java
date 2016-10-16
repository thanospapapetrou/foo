package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import com.github.thanospapapetrou.funcky.FunckyException;
import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;

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
	 * Construct a new fully qualified reference generated at runtime.
	 * 
	 * @param engine
	 *            the engine that generated this reference
	 * @param namespace
	 *            the namespace of this reference (the URI of the script that this reference refers to)
	 * @param name
	 *            the name of this reference
	 */
	public Reference(final FunckyScriptEngine engine, final URI namespace, final String name) {
		this(engine, FunckyScriptEngine.RUNTIME, 0, namespace, name);
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
			final Reference reference = (Reference) object;
			return Objects.equals(getNamespace(), reference.getNamespace()) && Objects.equals(getName(), reference.getName()); // TODO resolve prefix and take it into account
		}
		return false;
	}

	@Override
	public Literal eval(final ScriptContext context) throws FunckyException {
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
	public Expression getType(final ScriptContext context) throws FunckyException {
		super.eval(context);
		return resolve(context).getType(context);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNamespace(), getName()); // TODO resolve prefix and take it into account
	}

	@Override
	public String toString() {
		return name.toString(); // TODO what if URI is null?
	}

	private Expression resolve(final ScriptContext context) throws UndefinedReferenceException {
		final Object object = context.getAttribute(name.getLocalPart()); // TODO use different bindings?
		if (object instanceof Expression) {
			return (Expression) object;
		}
		throw new UndefinedReferenceException(this);
	}
}
