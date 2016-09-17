package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.runtime.Definition;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Script;
import com.github.thanospapapetrou.funcky.runtime.Import;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunckyType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.FunctionType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Class representing a Funcky library.
 * @author thanos
 *
 */
public abstract class Library extends Script {
	private static final String EMPTY_NAME = "Name must not be empty";
	private static final String NULL_DOMAIN = "Domain must not be null";
	private static final String NULL_EXPRESSION = "Expression must not be null";
	private static final String NULL_NAME = "Name must not be null";
	private static final String NULL_RANGE = "Range must not be null";

	private final Script s;

	// TODO fix this mess
	// private static List<Definition> load(final FunckyScriptEngine engine, final URI uri, final String resource) throws IOException, ScriptException {
	// try (final InputStreamReader reader = new InputStreamReader(Library.class.getResourceAsStream(resource), StandardCharsets.UTF_8)) {
	// return new Parser(engine, reader, uri).parseScript().getDefinitions();
	// }
	// }

	public Library(final FunckyScriptEngine engine, final URI uri, final String resource) throws IOException, ScriptException { // TODO validate arguemnts, maybe replace IOException with some kind of ScriptException
		// super(engine, uri, 0, load(engine, uri, resource));
		super(engine, uri, 0, new ArrayList<Import>(), new ArrayList<Definition>());
		try (final InputStreamReader reader = new InputStreamReader(Library.class.getResourceAsStream(resource), StandardCharsets.UTF_8)) {
			s = new Parser(engine, reader, uri).parseScript();
		}
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.eval(context);
		for (final Definition definition : s.getDefinitions()) {
			definition.eval(context);
		}
		return null;
	}

	protected void addDefinition(final Expression expression) {
		definitions.add(new Definition(engine, script, 0, Objects.requireNonNull(expression, NULL_EXPRESSION).toString(), expression));
	}

	protected FunctionType generateFunctionType(final FunckyType domain, final FunckyType range) {
		return new FunctionType(engine, Objects.requireNonNull(domain, NULL_DOMAIN), Objects.requireNonNull(range, NULL_RANGE)); // TODO do we need to define this in script instead of at runtime?
	}

	protected TypeVariable generateTypeVariable() {
		return new TypeVariable(engine);
	}
}
