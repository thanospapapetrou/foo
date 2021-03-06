package com.github.thanospapapetrou.funcky.runtime.expressions.literals.types;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.expressions.Application;
import com.github.thanospapapetrou.funcky.runtime.libraries.Prelude;

/**
 * Class representing a Funcky function type.
 * 
 * @author thanos
 */
public class FunctionType extends Type {
	private final Type domain;
	private final Type range;

	/**
	 * Construct a new function type.
	 * 
	 * @param engine
	 *            the engine that generated this function type
	 * @param script
	 *            the URI of the script from which this function type was generated
	 * @param domain
	 *            the domain of this function type
	 * @param range
	 *            the range of this function type
	 */
	public FunctionType(final FunckyScriptEngine engine, final URI script, final Type domain, final Type range) {
		super(engine, script, -1);
		this.domain = domain;
		this.range = range;
	}

	@Override
	public FunctionType bind(final Map<TypeVariable, Type> bindings) {
		super.bind(bindings);
		return engine.getFunctionType(domain.bind(bindings), range.bind(bindings));
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
	public Type getDomain() {
		return domain;
	}

	/**
	 * Get the range of this function type.
	 * 
	 * @return the range
	 */
	public Type getRange() {
		return range;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, range);
	}

	@Override
	public Map<TypeVariable, Type> infer(final Type type) {
		super.infer(type);
		if (type instanceof TypeVariable) {
			return Collections.<TypeVariable, Type> singletonMap((TypeVariable) type, this);
		} else if (type instanceof FunctionType) {
			final FunctionType functionType = (FunctionType) type;
			final Map<TypeVariable, Type> domainBindings = domain.infer(functionType.domain);
			if (domainBindings == null) {
				return null;
			}
			final Map<TypeVariable, Type> rangeBindings = range.infer(functionType.range);
			if (rangeBindings == null) {
				return null;
			}
			final Map<TypeVariable, Type> bindings = new HashMap<>();
			bindings.putAll(domainBindings);
			bindings.putAll(rangeBindings);
			return bindings;
		}
		return null;
	}

	@Override
	public Application toExpression() {
		return engine.getApplication(engine.getApplication(engine.getReference(Prelude.class, Prelude.FUNCTION), domain), range);
	}

	@Override
	protected FunctionType free(final Map<TypeVariable, TypeVariable> free) {
		super.free(free);
		return engine.getFunctionType(domain.free(free), range.free(free));
	}
}
