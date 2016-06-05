package com.github.thanospapapetrou.foo.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.foo.FooScriptEngine;
import com.github.thanospapapetrou.foo.runtime.Application;
import com.github.thanospapapetrou.foo.runtime.Expression;
import com.github.thanospapapetrou.foo.runtime.FooNumber;
import com.github.thanospapapetrou.foo.runtime.Reference;

/**
 * <script> ::= <definition>\n<script> | ε <definition> ::= <symbol>=<expression> <expression> ::= (<expression>) | <application> | <list> | <tuple> | <literal> | <symbol> <application> ::= <expression> <expression> <list> ::= [<expressions>] | [] <tuple> ::= {<expression>,<expressions>} <expressions> ::= <expression>,<expressions> | <expression> <literal> ::= <number> | <character> | <string> <symbol> ::= ... <number> ::= ... <character> ::= '...' <string> ::= "..."
 * 
 * @author thanos
 */
public class Parser {
	private static final String WHITESPACE = "\t\n\f\r ";
	private static final String WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
	private static final int HASH = '#';
	private static final int LEFT_PARENTHESIS = '(';
	private static final int RIGHT_PARENTHESIS = ')';
	private static final int SYMBOL = StreamTokenizer.TT_WORD;
	private static final int NUMBER = StreamTokenizer.TT_NUMBER;
	private static final int CHARACTER = '\'';
	private static final int STRING = '"';
	private static final int EOL = StreamTokenizer.TT_EOL;
	private static final int EOF = StreamTokenizer.TT_EOF;
	private static final Map<Integer, String> TOKEN_NAMES = new HashMap<Integer, String>();
	// TODO remove these
	private static final String UNEXPECTED = "Unexpected %1$s, expected %2$s";
	private static final String UNPARSABLE = "Unparsable input %1$s";
	private static final String OR_2 = "%1$s or %2$s";
	private static final String OR_MORE = "%1$s, %2$s";

	private final FooScriptEngine engine;
	private final StreamTokenizer tokenizer;

	static {
		TOKEN_NAMES.put(HASH, "hash"); // TODO do we need this as token?
		TOKEN_NAMES.put(LEFT_PARENTHESIS, "left parenthesis");
		TOKEN_NAMES.put(RIGHT_PARENTHESIS, "right parenthesis");
		TOKEN_NAMES.put(SYMBOL, "symbol");
		TOKEN_NAMES.put(NUMBER, "number");
		TOKEN_NAMES.put(CHARACTER, "character");
		TOKEN_NAMES.put(STRING, "string");
		TOKEN_NAMES.put(EOL, "end of line");
		TOKEN_NAMES.put(EOF, "end of input");
	}

	public Parser(final FooScriptEngine engine, final Reader reader) {
		this.engine = Objects.requireNonNull(engine, "Engine must not be null");
		tokenizer = new StreamTokenizer(Objects.requireNonNull(reader, "Reader must not be null"));
		tokenizer.resetSyntax();
		tokenizer.parseNumbers();
		tokenizer.eolIsSignificant(true);
		tokenizer.lowerCaseMode(false);
		tokenizer.slashSlashComments(false);
		tokenizer.slashStarComments(false);
		tokenizer.commentChar(HASH);
		tokenizer.quoteChar(CHARACTER);
		tokenizer.quoteChar(STRING);
		for (final char whitespace : WHITESPACE.toCharArray()) {
			tokenizer.whitespaceChars(whitespace, whitespace);
		}
		for (final char word : WORD.toCharArray()) {
			tokenizer.wordChars(word, word);
		}
	}

	public Expression parseExpression() throws ScriptException {
		Expression expression = null;
		try {
			while (true) {
				switch (tokenizer.nextToken()) {
				case LEFT_PARENTHESIS:
					final Expression nestedExpression = parseExpression();
					switch (tokenizer.ttype) {
					case RIGHT_PARENTHESIS:
						expression = (expression == null) ? nestedExpression : new Application(engine, expression, nestedExpression);
						break;
					default:
						unexpected(RIGHT_PARENTHESIS);
					}
					break;
				case SYMBOL:
					final Reference reference = new Reference(engine, tokenizer.sval);
					expression = (expression == null) ? reference : new Application(engine, expression, reference);
					break;
				case NUMBER:
					final FooNumber number = new FooNumber(engine, tokenizer.nval);
					expression = (expression == null) ? number : new Application(engine, expression, number);
					break;
				default:
					return expression;
				}
			}
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	private void unexpected(final Integer... expected) throws IOException, ScriptException {
		throw new ScriptException(String.format(UNEXPECTED, getTokenName(tokenizer.ttype), or(expected)), "file", tokenizer.lineno(), 0); // TODO file, column
	}

	private String or(final Integer... tokens) throws ScriptException {
		return (tokens.length == 1) ? getTokenName(tokens[0]) : ((tokens.length == 2) ? String.format(OR_2, getTokenName(tokens[0]), getTokenName(tokens[1])) : String.format(OR_MORE, getTokenName(tokens[0]), or(Arrays.asList(tokens).subList(1, tokens.length).toArray(new Integer[0]))));
	}

	private String getTokenName(final Integer token) throws ScriptException {
		final String name = TOKEN_NAMES.get(token);
		if (name == null) {
			throw new ScriptException(String.format(UNPARSABLE, tokenizer.sval), "file", tokenizer.lineno(), 0); // TODO file, column
		}
		return name;
	}
}
