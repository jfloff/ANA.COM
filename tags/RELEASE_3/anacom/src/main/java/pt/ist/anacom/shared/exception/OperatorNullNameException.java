package pt.ist.anacom.shared.exception;

public class OperatorNullNameException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String operatorPrefix;

	public OperatorNullNameException(String prefix) {
		super("Operator with prefix " + prefix + " with no name (null).");
		this.operatorPrefix = prefix;
	}

	public String getPrefix() {
		return operatorPrefix;
	}

}
