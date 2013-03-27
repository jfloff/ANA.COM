package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Phone;


public class InvalidBalanceOperationException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private Phone phone;
	
	public InvalidBalanceOperationException(Phone phone) {
		super("Phone balance must be between 0 and 100 [ has:" + phone.getBalance() + " ]");
		this.phone = phone;
	}

	public Phone getPhone() {
		return this.phone;
	}
	
}
