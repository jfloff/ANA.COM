package pt.ist.anacom.shared.exception;

public class OperatorNullNameException extends OperatorException {

    private static final long serialVersionUID = 1L;

    public OperatorNullNameException() {

    }

    public OperatorNullNameException(String operatorPrefix) {
        super("Operator with prefix [" + operatorPrefix + "] with empty name.", operatorPrefix);
    }
}
