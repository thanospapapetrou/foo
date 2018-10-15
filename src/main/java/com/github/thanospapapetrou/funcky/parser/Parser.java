package com.github.thanospapapetrou.funcky.parser;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.parser.tokenizer.ReadingException;
import com.github.thanospapapetrou.funcky.parser.tokenizer.Token;
import com.github.thanospapapetrou.funcky.parser.tokenizer.TokenType;
import com.github.thanospapapetrou.funcky.parser.tokenizer.Tokenizer;
import com.github.thanospapapetrou.funcky.parser.tokenizer.UnparsableInputRuntimeException;
import com.github.thanospapapetrou.funcky.script.Application;
import com.github.thanospapapetrou.funcky.script.Expression;
import com.github.thanospapapetrou.funcky.script.Number;
import com.github.thanospapapetrou.funcky.script.Reference;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class implementing a Funcky parser.
 * 
 * @author thanos
 */
public class Parser {
    private static final Predicate<Token> NO_COMMENT =
            ((Predicate<Token>) ((Function<TokenType, Boolean>) TokenType.COMMENT::equals)
                    .compose(Token::getType)::apply).negate();
    private static final Predicate<Token> NO_WHITESPACE =
            ((Predicate<Token>) ((Function<TokenType, Boolean>) TokenType.WHITESPACE::equals)
                    .compose(Token::getType)::apply).negate();
    private static final String NULL_ENGINE = "Engine must not be null";
    private static final String NULL_READER = "Reader must not be null";
    private static final String NULL_SCRIPT = "Script must not be null";

    private final FunckyEngine engine;
    private final PushbackIterator<Token> tokens;

    /**
     * Construct a new parser.
     * 
     * @param engine
     *            the Funcky engine that this parser belongs to
     * @param reader
     *            the reader from which to read the script to parse
     * @param script
     *            the URI of the script to parse
     * @throws ParsingErrorException
     *             if any I/O errors occur while parsing
     */
    public Parser(final FunckyEngine engine, final Reader reader, final URI script)
            throws ParsingErrorException {
        Objects.requireNonNull(engine, NULL_ENGINE);
        Objects.requireNonNull(reader, NULL_READER);
        Objects.requireNonNull(script, NULL_SCRIPT);
        this.engine = engine;
        try {
            tokens = new PushbackIterator<>(new Tokenizer(script).tokenize(reader)
                    .filter(NO_COMMENT).filter(NO_WHITESPACE).iterator());
        } catch (final IOException e) {
            throw new ParsingErrorException(script, e);
        }
    }

    void parseScript() {
        // TODO
    }

    /**
     * Parse an expression.
     * 
     * @return the expression parsed
     * @throws ParseException
     *             if any errors occur while parsing
     */
    public Expression parseExpression() throws ParseException {
        // TODO h = f produces invalid error (expected right parenthesis)
        try {
            final Expression expression = _parseExpression();
            parse(TokenType.EOL);
            parse(TokenType.EOF);
            return expression;
        } catch (final ReadingException e) {
            throw new ParsingErrorException(e.getScript(), e.getCause());
        } catch (final UnparsableInputRuntimeException e) {
            throw new UnparsableInputException(e.getScript(), e);
        }
    }

    private Expression _parseExpression() throws UnexpectedTokenException {
        Expression expression = parseSimpleExpression();
        while (true) {
            final Token token = parse(TokenType.EOL, TokenType.IDENTIFIER,
                    TokenType.LEFT_PARENTHESIS, TokenType.NUMBER, TokenType.RIGHT_PARENTHESIS);
            switch (token.getType()) {
            case EOL:
            case RIGHT_PARENTHESIS:
                tokens.pushback(token);
                return expression;
            case IDENTIFIER:
            case LEFT_PARENTHESIS:
            case NUMBER:
                tokens.pushback(token);
                expression = new Application(engine, expression, parseSimpleExpression());
                break;
            default:
                throw new ParserIllegalStateException(token);
            }
        }
    }

    private Expression parseSimpleExpression() throws UnexpectedTokenException {
        final Token token =
                parse(TokenType.IDENTIFIER, TokenType.LEFT_PARENTHESIS, TokenType.NUMBER);
        switch (token.getType()) {
        case IDENTIFIER:
            return new Reference(engine, token.getValue());
        case LEFT_PARENTHESIS:
            final Expression expression = _parseExpression();
            parse(TokenType.RIGHT_PARENTHESIS);
            return expression;
        case NUMBER:
            return new Number(engine, Double.valueOf(token.getValue()));
        default:
            throw new ParserIllegalStateException(token);
        }
    }

    private Token parse(final TokenType... expected) throws UnexpectedTokenException {
        final Token token = tokens.next();
        return Arrays.stream(expected).anyMatch(token.getType()::equals) ? token
                : unexpected(token, expected);
    }

    private Token unexpected(final Token token, final TokenType... expected)
            throws UnexpectedTokenException {
        throw new UnexpectedTokenException(token, expected);
    }
}
// TODO
// * <script> ::= <definition> <new line> <script>
// * | ε
// * <definition> ::= <symbol> <whitespace> "=" <whitespace> <expression>
// * <expression> ::= <simpleExpression>
// * | <application>
// * <simpleExpression> ::= <nestedExpression>
// * | <reference>
// * | <literal>
// * <application> ::= <expression> <whitespace> <simpleExpression>
// * <nestedExpression> ::= "(" <expression> ")"
// * <reference> ::= <identifier>
// * <literal> ::= <number>
// * <new line> ::= "\n"
// * | "\r"
// * | "\r\n"
// * <identifier> ::= [\w&&\D]\w*
// * <number> ::= \-?\d(\.\d*)?
// * | \-?.\d*
