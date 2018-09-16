package com.github.thanospapapetrou.funcky;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

class FunckyEngine extends AbstractScriptEngine
        implements Compilable, Invocable, ScriptEngine {
    private final FunckyEngineFactory factory;

    FunckyEngine(final FunckyEngineFactory factory) {
        this.factory = factory;
    }

    @Override
    public CompiledScript compile(final Reader script) throws ScriptException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompiledScript compile(final String expression) throws ScriptException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bindings createBindings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object eval(final Reader script, final ScriptContext context) throws ScriptException {
        return compile(script).eval(context);
    }

    @Override
    public Object eval(final String expression, final ScriptContext context)
            throws ScriptException {
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
            throws ScriptException, NoSuchMethodException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object invokeMethod(final Object object, final String method, final Object... arguments)
            throws ScriptException, NoSuchMethodException {
        // TODO Auto-generated method stub
        return null;
    }
}
