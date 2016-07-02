package com.github.thanospapapetrou.funcky.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.script.ScriptException;

import com.github.thanospapapetrou.funcky.FunckyScriptEngine;
import com.github.thanospapapetrou.funcky.parser.exceptions.UnexpectedTokenException;
import com.github.thanospapapetrou.funcky.parser.exceptions.UnparsableInputException;
import com.github.thanospapapetrou.funcky.runtime.Application;
import com.github.thanospapapetrou.funcky.runtime.Definition;
import com.github.thanospapapetrou.funcky.runtime.Expression;
import com.github.thanospapapetrou.funcky.runtime.FunckyNumber;
import com.github.thanospapapetrou.funcky.runtime.FunckyScript;
import com.github.thanospapapetrou.funcky.runtime.Reference;

/**
 * Class implementing a Funcky parser. This parser is based on the following BNF:
 * 
 * <pre>
 * {@code
 * <script>			::= <definition>\n<script>
 * 					  | ε
 * <definition>		::= <symbol>=<expression>
 * <expression>		::= (<expression>)
 * 					  | <application>
 * 					  | <literal>
 * 					  | <symbol>
 * 					  | <pair>
 * 					  | <list>
 * 					  | <string>
 * <application>	::= <expression> <expression>
 * <literal>		::= <number>
 * 					  | <character>
 * <symbol>			::= [\w&&\D]\w*
 * <pair>			::= &#123;<expression>, <expression>&#125;
 * <list>			::= [<expressions>]
 * <string>			::= "..." TODO
 * 					  | ε
 * <number>			::= \-?\d(\.\d*)?
 * 					  | \-?.\d*
 * <character> 		::= '...' TODO
 * <expressions>	::= <expression>, <expressions>
 * }
 * </pre>
 * 
 * @author thanos
 */
public class Parser {
	/**
	 * Comment
	 */
	public static final int COMMENT = '#';

	/**
	 * Equals ('=')
	 */
	public static final int EQUALS = '=';

	/**
	 * Left parenthesis ('(')
	 */
	public static final int LEFT_PARENTHESIS = '(';

	/**
	 * Right parenthesis (')')
	 */
	public static final int RIGHT_PARENTHESIS = ')';

	/**
	 * Symbol
	 */
	public static final int SYMBOL = StreamTokenizer.TT_WORD;

	/**
	 * Number
	 */
	public static final int NUMBER = StreamTokenizer.TT_NUMBER;

	/**
	 * Character
	 */
	public static final int CHARACTER = '\'';

	/**
	 * String
	 */
	public static final int STRING = '"';

	/**
	 * End of line
	 */
	public static final int EOL = StreamTokenizer.TT_EOL;

	/**
	 * End of input
	 */
	public static final int EOF = StreamTokenizer.TT_EOF;
	private static final String WHITESPACE = "\t\n\f\r ";
	private static final String WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";

	private final FunckyScriptEngine engine;
	private final StreamTokenizer tokenizer;
	private final String fileName;

	/**
	 * Construct a new parser.
	 * 
	 * @param engine
	 *            the engine of this parser
	 * @param reader
	 *            the reader to parse script from
	 * @param fileName
	 *            the name of the file to use for error reporting
	 */
	public Parser(final FunckyScriptEngine engine, final Reader reader, final String fileName) {
		this.engine = Objects.requireNonNull(engine, "Engine must not be null");
		tokenizer = new StreamTokenizer(Objects.requireNonNull(reader, "Reader must not be null"));
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
		this.fileName = Objects.requireNonNull(fileName, "File name must not be null");
	}

