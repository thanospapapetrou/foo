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
		super(null, null, 0);
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.TYPE;
	}
}
