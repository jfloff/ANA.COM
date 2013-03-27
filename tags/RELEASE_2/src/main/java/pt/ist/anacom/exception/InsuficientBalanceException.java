package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Phone;

public class InsuficientBalanceException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private Phone phone;

	public InsuficientBalanceException(Phone phone) {
		super("Insuficient Balance [" + phone.getNr() + " has " + phone.getBalance() + "]");
		this.phone = phone;
	}

	public Phone getPhone() {
		return this.phone;
	}

}
