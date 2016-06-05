package com.github.thanospapapetrou.foo.runtime;

/**
 * Abstract class representing a Foo type.
 * 
 * @author thanos
 */
public abstract class FooType extends Literal {
	/**
	 * Construct a new type.
	 */
	public FooType() {
		super(null);
	}

	@Override
	protected SimpleType getType() {
		return SimpleType.TYPE;
	}
}
