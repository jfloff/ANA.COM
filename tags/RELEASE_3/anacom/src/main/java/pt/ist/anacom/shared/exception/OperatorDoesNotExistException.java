package pt.ist.anacom.shared.exception;

public class OperatorDoesNotExistException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String operatorPrefix;

	public OperatorDoesNotExistException(String operatorPrefix) {
		super("Operator " + operatorPrefix + " does not exist");
		this.operatorPrefix = operatorPrefix;
	}
	
	public String getPrefix() {
		return operatorPrefix;
	}

}
