package pt.ist.anacom.shared.exception;

public class OperatorPrefixAlreadyExistsException extends OperatorException {

    private static final long serialVersionUID = 1L;

    public OperatorPrefixAlreadyExistsException() {

    }

    public OperatorPrefixAlreadyExistsException(String operatorPrefix) {
        super("Operator prefix [" + operatorPrefix + "] already exists.", operatorPrefix);
    }
}
