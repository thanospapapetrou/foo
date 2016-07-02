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
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.runtime.Definition;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.FunckyScript;
import com.github.thanospapapetrou.funcky.runtime.Literal;

/**
 * Class implementing a Funcky script engine.
 * 
 * @author thanos
 */
public class FunckyScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
	private static final String PRELUDE = "/Prelude.funcky";
	private static final String PRELUDE_FILE_NAME = "<prelude>";
	private static final String UNKNOWN = "<unknown>";

	private final FunckyScriptEngineFactory factory;

	FunckyScriptEngine(final FunckyScriptEngineFactory factory) {
		this.factory = Objects.requireNonNull(factory, "Factory must not be null");
		setBindings(new Builtins(), ScriptContext.ENGINE_SCOPE);
		try {
			for (final Definition definition : compile(new InputStreamReader(getClass().getResourceAsStream(PRELUDE), StandardCharsets.UTF_8), PRELUDE_FILE_NAME).getDefinitions()) {
				this.getBindings(ScriptContext.ENGINE_SCOPE).put(definition.getName(), definition.getExpression().eval());
			}
		} catch (final ScriptException e) {
			throw new IllegalStateException("Error loading prelude", e);
		}
	}

	@Override
	public FunckyScript compile(final Reader script) throws ScriptException {
		return compile(script, UNKNOWN);
	}

	@Override
	public Expression compile(final String script) throws ScriptException {
		return compile(script, UNKNOWN);
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public Void eval(final Reader script, final ScriptContext context) throws ScriptException {
		compile(script, (String) context.getAttribute(ScriptEngine.FILENAME)).eval(context);
		return null;
	}

	@Override
	public Literal eval(final String script, final ScriptContext context) throws ScriptException {
		return compile(script, (String) context.getAttribute(ScriptEngine.FILENAME)).eval(context);
	}

	@Override
	public FunckyScriptEngineFactory getFactory() {
		return factory;
	}

	@Override
	public <T> T getInterface(final Class<T> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getInterface(final Object object, final Class<T> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object invokeFunction(final String function, final Object... arguments) throws ScriptException, NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object invokeMethod(final Object object, final String method, final Object... arguments) throws ScriptException, NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	private FunckyScript compile(final Reader script, final String fileName) throws ScriptException {
		return new Parser(this, script, fileName).parseScript();
	}

	private Expression compile(final String script, final String fileName) throws ScriptException {
		return new Parser(this, new StringReader(script), fileName).parseExpression();
	}
}
