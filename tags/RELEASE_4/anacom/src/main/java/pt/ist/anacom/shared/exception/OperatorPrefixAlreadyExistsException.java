package pt.ist.anacom.shared.exception;

// VIT
public class OperatorPrefixAlreadyExistsException extends OperatorException {

    private static final long serialVersionUID = 1L;

    private String operatorPrefix;

    public OperatorPrefixAlreadyExistsException() {

    }

    public OperatorPrefixAlreadyExistsException(String operatorPrefix) {
        super("Duplicated Operator [Operator Prefix " + operatorPrefix + " already exists]");
        this.operatorPrefix = operatorPrefix;
    }

    public String getPrefix() {
        return this.operatorPrefix;
    }

}
