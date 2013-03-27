package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Operator;

public class OperatorAlreadyExistsException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private Operator operator;
	
	public OperatorAlreadyExistsException(Operator operator, String classname, String methodname) {
		super("[ERR @" + classname + "." + methodname + "] Duplicated operator [" + operator.getName() + "; " + operator.getPrefix() + "]");
		this.operator = operator;
	}
	
	public String getOperatorName() { 
		return operator.getName(); 
	}
	
	public int getOperatorPrefix() { 
		return operator.getPrefix(); 
	}	
	
}
