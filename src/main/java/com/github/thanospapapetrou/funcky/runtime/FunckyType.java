package com.github.thanospapapetrou.funcky.runtime;

import java.util.Map;

/**
 * Abstract class representing a Funcky type.
 * 
 * @author thanos
 */
public abstract class FunckyType extends Literal {
	/**
	 * Construct a new type.
	 */
	protected FunckyType() {
		super(null, null, 0);
	}

	public abstract FunckyType bind(final Map<TypeVariable, FunckyType> bindings);

	public abstract Map<TypeVariable, FunckyType> inferGenericBindings(final FunckyType type);

	@Override
	public SimpleType getType() {
		return SimpleType.TYPE;
	}
}
