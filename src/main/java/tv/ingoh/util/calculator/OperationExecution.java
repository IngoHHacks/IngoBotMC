package tv.ingoh.util.calculator;

import tv.ingoh.util.calculator.exceptions.OperationExecutionException;

public interface OperationExecution {

    Expressable doOperation(Expressable... expression) throws OperationExecutionException;

}
