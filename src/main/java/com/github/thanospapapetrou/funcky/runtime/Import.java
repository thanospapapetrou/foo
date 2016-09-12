package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedSymbolException;

public class Import extends AbstractSyntaxTreeNode {
	private final String prefix;
	private final String uri;

	public Import(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String prefix, final String uri) {
		super(engine, script, lineNumber);
		// TODO check prefix and URI
		this.prefix = prefix;
		this.uri = uri;
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedSymbolException {
		super.eval(context);
		// TODO implement
		return null;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getUri() {
		return uri;
	}
}
