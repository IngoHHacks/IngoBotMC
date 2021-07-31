package tv.ingoh.util.calculator;

import java.util.LinkedHashMap;

import tv.ingoh.util.calculator.exceptions.VariableSetException;

public class Variables {

    LinkedHashMap<String,Expressable> varList;

    public Variables() {
        varList = new LinkedHashMap<>();
        varList.put("ans", new Term(0));
    }

    public Expressable getVariable(String name) {
        return varList.get(name);
    }

    public Expressable setVariable(String name, Expressable value) throws VariableSetException {
        if (name.equals("ans")) throw new VariableSetException("Failed to assign " + value + " to " + name + ", idiot. Cause: [ans] cannot be modified during the evaluation, idiot.");
        if (!value.containsVariable(name)) {
            varList.put(name, value);
        } else {
            throw new VariableSetException("Failed to assign " + value + " to " + name + ", idiot. Cause: a variable cannot be assigned to itself, idiot.");
        }
        Term t = new Term(); 
        t.setVariable(name);
        if (varList.size() > 10000) varList.remove(varList.entrySet().stream().findFirst().get().getKey());
        return t;
    }

    
    public Expressable setAns(Expressable value) throws VariableSetException {
        if (!value.containsVariable("ans")) {
            varList.put("ans", value);
        } else {
            return null;
        }
        Term t = new Term(); 
        t.setVariable("ans");
        if (varList.size() > 10000) varList.remove(varList.entrySet().stream().findFirst().get().getKey());
        return t;
    }
    
}
