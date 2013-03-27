package pt.ist.anacom.shared.exception;

public class InvalidTaxException extends OperatorException {

    private static final long serialVersionUID = 1L;

    public InvalidTaxException() {

    }

    public InvalidTaxException(String operatorPrefix) {
        super("Tax value for Operator [" + operatorPrefix + "] must be at least 0.", operatorPrefix);
    }
}