	/**
	 * Parse a Funcky script.
	 * 
	 * @return the Funcky script parsed
	 * @throws ScriptException
	 *             if any errors occur
	 */
	public FunckyScript parseScript() throws ScriptException {
		try {
			final List<Definition> definitions = new ArrayList<>();
			while (true) {
				switch (tokenizer.nextToken()) {
				case SYMBOL:
					tokenizer.pushBack();
					definitions.add(parseDefinition());
					break;
				case EOL:
					break;
				case EOF:
					return new FunckyScript(engine, fileName, tokenizer.lineno(), definitions);
				case EQUALS:
				case LEFT_PARENTHESIS:
				case RIGHT_PARENTHESIS:
				case NUMBER:
				case CHARACTER:
				case STRING:
					return throwUnexpectedTokenException(SYMBOL, EOL, EOF);
				default:
					return throwUnparsableInputException();
				}
			}
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * Parse a Funcky expression.
	 * 
	 * @return the Funcky expression parsed
	 * @throws ScriptException
	 *             if any errors occur
	 */
	public Expression parseExpression() throws ScriptException {
		try {
			final Expression expression = _parseExpression();
			switch (tokenizer.nextToken()) {
			case EOF:
				return expression;
			case EQUALS:
			case LEFT_PARENTHESIS:
			case RIGHT_PARENTHESIS:
			case SYMBOL:
			case NUMBER:
			case CHARACTER:
			case STRING:
			case EOL:
				return throwUnexpectedTokenException(EOF);
			default:
				return throwUnparsableInputException();
			}
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	private Definition parseDefinition() throws IOException, UnexpectedTokenException, UnparsableInputException, ScriptException { // TODO remove ScriptException by applications
		switch (tokenizer.nextToken()) {
		case SYMBOL:
			final String name = tokenizer.sval;
			switch (tokenizer.nextToken()) {
			case EQUALS:
				final Expression expression = _parseExpression();
				switch (tokenizer.nextToken()) {
				case EOL:
					return new Definition(engine, fileName, tokenizer.lineno(), name, expression);
				case EQUALS:
				case LEFT_PARENTHESIS:
				case RIGHT_PARENTHESIS:
				case NUMBER:
				case CHARACTER:
				case STRING:
				case EOF:
					return throwUnexpectedTokenException(EOL);
				default:
					return throwUnparsableInputException();
				}
			case LEFT_PARENTHESIS:
			case RIGHT_PARENTHESIS:
			case SYMBOL:
			case NUMBER:
			case CHARACTER:
			case STRING:
			case EOL:
			case EOF:
				return throwUnexpectedTokenException(EQUALS);
			default:
				return throwUnparsableInputException();
			}
		case LEFT_PARENTHESIS:
		case RIGHT_PARENTHESIS:
		case NUMBER:
		case CHARACTER:
		case STRING:
		case EOL:
		case EOF:
			return throwUnexpectedTokenException(SYMBOL);
		default:
			return throwUnparsableInputException();
		}
	}

	private Expression _parseExpression() throws IOException, UnexpectedTokenException, UnparsableInputException, ScriptException { // TODO remove ScriptException by applications
		Expression expression = null;
		while (true) {
			switch (tokenizer.nextToken()) {
			case LEFT_PARENTHESIS:
				final Expression nestedExpression = _parseExpression();
				switch (tokenizer.nextToken()) {
				case RIGHT_PARENTHESIS:
					expression = (expression == null) ? nestedExpression : new Application(engine, fileName, tokenizer.lineno(), expression, nestedExpression);
					break;
				case EQUALS:
				case LEFT_PARENTHESIS:
				case SYMBOL:
				case NUMBER:
				case CHARACTER:
				case STRING:
				case EOL:
				case EOF:
					return throwUnexpectedTokenException(RIGHT_PARENTHESIS);
				default:
					return throwUnparsableInputException();
				}
				break;
			case SYMBOL:
				final Reference reference = new Reference(engine, fileName, tokenizer.lineno(), tokenizer.sval);
				expression = (expression == null) ? reference : new Application(engine, fileName, tokenizer.lineno(), expression, reference);
				break;
			case NUMBER:
				final FunckyNumber number = new FunckyNumber(engine, fileName, tokenizer.lineno(), tokenizer.nval);
				expression = (expression == null) ? number : new Application(engine, fileName, tokenizer.lineno(), expression, number);
				break;
			case EQUALS:
			case RIGHT_PARENTHESIS:
			case CHARACTER:
			case STRING:
			case EOL:
			case EOF:
				if (expression == null) {
					return throwUnexpectedTokenException(LEFT_PARENTHESIS, SYMBOL, NUMBER);
				} else {
					tokenizer.pushBack();
					return expression;
				}
			default:
				return throwUnparsableInputException();
			}
		}
	}

	private <T> T throwUnexpectedTokenException(final int... expectedTokens) throws UnexpectedTokenException {
		throw new UnexpectedTokenException(tokenizer.ttype, fileName, tokenizer.lineno(), expectedTokens);
	}

	private <T> T throwUnparsableInputException() throws UnparsableInputException {
		throw new UnparsableInputException((char) tokenizer.ttype, fileName, tokenizer.lineno());
	}
}
