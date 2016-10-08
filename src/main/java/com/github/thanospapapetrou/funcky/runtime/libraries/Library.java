package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.FunckyScriptEngineFactory;
import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.runtime.Definition;
import com.github.thanospapapetrou.funcky.runtime.Import;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.Script;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Class representing a Funcky library.
 * 
 * @author thanos
 */
public abstract class Library extends Script {
	private static final String NULL_DOMAIN = "Domain must not be null";
	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_LITERAL = "Literal must not be null";
	private static final String NULL_RANGE = "Range must not be null";
	private static final String SCRIPT = "/%1$s.funcky";

	/**
	 * Get the namespace URI corresponding to a library.
	 * 
	 * @param library
	 *            the library of which the namespace URI to get
	 * @return the namespace URI of the library provided
	 * @throws ScriptException
	 *             if any errors occur while retrieving the namespace URI of the library provided
	 */
	public static URI getUri(final Class<?> library) throws ScriptException {
		try {
			return new URI(new FunckyScriptEngineFactory().getLanguageName().toLowerCase(Locale.ROOT), library.getSimpleName().toLowerCase(Locale.ROOT), null);
		} catch (final URISyntaxException e) {
			throw new ScriptException(e);
		}
	}

	private static Class<?> getCurrentLibrary() throws ScriptException {
		try {
			return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
		} catch (final ClassNotFoundException e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * Construct and load a new library.
	 * 
	 * @param engine
	 *            the engine that is loading this library
	 * @throws ScriptException
	 *             if any errors occur while loading this library
	 */
	public Library(final FunckyScriptEngine engine) throws ScriptException {
		super(Objects.requireNonNull(engine, NULL_ENGINE), getUri(getCurrentLibrary()), 0, new ArrayList<Import>(), new ArrayList<Definition>());
		try (final InputStreamReader reader = new InputStreamReader(Library.class.getResourceAsStream(String.format(SCRIPT, getClass().getSimpleName())), StandardCharsets.UTF_8)) {
			definitions.addAll(new Parser(engine, reader, getUri(getClass())).parseScript().getDefinitions());
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	protected void addDefinition(final Literal literal) {
		definitions.add(new Definition(engine, script, 0, ((Reference) Objects.requireNonNull(literal, NULL_LITERAL).toExpression()).getName(), literal));
	}

	protected FunctionType generateFunctionType(final Type domain, final Type range) {
		return new FunctionType(engine, Objects.requireNonNull(domain, NULL_DOMAIN), Objects.requireNonNull(range, NULL_RANGE));
	}

	protected TypeVariable generateTypeVariable() {
		return new TypeVariable(engine);
	}
}
