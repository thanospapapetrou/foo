package com.github.thanospapapetrou.funcky;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * Class representing a Funcky reference.
 * 
 * @author thanos
 */
public class Reference extends Expression {
	private final String name;

	Reference(final FunckyScriptEngine engine, final String name) {
		super(Objects.requireNonNull(engine, "Engine must not be null"));
		this.name = Objects.requireNonNull(name, "Name must not be null");
	}

	@Override
	public Literal eval(final ScriptContext context) throws ScriptException {
		final Object object = context.getAttribute(name);
		if (object instanceof Literal) {
			return (Literal) object;
		}
		throw new ScriptException(String.format("Reference %1$s is undefined", name));
	}

	@Override
	public FunckyType getType(final ScriptContext context) throws ScriptException {
		return eval(context).getType();
	}

	@Override
	public String toString() {
		return name;
	}
}
