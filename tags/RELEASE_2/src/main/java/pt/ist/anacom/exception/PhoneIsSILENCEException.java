package pt.ist.anacom.exception;

import pt.ist.anacom.domain.Phone;

public class PhoneIsSILENCEException extends AnacomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Phone phone;

	public PhoneIsSILENCEException(Phone phone) {
		super("The phone number " + phone.getNr() + " is in silence.");
		this.phone = phone;
	}

	public String getPhoneNr() {
		return phone.getNr();
	}

}
