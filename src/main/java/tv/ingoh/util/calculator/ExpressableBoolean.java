package tv.ingoh.util.calculator;

import tv.ingoh.util.calculator.exceptions.ValueConversionException;
import tv.ingoh.util.calculator.exceptions.ValueSetException;

public class ExpressableBoolean implements Expressable {
    public boolean value;
    public ExpressableBoolean(boolean value) {
        this.value = value;
    }

    public ExpressableBoolean(String value) {
        this.value = parseBoolean(value);
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public int getPriority() {
        return 13;
    }

    @Override
    public boolean getToLeft() {
        return false;
    }

    @Override
    public double toDouble(double depth) throws ValueConversionException {
        return value ? 1 : 0;
    }

    @Override
    public int toInt(double depth) throws ValueConversionException {
        return value ? 1 : 0;
    }

    @Override
    public boolean toBoolean(double depth) throws ValueConversionException {
        return value;
    }

    @Override
    public Variable toVariable(double depth) throws ValueConversionException {
        return new Variable(Boolean.toString(value));
    }

    @Override
    public Term toTerm(double depth) throws ValueConversionException {
        return new Term(new Variable(Boolean.toString(value)));
    }

    @Override
    public String toPlainString(double depth) {
        return Boolean.toString(value);
    }

    @Override
    public double getNumber(double depth) throws ValueConversionException {
        return value ? 1 : 0;
    }

    @Override
    public void setNumber(double number) throws ValueSetException {
        value = parseBoolean(Double.toString(number));
    }

    @Override
    public boolean containsVariable(String name) {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExpressableBoolean b = new ExpressableBoolean(value);
        return b;
    }

    public static boolean parseBoolean(String s) {
        if (s.toLowerCase().contains("true") || s.contains("1")) {
            return true;
        } else return false;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
