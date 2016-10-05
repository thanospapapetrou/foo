package com.github.thanospapapetrou.funcky.runtime;

import java.net.URI;

import javax.script.ScriptContext;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.runtime.exceptions.AlreadyDefinedSymbolException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidArgumentException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.InvalidFunctionException;
import com.github.thanospapapetrou.funcky.runtime.exceptions.UndefinedReferenceException;

public class Import extends AbstractSyntaxTreeNode {
	private final String prefix;
	private final URI uri;

	public Import(final FunckyScriptEngine engine, final URI script, final int lineNumber, final String prefix, final URI uri) {
		super(engine, script, lineNumber);
		// TODO check prefix and URI
		this.prefix = prefix;
		this.uri = uri;
	}

	@Override
	public Void eval(final ScriptContext context) throws AlreadyDefinedSymbolException, InvalidArgumentException, InvalidFunctionException, UndefinedReferenceException {
		super.eval(context);
		// TODO implement
		return null;
	}

	public String getPrefix() {
		return prefix;
	}

	public URI getUri() {
		return uri;
	}
}
