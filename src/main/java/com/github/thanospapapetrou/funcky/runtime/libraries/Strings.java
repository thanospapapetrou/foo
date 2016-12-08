package com.github.thanospapapetrou.funcky.runtime.libraries;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;

/**
 * Strings related library.
 * 
 * @author thanos
 */
public class Strings extends Library {
	/**
	 * Type of strings.
	 */
	public static final String STRING = "string";

	/**
	 * Construct a new strings library.
	 * 
	 * @param engine
	 *            the engine constructing this strings library
	 * @throws ScriptException
	 *             if any errors occur while constructing this strings library
	 */
	public Strings(final FunckyScriptEngine engine) throws ScriptException {
		super(engine);
	}
}
