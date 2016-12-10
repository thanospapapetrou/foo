package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.FunckyScriptEngineFactory;
import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.runtime.Definition;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Import;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.Script;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.functors.Functor;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Class representing a Funcky library.
 * 
 * @author thanos
 */
public abstract class Library extends Script {
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String ERROR_RETRIEVING_URI_FOR_CURRENT_LIBRARY = "Error retrieving URI corresponding to current library class";
	private static final String ERROR_RETRIEVING_URI_FOR_LIBRARY = "Error retrieving URI corresponding to library class %1$s";
	private static final String NULL_DOMAIN = "Domain must not be null";
	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_LITERAL = "Literal must not be null";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_RANGE = "Range must not be null";
	private static final String OUTSIDE_LIBRARY_CONTEXT = String.format("Method %1$s.getUri() should not be called outside the context of a library class", Library.class.getName());
	private static final String SCRIPT = "/%1$s.funcky";

	/**
	 * Construct a new library.
	 * 
	 * @param engine
	 *            the engine constructing this library
	 * @throws ScriptException
	 *             if any errors occur while constructing this library
	 */
	public Library(final FunckyScriptEngine engine) throws ScriptException {
		super(Objects.requireNonNull(engine, NULL_ENGINE), getUri(), 0, new ArrayList<Import>(), new ArrayList<Definition>());
		try (final InputStreamReader reader = new InputStreamReader(Library.class.getResourceAsStream(String.format(SCRIPT, getClass().getSimpleName())), StandardCharsets.UTF_8)) {
			final Script script = new Parser(engine, reader, getUri(getClass())).parseScript();
			imports.addAll(script.getImports());
			definitions.addAll(script.getDefinitions());
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * Get the URI corresponding to a library.
	 * 
	 * @param library
	 *            the library of which the URI to get
	 * @return the URI of the library provided
	 */
	public static URI getUri(final Class<? extends Library> library) {
		try {
			return new URI(new FunckyScriptEngineFactory().getExtensions().get(0), library.getSimpleName().toLowerCase(Locale.ROOT), null);
		} catch (final URISyntaxException e) {
			throw new RuntimeException(String.format(ERROR_RETRIEVING_URI_FOR_LIBRARY, library.getName()), e);
		}
	}

	@SuppressWarnings("unchecked")
	private static URI getUri() {
		try {
			for (final StackTraceElement element : Thread.currentThread().getStackTrace()) {
				final Class<?> clazz = Class.forName(element.getClassName());
				if (Library.class.isAssignableFrom(clazz) && (!Library.class.equals(clazz))) {
					return getUri((Class<? extends Library>) clazz);
				}
			}
			throw new IllegalStateException(OUTSIDE_LIBRARY_CONTEXT);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(ERROR_RETRIEVING_URI_FOR_CURRENT_LIBRARY, e);
		}
	}

	private static String requireValidName(final String name) {
		if (Objects.requireNonNull(name, NULL_NAME).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_NAME);
		}
		return name;
	}

	/**
	 * Add a new literal definition to this library.
	 * 
	 * @param name
	 *            the definition name
	 * @param literal
	 *            the definition literal
	 */
	protected void addDefinition(final String name, final Literal literal) {
		definitions.add(new Definition(engine, script, 0, requireValidName(name), Objects.requireNonNull(literal, NULL_LITERAL)));
	}

	/**
	 * Add a new literal definition to this library, using the literal representation as name.
	 * 
	 * @param literal
	 *            the definition literal
	 */
	protected void addDefinition(final Literal literal) {
		addDefinition(((Reference) Objects.requireNonNull(literal, NULL_LITERAL).toExpression()).getName(), literal);
	}

	/**
	 * Add a new function definition to this library.
	 * 
	 * @param name
	 *            the name of the function to define
	 * @param domain
	 *            the domain of the function to define
	 * @param range
	 *            the range of the function to define
	 * @param function
	 *            the implementation of the function to define
	 */
	protected void addFunctionDefinition(final String name, final Type domain, final Type range, final ApplicableFunction function) {
		addDefinition(new Function(engine, getUri(), name, getFunctionType(domain, range)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				super.apply(argument, context);
				return function.apply(argument, context);
			}
		});
	}

	/**
	 * Add a new functor definition to this library.
	 * 
	 * @param name
	 *            the name of the functor to define
	 * @param functor
	 *            the implementation of the functor to define
	 * @param types
	 *            the types of the functor to define
	 */
	protected void addFunctorDefinition(final String name, final ApplicableFunctor functor, final Type... types) {
		addDefinition(new Functor(engine, getUri(), name, types) {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return functor.apply(context, arguments);
			}
		});
	}

	/**
	 * Generate a new function type.
	 * 
	 * @param domain
	 *            the domain of the function type to generate
	 * @param range
	 *            the range of the function type to generate
	 * @return a new function type with the given domain and range
	 */
	protected FunctionType getFunctionType(final Type domain, final Type range) {
		return new FunctionType(engine, Objects.requireNonNull(domain, NULL_DOMAIN), Objects.requireNonNull(range, NULL_RANGE));
	}

	/**
	 * Generate a new simple type.
	 * 
	 * @param script
	 *            the URI of the script of the simple type to generate
	 * @param name
	 *            the name of the simple type to generate
	 * @return a new simple type with the given script URI and name
	 */
	protected SimpleType getSimpleType(final URI script, final String name) {
		return new SimpleType(engine, script, name);
	}

	/**
	 * Generate a new type variable.
	 * 
	 * @return a new type variable
	 */
	protected TypeVariable getTypeVariable() {
		return new TypeVariable(engine);
	}
}
