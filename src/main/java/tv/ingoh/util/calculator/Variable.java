package tv.ingoh.util.calculator;

import tv.ingoh.util.calculator.exceptions.ValueConversionException;

public class Variable {

    String name;
    public Variable (String name) {
        this.name = name;
    }

    public Variable () {
        this.name = "";
    }

    @Override
    public String toString() {
        Expressable t;
        if ((t=getValue(Calculator.getVarList())) == null) return name;
        else {
            if (t instanceof Evaluateable)
                try {
                    t = ((Evaluateable) t).evaluate();
                } catch (Exception e) {
                    // Leave t as is.
                }
            return "{" + name + "=" + t.toPlainString(0) + "}";
        }
    }
    public String toPlainString() throws ValueConversionException {
        return name;
    }

    @Override
    protected Variable clone() throws CloneNotSupportedException {
        Variable newVar = new Variable(name);
        return newVar;
    }

    public void append(char c) {
        name += c;
    }

    public Expressable getValue(Variables varList) {
        return varList.getVariable(name);
    }

}
