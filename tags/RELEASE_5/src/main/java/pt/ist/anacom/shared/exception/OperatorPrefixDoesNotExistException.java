package pt.ist.anacom.shared.exception;

public class OperatorPrefixDoesNotExistException extends OperatorException {

    private static final long serialVersionUID = 1L;

    public OperatorPrefixDoesNotExistException() {

    }

    public OperatorPrefixDoesNotExistException(String operatorPrefix) {
        super("Operator prefix [" + operatorPrefix + "] does not exist.", operatorPrefix);
    }
}
