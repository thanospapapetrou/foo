package com.github.thanospapapetrou.funcky;

import javax.script.SimpleBindings;

import com.github.thanospapapetrou.funcky.runtime.FunckyBoolean;
import com.github.thanospapapetrou.funcky.runtime.FunckyNumber;
import com.github.thanospapapetrou.funcky.runtime.Function;
import com.github.thanospapapetrou.funcky.runtime.SimpleType;

/**
 * Class defining the built-in references of a Funcky script engine.
 * 
 * @author thanos
 */
public class Builtins extends SimpleBindings {
	private static final String PI = "pi";
	private static final String E = "e";

	Builtins() {
		for (final SimpleType simpleType : new SimpleType[] {SimpleType.TYPE, SimpleType.NUMBER, SimpleType.BOOLEAN}) {
			put(simpleType.toString(), simpleType);
		}
		put(PI, FunckyNumber.PI);
		put(E, FunckyNumber.E);
		for (final FunckyNumber number : new FunckyNumber[] {FunckyNumber.INFINITY, FunckyNumber.NAN}) {
			put(number.toString(), number);
		}
		for (final FunckyBoolean bool : new FunckyBoolean[] {FunckyBoolean.FALSE, FunckyBoolean.TRUE}) {
			put(bool.toString(), bool);
		}
		for (final Function function : new Function[] {Function.FUNCTION, Function.TYPE_OF, Function.IF, Function.ADD, Function.SUBTRACT, Function.MULTIPLY, Function.DIVIDE}) {
			put(function.toString(), function);
		}
	}
}
