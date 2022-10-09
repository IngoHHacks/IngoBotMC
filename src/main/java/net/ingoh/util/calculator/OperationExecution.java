package net.ingoh.util.calculator;

import net.ingoh.util.calculator.exceptions.OperationExecutionException;

public interface OperationExecution {

    Expressable doOperation(Expressable... expression) throws OperationExecutionException;

}
