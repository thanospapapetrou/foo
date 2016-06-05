package com.github.thanospapapetrou.foo.runtime;

import java.util.Objects;

import javax.script.ScriptContext;
import javax.script.ScriptException;

public abstract class Function extends Literal {
	public static final Function ADD = new Function(SimpleType.NUMBER, new FunctionType(SimpleType.NUMBER, SimpleType.NUMBER)) {
		@Override
		public Literal apply(final Expression expression, final ScriptContext context) throws ScriptException {
			final FooNumber argument1 = (FooNumber) expression.eval(context);
			return new Function(SimpleType.NUMBER, SimpleType.NUMBER) {
				@Override
				public Literal apply(final Expression expression, final ScriptContext context) throws ScriptException {
					final FooNumber argument2 = (FooNumber) expression.eval(context);
					return new FooNumber(null, argument1.getValue() + argument2.getValue());
				}
			};
		}
	};

	private final FooType domain;
	private final FooType range;

	private Function(final FooType domain, final FooType range) {
		super(null);
		this.domain = Objects.requireNonNull(domain, "Domain must not be null");
		this.range = Objects.requireNonNull(range, "Range must not be null");
	}

	public abstract Literal apply(final Expression expression, final ScriptContext context) throws ScriptException;

	@Override
	protected FooType getType() {
		return new FunctionType(domain, range);
	}
}
