package com.github.thanospapapetrou.funcky.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Funcky function type.
 * 
 * @author thanos
 */
class FunctionType extends FunckyType {
	private final FunckyType domain;
	private final FunckyType range;

	FunctionType(final FunckyType domain, final FunckyType range) {
		this.domain = domain;
		this.range = range;
	}

	@Override
	public FunckyType bind(final Map<TypeVariable, FunckyType> bindings) {
		return new FunctionType(domain.bind(bindings), range.bind(bindings));
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof FunctionType) {
			final FunctionType functionType = (FunctionType) object;
			return domain.equals(functionType.domain) && range.equals(functionType.range);
		}
		return false;
	}

	public FunckyType getDomain() {
		return domain;
	}

	public FunckyType getRange() {
		return range;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, range);
	}

	@Override
	public Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type) {
		if (type instanceof FunctionType) {
			final FunctionType functionType = (FunctionType) type;
			final Map<TypeVariable, FunckyType> domainBindings = domain.inferGenericBindings(functionType.domain);
			final Map<TypeVariable, FunckyType> rangeBindings = range.inferGenericBindings(functionType.range);
			return ((domainBindings == null) || (rangeBindings == null)) ? null : new HashMap<TypeVariable, FunckyType>(domainBindings) {
				private static final long serialVersionUID = 1L;

				{
					putAll(rangeBindings); // TODO may domain bindings and range bindings be conflicting?
				}
			};
		}
		return null;
	}

	@Override
	public Expression toExpression() {
		return new Application(new Application(Function.FUNCTION, domain), range);
	}

	@Override
	public String toString() {
		return toExpression().toString();
	}
}
