package net.ingoh.util.calculator;

import net.ingoh.util.calculator.exceptions.ParseException;
import org.bukkit.ChatColor;

import net.ingoh.util.calculator.Operator.OperatorType;
import net.ingoh.util.calculator.Operator.Operators;

public class Calculator {

    static Variables varList = new Variables();

    public static Expression parse(String str) throws ParseException {
        Term curTerm = new Term();
        Expression expression = new Expression();
        boolean expectingMinus = true;
        String curVar = "";
        boolean parseString = false;
        boolean escape = false;
        String curStr = "";
        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);
            if (current == '"' && !escape) {
                if (parseString) {
                    curTerm.setVariable(curVar);
                    expression.append(curTerm);
                    curTerm = new Term();
                    curVar = "";
                    if (!curStr.equals("")) expression.expression.add(new ExpressableString(curStr));
                } else {
                    curStr = "";
                }
                parseString = !parseString;
            } else if (parseString) {
                if (current == '\\' && !escape) {
                    escape = true;
                } else {
                    curStr += current;
                }
            } else if (!parseString && current != ' ') {
                if (Character.isDigit(current) || (expectingMinus && current == '-') || current == '.') {
                    curTerm.appendNumeric(current);
                    expectingMinus = false;
                } else if (Operator.canBeOperatorOrBoolean(current)) {
                    String check = Character.toString(current);
                    int j = i;
                    String prev = "";
                    boolean wasOperator = false;
                    boolean added = false;
                    while (j < str.length() - 1) {
                        int ct = Operator.canBeOperatorOrBooleanCount(check);
                        if (ct > 1) {
                            j++;
                        } else if (ct == 1) {
                            if (Operator.isOperatorOrBoolean(check)) {
                                if (curVar != "") curTerm.setVariable(curVar);
                                Expressable expr = Operator.getOperatorOrBooleanFromString(check);
                                if (expr instanceof Operator) {
                                    appendOperatorAndTerm(((Operator) expr).operator, curTerm, expression);
                                    expectingMinus = (((Operator) expr).operator.type != OperatorType.UNARYLEFT);
                                } else {
                                    expression.append(curTerm);
                                    expression.append(expr);
                                    expectingMinus = false;
                                }
                                curTerm = new Term();
                                curVar = "";
                                i = j;
                                added = true;
                                break;
                            } else {
                                j++;
                            }
                        } else {
                            if (wasOperator) {
                                if (curVar != "") curTerm.setVariable(curVar);
                                Expressable expr = Operator.getOperatorOrBooleanFromString(prev);
                                if (expr instanceof Operator) {
                                    appendOperatorAndTerm(((Operator) expr).operator, curTerm, expression);
                                    expectingMinus = (((Operator) expr).operator.type != OperatorType.UNARYLEFT);
                                } else {
                                    expression.append(curTerm);
                                    expression.append(expr);
                                    expectingMinus = false;
                                }
                                curTerm = new Term();
                                curVar = "";
                                i = j-1;
                                added = true;
                                break;
                            } else {
                                curVar += current;
                                added = true;
                            }
                            break;
                        }
                        if (Operator.isOperatorOrBoolean(check)) {
                            wasOperator = true;
                        }
                        prev = check;
                        check += str.charAt(j);
                    }
                    if (!added) {
                        if (Operator.isOperatorOrBoolean(check)) {
                            if (curVar != "") curTerm.setVariable(curVar);
                            Expressable expr = Operator.getOperatorOrBooleanFromString(check);
                            if (expr instanceof Operator) {
                                appendOperatorAndTerm(((Operator) expr).operator, curTerm, expression);
                                expectingMinus = (((Operator) expr).operator.type != OperatorType.UNARYLEFT);
                            } else {
                                expression.append(curTerm);
                                expression.append(expr);
                                expectingMinus = false;
                            }
                            curTerm = new Term();
                            curVar = "";
                            i = j;
                        } else if (Operator.isOperatorOrBoolean(prev)) {
                            if (curVar != "") curTerm.setVariable(curVar);
                            Expressable expr = Operator.getOperatorOrBooleanFromString(prev);
                            if (expr instanceof Operator) {
                                appendOperatorAndTerm(((Operator) expr).operator, curTerm, expression);
                                expectingMinus = (((Operator) expr).operator.type != OperatorType.UNARYLEFT);
                            } else {
                                expression.append(curTerm);
                                expression.append(expr);
                                expectingMinus = false;
                            }
                            curTerm = new Term();
                            curVar = "";
                            i = j - 1;
                        } else {
                            curVar += current;
                        }
                    }
                } else if (current == '(') {
                    if (curVar != "") curTerm.setVariable(curVar);
                    expression.append(curTerm);
                    curTerm = new Term();
                    curVar = "";
                    expression.in();
                } else if (current == ')') {
                    if (curVar != "") curTerm.setVariable(curVar);
                    expression.append(curTerm);
                    curTerm = new Term();
                    curVar = "";
                    expression.out();
                } else {
                    curVar += current;
                }
            }
        }

        if (parseString) {
            expression.expression.add(new ExpressableString(curStr));
        }

        curTerm.setVariable(curVar);
        expression.append(curTerm);

        while (expression.parent != null) {
            expression.out();
        }

        return expression;

    }

    private static void appendOperatorAndTerm(Operators operator, Term term, Expression expression) {
        expression.append(term);
        expression.append(new Operator(operator));
    }

    public static String[] calculate(String string) {
        String[] evals = string.split(";");
        String returns = "";
        for (String s : evals) {
            try {
                Expression exp = parse(s);
                Expression res;
                res = exp.evaluate();
                varList.setAns(res);
                returns = res.toString();
            } catch (Exception e /* Catch all calculation errors */) {
                int element = 0;
                while (element < e.getStackTrace().length-1 && !e.getStackTrace()[element].getClassName().contains("tv.ingoh")) {
                    element++;
                }
                int start = element;
                while (element < e.getStackTrace().length-1 && e.getStackTrace()[element].getClassName().contains("tv.ingoh")) {
                    element++;
                }
                int end = element;
                String[] r = new String[end-start+2];
                r[0] = ChatColor.DARK_RED + "ERROR " + e.getMessage();
                int a = 1;
                for (int i = start; i <= end; i++) {
                    r[a] = "at " + e.getStackTrace()[i].getClassName() + "." + e.getStackTrace()[i].getMethodName() + "():" + e.getStackTrace()[i].getLineNumber();
                    a++;
                }
                return r;
            }
        }
        String[] r = new String[1];
        r[0] = returns;
        return r;
    }

    public static Variables getVarList(){
        return varList;
    }

}
