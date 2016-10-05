package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
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
	private static final String NULL_LITERAL = "Literal must not be null";
	private static final String NULL_RANGE = "Range must not be null";

	private static List<Definition> load(final FunckyScriptEngine engine, final URI uri, final String resource) throws IOException, ScriptException {
		try (final InputStreamReader reader = new InputStreamReader(Library.class.getResourceAsStream(resource), StandardCharsets.UTF_8)) {
			return new Parser(engine, reader, uri).parseScript().getDefinitions();
		}
	}

	public Library(final FunckyScriptEngine engine, final URI uri, final String resource) throws IOException, ScriptException { // TODO validate arguemnts, maybe replace IOException with some kind of ScriptException
		super(engine, uri, 0, new ArrayList<Import>(), load(engine, uri, resource));
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
