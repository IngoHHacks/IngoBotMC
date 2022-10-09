package net.ingoh.util.calculator;

import net.ingoh.util.calculator.exceptions.ValueSetException;
import net.ingoh.util.calculator.exceptions.ValueConversionException;

public class ExpressableString implements Expressable {

    String str;

    public ExpressableString(String str) {
        this.str = str;
    }

    @Override
    public boolean isDefined() {
        return (str != "");
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
        return Double.parseDouble(str);
    }

    @Override
    public int toInt(double depth) throws ValueConversionException {
        return (int) Double.parseDouble(str);
    }

    @Override
    public boolean toBoolean(double depth) throws ValueConversionException {
        return ExpressableBoolean.parseBoolean(str);
    }

    @Override
    public Variable toVariable(double depth) throws ValueConversionException{
        return new Variable(str);
    }

    @Override
    public Term toTerm(double depth) throws ValueConversionException {
        return new Term(new Variable(str));
    }

    
    @Override
    public String toPlainString(double depth) {
        return str;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExpressableString s = new ExpressableString(str);
        return s;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public double getNumber(double depth) throws ValueConversionException {
        if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
        return toDouble(depth + 1);
    }

    @Override
    public void setNumber(double number) throws ValueSetException {
        str = Double.toString(number);
    }

    @Override
    public boolean containsVariable(String name) {
        return false;
    }
    
}
