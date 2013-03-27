package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Phone;

public class PhoneIsOFFException extends AnacomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Phone phone;

	public PhoneIsOFFException(Phone phone) {
		super("The phone number " + phone.getNr() + " is turned off.");
		this.phone = phone;
	}

	public String getPhoneNr() {
		return phone.getNr();
	}

}
