package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.net.URI;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.functors.ApplicableFunctor;
import com.github.thanospapapetrou.funcky.runtime.literals.Boolean;
import com.github.thanospapapetrou.funcky.runtime.literals.Literal;
import com.github.thanospapapetrou.funcky.runtime.literals.types.SimpleType;
import com.github.thanospapapetrou.funcky.runtime.literals.types.Type;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Booleans related library.
 * 
 * @author thanos
 */
public class Booleans extends Library {
	/**
	 * Type of booleans.
	 */
	public static final String BOOLEAN = "boolean";

	/**
	 * Equality operator.
	 */
	public static final String EQUAL = "equal";

	/**
	 * Type equivalence operator.
	 */
	public static final String EQUIVALENT = "equivalent";

	/**
	 * Boolean false.
	 */
	public static final String FALSE = "false";

	/**
	 * Ternary operator.
	 */
	public static final String IF = "if";

	/**
	 * Boolean true.
	 */
	public static final String TRUE = "true";

	private static final URI BOOLEANS = Library.getUri(Booleans.class);

	/**
	 * Construct a new booleans library.
	 * 
	 * @param engine
	 *            the engine constructing this booleans library
	 * @throws ScriptException
	 *             if any errors occur while constructing this booleans library
	 */
	public Booleans(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		final SimpleType booleanType = new SimpleType(engine, BOOLEANS, BOOLEAN);
		addDefinition(booleanType);
		final Boolean booleanTrue = getBoolean(true);
		addDefinition(booleanTrue);
		final Boolean booleanFalse = getBoolean(false);
		addDefinition(booleanFalse);
		final TypeVariable equalType = engine.getTypeVariable();
		addFunctorDefinition(EQUAL, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return arguments[0].eval().equals(arguments[1].eval()) ? booleanTrue : booleanFalse;
			}
		}, equalType, equalType, booleanType);
		final SimpleType type = engine.getLiteral(Prelude.class, Prelude.TYPE);
		addFunctorDefinition(EQUIVALENT, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return (((Type) arguments[0].eval()).inferGenericBindings((Type) arguments[1].eval()) == null) ? booleanFalse : booleanTrue;
			}
		}, type, type, booleanType);
		final TypeVariable ifType = engine.getTypeVariable();
		addFunctorDefinition(IF, new ApplicableFunctor() {
			@Override
			public Literal apply(final Expression... arguments) throws ScriptException {
				return ((Boolean) arguments[0].eval()).equals(booleanTrue) ? arguments[1].eval() : arguments[2].eval();
			}
		}, booleanType, ifType, ifType, ifType);
	}

	private Boolean getBoolean(final boolean value) {
		return new Boolean(engine, BOOLEANS, value);
	}
}
