package com.github.thanospapapetrou.funcky;

import com.github.thanospapapetrou.funcky.parser.Parser;
import com.github.thanospapapetrou.funcky.script.expression.Expression;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

import java.io.Reader;
import java.io.StringReader;
import java.net.URI;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;

public class FunckyEngine extends AbstractScriptEngine
        implements Compilable, Invocable, ScriptEngine {
    private final FunckyEngineFactory factory;

    FunckyEngine(final FunckyEngineFactory factory) {
        this.factory = factory;
    }

    @Override
    public Expression compile(final Reader script) throws FunckyException {
        // TODO return something more specific than compiled script
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Expression compile(final String expression) throws FunckyException {
        final Expression exp =
                new Parser(this, new StringReader(expression), URI.create("funcky:stdin"))
                        .parseExpression();
        System.out.println("Parsed " + exp);
        return exp;
    }

    @Override
    public Bindings createBindings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Literal eval(final Reader script, final ScriptContext context) throws FunckyException {
        // TODO change to FunckyException
        return compile(script).eval(context);
    }

    @Override
    public Literal eval(final String expression, final ScriptContext context)
            throws FunckyException {
        // TODO change to FunckyException
        return compile(expression).eval(context);
    }

    @Override
    public FunckyEngineFactory getFactory() {
        return factory;
    }

    @Override
    public <T> T getInterface(final Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getInterface(final Object object, final Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object invokeFunction(final String function, final Object... arguments)
            throws FunckyException, NoSuchMethodException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object invokeMethod(final Object object, final String method, final Object... arguments)
            throws FunckyException, NoSuchMethodException {
        // TODO Auto-generated method stub
        return null;
    }
}
