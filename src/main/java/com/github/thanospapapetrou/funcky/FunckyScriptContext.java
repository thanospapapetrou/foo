package com.github.thanospapapetrou.funcky;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;

/**
 * Class representing a Funcky script context.
 * 
 * @author thanos
 */
public class FunckyScriptContext implements ScriptContext {
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_ENGINE_SCOPE_BINDINGS = "Engine scope bindings must not be null";
	private static final String NULL_GLOBAL_SCOPE_BINDINGS = "Global scope bindings must not be null";
	private static final String NULL_NAME = "Name must not be null";

	private final SortedMap<Integer, Bindings> bindings;
	private Reader reader;
	private Writer writer;
	private Writer errorWriter;

	/**
	 * Construct a new script context.
	 * 
	 * @param globalScopeBindings
	 *            the bindings to use for global scope
	 */
	public FunckyScriptContext(final Bindings globalScopeBindings) {
		bindings = new TreeMap<>();
		reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
		writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
		errorWriter = new OutputStreamWriter(System.err, StandardCharsets.UTF_8);
		setBindings(new SimpleBindings(), ENGINE_SCOPE);
		setBindings(Objects.requireNonNull(globalScopeBindings, NULL_GLOBAL_SCOPE_BINDINGS), GLOBAL_SCOPE);
	}

	private static String requireValidName(final String name) {
		if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		return name;
	}

	@Override
	public Object getAttribute(final String name) {
		for (final Map.Entry<Integer, Bindings> bindings : this.bindings.entrySet()) {
			if (bindings.getValue().containsKey(requireValidName(name))) {
				return bindings.getValue().get(name);
			}
		}
		return null;
	}

	@Override
	public Object getAttribute(final String name, final int scope) {
		return bindings.containsKey(scope) ? bindings.get(scope).get(requireValidName(name)) : null;
	}

	@Override
	public int getAttributesScope(final String name) {
		for (final Map.Entry<Integer, Bindings> bindings : this.bindings.entrySet()) {
			if (bindings.getValue().containsKey(requireValidName(name))) {
				bindings.getKey();
			}
		}
		return -1;
	}

	@Override
	public Bindings getBindings(final int scope) {
		return bindings.get(scope);
	}

	@Override
	public Writer getErrorWriter() {
		return errorWriter;
	}

	@Override
	public Reader getReader() {
		return reader;
	}

	@Override
	public List<Integer> getScopes() {
		return Arrays.asList(bindings.keySet().toArray(new Integer[] {}));
	}

	@Override
	public Writer getWriter() {
		return writer;
	}

	@Override
	public Object removeAttribute(final String name, final int scope) {
		return bindings.containsKey(scope) ? bindings.get(scope).remove(requireValidName(name)) : null;
	}

	@Override
	public void setAttribute(final String name, final Object value, final int scope) {
		if (!bindings.containsKey(scope)) {
			bindings.put(scope, new SimpleBindings());
		}
		bindings.get(scope).put(requireValidName(name), value);
	}

	@Override
	public void setBindings(final Bindings bindings, final int scope) {
		if (scope == ENGINE_SCOPE) {
			this.bindings.put(scope, Objects.requireNonNull(bindings, NULL_ENGINE_SCOPE_BINDINGS));
		} else {
			if (bindings == null) {
				this.bindings.remove(scope);
			} else {
				this.bindings.put(scope, bindings);
			}
		}
	}

	@Override
	public void setErrorWriter(final Writer errorWriter) {
		this.errorWriter = errorWriter;
	}

	@Override
	public void setReader(final Reader reader) {
		this.reader = reader;
	}

	@Override
	public void setWriter(final Writer writer) {
		this.writer = writer;
	}
}
