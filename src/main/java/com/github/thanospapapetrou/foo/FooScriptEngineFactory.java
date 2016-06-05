package com.github.thanospapapetrou.foo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

/**
 * Class implementing a Foo script engine factory
 * 
 * @author thanos
 */
public class FooScriptEngineFactory implements ScriptEngineFactory {
	private static final String LANGUAGE_NAME = "Foo";
	private static final String LANGUAGE_VERSION = "1.0.0";
	private static final String ENGINE_NAME = "Foo";
	private static final String ENGINE_VERSION = "1.0.0";
	private static final List<String> NAMES = Arrays.asList("Foo", "foo");
	private static final List<String> EXTENSIONS = Collections.singletonList("foo");

	@Override
	public String getEngineName() {
		return ENGINE_NAME;
	}

	@Override
	public String getEngineVersion() {
		return ENGINE_VERSION;
	}

	@Override
	public List<String> getExtensions() {
		return EXTENSIONS;
	}

	@Override
	public List<String> getMimeTypes() {
		return Collections.<String> emptyList();
	}

	@Override
	public List<String> getNames() {
		return NAMES;
	}

	@Override
	public String getLanguageName() {
		return LANGUAGE_NAME;
	}

	@Override
	public String getLanguageVersion() {
		return LANGUAGE_VERSION;
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
	public String getMethodCallSyntax(final String object, final String method, final String... arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOutputStatement(final String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProgram(final String... statements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FooScriptEngine getScriptEngine() {
		return new FooScriptEngine(this);
	}
}
