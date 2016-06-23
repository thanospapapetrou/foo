package com.github.thanospapapetrou.funcky;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * Class implementing a Funcky script engine.
 * 
 * @author thanos
 */
public class FunckyScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
	private static final Bindings BUILTINS = new SimpleBindings() {
		{
			put(SimpleType.TYPE.toString(), SimpleType.TYPE);
			put(SimpleType.NUMBER.toString(), SimpleType.NUMBER);
			put(SimpleType.BOOLEAN.toString(), SimpleType.BOOLEAN);
			put("pi", FunckyNumber.PI);
			put("e", FunckyNumber.E);
			put(FunckyNumber.INFINITY.toString(), FunckyNumber.INFINITY);
			put(FunckyNumber.NAN.toString(), FunckyNumber.NAN);
			put(FunckyBoolean.TRUE.toString(), FunckyBoolean.TRUE);
			put(FunckyBoolean.FALSE.toString(), FunckyBoolean.FALSE);
			put(Function.FUNCTION.toString(), Function.FUNCTION);
			put(Function.ADD.toString(), Function.ADD);
			put(Function.SUBTRACT.toString(), Function.SUBTRACT);
			put(Function.MULTIPLY.toString(), Function.MULTIPLY);
			put(Function.DIVIDE.toString(), Function.DIVIDE);
		}
	};
	private static final String PRELUDE = "/Prelude.funcky";

	private final FunckyScriptEngineFactory factory;

	FunckyScriptEngine(final FunckyScriptEngineFactory factory) {
		this.factory = Objects.requireNonNull(factory, "Factory must not be null");
		setBindings(BUILTINS, ScriptContext.ENGINE_SCOPE);
		try {
			for (final Definition definition : compile(new InputStreamReader(getClass().getResourceAsStream(PRELUDE), StandardCharsets.UTF_8)).getDefinitions()) {
				this.getBindings(ScriptContext.ENGINE_SCOPE).put(definition.getName(), definition.getExpression().eval());
			}
		} catch (final ScriptException e) {
			throw new RuntimeException("Error loading prelude", e);
		}
	}

	@Override
	public FunckyScript compile(final Reader script) throws ScriptException {
		return new Parser(this, script).parseScript();
	}

	@Override
	public Expression compile(final String script) throws ScriptException {
		return new Parser(this, new StringReader(script)).parseExpression();
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public Void eval(final Reader script, final ScriptContext context) throws ScriptException {
		return compile(script).eval(context);
	}

	@Override
	public Literal eval(final String script, final ScriptContext context) throws ScriptException {
		return compile(script).eval(context);
	}

	@Override
	public FunckyScriptEngineFactory getFactory() {
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
	public Object invokeFunction(final String function, final Object... arguments) throws ScriptException, NoSuchMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeMethod(final Object object, final String method, final Object... arguments) throws ScriptException, NoSuchMethodException {
		// TODO Auto-generated method stub
		return null;
	}
}
