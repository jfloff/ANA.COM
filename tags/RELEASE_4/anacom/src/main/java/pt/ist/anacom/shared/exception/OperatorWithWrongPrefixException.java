package pt.ist.anacom.shared.exception;

public class OperatorWithWrongPrefixException extends OperatorException {

    private static final long serialVersionUID = 1L;

    private String operatorName;
    private String operatorPrefix;

    public OperatorWithWrongPrefixException() {

    }

    public OperatorWithWrongPrefixException(String operatorName, String prefix) {
        super("Operator (" + operatorName + ") with " + "wrong prefix (" + prefix + ")");
        this.operatorName = operatorName;
        this.operatorPrefix = prefix;
    }

    public String getName() {
        return this.operatorName;
    }

    public String getOperatorPrefix() {
        return this.operatorPrefix;
    }

}
