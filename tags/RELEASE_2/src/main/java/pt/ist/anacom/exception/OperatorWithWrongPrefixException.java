package pt.ist.anacom.exception;

public class OperatorWithWrongPrefixException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String operatorName;
	private String prefix;

	public OperatorWithWrongPrefixException(String operatorName, String prefix) {
		super("Operator (" + operatorName + ") with " + "wrong prefix (" + prefix + ")");
		this.operatorName = operatorName;
		this.prefix = prefix;
	}

	public String getName() {
		return this.operatorName;
	}

	public String getPrefix() {
		return this.prefix;
	}

}
