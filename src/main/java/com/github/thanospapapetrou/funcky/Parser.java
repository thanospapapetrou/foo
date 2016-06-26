package com.github.thanospapapetrou.funcky;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.script.ScriptException;

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
	private static final String WHITESPACE = "\t\n\f\r ";
	private static final String WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
	private static final int COMMENT = '#';
	private static final int EQUALS = '=';
	private static final int LEFT_PARENTHESIS = '(';
	private static final int RIGHT_PARENTHESIS = ')';
	private static final int SYMBOL = StreamTokenizer.TT_WORD;
	private static final int NUMBER = StreamTokenizer.TT_NUMBER;
	private static final int CHARACTER = '\'';
	private static final int STRING = '"';
	private static final int EOL = StreamTokenizer.TT_EOL;
	private static final int EOF = StreamTokenizer.TT_EOF;
	private static final Map<Integer, String> TOKEN_NAMES = new HashMap<>();

	private final FunckyScriptEngine engine;
	private final StreamTokenizer tokenizer;

	static {
		TOKEN_NAMES.put(EQUALS, "equals");
		TOKEN_NAMES.put(LEFT_PARENTHESIS, "left parenthesis");
		TOKEN_NAMES.put(RIGHT_PARENTHESIS, "right parenthesis");
		TOKEN_NAMES.put(SYMBOL, "symbol");
		TOKEN_NAMES.put(NUMBER, "number");
		TOKEN_NAMES.put(CHARACTER, "character");
		TOKEN_NAMES.put(STRING, "string");
		TOKEN_NAMES.put(EOL, "end of line");
		TOKEN_NAMES.put(EOF, "end of input");
	}

	Parser(final FunckyScriptEngine engine, final Reader reader) {
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
					continue;
				case EOL:
					continue;
				case EOF:
					return new FunckyScript(engine, definitions);
				default:
					return this.<FunckyScript> unexpected(SYMBOL, EOL, EOF);
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
			return (tokenizer.nextToken() == EOF) ? expression : this.<Expression> unexpected(EOF);
		} catch (final IOException e) {
			throw new ScriptException(e);
		}
	}

	private Definition parseDefinition() throws IOException, ScriptException {
		if (tokenizer.nextToken() == SYMBOL) {
			final String name = tokenizer.sval;
			if (tokenizer.nextToken() == EQUALS) {
				final Expression expression = _parseExpression();
				return (tokenizer.nextToken() == EOL) ? new Definition(name, expression) : this.<Definition> unexpected(EOL);
			} else {
				return this.<Definition> unexpected(EQUALS);
			}
		} else {
			return this.<Definition> unexpected(SYMBOL);
		}
	}

	private Expression _parseExpression() throws IOException, ScriptException {
		Expression expression = null;
		while (true) {
			switch (tokenizer.nextToken()) {
			case LEFT_PARENTHESIS:
				final Expression nestedExpression = _parseExpression();
				expression = (tokenizer.nextToken() == RIGHT_PARENTHESIS) ? ((expression == null) ? nestedExpression : new Application(engine, expression, nestedExpression)) : this.<Expression> unexpected(RIGHT_PARENTHESIS);
				break;
			case SYMBOL:
				final Reference reference = new Reference(engine, tokenizer.sval);
				expression = (expression == null) ? reference : new Application(engine, expression, reference);
				break;
			case NUMBER:
				final FunckyNumber number = new FunckyNumber(engine, tokenizer.nval);
				expression = (expression == null) ? number : new Application(engine, expression, number);
				break;
			default:
				tokenizer.pushBack();
				return (expression == null) ? this.<Expression> unexpected(LEFT_PARENTHESIS, SYMBOL, NUMBER) : expression;
			}
		}
	}

	private <T> T unexpected(final Integer... expected) throws IOException, ScriptException {
		throw new ScriptException(String.format("Unexpected %1$s, expected %2$s", getTokenName(tokenizer.ttype), or(expected)), "file", tokenizer.lineno(), 0); // TODO file, column
	}

	private String or(final Integer... tokens) throws ScriptException {
		return (tokens.length == 1) ? getTokenName(tokens[0]) : ((tokens.length == 2) ? String.format("%1$s or %2$s", getTokenName(tokens[0]), getTokenName(tokens[1])) : String.format("%1$s, %2$s", getTokenName(tokens[0]), or(Arrays.asList(tokens).subList(1, tokens.length).toArray(new Integer[0]))));
	}

	private String getTokenName(final Integer token) throws ScriptException {
		final String name = TOKEN_NAMES.get(token);
		if (name == null) {
			throw new ScriptException(String.format("Unparsable input %1$s", tokenizer.sval), "file", tokenizer.lineno(), 0); // TODO file, column
		}
		return name;
	}
}
