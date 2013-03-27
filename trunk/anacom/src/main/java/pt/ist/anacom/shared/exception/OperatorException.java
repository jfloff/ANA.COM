package pt.ist.anacom.shared.exception;

public class OperatorException extends AnacomException {

    private static final long serialVersionUID = 1L;

    private String operatorPrefix;

    public OperatorException() {

    }

    public OperatorException(String message) {
        super("OPERATOR:" + message);
        this.operatorPrefix = null;
    }

    public OperatorException(String message, String operatorPrefix) {
        super("OPERATOR:" + message);
        this.operatorPrefix = operatorPrefix;
    }

    public String getOperatorPrefix() {
        return this.operatorPrefix;
    }
}
