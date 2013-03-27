package pt.ist.anacom.exception;

public class PhoneWithDiffPrefix extends AnacomException{

	private static final long serialVersionUID = 1L;

	private int prefix;
	
	public PhoneWithDiffPrefix(int prefix, String classname, String methodname) {
		super("[ERR @" + classname +"." + methodname + "] Prefix does not match [" + prefix + "]");
		this.prefix = prefix;
	}
	
	public Integer getPrefix() {
		return prefix;
	}
}
