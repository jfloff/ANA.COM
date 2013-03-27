package pt.ist.anacom.shared.exception;


public class OperatorAlreadyExistsException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String operatorName;
	private String operatorPrefix;

	public OperatorAlreadyExistsException(String operatorName, String operatorPrefix) {
		super("Duplicated operator [" + operatorName + "; " + operatorPrefix + "]");
		this.operatorName = operatorName;
		this.operatorPrefix = operatorPrefix;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public String getPrefix() {
		return this.operatorPrefix;
	}

}
