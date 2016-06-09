package com.github.thanospapapetrou.funcky;

import java.util.List;
import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * Class representing a Funcky script.
 * 
 * @author thanos
 */
public class FunckyScript extends CompiledScript {
	private final FunckyScriptEngine engine;
	private final List<Definition> definitions;

	FunckyScript(final FunckyScriptEngine engine, final List<Definition> definitions) {
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
	public FunckyScriptEngine getEngine() {
		return engine;
	}
}
