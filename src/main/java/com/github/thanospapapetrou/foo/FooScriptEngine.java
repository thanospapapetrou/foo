package com.github.thanospapapetrou.foo;

import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.github.thanospapapetrou.foo.parser.Parser;
import com.github.thanospapapetrou.foo.runtime.Expression;
import com.github.thanospapapetrou.foo.runtime.FooCompiledScript;
import com.github.thanospapapetrou.foo.runtime.Literal;

public class FooScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
	private final FooScriptEngineFactory factory;

	public FooScriptEngine(final FooScriptEngineFactory factory) {
		this.factory = Objects.requireNonNull(factory, "Factory must not be null");
	}

	@Override
	public Expression compile(final String script) throws ScriptException {
		return new Parser(this, new StringReader(script)).parseExpression();
	}

	@Override
	public FooCompiledScript compile(final Reader script) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public Literal eval(final String script, final ScriptContext context) throws ScriptException {
		return compile(script).eval(context);
	}

	@Override
	public Void eval(final Reader script, final ScriptContext context) throws ScriptException {
		return compile(script).eval(context);
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return factory;
	}

	@Override
	public <T> T getInterface(final Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getInterface(final Object object, final Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeMethod(final Object object, final String method, final Object... arguments) throws ScriptException, NoSuchMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeFunction(final String function, final Object... arguments) throws ScriptException, NoSuchMethodException {
		// TODO Auto-generated method stub
		return null;
	}
}
