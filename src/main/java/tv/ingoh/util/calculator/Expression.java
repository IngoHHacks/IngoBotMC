package tv.ingoh.util.calculator;

import java.util.ArrayList;

import tv.ingoh.util.calculator.Operator.Operators;
import tv.ingoh.util.calculator.exceptions.EvaluateException;
import tv.ingoh.util.calculator.exceptions.ImpossibleOperationException;
import tv.ingoh.util.calculator.exceptions.OperationExecutionException;
import tv.ingoh.util.calculator.exceptions.ValueConversionException;
import tv.ingoh.util.calculator.exceptions.ValueSetException;

public class Expression implements Expressable, Evaluateable {

    ArrayList<Expressable> expression;
    Expression parent;

    public Expression() {
        parent = null;
        expression = new ArrayList<>();
    }

    public Expression(Expression exp) {
        expression = exp.expression;
        parent = exp.parent;
    }

    public Expression(Expression exp, boolean copy) throws CloneNotSupportedException {
        if (copy) {
            expression = new ArrayList<>();
            for (Expressable ex : exp.expression) {
                expression.add((Expressable) ex.clone());
            }
            parent = exp.parent;
        } else {
            expression = exp.expression;
            parent = exp.parent;
        }
    }

    public Expression(ArrayList<Expressable> exps) {
        expression = exps;
        parent = null;
    }

    public Expression(ArrayList<Expressable> exps, Expression parent) {
        expression = exps;
        this.parent = parent;
    }

    public Expression(String str) {
        expression = new ArrayList<>();
        expression.add(new ExpressableString(str));
        parent = null;
    }

    public void append(Expressable exp) {
        if (exp.isDefined()) expression.add(exp);
    }

    @Override
    public String toString() {
        if (expression.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for (Expressable expressable : expression) {
                sb.append(expressable.toString() + " ");
            }
            return sb.toString().trim();
        }
        else if (expression.size() > 0) return expression.get(0).toString();
        else return null;
    }

    public void in() {
        parent = new Expression(this);
        expression = new ArrayList<>();
    }

    public void out() {
        Expression toAdd = new Expression(expression);
        expression = parent.expression;
        parent = parent.parent;
        toAdd.parent = this;
        append(toAdd);
    }

    @Override
    public boolean isDefined() {
        return expression.size() > 0;
    }

    public Expression evaluate() throws EvaluateException {
    
        Expression exp;
        try {
            exp = new Expression(this, true);
        } catch (CloneNotSupportedException e) {
            throw new EvaluateException("Failed to evaluate " + toPlainString(0)  + ", idiot. Cause: " + e.getMessage());
        }
        boolean done = false;
        while (!done && exp.expression.size() > 1) {
            boolean toLeft = false;
            int highestPriority = 99;
            int index = 0;
            Expressable highestPrioExpr = null;
            int i = 0;
            for (Expressable expr : exp.expression) {
                if (expr.getPriority() < highestPriority) {
                    highestPriority = expr.getPriority();
                    highestPrioExpr = expr;
                    toLeft = expr.getToLeft();
                    index = i;
                } else if (expr.getPriority() == highestPriority && toLeft) {
                    highestPrioExpr = expr;
                    index = i;
                }
                i++;
            }

            try {
            
                if (!(highestPrioExpr instanceof Operator)) done = true;
                else {
                    Operators o = ((Operator)highestPrioExpr).operator;
                    switch (o.type) {
                        case ASSIGNMENT:
                        case BINARY:
                            if (index < 1) {
                                if (index > exp.expression.size() - 2) {
                                    throw new ImpossibleOperationException("Binary operator " + o.representation + " requires terms to the left and right. " + "Can't calculate [null" + o.representation + "null], idiot.");
                                } else {
                                    throw new ImpossibleOperationException("Binary operator " + o.representation + " requires a term to the left. " + "Can't calculate [null" + o.representation + exp.expression.get(index+1) + "], idiot.");
                                }
                            } else if (index > exp.expression.size() - 2) {
                                throw new ImpossibleOperationException("Binary operator " + o.representation + " requires a term to the right. " + "Can't calculate [" + exp.expression.get(index-1) + o.representation + "null], idiot.");
                            }
                            Expressable a = exp.expression.get(index-1);
                            Expressable b = exp.expression.get(index+1);
                            exp.expression.set(index, (o.doOperation(a, b)));
                            exp.expression.remove(a);
                            exp.expression.remove(b);
                            break;
                        case UNARYLEFT:
                            if (index < 1) {
                                throw new ImpossibleOperationException("Unary operator " + o.representation + " requires a term to the left. " + "Can't calculate [null" + o.representation + "], idiot.");
                            }
                            Expressable c = exp.expression.get(index-1);
                            exp.expression.set(index, (o.doOperation(c)));
                            exp.expression.remove(c);
                            break;
                        case UNARYRIGHT:
                            if (index > exp.expression.size() - 2) {
                                throw new ImpossibleOperationException("Unary operator " + o.representation + " requires a term to the right. " + "Can't calculate [" + o.representation + "null], idiot.");
                            }
                            Expressable d = exp.expression.get(index+1);
                            exp.expression.set(index, (o.doOperation(d)));
                            exp.expression.remove(d);
                            break;
                        default:
                            throw new ImpossibleOperationException("Operator " + o.representation + " does not exist, idiot.");
                    }
                }
            } catch (ImpossibleOperationException | OperationExecutionException e) {
                throw new EvaluateException("Failed to evaluate " + toPlainString(0) + ", idiot. Cause: " + e.getMessage());
            }
        }

        return exp;
    }

