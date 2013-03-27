package pt.ist.anacom.shared.exception;

public class PrefixDoesNotExistException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String operatorPrefix;

	public PrefixDoesNotExistException(String prefix) {
		super("Prefix does not exist [" + prefix + "]");
		this.operatorPrefix = prefix;
	}

	public String getOperatorPrefix() {
		return operatorPrefix;
	}
}
