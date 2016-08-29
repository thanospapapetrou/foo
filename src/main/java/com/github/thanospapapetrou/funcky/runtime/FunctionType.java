package com.github.thanospapapetrou.funcky.runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Class representing a Funcky function type.
 * 
 * @author thanos
 */
public class FunctionType extends FunckyType {
	private static final String FUNCTION = "function";
	private static final String NULL_TYPE = "Type must not be null";

	private final FunckyType domain;
	private final FunckyType range;

	/**
	 * Construct a new function type.
	 * 
	 * @param engine
	 *            the engine that created this function type
	 * @param domain
	 *            the domain of this function type
	 * @param range
	 *            the range of this function type
	 */
	public FunctionType(final FunckyScriptEngine engine, final FunckyType domain, final FunckyType range) {
		super(engine, FunckyScriptEngine.RUNTIME, 0);
		this.domain = domain;
		this.range = range;
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		return new FunctionType(engine, domain.bind(bindings), range.bind(bindings));
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof FunctionType) {
			final FunctionType functionType = (FunctionType) object;
			return domain.equals(functionType.domain) && range.equals(functionType.range);
		}
		return false;
	}

	/**
	 * Get the domain of this function type.
	 * 
	 * @return the domain
	 */
	public FunckyType getDomain() {
		return domain;
	}

	/**
	 * Get the range of this function type.
	 * 
	 * @return the range
	 */
	public FunckyType getRange() {
		return range;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, range);
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		if (Objects.requireNonNull(type, NULL_TYPE) instanceof TypeVariable) {
			return Collections.<TypeVariable, FunckyType> singletonMap((TypeVariable) type, this);
		} else if (type instanceof FunctionType) {
			final FunctionType functionType = (FunctionType) type;
			final Map<TypeVariable, FunckyType> domainBindings = domain.inferGenericBindings(functionType.domain);
			final Map<TypeVariable, FunckyType> rangeBindings = range.inferGenericBindings(functionType.range);
			return ((domainBindings == null) || (rangeBindings == null)) ? null : new HashMap<TypeVariable, FunckyType>(domainBindings) {
				private static final long serialVersionUID = 1L;

				{
					putAll(rangeBindings);
					putAll(domainBindings);
				}
			};
		}
		return null;
	}

	@Override
	public Expression toExpression() {
		return new Application(engine, script, lineNumber, new Application(engine, script, lineNumber, new Reference(engine, script, lineNumber, FUNCTION), domain), range);
	}

	@Override
	public String toString() {
		return toExpression().toString();
	}
}
