package com.github.thanospapapetrou.foo.runtime;

import java.util.List;
import java.util.Objects;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.github.thanospapapetrou.foo.FooScriptEngine;

/**
 * Class representing a Foo compiled script.
 * 
 * @author thanos
 */
public class FooCompiledScript extends CompiledScript {
	private final FooScriptEngine engine;
	private final List<Command> commands;

	/**
	 * Construct a new Foo compiled script.
	 * 
	 * @param engine
	 *            the Foo script engine that created this compiled script
	 * @param commands
	 *            the list of commands that make up this compiled script
	 */
	public FooCompiledScript(final FooScriptEngine engine, final List<Command> commands) {
		this.engine = Objects.requireNonNull(engine, "Engine must not be null");
		this.commands = Objects.requireNonNull(commands, "Commands must not be null");
	}

	@Override
	public Literal eval(final ScriptContext context) throws ScriptException {
		Literal result = null;
		for (final Command command : commands) {
			result = command.eval(context);
		}
		return result;
	}

	@Override
	public ScriptEngine getEngine() {
		return engine;
	}
}
