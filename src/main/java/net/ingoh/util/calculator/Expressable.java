package net.ingoh.util.calculator;

import net.ingoh.util.calculator.exceptions.ValueConversionException;
import net.ingoh.util.calculator.exceptions.ValueSetException;

public interface Expressable extends Cloneable {

    boolean isDefined();
    Object clone() throws CloneNotSupportedException;
    int getPriority();
    boolean getToLeft();
    double toDouble(double depth) throws ValueConversionException;
    int toInt(double depth) throws ValueConversionException;
    boolean toBoolean(double depth) throws ValueConversionException;
    Variable toVariable(double depth) throws ValueConversionException;
    Term toTerm(double depth) throws ValueConversionException;
    String toPlainString(double depth);
    double getNumber(double depth) throws ValueConversionException;
    void setNumber(double number) throws ValueSetException;
    boolean containsVariable(String name);

}
