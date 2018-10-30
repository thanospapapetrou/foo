package com.github.thanospapapetrou.funcky.script.expression.literal;

import com.github.thanospapapetrou.funcky.FunckyEngine;
import com.github.thanospapapetrou.funcky.library.Numbers;
import com.github.thanospapapetrou.funcky.script.expression.Application;
import com.github.thanospapapetrou.funcky.script.expression.Reference;
import com.github.thanospapapetrou.funcky.script.expression.literal.type.SimpleType;

import java.net.URI;

/**
 * Class representing a Funcky number.
 * 
 * @author thanos
 */
public class Number extends Literal {
    private static final String INFINITY = new Reference("infinity").toString();
    private static final String MINUS_INFINITY =
            new Application(new Reference("minus"), new Reference("infinity")).toString();
    private static final String NAN = new Reference("nan").toString();

    private final double value;

    /**
     * Construct a new number.
     * 
     * @param engine
     *            the engine that compiled this number
     * @param script
     *            the URI of the script in which this number was encountered
     * @param line
     *            the line of the script at which this number was encountered (starting from
     *            <code>1</code>)
     * @param column
     *            the column of the script at which this number was encountered (starting from
     *            <code>1</code>)
     * @param value
     *            the value of this number
     */
    public Number(final FunckyEngine engine, final URI script, final int line, final int column,
            final double value) {
        super(engine, script, line, column);
        this.value = value;
    }

    /**
     * Construct a new number.
     * 
     * @param value
     *            the value of this number
     */
    public Number(final double value) {
        super();
        this.value = value;
    }

    /**
     * Get value.
     * 
     * @return the value of this number
     */
    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Number) {
            final Number number = (Number) object;
            return Double.isNaN(value) ? Double.isNaN(number.value) : (value == number.value);
        }
        return false;
    }

    @Override
    public SimpleType getType() {
        return Numbers.NUMBER;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(Double.isNaN(value) ? Double.NaN : value);
    }

    @Override
    public String toString() {
        return (value == Double.POSITIVE_INFINITY) ? INFINITY : ((value == Double.NEGATIVE_INFINITY)
                ? MINUS_INFINITY : (Double.isNaN(value) ? NAN : Double.toString(value)));
    }
}
