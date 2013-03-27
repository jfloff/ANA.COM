package pt.ist.anacom.shared.exception;

// VIT
public class OperatorNameAlreadyExistsException extends OperatorException {

    private static final long serialVersionUID = 1L;

    private String operatorName;

    public OperatorNameAlreadyExistsException() {

    }

    public OperatorNameAlreadyExistsException(String operatorName) {
        super("Operator name [" + operatorName + "] already exists.");
        this.operatorName = operatorName;
    }

    public String getOperatorName() {
        return this.operatorName;
    }
}
