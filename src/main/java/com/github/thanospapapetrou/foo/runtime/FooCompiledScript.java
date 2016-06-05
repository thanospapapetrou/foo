package com.github.thanospapapetrou.foo.runtime;

import java.util.List;
import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.foo.FooScriptEngine;

/**
 * Class representing a Foo compiled script.
 * 
 * @author thanos
 */
public class FooCompiledScript extends CompiledScript {
	private final FooScriptEngine engine;
	private final List<Definition> definitions;

	/**
	 * Construct a new Foo compiled script.
	 * 
	 * @param engine
	 *            the Foo script engine that created this compiled script
	 * @param definitions
	 *            the list of definitions that make up this compiled script
	 */
	public FooCompiledScript(final FooScriptEngine engine, final List<Definition> definitions) {
		this.engine = Objects.requireNonNull(engine, "Engine must not be null");
		this.definitions = Objects.requireNonNull(definitions, "Definitions must not be null");
	}

	@Override
	public Void eval(final ScriptContext context) throws ScriptException {
		for (final Definition definition : definitions) {
			definition.eval(context);
		}
		return null;
	}

	@Override
	public FooScriptEngine getEngine() {
		return engine;
	}
}
