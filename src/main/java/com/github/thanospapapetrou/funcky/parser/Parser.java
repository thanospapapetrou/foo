package com.github.thanospapapetrou.funcky.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.parser.exceptions.InvalidCharacterLiteralException;
import com.github.thanospapapetrou.funcky.parser.exceptions.InvalidUriException;
import com.github.thanospapapetrou.funcky.parser.exceptions.UnexpectedTokenException;
import com.github.thanospapapetrou.funcky.parser.exceptions.UnparsableInputException;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Definition;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.Import;
import com.github.thanospapapetrou.funcky.runtime.Reference;
import com.github.thanospapapetrou.funcky.runtime.Script;
import com.github.thanospapapetrou.funcky.runtime.libraries.Lists;
import com.github.thanospapapetrou.funcky.runtime.libraries.Pairs;
import com.github.thanospapapetrou.funcky.runtime.literals.Character;
import com.github.thanospapapetrou.funcky.runtime.literals.List;
import com.github.thanospapapetrou.funcky.runtime.literals.Number;
import com.github.thanospapapetrou.funcky.runtime.literals.types.TypeVariable;

/**
 * Class implementing a Funcky parser. This parser is based on the following BNF:
 * 
 * <pre>
 * {@code
 * <script>           ::= <import>\n<script>
 *                      | <definition>\n<script>
 *                      | ε
 * <import>           ::= <symbol>\: <uri>
 * <definition>       ::= <symbol> \= <expression>
 * <expression>       ::= <simpleExpression>
 *                      | <application>
 * <simpleExpression> ::= <nestedExpression>
 *                      | <literal>
 *                      | <reference>
 * <application>      ::= <expression> <simpleExpression>
 * <nestedExpression  ::= (<expression>)
 * <literal>          ::= <number>
 *                      | <character>
 *                      | <pair>
 *                      | <list>
 *                      | <string>
 *                      | <typeVariable>
 * <reference>        ::= \{<uri>\}\:<symbol>
 * 						| <symbol>\:<symbol>
 *                      | <symbol>
 * <number>           ::= \-?\d(\.\d*)?
 *                      | \-?.\d*
 * <character>        ::= '[^\\']|(\\[tbnrf'\"\\])'
 * <pair>             ::= \<<expression>, <expression>\>
 * <list>             ::= [<elements>]
 *                      | []
 * <string>           ::= "([^\\\"]|(\\[tbnrf'\"\\]))*"
 * <typeVariable>     ::= \$<symbol>
 * <symbol>           ::= [\w&&\D]\w*
 * <uri>              ::= !#\$%&'\(\)\*\+\,\-\./\:;=\?@\[\]_~
 * <elements>         ::= <expression>, <elements>
 *                      | <expression>
 * }
 * </pre>
 * 
 * @author thanos
 */
@SuppressWarnings("javadoc")
public class Parser {
	private static final String ILLEGAL_STATE = "Parser is in illegal state";
	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_READER = "Reader must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";
	private static final String WHITESPACE = " \t\n\u000b\f\r";
	private static final String WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
	private static final String URI = "!#$%&'()*+,-./:;=?@[]~";

	private final FunckyScriptEngine engine;
	private final StreamTokenizer tokenizer;
	private final URI script;

	/**
	 * Construct a new parser.
	 * 
	 * @param engine
	 *            the engine of this parser
	 * @param reader
	 *            the reader to parse script from
	 * @param script
	 *            the URI of the script to use for error reporting
	 */
	public Parser(final FunckyScriptEngine engine, final Reader reader, final URI script) {
		this.engine = Objects.requireNonNull(engine, NULL_ENGINE);
		tokenizer = new StreamTokenizer(Objects.requireNonNull(reader, NULL_READER));
		initializeForSymbols();
		this.script = Objects.requireNonNull(script, NULL_SCRIPT);
	}

	/**
	 * Parse a Funcky expression.
	 * 
	 * @return the Funcky expression parsed
	 * @throws ScriptException
	 *             if any errors occur
	 */
	public Expression parseExpression() throws ScriptException {
		final Expression expression = parseExpression(Token.EOF);
		parseExpectedTokens(Token.EOF);
		return expression;
	}

