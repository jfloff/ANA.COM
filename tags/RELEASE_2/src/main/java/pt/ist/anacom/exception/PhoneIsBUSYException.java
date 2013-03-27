package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Phone;

public class PhoneIsBUSYException extends AnacomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Phone phone;

	public PhoneIsBUSYException(Phone phone) {
		super("The phone number " + phone.getNr() + " is busy.");
		this.phone = phone;
	}

	public String getPhoneNr() {
		return phone.getNr();
	}
}
