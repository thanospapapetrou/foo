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
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableTwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.functors.Functor;
import com.github.thanospapapetrou.funcky.runtime.functors.TwoArgumentArithmeticOperator;
import com.github.thanospapapetrou.funcky.runtime.literals.ApplicableFunction;
import com.github.thanospapapetrou.funcky.runtime.literals.Function;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.Number;
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
	private static final int CURRENT_LIBRARY = 3;
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
	 */
	public static URI getUri(final Class<? extends Library> library) {
		try {
			return new URI(new FunckyScriptEngineFactory().getExtensions().get(0), library.getSimpleName().toLowerCase(Locale.ROOT), null);
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e); // TODO add message
		}
	}

	@SuppressWarnings("unchecked")
	private static URI getUri() {
		try {
			return getUri((Class<? extends Library>) Class.forName(Thread.currentThread().getStackTrace()[CURRENT_LIBRARY].getClassName()));
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e); // TODO add message
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
		super(Objects.requireNonNull(engine, NULL_ENGINE), getUri(), 0, new ArrayList<Import>(), new ArrayList<Definition>());
		try (final InputStreamReader reader = new InputStreamReader(Library.class.getResourceAsStream(String.format(SCRIPT, getClass().getSimpleName())), StandardCharsets.UTF_8)) {
			final Script script = new Parser(engine, reader, getUri(getClass())).parseScript();
			imports.addAll(script.getImports());
			definitions.addAll(script.getDefinitions());
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	protected void addDefinition(final Literal literal) {
		definitions.add(new Definition(engine, script, 0, ((Reference) Objects.requireNonNull(literal, NULL_LITERAL).toExpression()).getName(), literal));
	}

	protected void addFunctionDefinition(final String name, final Type domain, final Type range, final ApplicableFunction function) {
		addDefinition(new Function(engine, getUri(), name, getFunctionType(domain, range)) {
			@Override
			public Literal apply(final Expression argument, final ScriptContext context) throws ScriptException {
				super.apply(argument, context);
				return function.apply(argument, context);
			}
		});
	}

	protected void addFunctorDefinition(final String name, final ApplicableFunctor functor, final Type... types) {
		addDefinition(new Functor(engine, getUri(), name, types) {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				super.apply(context, arguments);
				return functor.apply(context, arguments);
			}
		});
	}

	protected void addTwoArgumentArithmeticOperatorDefinition(final String name, final SimpleType numberType, final ApplicableTwoArgumentArithmeticOperator operator) throws ScriptException { // TODO move this one level down in class hierarchy and remove number type
		addDefinition(new TwoArgumentArithmeticOperator(engine, getUri(), name, numberType) {
			@Override
			public Literal apply(final Number argument1, final Number argument2, final ScriptContext context) {
				super.apply(argument1, argument2, context);
				return operator.apply(argument1, argument2, context);
			}
		});
	}

	protected FunctionType getFunctionType(final Type domain, final Type range) {
		return new FunctionType(engine, Objects.requireNonNull(domain, NULL_DOMAIN), Objects.requireNonNull(range, NULL_RANGE));
	}

	protected SimpleType getSimpleType(final URI script, final String name) {
		return new SimpleType(engine, script, name);
	}

	protected TypeVariable getTypeVariable() {
		return new TypeVariable(engine);
	}
}
