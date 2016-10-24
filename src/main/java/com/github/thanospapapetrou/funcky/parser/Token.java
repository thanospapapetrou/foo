package com.github.thanospapapetrou.funcky.parser;

import java.io.StreamTokenizer;

/**
 * Enumeration representing a Funcky token.
 * 
 * @author thanos
 */
public enum Token {
	/**
	 * Character
	 */
	CHARACTER('\'', "character"),

	/**
	 * Colon (':')
	 */
	COLON(':', "colon"),

	/**
	 * Comma (',')
	 */
	COMMA(',', "comma"),

	/**
	 * Comment
	 */
	COMMENT('#', "comment"),

	/**
	 * Dollar ('$')
	 */
	DOLLAR('$', "dollar"),

	/**
	 * End of file
	 */
	EOF(StreamTokenizer.TT_EOF, "end of input"),

	/**
	 * End of line
	 */
	EOL(StreamTokenizer.TT_EOL, "end of line"),

	/**
	 * Equals ('=')
	 */
	EQUALS('=', "equals"),

	/**
	 * Left angle bracket ('<')
	 */
	LEFT_ANGLE_BRACKET('<', "left angle bracket"),

	/**
	 * Left curly bracket ('{')
	 */
	LEFT_CURLY_BRACKET('{', "left curly bracket"),

	/**
	 * Left parenthesis ('(')
	 */
	LEFT_PARENTHESIS('(', "left parenthesis"),

	/**
	 * Left square bracket ('[')
	 */
	LEFT_SQUARE_BRACKET('[', "left square bracket"),

	/**
	 * Number
	 */
	NUMBER(StreamTokenizer.TT_NUMBER, "number"),

	/**
	 * Right angle bracket ('>')
	 */
	RIGHT_ANGLE_BRACKET('>', "right angle bracket"),

	/**
	 * Right curly bracket ('}')
	 */
	RIGHT_CURLY_BRACKET('}', "right curly bracket"),

	/**
	 * Right parenthesis (')')
	 */
	RIGHT_PARENTHESIS(')', "right parenthesis"),

	/**
	 * Right square bracket (']')
	 */
	RIGHT_SQUARE_BRACKET(']', "right square bracket"),

	/**
	 * String
	 */
	STRING('"', "string"),

	/**
	 * Symbol
	 */
	SYMBOL(StreamTokenizer.TT_WORD, "symbol"),

	/**
	 * URI
	 */
	URI(StreamTokenizer.TT_WORD, "URI");

	private final int code;
	private final String name;

	private Token(final int code, final String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * Get the code.
	 * 
	 * @return the code of this token
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Get the name.
	 * 
	 * @return the name of this token
	 */
	public String getName() {
		return name;
	}
}
