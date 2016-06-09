package com.github.thanospapapetrou.funcky;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

/**
 * Class implementing a Funcky script engine factory.
 * 
 * @author thanos
 */
public class FunckyScriptEngineFactory implements ScriptEngineFactory {
	private static final String LANGUAGE_NAME = "Funcky";
	private static final String LANGUAGE_VERSION = "1.0.0";
	private static final String ENGINE_NAME = "Funcky";
	private static final String ENGINE_VERSION = "1.0.0";
	private static final List<String> NAMES = Arrays.asList("Funcky", "funcky");
	private static final List<String> EXTENSIONS = Collections.singletonList("funcky");

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunckyScriptEngine getScriptEngine() {
		return new FunckyScriptEngine(this);
	}
}
