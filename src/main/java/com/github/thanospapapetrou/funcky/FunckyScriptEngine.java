package com.github.thanospapapetrou.funcky;

import java.io.Reader;
import java.io.StringReader;
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
	private final FunckyScriptEngineFactory factory;

	FunckyScriptEngine(final FunckyScriptEngineFactory factory) {
		this.factory = Objects.requireNonNull(factory, "Factory must not be null");
		setBindings(new SimpleBindings() {
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
				put(Function.ADD.toString(), Function.ADD);
			}
		}, ScriptContext.ENGINE_SCOPE);
	}

	@Override
	public FunckyScript compile(final Reader script) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
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