    @Override
    public Expression clone() throws CloneNotSupportedException {
        Expression newExp = new Expression();
        newExp.expression = new ArrayList<>();
        for (Expressable ex : expression) {
            newExp.expression.add((Expressable) ex.clone());
        }
        if (parent != null) newExp.parent = parent;
        return newExp;
    }

    @Override
    public int getPriority() {
        return 11;
    }

    @Override
    public boolean getToLeft() {
        return false;
    }

    @Override
    public double toDouble(double depth) throws ValueConversionException {
        Expression exp;
        try {
            exp = evaluate();
        } catch (EvaluateException e) {
            throw new ValueConversionException("Cannot convert " + toPlainString(0)  + " to a double, idiot.");
        }
        if (exp.expression.size() == 1) {
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            return exp.expression.get(0).toDouble(depth + 1);
        }
        throw new ValueConversionException("Cannot convert " + toPlainString(0)  + " to a double, idiot.");
    }
    
    @Override
    public int toInt(double depth) throws ValueConversionException {
        Expression exp;
        try {
            exp = evaluate();
        } catch (EvaluateException e) {
            throw new ValueConversionException("Cannot convert " + toPlainString(0)  + " to an integer, idiot.");
        }
        if (exp.expression.size() == 1) {
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            return exp.expression.get(0).toInt(depth + 1);
        }
        throw new ValueConversionException("Cannot convert " + toPlainString(0)  + " to an integer, idiot.");
    }

    @Override
    public boolean toBoolean(double depth) throws ValueConversionException {
        return ExpressableBoolean.parseBoolean(toString());
    }

    @Override
    public Variable toVariable(double depth) throws ValueConversionException {
        Expression exp;
        try {
            exp = evaluate();
        } catch (EvaluateException e) {
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            throw new ValueConversionException("Cannot convert " + toPlainString(depth+1)  + " to a variable, idiot.");
        }
        if (exp.expression.size() == 1) {
            Expressable e = exp.expression.get(0);
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            return e.toVariable(depth + 1);
        }
        if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
        throw new ValueConversionException("Cannot convert " + toPlainString(depth+1)  + " to a variable, idiot.");
    }

    @Override
    public Term toTerm(double depth) throws ValueConversionException {
        Expression exp;
        try {
            exp = evaluate();
        } catch (EvaluateException e) {
            if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
            throw new ValueConversionException("Cannot convert " + toPlainString(depth+1)  + " to a term, idiot.");
        }
        if (exp.expression.size() == 1) {
            Expressable e = exp.expression.get(0);
            return e.toTerm(depth + 1);
        }
        if (depth > 20) throw new ValueConversionException("Infinite loop, idiot.");
        throw new ValueConversionException("Cannot convert " + toPlainString(depth+1)  + " to a term, idiot.");
    }

    
    @Override
    public String toPlainString(double depth) {
        if (expression.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for (Expressable expressable : expression) {
                sb.append(expressable.toPlainString(depth + 1) + " ");
            }
            return sb.toString().trim();
        }
        else if (expression.size() > 0) return expression.get(0).toString();
        else return null;
    }

    @Override
    public double getNumber(double depth) throws ValueConversionException {
        return toDouble(depth + 1);
    }

    @Override
    public void setNumber(double number) throws ValueSetException {
        Expression exp;
        try {
            exp = evaluate();
        } catch (EvaluateException e) {
            throw new ValueSetException("Cannot set " + toPlainString(0) + " to " + number + ", idiot.");
        }
        if (exp.expression.size() == 1) {
            exp.expression.get(0).setNumber(number);
        }
        throw new ValueSetException("Cannot set " + toPlainString(0) + " to " + number + ", idiot.");
    }

    @Override
    public boolean containsVariable(String name) {
        for (Expressable exp : expression) {
            if (exp.containsVariable(name)) return true;
        }
        return false;
    }

}