	/**
	 * Parse a Funcky script.
	 * 
	 * @return the Funcky script parsed
	 * @throws ScriptException
	 *             if any errors occur
	 */
	public Script parseScript() throws ScriptException {
		final java.util.List<Import> imports = new ArrayList<>();
		final java.util.List<Definition> definitions = new ArrayList<>();
		while (true) {
			switch (parseExpectedTokens(Token.EOF, Token.EOL, Token.SYMBOL)) {
			case EOF:
				return new Script(engine, script, tokenizer.lineno(), imports, definitions);
			case EOL:
				break;
			case SYMBOL:
				final String symbol = tokenizer.sval;
				switch (parseExpectedTokens(Token.COLON, Token.EQUALS)) {
				case COLON:
					final URI uri = parseUri();
					parseExpectedTokens(Token.EOL);
					imports.add(new Import(engine, script, tokenizer.lineno() - 1, symbol, uri));
					break;
				case EQUALS:
					final Expression expression = parseExpression(Token.EOL);
					parseExpectedTokens(Token.EOL);
					definitions.add(new Definition(engine, script, tokenizer.lineno() - 1, symbol, expression));
					break;
				default:
					throw new IllegalStateException(ILLEGAL_STATE);
				}
				break;
			default:
				throw new IllegalStateException(ILLEGAL_STATE);
			}
		}
	}

	private void initializeForSymbols() {
		tokenizer.resetSyntax();
		tokenizer.parseNumbers();
		tokenizer.eolIsSignificant(true);
		tokenizer.lowerCaseMode(false);
		tokenizer.slashSlashComments(false);
		tokenizer.slashStarComments(false);
		tokenizer.commentChar(Token.COMMENT.getCode());
		tokenizer.quoteChar(Token.CHARACTER.getCode());
		tokenizer.quoteChar(Token.STRING.getCode());
		for (final char whitespace : WHITESPACE.toCharArray()) {
			tokenizer.whitespaceChars(whitespace, whitespace);
		}
		for (final char word : WORD.toCharArray()) {
			tokenizer.wordChars(word, word);
		}
	}

	private void initializeForUris() {
		tokenizer.resetSyntax();
		tokenizer.eolIsSignificant(true);
		tokenizer.lowerCaseMode(false);
		tokenizer.slashSlashComments(false);
		tokenizer.slashStarComments(false);
		for (final char whitespace : WHITESPACE.toCharArray()) {
			tokenizer.whitespaceChars(whitespace, whitespace);
		}
		for (final char word : WORD.toCharArray()) {
			tokenizer.wordChars(word, word);
		}
		for (final char uri : URI.toCharArray()) {
			tokenizer.wordChars(uri, uri);
		}
	}

	private Character parseCharacter() throws ScriptException {
		parseExpectedTokens(Token.CHARACTER);
		if (tokenizer.sval.length() != 1) {
			throw new InvalidCharacterLiteralException(tokenizer.sval, script, tokenizer.lineno());
		}
		return new Character(engine, script, tokenizer.lineno(), tokenizer.sval.charAt(0));
	}

	private Expression parseElements() throws ScriptException {
		Expression list = new List(engine, script, tokenizer.lineno());
		while (true) {
			list = new Application(engine, script, tokenizer.lineno(), new Application(engine, script, tokenizer.lineno(), engine.getReference(Lists.class, Lists.APPEND), list), parseExpression(Token.COMMA, Token.RIGHT_SQUARE_BRACKET));
			if (parseExpectedTokens(Token.COMMA, Token.RIGHT_SQUARE_BRACKET) == Token.RIGHT_SQUARE_BRACKET) {
				return list;
			}
		}
	}

