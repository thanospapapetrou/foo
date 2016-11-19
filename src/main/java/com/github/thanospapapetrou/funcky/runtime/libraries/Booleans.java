package com.github.thanospapapetrou.funcky.runtime.libraries;

import java.net.URI;

import javax.script.ScriptContext;
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
	 * Construct and load a new booleans library.
	 * 
	 * @param engine
	 *            the engine loading this booleans library
	 * @throws ScriptException
	 *             if any errors occur while loading this booleans library
	 */
	public Booleans(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
		final SimpleType booleanType = getSimpleType(BOOLEANS, BOOLEAN);
		addDefinition(booleanType);
		final Boolean booleanTrue = getBoolean(true);
		addDefinition(booleanTrue);
		final Boolean booleanFalse = getBoolean(false);
		addDefinition(booleanFalse);
		final TypeVariable equalType = getTypeVariable();
		addFunctorDefinition(EQUAL, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return arguments[0].eval(context).equals(arguments[1].eval(context)) ? booleanTrue : booleanFalse;
			}
		}, equalType, equalType, booleanType);
		final SimpleType type = (SimpleType) engine.getReference(Prelude.class, Prelude.TYPE).eval();
		addFunctorDefinition(EQUIVALENT, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return (((Type) arguments[0].eval(context)).inferGenericBindings((Type) arguments[1].eval(context)) == null) ? booleanFalse : booleanTrue;
			}
		}, type, type, booleanType);
		final TypeVariable ifType = getTypeVariable();
		addFunctorDefinition(IF, new ApplicableFunctor() {
			@Override
			public Literal apply(final ScriptContext context, final Expression... arguments) throws ScriptException {
				return ((Boolean) arguments[0].eval(context)).equals(booleanTrue) ? arguments[1].eval(context) : arguments[2].eval(context);
			}
		}, booleanType, ifType, ifType, ifType);
	}

	private Boolean getBoolean(final boolean value) {
		return new Boolean(engine, BOOLEANS, value);
	}
}
