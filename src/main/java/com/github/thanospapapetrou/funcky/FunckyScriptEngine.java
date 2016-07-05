package com.github.thanospapapetrou.funcky;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final String NULL_FACTORY = "Factory must not be null";
	private static final String ERROR_LOADING_PRELUDE = "Error loading prelude";
	private static final String NULL_SCRIPT = "Script must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String UNSUPPORTED_GET_INTERFACE = "getInterface() is not supported";
	private static final String UNSUPPORTED_INVOKE_FUNCTION = "invokeFunction() is not supported";
	private static final String UNSUPPORTED_INVOKE_METHOD = "invokeMethod() is not supported";

	private final FunckyScriptEngineFactory factory;

	FunckyScriptEngine(final FunckyScriptEngineFactory factory) {
		this.factory = Objects.requireNonNull(factory, NULL_FACTORY);
		setBindings(new Builtins(), ScriptContext.ENGINE_SCOPE);
		try {
			for (final Definition definition : compile(new InputStreamReader(getClass().getResourceAsStream(PRELUDE), StandardCharsets.UTF_8), PRELUDE_FILE_NAME).getDefinitions()) {
				definition.eval(context);
			}
		} catch (final ScriptException e) {
			Logger.getLogger(FunckyScriptEngine.class.getName()).log(Level.WARNING, ERROR_LOADING_PRELUDE, e);
		}
	}

	@Override
	public FunckyScript compile(final Reader script) throws ScriptException {
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), UNKNOWN);
	}

	@Override
	public Expression compile(final String script) throws ScriptException {
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), UNKNOWN);
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public Void eval(final Reader script, final ScriptContext context) throws ScriptException {
		compile(Objects.requireNonNull(script, NULL_SCRIPT), (String) Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(ScriptEngine.FILENAME)).eval(context);
		return null;
	}

	@Override
	public Literal eval(final String script, final ScriptContext context) throws ScriptException {
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), (String) Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(ScriptEngine.FILENAME)).eval(context);
	}

	@Override
	public FunckyScriptEngineFactory getFactory() {
		return factory;
	}

	@Override
	public <T> T getInterface(final Class<T> clazz) {
		throw new UnsupportedOperationException(UNSUPPORTED_GET_INTERFACE);
	}

	@Override
	public <T> T getInterface(final Object object, final Class<T> clazz) {
		throw new UnsupportedOperationException(UNSUPPORTED_GET_INTERFACE);
	}

	@Override
	public Object invokeFunction(final String function, final Object... arguments) throws ScriptException, NoSuchMethodException {
		throw new UnsupportedOperationException(UNSUPPORTED_INVOKE_FUNCTION);
	}

	@Override
	public Object invokeMethod(final Object object, final String method, final Object... arguments) throws ScriptException, NoSuchMethodException {
		throw new UnsupportedOperationException(UNSUPPORTED_INVOKE_METHOD);
	}

	private FunckyScript compile(final Reader script, final String fileName) throws ScriptException {
		return new Parser(this, script, fileName).parseScript();
	}

	private Expression compile(final String script, final String fileName) throws ScriptException {
		return new Parser(this, new StringReader(script), fileName).parseExpression();
	}
}
