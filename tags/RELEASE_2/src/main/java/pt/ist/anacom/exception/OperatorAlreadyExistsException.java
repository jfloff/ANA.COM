package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Operator;

public class OperatorAlreadyExistsException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private Operator operator;

	public OperatorAlreadyExistsException(Operator operator) {
		super("Duplicated operator [" + operator.getName() + "; " + operator.getPrefix() + "]");
		this.operator = operator;
	}

	public String getOperatorName() {
		return operator.getName();
	}

	public String getOperatorPrefix() {
		return operator.getPrefix();
	}

}
