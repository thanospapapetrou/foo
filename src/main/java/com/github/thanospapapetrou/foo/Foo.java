package com.github.thanospapapetrou.foo;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Foo {
	public static void main(final String[] arguments) {
		for (final ScriptEngineFactory factory : new ScriptEngineManager().getEngineFactories()) {
			System.out.println("Engine name: " + factory.getEngineName());
			System.out.println("Engine version: " + factory.getEngineVersion());
			System.out.println("Language name: " + factory.getLanguageName());
			System.out.println("Language version: " + factory.getLanguageVersion());
			System.out.println("Method call syntax: " + factory.getMethodCallSyntax("object", "method", "argument1", "argument2"));
			System.out.println("Output statement: " + factory.getOutputStatement("Hello world!"));
			System.out.println("Program: " + factory.getProgram("statement"));
			System.out.println("Extensions: " + factory.getExtensions());
			System.out.println("MIME types: " + factory.getMimeTypes());
			System.out.println("Names: " + factory.getNames());
			
		}
	}
}
