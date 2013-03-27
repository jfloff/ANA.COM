package pt.ist.anacom.exception;

public class OperatorNullNameException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String prefix;

	public OperatorNullNameException(String prefix) {
		super("Operator with prefix " + prefix + " with no name (null).");
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

}
