package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;
import java.util.Objects;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

/**
 * Class representing a Funcky import.
 * 
 * @author thanos
 */
public class Import extends AbstractSyntaxTreeNode {
	private static final String EMPTY_PREFIX = "Prefix must not be empty";
	private static final String NULL_PREFIX = "Prefix must not be null";
	private static final String NULL_URI = "URI must not be null";

	private final String prefix;
	private final URI uri;

	/**
	 * Construct a new import.
	 * 
	 * @param engine
	 *            the engine that generated this import
	 * @param script
	 *            the URI of the script from which this import was generated
	 * @param line
	 *            the line from which this import was parsed or <code>0</code> if this import was not parsed (is builtin or generated at runtime)
	 * @param prefix
	 *            the prefix of this import
	 * @param uri
	 *            the URI of this import
	 */
	public Import(final FunckyScriptEngine engine, final URI script, final int line, final String prefix, final URI uri) {
		super(engine, script, line);
		if ((this.prefix = Objects.requireNonNull(prefix, NULL_PREFIX)).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_PREFIX);
		}
		this.uri = Objects.requireNonNull(uri, NULL_URI);
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super.eval(context);
		// TODO implement
		return null;
	}

	/**
	 * Get the prefix.
	 * 
	 * @return the prefix of this import
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Get the URI.
	 * 
	 * @return the URI of this import
	 */
	public URI getUri() {
		return uri;
	}
}
