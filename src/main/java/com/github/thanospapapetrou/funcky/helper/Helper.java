package com.github.thanospapapetrou.funcky.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Helper {
	public static void main(final String[] arguments) {
		final String nand = "if $1 false true";
		System.out.println("nand");
		System.out.println(nand);
		System.out.println(help(nand));
	}
	
	public static Expression help(final String string) {
		return simplify(parse(tokenize(string)));
	}

	public static List<Token> tokenize(final String string) {
		for (final TokenType type : TokenType.values()) {
			final Matcher matcher = type.getPattern().matcher(string);
			if (matcher.lookingAt()) {
				final List<Token> tokens = new ArrayList<>();
				if (type != TokenType.SPACE) {
					tokens.add(new Token(type, (type.getGroup() == null) ? null : matcher.group(type.getGroup())));
				}
				if (matcher.end() != string.length()) {
					tokens.addAll(tokenize(string.substring(matcher.end())));
				}
				return tokens;
			}
		}
		throw new IllegalArgumentException(String.format("Unparsable input %1$s", string));
	}

	public static Expression parse(final List<Token> tokens) {
		return parse(new ParseState(tokens)).getExpression();
	}

	public static ParseState parse(final ParseState state) {
		if (state.getTokens().isEmpty()) {
			return state;
		}
		switch (state.getTokens().get(0).getType()) {
		case LEFT_PARENTHESIS:
			final ParseState newState = parse(new ParseState(state.getTokens().subList(1, state.getTokens().size())));
			switch (newState.getTokens().get(0).getType()) {
			case RIGHT_PARENTHESIS:
				return parse(new ParseState(newState.getTokens().subList(1, newState.getTokens().size()), (state.getExpression() == null) ? newState.getExpression() : new Application(state.getExpression(), newState.getExpression())));
			default:
				throw new IllegalStateException("Unexpected " + newState.getTokens().get(0).getType());
			}
		case RIGHT_PARENTHESIS:
			return state;
		case SYMBOL:
			final Symbol symbol = new Symbol(state.getTokens().get(0).getValue());
			return parse(new ParseState(state.getTokens().subList(1, state.getTokens().size()), (state.getExpression() == null) ? symbol : new Application(state.getExpression(), symbol)));
		case VARIABLE:
			final Variable variable = new Variable(Integer.parseInt(state.getTokens().get(0).getValue()));
			return parse(new ParseState(state.getTokens().subList(1, state.getTokens().size()), (state.getExpression() == null) ? variable : new Application(state.getExpression(), variable)));
		default:
			throw new IllegalStateException("Unexpected " + state.getTokens().get(0).getType());
		}
	}

	public static Expression simplify(final Expression expression) {
		Expression e = expression;
		while (true) {
			final Expression e2 = compose(flip(compose(expression)));
			if (e.equals(e2)) {
				return e;
			} else {
				e = e2;
			}
		}
	}

	public static Expression compose(final Expression expression) {
		// compose f g x = f (g x)
		if (expression instanceof Application) {
			final Application application1 = (Application) expression; // f (g x)
			if (application1.getArgument() instanceof Application) {
				final Application application2 = (Application) application1.getArgument(); // g x
				if (application2.getArgument() instanceof Variable) {
					return new Application(new Application(new Application(new Symbol("compose"), application1.getFunction()), application2.getFunction()), application2.getArgument());
				}
			}
			return new Application(simplify(application1.getFunction()), simplify(application1.getArgument()));
		}
		return expression;
	}

	public static Expression flip(final Expression expression) {
		// flip f y x = f x y
		if (expression instanceof Application) {
			final Application application1 = (Application) expression; // f x y
			if (application1.getFunction() instanceof Application) {
				final Application application2 = (Application) application1.getFunction(); // f x
				if (application2.getArgument() instanceof Variable) {
					final Variable variable2 = (Variable) application2.getArgument(); // x
					if (application1.getArgument() instanceof Variable) {
						final Variable variable1 = (Variable) application1.getArgument(); // y
						if (variable2.getValue() > variable1.getValue()) {
							return new Application(new Application(new Application(new Symbol("flip"), application2.getFunction()), application1.getArgument()), application2.getArgument());
						}
						return new Application(simplify(application1.getFunction()), simplify(application1.getArgument())); 
					}
					return new Application(new Application(new Application(new Symbol("flip"), application2.getFunction()), application1.getArgument()), application2.getArgument());
				}
			}
			return new Application(simplify(application1.getFunction()), simplify(application1.getArgument()));
		}
		return expression;
	}

	// # Identity function.
	// # function $type $type
	// # identity argument
	// # argument the value to return
	// # return the given argument
	// # identity = ...

	// # Flip the arguments of a function.
	// # function (function $type1 (function $type2 $type3)) (function $type2 (function $type1 $type3))
	// # flip function argument1 argument2
	// # function the function whose arguments to flip
	// # argument1 the second argument to pass to the function
	// # argument2 the first argument to pass to the function
	// # return a new function corresponding to applying the given function to the given arguments in reverse order (function argument2 argument1)
	// # flip = ...
	//
	// # Duplicate the argument of a function.
	// # function (function $type1 (function $type1 $type2)) $type1 $type2
	// # duplicate function argument
	// # function the function whose argument to duplicate
	// # argument the argument to duplicate
	// # return a new function corresponding to applying the given function to the given argument and reapplying the resulting function to the same argument (function argument argument)
	// # duplicate = ...

}
