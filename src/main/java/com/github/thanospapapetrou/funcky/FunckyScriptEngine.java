package com.github.thanospapapetrou.funcky;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;
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
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Script;
import com.github.thanospapapetrou.funcky.runtime.libraries.Prelude;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;

/**
 * Class implementing a Funcky script engine.
 * 
 * @author thanos
 */
public class FunckyScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
	/**
	 * The script URI corresponding to abstract syntax tree nodes generated at runtime.
	 */
	public static final URI RUNTIME = URI.create("funcky:runtime");

	private static final String ERROR_LOADING_PRELUDE = "Error loading prelude";
	private static final Logger LOGGER = Logger.getLogger(FunckyScriptEngine.class.getName());
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_GLOBAL_SCOPE_BINDINGS = "Global scope bindings must not be null";
	private static final String NULL_FACTORY = "Factory must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";
	private static final URI STDIN = URI.create("funcky:stdin");
	private static final String UNSUPPORTED_GET_INTERFACE = "getInterface() is not supported";
	private static final String UNSUPPORTED_INVOKE_FUNCTION = "invokeFunction() is not supported";
	private static final String UNSUPPORTED_INVOKE_METHOD = "invokeMethod() is not supported";

	private final FunckyScriptEngineFactory factory;
	private Prelude prelude;

	/**
	 * Construct a new script engine.
	 * 
	 * @param factory
	 *            the script engine factory of this script engine
	 * @param globalScopeBindings
	 *            the global scope bindings of the initial context of this script engine
	 */
	public FunckyScriptEngine(final FunckyScriptEngineFactory factory, final Bindings globalScopeBindings) {
		this.factory = Objects.requireNonNull(factory, NULL_FACTORY);
		setContext(new FunckyScriptContext(Objects.requireNonNull(globalScopeBindings, NULL_GLOBAL_SCOPE_BINDINGS)));
		try {
			(this.prelude = new Prelude(this)).eval(); // TODO do not eval prelude to load it
		} catch (final IOException | ScriptException e) {
			LOGGER.log(Level.WARNING, ERROR_LOADING_PRELUDE, e);
		}
	}

	@Override
	public Script compile(final Reader script) throws ScriptException {
		try {
			return compile(Objects.requireNonNull(script, NULL_SCRIPT), Paths.get(Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(ScriptEngine.FILENAME).toString()).toRealPath().toUri());
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	@Override
	public Expression compile(final String script) throws ScriptException {
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), STDIN);
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public Void eval(final Reader script, final ScriptContext context) throws ScriptException {
		try {
			compile(Objects.requireNonNull(script, NULL_SCRIPT), Paths.get(Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(ScriptEngine.FILENAME).toString()).toRealPath().toUri()).eval(context);
			return null;
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	@Override
	public Literal eval(final String script, final ScriptContext context) throws ScriptException {
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), STDIN).eval(Objects.requireNonNull(context, NULL_CONTEXT));
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

	/**
	 * Get the prelude loaded by this engine.
	 * 
	 * @return the prelude
	 */
	public Prelude getPrelude() {
		return prelude;
	}

	@Override
	public Object invokeFunction(final String function, final Object... arguments) {
		throw new UnsupportedOperationException(UNSUPPORTED_INVOKE_FUNCTION);
	}

	@Override
	public Object invokeMethod(final Object object, final String method, final Object... arguments) {
		throw new UnsupportedOperationException(UNSUPPORTED_INVOKE_METHOD);
	}

	private Script compile(final Reader script, final URI scriptUri) throws ScriptException {
		return new Parser(this, script, scriptUri).parseScript();
	}

	private Expression compile(final String script, final URI scriptUri) throws ScriptException {
		return new Parser(this, new StringReader(script), scriptUri).parseExpression();
	}
}
