package com.github.thanospapapetrou.funcky;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.SimpleBindings;

/**
 * Class implementing a Funcky script engine factory.
 * 
 * @author thanos
 */
public class FunckyScriptEngineFactory implements ScriptEngineFactory {
	private static final String DELIMITER = "\n";
	private static final String NAME = "Funcky";
	private static final String NULL_GLOBAL_SCOPE_BINDINGS = "Global scope bindings must not be null";
	private static final String NULL_STATEMENTS = "Statements must not be null";
	private static final String UNSUPPORTED_GET_METHOD_CALL_SYNTAX = "getMethodCallSyntax() is not supported";
	private static final String UNSUPPORTED_GET_OUTPUT_STATEMENT = "getOutputStatement() is not supported";
	private static final String VERSION = "1.0.0-SNAPSHOT";

	private final Bindings globalScopeBindings;

	/**
	 * Construct a new script engine factory.
	 * 
	 * @param globalScopeBindings
	 *            the global scope bindings to use
	 */
	public FunckyScriptEngineFactory(final Bindings globalScopeBindings) {
		this.globalScopeBindings = Objects.requireNonNull(globalScopeBindings, NULL_GLOBAL_SCOPE_BINDINGS);
	}

	/**
	 * Construct a new script engine factory with empty global scope bindings.
	 */
	public FunckyScriptEngineFactory() {
		this(new SimpleBindings());
	}

	@Override
	public String getEngineName() {
		return NAME;
	}

	@Override
	public String getEngineVersion() {
		return VERSION;
	}

	@Override
	public List<String> getExtensions() {
		return Collections.singletonList(NAME.toLowerCase(Locale.ROOT));
	}

	@Override
	public List<String> getMimeTypes() {
		return Collections.<String> emptyList();
	}

	@Override
	public List<String> getNames() {
		return Arrays.asList(NAME, NAME.toLowerCase(Locale.ROOT));
	}

	@Override
	public String getLanguageName() {
		return NAME;
	}

	@Override
	public String getLanguageVersion() {
		return VERSION;
	}

	@Override
	public String getMethodCallSyntax(final String object, final String method, final String... arguments) {
		throw new UnsupportedOperationException(UNSUPPORTED_GET_METHOD_CALL_SYNTAX);
	}

	@Override
	public String getOutputStatement(final String string) {
		throw new UnsupportedOperationException(UNSUPPORTED_GET_OUTPUT_STATEMENT);
	}

	@Override
	public String getParameter(final String key) {
		if (key == null) {
			return null;
		}
		switch (key) {
		case ScriptEngine.LANGUAGE:
			return getLanguageName();
		case ScriptEngine.LANGUAGE_VERSION:
			return getLanguageVersion();
		case ScriptEngine.ENGINE:
			return getEngineName();
		case ScriptEngine.ENGINE_VERSION:
			return getEngineVersion();
		case ScriptEngine.NAME:
			return getNames().get(0);
		default:
			return null;
		}
	}

	@Override
	public String getProgram(final String... statements) {
		final StringBuilder program = new StringBuilder();
		for (final String statement : Objects.requireNonNull(statements, NULL_STATEMENTS)) {
			program.append(statement).append(DELIMITER);
		}
		return program.toString();
	}

	@Override
	public FunckyScriptEngine getScriptEngine() {
		return new FunckyScriptEngine(this, globalScopeBindings);
	}
}
