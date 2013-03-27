package pt.ist.anacom.shared.exception;


public class CouldNotConnectException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String prefix;

	public CouldNotConnectException(String prefix) {
		super("Could not connect to " + prefix + ".");
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

}
