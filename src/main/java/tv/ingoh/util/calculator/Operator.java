package tv.ingoh.util.calculator;

import tv.ingoh.util.calculator.exceptions.OperationExecutionException;
import tv.ingoh.util.calculator.exceptions.ValueConversionException;
import tv.ingoh.util.calculator.exceptions.ValueSetException;
import tv.ingoh.util.calculator.exceptions.VariableSetException;

public class Operator implements Expressable {

    public enum Operators {

        FACT2("!!", -1, OperatorType.UNARYLEFT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    int vLeft = expression[0].toInt(0);
                    double val = 1;
                    for (int i = 2; i <= vLeft; i++) {
                        val *= i;
                    }
                    return new Term(val);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation !! on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        INCR("++", -1, OperatorType.UNARYLEFT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    int vLeft = expression[0].toInt(0);
                    return new Term(++vLeft);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation ++ on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        DECR("--", -1, OperatorType.UNARYLEFT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    int vLeft = expression[0].toInt(0);
                    return new Term(--vLeft);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation -- on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        NOT("!", 0, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    boolean vRight = expression[0].toBoolean(0);
                    return new ExpressableBoolean(!vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation ! on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        ABS("abs", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.abs(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation abs on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        SQRT("sqrt", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.sqrt(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation sqrt on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        SIN("sin", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.sin(Math.toRadians(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation sin on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        COS("cos", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.cos(Math.toRadians(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation cos on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        TAN("tan", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.tan(Math.toRadians(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation tan on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        ASIN("asin", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.toDegrees(Math.asin(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation asin on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        ACOS("acos", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.toDegrees(Math.acos(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation acos on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        ATAN("atan", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.toDegrees(Math.atan(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation atan on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        SINH("sinh", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.sinh(Math.toRadians(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation sinh on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        COSH("cosh", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.cosh(Math.toRadians(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation cosh on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        TANH("tanh", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.tanh(Math.toRadians(vRight)));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation tanh on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        LOG("log", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.log10(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation log on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        LN("ln", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.log(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation ln on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        FACT("fact", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    int vRight = expression[0].toInt(0);
                    double val = 1;
                    for (int i = 2; i <= vRight; i++) {
                        val *= i;
                    }
                    return new Term(val);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation fact on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        ROUND("round", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.round(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation round on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        CEIL("ceil", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.ceil(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation ceil on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        FLOOR("floor", 1, OperatorType.UNARYRIGHT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vRight = expression[0].toDouble(0);
                    return new Term(Math.floor(vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation floor on " + expression[0].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        POW("^", 2, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new Term(Math.pow(vLeft, vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation ^ on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        MUL("*", 3, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new Term(vLeft * vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation * on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        DIV("/", 3, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new Term(vLeft / vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation / on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        MOD("%", 3, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new Term(vLeft % vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation % on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        ADD("+", 4, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new Term(vLeft + vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation + on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        SUB("-", 4, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new Term(vLeft - vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation - on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        LT("<", 5, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new ExpressableBoolean(vLeft < vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation < on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        LTE("<=", 5, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new ExpressableBoolean(vLeft <= vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation <= on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        GT(">", 5, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new ExpressableBoolean(vLeft > vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation > on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        GTE(">=", 5, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new ExpressableBoolean(vLeft >= vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation >= on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        EQ("==", 6, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new ExpressableBoolean(vLeft == vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation == on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        NEQ("!=", 6, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    double vLeft = expression[0].toDouble(0);
                    double vRight = expression[1].toDouble(0);
                    return new ExpressableBoolean(vLeft != vRight);
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation != on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        AND("&&", 7, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    boolean vLeft = expression[0].toBoolean(0);
                    boolean vRight = expression[1].toBoolean(0);
                    return new ExpressableBoolean(Boolean.logicalAnd(vLeft,vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation && on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        OR("||", 8, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    boolean vLeft = expression[0].toBoolean(0);
                    boolean vRight = expression[1].toBoolean(0);
                    return new ExpressableBoolean(Boolean.logicalOr(vLeft,vRight));
                } catch (ValueConversionException e) {
                    throw new OperationExecutionException("Failed to execute operation || on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        }),
        CONCAT("&", 9, OperatorType.BINARY, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                String vLeft = expression[0].toPlainString(0);
                String vRight = expression[1].toPlainString(0);
                return new ExpressableString(vLeft + vRight);
            }
        }),
        ASSIGN("=", 10, OperatorType.ASSIGNMENT, new OperationExecution(){
            @Override
            public Expressable doOperation(Expressable... expression) throws OperationExecutionException {
                try {
                    Variable vLeft = expression[0].toVariable(0);
                    Term vRight = expression[1].toTerm(0);
                    vRight.setNumber(vRight.getNumber(0) / expression[0].getNumber(0));
                    expression[0].setNumber(1);
                    return Calculator.getVarList().setVariable(vLeft.name, vRight);
                } catch (ValueConversionException | ValueSetException | VariableSetException e) {
                    throw new OperationExecutionException("Failed to execute operation = on " + expression[0].toPlainString(0) + " and " + expression[1].toPlainString(0) + ", idiot. Cause: " + e.getMessage());
                }
            }
        });
        // Expression has priority 11
        // Term has priority 12
        // String and Boolean have priority 13

        final String representation;
        final int priority;
        final OperatorType type;
        private final OperationExecution oe;
        Variables varList;
        
        Operators(String representation, int priority, OperatorType type, OperationExecution oe) {
            this.representation = representation;
            this.priority = priority;
            this.type = type;
            this.oe = oe;
        }

        public Expressable doOperation(Expressable... exp) throws OperationExecutionException {
            return oe.doOperation(exp);
        }

        public void setVarList(Variables varList) {
            this.varList = varList;
        }
    }

    public enum OperatorType {
        UNARYLEFT, UNARYRIGHT, BINARY, ASSIGNMENT;
    }

    public final Operators operator;

    public Operator(Operator.Operators operator) {
        this.operator = operator;
    }

    public static boolean isOperatorOrBoolean(String s) {
        for (Operators o : Operator.Operators.values()) {
            if (o.representation.equals(s.toLowerCase())) return true;
        }
        if (s.toLowerCase().equals("true") || s.toLowerCase().equals("false")) {
            return true;
        }
        return false;
    }

    public static int canBeOperatorOrBooleanCount(String s) {
        int count = 0;
        for (Operators o :Operator.Operators.values()) {
            if (o.representation.startsWith(s.toLowerCase())) count++;
        }
        if ("true".startsWith(s.toLowerCase()) || "false".startsWith(s.toLowerCase())) {
            count++;
        }
        return count;
    }

    public static boolean canBeOperatorOrBoolean(String s) {
        for (Operators o : Operator.Operators.values()) {
            if (o.representation.startsWith(s.toLowerCase())) return true;
        }
        if ("true".startsWith(s.toLowerCase()) || "false".startsWith(s.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static boolean canBeOperatorOrBoolean(char c) {
        for (Operators o : Operator.Operators.values()) {
            if (o.representation.charAt(0) == Character.toLowerCase(c)) return true;
        }
        if (Character.toLowerCase(c) == 't' || Character.toLowerCase(c) == 'f') {
            return true;
        }
        return false;
    }

    public static Expressable getOperatorOrBooleanFromString(String s) throws IllegalArgumentException {
        for (Operators o : Operator.Operators.values()) {
            if (o.representation.equals(s.toLowerCase())) return new Operator(o);
        }
        if (s.toLowerCase().equals("true")) return new ExpressableBoolean(true);
        else if (s.toLowerCase().equals("false")) return new ExpressableBoolean(false);
        throw new IllegalArgumentException(s + " is not a valid operator.");
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public String toString() {
        return operator.representation;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Operator(operator);
    }
    
    @Override
    public int getPriority() {
        return operator.priority;
    }

    @Override
    public boolean getToLeft() {
        // Left unary operators are handled from right to left
        return (operator.type == OperatorType.UNARYRIGHT);
    }

    @Override
    public double toDouble(double depth) throws ValueConversionException {
        throw new ValueConversionException("Cannot convert " + operator.representation + " to a double, idiot.");
    }

    @Override
    public int toInt(double depth) throws ValueConversionException {
        throw new ValueConversionException("Cannot convert " + operator.representation + " to an integer, idiot.");
    }

    @Override
    public boolean toBoolean(double depth) throws ValueConversionException {
        throw new ValueConversionException("Cannot convert " + operator.representation + " to a boolean, idiot.");
    }

    @Override
    public Variable toVariable(double depth) throws ValueConversionException {
        throw new ValueConversionException("Cannot convert " + operator.representation + " to a variable, idiot.");
    }

    @Override
    public Term toTerm(double depth) throws ValueConversionException {
        throw new ValueConversionException("Cannot convert " + operator.representation + " to a term, idiot.");
    }

    @Override
    public String toPlainString(double depth) {
        return operator.representation;
    }

    @Override
    public double getNumber(double depth) throws ValueConversionException {
        throw new ValueConversionException("Cannot convert " + operator.representation + " to a number, idiot.");
    }

    @Override
    public void setNumber(double number) throws ValueSetException {
        throw new ValueSetException("Cannot set " + operator.representation + " to " + number + ", idiot.");
    }

    @Override
    public boolean containsVariable(String name) {
        return false;
    }

}
