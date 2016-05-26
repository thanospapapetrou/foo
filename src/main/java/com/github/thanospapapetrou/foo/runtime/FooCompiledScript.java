package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.github.thanospapapetrou.foo.FooScriptEngine;

public class FooCompiledScript extends CompiledScript {
	private final FooScriptEngine engine;
	
	public FooCompiledScript(final FooScriptEngine engine) {
		this.engine = Objects.requireNonNull(engine, "Engine must not be null");
	}
	
	@Override
	public Object eval(final ScriptContext context) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptEngine getEngine() {
		return engine;
	}
}