	private Token parseExpectedTokens(final Token... expectedTokens) throws ScriptException {
		try {
			final int token = tokenizer.nextToken();
			for (final Token expectedToken : expectedTokens) {
				if (token == expectedToken.getCode()) {
					return expectedToken;
				}
			}
			for (final Token unexpectedToken : Token.values()) {
				if (token == unexpectedToken.getCode()) {
					throw new UnexpectedTokenException(unexpectedToken, script, tokenizer.lineno(), expectedTokens);
				}
			}
			throw new UnparsableInputException((char) tokenizer.ttype, script, tokenizer.lineno());
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	private Expression parseExpression(final Token... expectedTerminators) throws ScriptException {
		switch (parseExpectedTokens(Token.CHARACTER, Token.DOLLAR, Token.LEFT_ANGLE_BRACKET, Token.LEFT_CURLY_BRACKET, Token.LEFT_PARENTHESIS, Token.LEFT_SQUARE_BRACKET, Token.NUMBER, Token.STRING, Token.SYMBOL)) {
		case CHARACTER:
		case DOLLAR:
		case LEFT_ANGLE_BRACKET:
		case LEFT_CURLY_BRACKET:
		case LEFT_PARENTHESIS:
		case LEFT_SQUARE_BRACKET:
		case NUMBER:
		case STRING:
		case SYMBOL:
			tokenizer.pushBack();
			Expression expression = parseSimpleExpression();
			while (true) {
				final Token token = parseExpectedTokens(Token.CHARACTER, Token.COMMA, Token.DOLLAR, Token.EOF, Token.EOL, Token.LEFT_ANGLE_BRACKET, Token.LEFT_CURLY_BRACKET, Token.LEFT_PARENTHESIS, Token.LEFT_SQUARE_BRACKET, Token.NUMBER, Token.RIGHT_ANGLE_BRACKET, Token.RIGHT_PARENTHESIS, Token.RIGHT_SQUARE_BRACKET, Token.STRING, Token.SYMBOL);
				switch (token) {
				case CHARACTER:
				case DOLLAR:
				case LEFT_ANGLE_BRACKET:
				case LEFT_CURLY_BRACKET:
				case LEFT_PARENTHESIS:
				case LEFT_SQUARE_BRACKET:
				case NUMBER:
				case STRING:
				case SYMBOL:
					tokenizer.pushBack();
					expression = new Application(engine, script, tokenizer.lineno(), expression, parseSimpleExpression());
					break;
				case COMMA:
				case EOF:
				case EOL:
				case RIGHT_ANGLE_BRACKET:
				case RIGHT_PARENTHESIS:
				case RIGHT_SQUARE_BRACKET:
					for (final Token expectedTerminator : expectedTerminators) {
						if (token == expectedTerminator) {
							tokenizer.pushBack();
							return expression;
						}
					}
					throw new UnexpectedTokenException(token, script, tokenizer.lineno(), expectedTerminators);
				default:
					throw new IllegalStateException(ILLEGAL_STATE);
				}
			}
		default:
			throw new IllegalStateException(ILLEGAL_STATE);
		}
	}

	private Expression parseList() throws ScriptException {
		parseExpectedTokens(Token.LEFT_SQUARE_BRACKET);
		if (parseExpectedTokens(Token.CHARACTER, Token.DOLLAR, Token.LEFT_ANGLE_BRACKET, Token.LEFT_CURLY_BRACKET, Token.LEFT_PARENTHESIS, Token.LEFT_SQUARE_BRACKET, Token.NUMBER, Token.RIGHT_SQUARE_BRACKET, Token.STRING, Token.SYMBOL) == Token.RIGHT_SQUARE_BRACKET) {
			return new List(engine, script, tokenizer.lineno());
		}
		tokenizer.pushBack();
		return parseElements();
	}

	private Expression parseNestedExpression() throws ScriptException {
		parseExpectedTokens(Token.LEFT_PARENTHESIS);
		final Expression expression = parseExpression(Token.RIGHT_PARENTHESIS);
		parseExpectedTokens(Token.RIGHT_PARENTHESIS);
		return expression;
	}

	private Number parseNumber() throws ScriptException {
		parseExpectedTokens(Token.NUMBER);
		return new Number(engine, script, tokenizer.lineno(), tokenizer.nval);
	}

	private Application parsePair() throws ScriptException {
		parseExpectedTokens(Token.LEFT_ANGLE_BRACKET);
		final Expression first = parseExpression(Token.COMMA);
		parseExpectedTokens(Token.COMMA);
		final Expression second = parseExpression(Token.RIGHT_ANGLE_BRACKET);
		parseExpectedTokens(Token.RIGHT_ANGLE_BRACKET);
		return new Application(engine, script, tokenizer.lineno(), new Application(engine, script, tokenizer.lineno(), engine.getReference(Pairs.class, Pairs.COMBINE), first), second);
	}

	private Reference parseReference() throws ScriptException {
		switch (parseExpectedTokens(Token.LEFT_CURLY_BRACKET, Token.SYMBOL)) {
		case LEFT_CURLY_BRACKET:
			final URI uri = parseUri();
			parseExpectedTokens(Token.RIGHT_CURLY_BRACKET);
			parseExpectedTokens(Token.SYMBOL);
			return new Reference(engine, script, tokenizer.lineno(), uri, tokenizer.sval);
		case SYMBOL:
			final String symbol = tokenizer.sval;
			if (parseExpectedTokens(Token.CHARACTER, Token.COLON, Token.COMMA, Token.DOLLAR, Token.EOF, Token.EOL, Token.LEFT_ANGLE_BRACKET, Token.LEFT_CURLY_BRACKET, Token.LEFT_PARENTHESIS, Token.LEFT_SQUARE_BRACKET, Token.NUMBER, Token.RIGHT_ANGLE_BRACKET, Token.RIGHT_PARENTHESIS, Token.RIGHT_SQUARE_BRACKET, Token.STRING, Token.SYMBOL) == Token.COLON) {
				parseExpectedTokens(Token.SYMBOL);
				return new Reference(engine, script, tokenizer.lineno(), symbol, tokenizer.sval);
			}
			tokenizer.pushBack();
			return new Reference(engine, script, tokenizer.lineno(), script, symbol);
		default:
			throw new IllegalStateException(ILLEGAL_STATE);
		}
	}

	private Expression parseSimpleExpression() throws ScriptException {
		switch (parseExpectedTokens(Token.CHARACTER, Token.DOLLAR, Token.LEFT_ANGLE_BRACKET, Token.LEFT_CURLY_BRACKET, Token.LEFT_PARENTHESIS, Token.LEFT_SQUARE_BRACKET, Token.NUMBER, Token.STRING, Token.SYMBOL)) {
		case CHARACTER:
			tokenizer.pushBack();
			return parseCharacter();
		case DOLLAR:
			tokenizer.pushBack();
			return parseTypeVariable();
		case LEFT_ANGLE_BRACKET:
			tokenizer.pushBack();
			return parsePair();
		case LEFT_CURLY_BRACKET:
			tokenizer.pushBack();
			return parseReference();
		case LEFT_PARENTHESIS:
			tokenizer.pushBack();
			return parseNestedExpression();
		case LEFT_SQUARE_BRACKET:
			tokenizer.pushBack();
			return parseList();
		case NUMBER:
			tokenizer.pushBack();
			return parseNumber();
		case STRING:
			tokenizer.pushBack();
			return parseString();
		case SYMBOL:
			tokenizer.pushBack();
			return parseReference();
		default:
			throw new IllegalStateException(ILLEGAL_STATE);
		}
	}

	private List parseString() throws ScriptException {
		parseExpectedTokens(Token.STRING);
		return tokenizer.sval.isEmpty() ? new List(engine, script, tokenizer.lineno()) : new List(engine, script, tokenizer.lineno(), tokenizer.sval);
	}

	private TypeVariable parseTypeVariable() throws ScriptException {
		parseExpectedTokens(Token.DOLLAR);
		parseExpectedTokens(Token.SYMBOL);
		final String name = tokenizer.sval;
		return new TypeVariable(engine, script, tokenizer.lineno(), name);
	}

	private URI parseUri() throws ScriptException {
		initializeForUris();
		try {
			parseExpectedTokens(Token.URI);
			final URI uri = new URI(tokenizer.sval);
			if (uri.getScheme() == null) {
				throw new InvalidUriException(uri, script, tokenizer.lineno());
			}
			return uri;
		} catch (final URISyntaxException e) {
			throw new InvalidUriException(e, script, tokenizer.lineno());
		} finally {
			initializeForSymbols();
		}
	}
}
