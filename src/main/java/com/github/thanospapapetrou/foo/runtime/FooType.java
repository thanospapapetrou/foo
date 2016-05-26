package com.github.thanospapapetrou.foo.runtime;

/**
 * Abstract class representing a Foo type.
 * 
 * @author thanos
 */
public abstract class FooType extends Literal {
	@Override
	protected SimpleType getType() {
		return SimpleType.TYPE;
	}
}
