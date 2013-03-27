package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Phone;

public class PhoneAlreadyExistsException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private Phone phone;

	public PhoneAlreadyExistsException(Phone phone) {
		super("Duplicate Phone [" + phone.getNr() + "]");
		this.phone = phone;
	}

	public Phone getPhone() {
		return this.phone;
	}

}
