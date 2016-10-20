package com.github.thanospapapetrou.funcky.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
import com.github.thanospapapetrou.funcky.runtime.libraries.Prelude;
import com.github.thanospapapetrou.funcky.runtime.literals.Character;
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
	/**
	 * Character
	 */
	public static final int CHARACTER = '\'';

	/**
	 * Colon
	 */
	public static final int COLON = ':';

	/**
	 * Comma
	 */
	public static final int COMMA = ',';

	/**
	 * Comment
	 */
	public static final int COMMENT = '#';

	/**
	 * Dollar ('$')
	 */
	public static final int DOLLAR = '$';

	/**
	 * End of input
	 */
	public static final int EOF = StreamTokenizer.TT_EOF;

	/**
	 * End of line
	 */
	public static final int EOL = StreamTokenizer.TT_EOL;

	/**
	 * Equals ('=')
	 */
	public static final int EQUALS = '=';

	/**
	 * Left angle bracket ('<')
	 */
	public static final int LEFT_ANGLE_BRACKET = '<';

	/**
	 * Left curly bracket ('{')
	 */
	public static final int LEFT_CURLY_BRACKET = '{';

	/**
	 * Left parenthesis ('(')
	 */
	public static final int LEFT_PARENTHESIS = '(';

	/**
	 * Left square bracket ('[')
	 */
	public static final int LEFT_SQUARE_BRACKET = '[';

	/**
	 * Number
	 */
	public static final int NUMBER = StreamTokenizer.TT_NUMBER;

	/**
	 * Right angle bracket ('>')
	 */
	public static final int RIGHT_ANGLE_BRACKET = '>';

	/**
	 * Right curly bracket
	 */
	public static final int RIGHT_CURLY_BRACKET = '}';

	/**
	 * Right parenthesis (')')
	 */
	public static final int RIGHT_PARENTHESIS = ')';

	/**
	 * Right square bracket (']')
	 */
	public static final int RIGHT_SQUARE_BRACKET = ']';

	/**
	 * String
	 */
	public static final int STRING = '"';

	/**
	 * Symbol
	 */
	public static final int SYMBOL = StreamTokenizer.TT_WORD;

	private static final String NULL_ENGINE = "Engine must not be null";
	private static final String NULL_READER = "Reader must not be null";
	private static final String NULL_SCRIPT = "Script must not be null";
	private static final String PRODUCT = "product";
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
		tokenizer.resetSyntax();
		tokenizer.parseNumbers();
		tokenizer.eolIsSignificant(true);
		tokenizer.lowerCaseMode(false);
		tokenizer.slashSlashComments(false);
		tokenizer.slashStarComments(false);
		tokenizer.commentChar(COMMENT);
		tokenizer.quoteChar(CHARACTER);
		tokenizer.quoteChar(STRING);
		for (final char whitespace : WHITESPACE.toCharArray()) {
			tokenizer.whitespaceChars(whitespace, whitespace);
		}
		for (final char word : WORD.toCharArray()) {
			tokenizer.wordChars(word, word);
		}
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
		final Expression expression = parseExpression(EOF);
		parseExpectedTokens(EOF);
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
		final List<Import> imports = new ArrayList<>();
		final List<Definition> definitions = new ArrayList<>();
		while (true) {
			switch (parseExpectedTokens(EOF, EOL, SYMBOL)) {
			case EOF:
				return new Script(engine, script, tokenizer.lineno(), imports, definitions);
			case EOL:
				break;
			case SYMBOL:
				final String symbol = tokenizer.sval;
				switch (parseExpectedTokens(COLON, EQUALS)) {
				case COLON:
					final URI uri = parseUri();
					parseExpectedTokens(EOL);
					imports.add(new Import(engine, script, tokenizer.lineno() - 1, symbol, uri));
					break;
				case EQUALS:
					final Expression expression = parseExpression(EOL);
					parseExpectedTokens(EOL);
					definitions.add(new Definition(engine, script, tokenizer.lineno() - 1, symbol, expression));
				}
			}
		}
	}

	private Character parseCharacter() throws ScriptException {
		parseExpectedTokens(CHARACTER);
		if (tokenizer.sval.length() != 1) {
			throw new InvalidCharacterLiteralException(tokenizer.sval, script, tokenizer.lineno());
		}
		return new Character(engine, script, tokenizer.lineno(), tokenizer.sval.charAt(0));
	}

	private List<Expression> parseElements() throws ScriptException {
		final List<Expression> expressions = new ArrayList<>();
		while (true) {
			expressions.add(parseExpression(COMMA, RIGHT_SQUARE_BRACKET));
			if (parseExpectedTokens(COMMA, RIGHT_SQUARE_BRACKET) == RIGHT_SQUARE_BRACKET) {
				return expressions;
			}
		}
	}

	private int parseExpectedTokens(final int... expectedTokens) throws ScriptException {
		try {
			final int token = tokenizer.nextToken();
			for (final int expectedToken : expectedTokens) {
				if (token == expectedToken) {
					return token;
				}
			}
			for (final int unexpectedToken : new int[] {CHARACTER, COLON, COMMA, COMMENT, DOLLAR, EOF, EOL, EQUALS, LEFT_ANGLE_BRACKET, LEFT_CURLY_BRACKET, LEFT_PARENTHESIS, LEFT_SQUARE_BRACKET, NUMBER, RIGHT_ANGLE_BRACKET, RIGHT_CURLY_BRACKET, RIGHT_PARENTHESIS, RIGHT_SQUARE_BRACKET, STRING, SYMBOL}) {
				if (token == unexpectedToken) {
					throw new UnexpectedTokenException(tokenizer.ttype, script, tokenizer.lineno(), expectedTokens);
				}
			}
			throw new UnparsableInputException((char) tokenizer.ttype, script, tokenizer.lineno());
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	private Expression parseExpression(final int... expectedTerminators) throws ScriptException {
		switch (parseExpectedTokens(CHARACTER, DOLLAR, LEFT_ANGLE_BRACKET, LEFT_CURLY_BRACKET, LEFT_PARENTHESIS, LEFT_SQUARE_BRACKET, NUMBER, STRING, SYMBOL)) {
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
				final int token = parseExpectedTokens(CHARACTER, COMMA, DOLLAR, EOF, EOL, LEFT_ANGLE_BRACKET, LEFT_CURLY_BRACKET, LEFT_PARENTHESIS, LEFT_SQUARE_BRACKET, NUMBER, RIGHT_ANGLE_BRACKET, RIGHT_PARENTHESIS, RIGHT_SQUARE_BRACKET, STRING, SYMBOL);
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
					for (final int expectedTerminator : expectedTerminators) {
						if (token == expectedTerminator) {
							tokenizer.pushBack();
							return expression;
						}
					}
					throw new UnexpectedTokenException(token, script, tokenizer.lineno(), expectedTerminators);
				}
			}
		default:
			throw new IllegalStateException();
		}
	}

	private Expression parseList() throws ScriptException { // TODO change return type
		parseExpectedTokens(LEFT_SQUARE_BRACKET);
		if (parseExpectedTokens(CHARACTER, DOLLAR, LEFT_ANGLE_BRACKET, LEFT_CURLY_BRACKET, LEFT_PARENTHESIS, LEFT_SQUARE_BRACKET, NUMBER, RIGHT_SQUARE_BRACKET, STRING, SYMBOL) == RIGHT_SQUARE_BRACKET) {
			// TODO return empty list
			return null;
		}
		tokenizer.pushBack();
		final List<Expression> elements = parseElements();
		parseExpectedTokens(RIGHT_SQUARE_BRACKET);
		// TODO return elements
		return null;
	}

	private Expression parseNestedExpression() throws ScriptException {
		parseExpectedTokens(LEFT_PARENTHESIS);
		final Expression expression = parseExpression(RIGHT_PARENTHESIS);
		parseExpectedTokens(RIGHT_PARENTHESIS);
		return expression;
	}

	private Number parseNumber() throws ScriptException {
		parseExpectedTokens(NUMBER);
		return new Number(engine, script, tokenizer.lineno(), tokenizer.nval);
	}

	private Application parsePair() throws ScriptException {
		parseExpectedTokens(LEFT_ANGLE_BRACKET);
		final Expression first = parseExpression(COMMA);
		parseExpectedTokens(COMMA);
		final Expression second = parseExpression(RIGHT_ANGLE_BRACKET);
		parseExpectedTokens(RIGHT_ANGLE_BRACKET);
		return new Application(engine, script, tokenizer.lineno(), new Application(engine, script, tokenizer.lineno(), engine.getReference(Prelude.class, Prelude.PRODUCT), first), second);
	}

	private Reference parseReference() throws ScriptException {
		switch (parseExpectedTokens(LEFT_CURLY_BRACKET, SYMBOL)) {
		case LEFT_CURLY_BRACKET:
			final URI uri = parseUri();
			parseExpectedTokens(RIGHT_CURLY_BRACKET);
			parseExpectedTokens(SYMBOL);
			return new Reference(engine, script, tokenizer.lineno(), uri, tokenizer.sval);
		case SYMBOL:
			final String symbol = tokenizer.sval;
			if (parseExpectedTokens(CHARACTER, COLON, COMMA, DOLLAR, EOF, EOL, LEFT_ANGLE_BRACKET, LEFT_CURLY_BRACKET, LEFT_PARENTHESIS, LEFT_SQUARE_BRACKET, NUMBER, RIGHT_ANGLE_BRACKET, RIGHT_PARENTHESIS, RIGHT_SQUARE_BRACKET, STRING, SYMBOL) == COLON) {
				parseExpectedTokens(SYMBOL);
				return new Reference(engine, script, tokenizer.lineno(), symbol, tokenizer.sval);
			}
			tokenizer.pushBack();
			return new Reference(engine, script, tokenizer.lineno(), script, symbol);
		default:
			throw new IllegalStateException();
		}
	}

	private Expression parseSimpleExpression() throws ScriptException {
		switch (parseExpectedTokens(CHARACTER, DOLLAR, LEFT_ANGLE_BRACKET, LEFT_CURLY_BRACKET, LEFT_PARENTHESIS, LEFT_SQUARE_BRACKET, NUMBER, STRING, SYMBOL)) {
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
			throw new IllegalStateException();
		}
	}

	private Expression parseString() throws ScriptException {
		parseExpectedTokens(STRING);
		// return new List(tokenizer.sval,... TODO retun new list of chars
		return null;
	}

	private TypeVariable parseTypeVariable() throws ScriptException {
		parseExpectedTokens(DOLLAR);
		parseExpectedTokens(SYMBOL);
		final String name = tokenizer.sval;
		return new TypeVariable(engine, script, tokenizer.lineno(), name);
	}

	private URI parseUri() throws ScriptException {
		for (final char word : URI.toCharArray()) {
			tokenizer.wordChars(word, word);
		}
		try {
			parseExpectedTokens(SYMBOL); // TODO if this fails throw UnexpectedTokenException with URI instead of symbol as name
			return new URI(tokenizer.sval);
		} catch (final URISyntaxException e) {
			throw new InvalidUriException(e, script, tokenizer.lineno());
		} finally {
			for (final char word : URI.toCharArray()) {
				tokenizer.ordinaryChar(word);
			}
		}
	}
}
