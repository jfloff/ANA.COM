package pt.ist.anacom.exception;

public class PrefixDoesNotExistException extends AnacomException{

	private static final long serialVersionUID = 1L;

	private int prefix;
	
	public PrefixDoesNotExistException(int prefix, String classname, String methodname) {
		super("[ERR @" + classname + "." + methodname + "] Prefix does not exist [" + prefix + "]");
		this.prefix = prefix;
	}
	
	public Integer getPrefix() {
		return prefix;
	}
}
