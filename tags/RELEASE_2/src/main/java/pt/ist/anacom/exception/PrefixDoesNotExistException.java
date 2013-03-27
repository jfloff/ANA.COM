package pt.ist.anacom.exception;

public class PrefixDoesNotExistException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String prefix;

	public PrefixDoesNotExistException(String prefix) {
		super("Prefix does not exist [" + prefix + "]");
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
