package pt.ist.anacom.shared.exception;

public class CouldNotConnectException extends OperatorException {

    private static final long serialVersionUID = 1L;

    public CouldNotConnectException() {

    }

    public CouldNotConnectException(String operatorPrefix) {
        super("Could not connect to Operator [" + operatorPrefix + "].", operatorPrefix);
    }

}
