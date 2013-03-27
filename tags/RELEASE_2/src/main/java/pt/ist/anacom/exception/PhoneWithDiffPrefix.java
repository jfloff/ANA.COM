package pt.ist.anacom.exception;

public class PhoneWithDiffPrefix extends AnacomException {

	private static final long serialVersionUID = 1L;

	private int prefix;

	public PhoneWithDiffPrefix(int prefix, String methodname) {
		super("Prefix does not match [" + prefix + "]");
		this.prefix = prefix;
	}

	public Integer getPrefix() {
		return prefix;
	}
}
