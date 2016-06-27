package com.github.thanospapapetrou.funcky.runtime;

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
		super(null);
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.TYPE;
	}
}
