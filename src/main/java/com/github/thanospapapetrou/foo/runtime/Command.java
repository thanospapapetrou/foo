package com.github.thanospapapetrou.foo.runtime;

import javax.script.ScriptContext;
import javax.script.ScriptException;

public interface Command {
	public Literal eval(final ScriptContext context) throws ScriptException;

	public FooType getType(final ScriptContext context) throws ScriptException;
}
