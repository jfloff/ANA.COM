package pt.ist.anacom.shared.exception;

public class OperatorPrefixWrongLengthException extends OperatorException {

    private static final long serialVersionUID = 1L;

    private String operatorName;

    public OperatorPrefixWrongLengthException() {

    }

    public OperatorPrefixWrongLengthException(String operatorName, String operatorPrefix) {
        super("Operator [" + operatorName + "] prefix [" + operatorPrefix + "] must have 2 digits.", operatorPrefix);
        this.operatorName = operatorName;
    }

    public String getOperatorName() {
        return this.operatorName;
    }
}
