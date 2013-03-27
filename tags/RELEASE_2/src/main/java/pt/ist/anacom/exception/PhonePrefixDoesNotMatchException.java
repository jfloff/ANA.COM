package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;

public class PhonePrefixDoesNotMatchException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private Operator operator;
	private Phone phone;

	public PhonePrefixDoesNotMatchException(Operator operator, Phone phone) {
		super("Phone " + phone.getNr() + " prefix (" + phone.getPrefix() + ") does not match operator " + operator.getName() + " prefix ("
				+ operator.getPrefix() + ")");
		this.operator = operator;
		this.phone = phone;
	}

	public Operator getOperator() {
		return this.operator;
	}

	public Phone getPhone() {
		return this.phone;
	}

}
