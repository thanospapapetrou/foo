package com.github.thanospapapetrou.funcky;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
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
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.Script;
import com.github.thanospapapetrou.funcky.runtime.libraries.Booleans;
import com.github.thanospapapetrou.funcky.runtime.libraries.Library;
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

	@SuppressWarnings("unchecked")
	private static final Class<Library>[] BUILTIN_LIBRARIES = (Class<Library>[]) new Class<?>[] {Prelude.class, Booleans.class};
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String EMPTY_PREFIX = "Prefix must not be empty";
	private static final String IMPORTS = "%1$s.imports";
	private static final String MAX_SCOPES = "Maximum number of scopes reached";
	private static final String NULL_ARGUMENT = "Argument must not be null";
	private static final String NULL_CONTEXT = "Context must not be null";
	private static final String NULL_GLOBAL_SCOPE_BINDINGS = "Global scope bindings must not be null";
	private static final String NULL_FACTORY = "Factory must not be null";
	private static final String NULL_FUNCTION = "Function must not be null";
	private static final String NULL_LIBRARY = "Library must not be null";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_NAMESPACE = "Namespace must not be null";
	private static final String NULL_PREFIX = "Prefix must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";
	private static final String NULL_URI = "URI must not be null";
	private static final String SCRIPT = "%1$s.script";
	private static final URI STDIN = URI.create("funcky:stdin");
	private static final String UNSUPPORTED_GET_INTERFACE = "getInterface() is not supported";
	private static final String UNSUPPORTED_INVOKE_FUNCTION = "invokeFunction() is not supported";
	private static final String UNSUPPORTED_INVOKE_METHOD = "invokeMethod() is not supported";

	private final FunckyScriptEngineFactory factory;

	private static String requireValidString(final String string, final String nullError, final String emptyError) {
		if (Objects.requireNonNull(string, nullError).isEmpty()) {
			throw new IllegalArgumentException(emptyError);
		}
		return string;
	}

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
		createScope(context, STDIN);
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), STDIN);
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	/**
	 * Create a new scope for a script.
	 * 
	 * @param context
	 *            the context in which to create the new scope
	 * @param script
	 *            the URI of the script for which to create the new scope
	 * @throws ScriptException
	 *             if the new scope can not be created because the maximum number of scopes has been reached
	 */
	public void createScope(final ScriptContext context, final URI script) throws ScriptException {
		Objects.requireNonNull(context, NULL_CONTEXT);
		Objects.requireNonNull(script, NULL_SCRIPT);
		for (int scope = Integer.MIN_VALUE; scope < Integer.MAX_VALUE; scope++) {
			if (!context.getScopes().contains(scope)) {
				context.setAttribute(script.toString(), scope, ScriptContext.ENGINE_SCOPE);
				context.setAttribute(String.format(SCRIPT, getFactory().getExtensions().get(0)), script, scope);
				context.setAttribute(String.format(IMPORTS, getFactory().getExtensions().get(0)), new HashMap<String, URI>(), scope);
				return;
			}
		}
		throw new ScriptException(MAX_SCOPES);
	}

	/**
	 * Declare an import.
	 * 
	 * @param context
	 *            the context in which to declare the import
	 * @param script
	 *            the URI of the script in which to declare the import
	 * @param prefix
	 *            the prefix of the import to declare
	 * @param uri
	 *            the URI of the import to declare
	 */
	@SuppressWarnings("unchecked")
	public void declareImport(final ScriptContext context, final URI script, final String prefix, final URI uri) {
		final int scope = getScope(Objects.requireNonNull(context, NULL_CONTEXT), Objects.requireNonNull(script, NULL_SCRIPT));
		((HashMap<String, URI>) context.getAttribute(String.format(IMPORTS, getFactory().getExtensions().get(0)), scope)).put(requireValidString(prefix, NULL_PREFIX, EMPTY_PREFIX), Objects.requireNonNull(uri, NULL_URI));
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
		createScope(context, STDIN);
		return compile(Objects.requireNonNull(script, NULL_SCRIPT), STDIN).eval(Objects.requireNonNull(context, NULL_CONTEXT));
	}

	/**
	 * Generate a new application at runtime.
	 * 
	 * @param function
	 *            the function of the application
	 * @param argument
	 *            the argument of the application
	 * @return a new application
	 */
	public Application getApplication(final Expression function, final Expression argument) {
		return new Application(this, RUNTIME, 0, Objects.requireNonNull(function, NULL_FUNCTION), Objects.requireNonNull(argument, NULL_ARGUMENT));
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
	 * Generate a new reference at runtime.
	 * 
	 * @param namespace
	 *            the namespace of the reference
	 * @param name
	 *            the name of the reference
	 * @return a new reference
	 */
	public Reference getReference(final URI namespace, final String name) {
		return new Reference(this, FunckyScriptEngine.RUNTIME, 0, Objects.requireNonNull(namespace, NULL_NAMESPACE), requireValidString(name, NULL_NAME, EMPTY_NAME));
	}

	/**
	 * Generate a new reference at runtime.
	 * 
	 * @param library
	 *            the library of the reference
	 * @param name
	 *            the name of the reference
	 * @return a new reference
	 */
	public Reference getReference(final Class<? extends Library> library, final String name) {
		return getReference(Library.getUri(Objects.requireNonNull(library, NULL_LIBRARY)), requireValidString(name, NULL_NAME, EMPTY_NAME));
	}

	/**
	 * Get the scope of a script.
	 * 
	 * @param context
	 *            the context in which to search
	 * @param script
	 *            the URI of the script whose scope to retrieve
	 * @return the scope of the given script in the given context or <code>null</code> if the given script is not loaded in the current context
	 */
	public Integer getScope(final ScriptContext context, final URI script) {
		return (Integer) Objects.requireNonNull(context, NULL_CONTEXT).getAttribute(Objects.requireNonNull(script, NULL_SCRIPT).toString(), ScriptContext.ENGINE_SCOPE);
	}

	@Override
	public Object invokeFunction(final String function, final Object... arguments) {
		throw new UnsupportedOperationException(UNSUPPORTED_INVOKE_FUNCTION);
	}

	@Override
	public Object invokeMethod(final Object object, final String method, final Object... arguments) {
		throw new UnsupportedOperationException(UNSUPPORTED_INVOKE_METHOD);
	}

	/**
	 * Load a script.
	 * 
	 * @param context
	 *            the context in which to load the script
	 * @param script
	 *            the URI of the script to load
	 * @throws ScriptException
	 *             if any errors occur while loading the script
	 */
	public void load(final ScriptContext context, final URI script) throws ScriptException {
		Objects.requireNonNull(context, NULL_CONTEXT);
		if (Objects.requireNonNull(script, NULL_SCRIPT).getScheme().equals(getFactory().getExtensions().get(0))) { // builtin library
			for (final Class<Library> library : BUILTIN_LIBRARIES) {
				if (Library.getUri(library).equals(script)) {
					try {
						library.getConstructor(FunckyScriptEngine.class).newInstance(this).eval(context);
						return;
					} catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
						throw new ScriptException(e);
					}
				}
			}
			throw new ScriptException("Unknown library " + script); // TODO throw something more specific
		} else {
			try (final InputStreamReader reader = new InputStreamReader(script.toURL().openStream(), StandardCharsets.UTF_8)) {
				this.compile(reader, script).eval(context);
			} catch (final IOException e) {
				throw new ScriptException(e);
			}
		}
	}

	/**
	 * Resolve a prefix.
	 * 
	 * @param context
	 *            the context in which to search
	 * @param script
	 *            the URI of the script in which to search
	 * @param prefix
	 *            the prefix to resolve
	 * @return the URI corresponding to the given prefix in the given script and context or <code>null</code> if the given prefix is not declared in the given script and context
	 */
	@SuppressWarnings("unchecked")
	public URI resolvePrefix(final ScriptContext context, final URI script, final String prefix) {
		final int scope = getScope(Objects.requireNonNull(context, NULL_CONTEXT), Objects.requireNonNull(script, NULL_SCRIPT));
		return ((HashMap<String, URI>) context.getAttribute(String.format(IMPORTS, getFactory().getExtensions().get(0)), scope)).get(prefix);
	}

	private Script compile(final Reader script, final URI scriptUri) throws ScriptException {
		return new Parser(this, script, scriptUri).parseScript();
	}

	private Expression compile(final String script, final URI scriptUri) throws ScriptException {
		return new Parser(this, new StringReader(script), scriptUri).parseExpression();
	}
}
