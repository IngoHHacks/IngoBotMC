package tv.ingoh.util.calculator;

import java.math.BigDecimal;
import java.math.MathContext;

import tv.ingoh.util.calculator.exceptions.ValueConversionException;
import tv.ingoh.util.calculator.exceptions.ValueSetException;

public class Term implements Expressable, Evaluateable {

    String number;
    String variable;

    public Term() {
        number = "";
        variable = "";
    }

    public Term(double val) {
        number = Double.toString(val);
        variable = "";
    }

    
    public Term(Variable variable) {
        number = "";
        this.variable = variable.name;
    }

    public void appendNumeric(char current) {
        if (variable.equals("")) number += current;
        else variable += current;
    }

    @Override
    public boolean isDefined() {
        return (!number.equals("") || !variable.equals(""));
    }

    @Override
    public String toString() {
        try {
            String number = this.number;
            if (!number.equals("")) {
                String res = number;
                if (!variable.equals("")) res = Double.toString(Double.parseDouble(number) * new Variable(variable).getValue(Calculator.getVarList()).toDouble(0));
                if (Math.abs(Double.parseDouble(number) - (int) Double.parseDouble(number)) < 1e-7) {
                    number =  Integer.toString((int) Double.parseDouble(number));
                } else {
                    try {
                        BigDecimal bd = new BigDecimal(number);
                        number = bd.round(new MathContext(15)).toString();
                    } catch (Exception e) {
                        return "NaN";
                    }
                }
                // TODO: Should be improved/combined
                if (Math.abs(Double.parseDouble(res) - (int) Double.parseDouble(res)) < 1e-7) {
                    res =  Integer.toString((int) Double.parseDouble(res));
                } else {
                    try {
                        BigDecimal bd = new BigDecimal(res);
                        res = bd.round(new MathContext(15)).toString();
                    } catch (Exception e) {
                        res = "NaN";
                    }
                }
                if (!variable.equals("")) {
                    try {
                        return "(" + number + new Variable(variable).toString() + "=[" + res + "])";
                    } catch (Exception e) {
                        return null;
                    }
                } 
                else return number;
            } else if (!variable.equals("")) {
                return new Variable(variable).toString();
            }
        } catch (Exception e) {
            return number + variable;
        }
        return "";
    }

    @Override
    public Expression evaluate() {
        Expression exp = new Expression();
        exp.append(this);
        return exp;
    }

    @Override
    public Term clone() throws CloneNotSupportedException {
        Term newTerm = new Term();
        newTerm.number = this.number;
        newTerm.variable = this.variable;
        return newTerm;
    }

    @Override
    public int getPriority() {
        return 12;
    }

    @Override
    public boolean getToLeft() {
        return false;
    }

    @Override
    public double toDouble(double depth) throws ValueConversionException {
        try {
            if (variable.equals("") && !number.equals("")) {
                return Double.parseDouble(number);
            } else if (!variable.equals("")) {
                double v = 1;
                if (!number.equals("")) v = Double.parseDouble(number);
                Expressable var = new Variable(variable).getValue(Calculator.getVarList());
                if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
                if (!variable.equals("")) v *= var.toDouble(depth + 1);
                return v;
            }
        } catch (Exception e) {
            throw new ValueConversionException("Cannot convert " + toPlainString(0) + " to a double, idiot.");
        }
        throw new ValueConversionException("Cannot convert " + toPlainString(0) + " to a double, idiot.");
    }

    @Override
    public int toInt(double depth) throws ValueConversionException {
        try {
            if (variable.equals("") && !number.equals("")) {
                return (int) Double.parseDouble(number);
            } else if (!variable.equals("")) {
                double v = 1;
                if (!number.equals("")) v = Double.parseDouble(number);
                if (!variable.equals("")) v *= new Variable(variable).getValue(Calculator.getVarList()).toDouble(0);
                return (int) v;
            }
        } catch (Exception e) {
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            throw new ValueConversionException("Cannot convert " + toPlainString(depth+1) + " to an integer, idiot.");
        }
        if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
        throw new ValueConversionException("Cannot convert " + toPlainString(depth+1) + " to an integer, idiot.");
    }

    @Override
    public boolean toBoolean(double depth) throws ValueConversionException {
        return ExpressableBoolean.parseBoolean(toString());
    }

    @Override
    public Variable toVariable(double depth) throws ValueConversionException {
        try {
            if (!variable.equals("")) return new Variable(variable);
        } catch (NullPointerException e) {
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            throw new ValueConversionException("Cannot convert " + toPlainString(depth+1) + " to a variable, idiot.");
        }
        if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
        throw new ValueConversionException("Cannot convert " + toPlainString(depth+1) + " to a variable, idiot.");
    }

    @Override
    public Term toTerm(double depth) throws ValueConversionException {
        return this;
    }

    @Override
    public String toPlainString(double depth) {
        if (isDefined()) {
            if (number != "") {
                if (Math.abs(Double.parseDouble(number) - (int) Double.parseDouble(number)) < 1e-7) {
                    number =  Integer.toString((int) Double.parseDouble(number));
                } else {
                    try {
                        BigDecimal bd = new BigDecimal(number);
                        number = bd.round(new MathContext(15)).toString();
                    } catch (Exception e) {
                        return "NaN";
                    }
                }
                if (variable != "") {
                    return number + variable;
                } else {
                    return number;
                }
            } else {
                return variable;
            }
        }
        return "";
    }

    public void setVariable(String variable) {
        if (!variable.equals("")) this.variable = variable;
    }

    @Override
    public double getNumber(double depth) throws ValueConversionException {
        if (number.equals("")) return 1.0;
        return Double.parseDouble(number);
    }

    @Override
    public void setNumber(double number) throws ValueSetException {
        this.number = Double.toString(number);
    }

    @Override
    public boolean containsVariable(String name) {
        return variable.equals(name);
    }
}
