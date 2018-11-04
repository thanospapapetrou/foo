package com.github.thanospapapetrou.funcky.script.expression.literal.type;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.library.Core;
import com.github.thanospapapetrou.funcky.script.expression.literal.Literal;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing a Funcky type.
 * 
 * @author thanos
 */
public abstract class Type extends Literal {
    protected Type(final FunckyEngine engine, final URI script, final int line, final int column) {
        super(engine, script, line, column);
    }

    protected Type() {
        super();
    }

    /**
     * Bind any type variables occurring in this type by replacing each one of them with another
     * type.
     * 
     * @param bindings
     *            a map containing each type variable of this type along with the type to bind it to
     * @return the type that occurs after binding any type variable occurring in this type
     */
    public abstract Type bind(final Map<TypeVariable, Type> bindings);

    /**
     * Free any type variables occurring in this type by replacing each one of them with a new
     * unbound type variable.
     * 
     * @return the type that occurs after freeing any type variable occurring in this type
     */
    public Type free() {
        return free(new HashMap<TypeVariable, TypeVariable>());
    }

    /**
     * Infer the generic bindings that derive when binding this type to another type.
     * 
     * @param type
     *            the type to bind this type to
     * @return a map containing each type variable of this type along with the type it should be
     *         bound with, or <code>null</code> if this type can not be bound to the given type
     */
    public abstract Map<TypeVariable, Type> infer(final Type type);

    @Override
    public SimpleType getType() {
        return Core.TYPE;
    }

    /**
     * Free any type variables occurring in this type by replacing each one of them with a new
     * unbound type variable.
     * 
     * @param freed
     *            a map containing already freed type variables along with the unbound variable each
     *            one of them has been replaced with
     * @return the type that occurs after freeing any type variable occurring in this type
     */
    protected abstract Type free(final Map<TypeVariable, TypeVariable> freed);
}
