//package com.github.thanospapapetrou.funcky.script.expression.literal;
//
//import com.github.thanospapapetrou.funcky.FunckyEngine;
//import com.github.thanospapapetrou.funcky.script.expression.literal.type.Type;
//
//public class Functor extends Function {
//
//    protected Functor(FunckyEngine engine) {
//        super(engine);
//        // TODO Auto-generated constructor stub
//    }
//
//    @Override
//    public Literal apply(final Expression argument) {
//        super.apply(argument);
//        final Functor that = this;
//        final Type newType =
//                getType().getRange().bind(getType().getDomain().infer(argument.getType().free()));
//        return (arguments > 1)
//                ? new Functor(engine, script, toString(), (FunctionType) newType, arguments - 1) { // TODO
//                                                                                                   // no
//                                                                                                   // casting
//                    @Override
//                    public Expression toExpression() {
//                        return engine.getApplication(that, argument);
//                    }
//
//                    @Override
//                    public Literal apply(final Expression... arguments) throws ScriptException {
//                        super.apply(arguments);
//                        final Expression[] newArguments = new Expression[arguments.length + 1];
//                        newArguments[0] = argument;
//                        System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
//                        return that.apply(newArguments);
//                    }
//                } : apply(new Expression[] {argument});
//    }
//
//    @Override
//    public Literal apply(final Expression... arguments) throws ScriptException {
//        if (Objects.requireNonNull(arguments, NULL_ARGUMENTS).length != this.arguments) {
//            throw new IllegalArgumentException(String.format(DIFFERENT_ARGUMENTS, this.arguments));
//        }
//        for (final Expression argument : arguments) {
//            Objects.requireNonNull(argument, NULL_ARGUMENT);
//        }
//        return null;
//    }
//}
