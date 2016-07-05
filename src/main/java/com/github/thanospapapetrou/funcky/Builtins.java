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
		
		put(SimpleType.TYPE.toString(), SimpleType.TYPE);
		put(SimpleType.NUMBER.toString(), SimpleType.NUMBER);
		put(SimpleType.BOOLEAN.toString(), SimpleType.BOOLEAN);
		put(PI, FunckyNumber.PI);
		put(E, FunckyNumber.E);
		put(FunckyNumber.INFINITY.toString(), FunckyNumber.INFINITY);
		put(FunckyNumber.NAN.toString(), FunckyNumber.NAN);
		put(FunckyBoolean.TRUE.toString(), FunckyBoolean.TRUE);
		put(FunckyBoolean.FALSE.toString(), FunckyBoolean.FALSE);
		put(Function.FUNCTION.toString(), Function.FUNCTION);
		put(Function.TYPE_OF.toString(), Function.TYPE_OF);
		put(Function.IF.toString(), Function.IF);
		put(Function.ADD.toString(), Function.ADD);
		put(Function.SUBTRACT.toString(), Function.SUBTRACT);
		put(Function.MULTIPLY.toString(), Function.MULTIPLY);
		put(Function.DIVIDE.toString(), Function.DIVIDE);
	}
}
