package pt.ist.anacom.domain;

import pt.ist.anacom.exception.OperatorNullNameException;
import pt.ist.anacom.exception.OperatorWithWrongPrefixException;
import pt.ist.anacom.exception.PhoneAlreadyExistsException;

public class Operator extends Operator_Base {

	public Operator(String prefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {
		super();

		if(name == null)
			throw new OperatorNullNameException(prefix);
		if (prefix.length() != Anacom.prefixLength)
			throw new OperatorWithWrongPrefixException(name, prefix);

		setPrefix(prefix);
		setName(name);
		setPlan(new Plan(tax, taxVoice, taxSMS, taxVideo));
	}

	@Override
	public void addPhone(Phone phone) {
		if (hasPhone(phone.getNr()))
			throw new PhoneAlreadyExistsException(phone);
		super.addPhone(phone);
	}

	@Override
	public String toString() {
		return getName() + " (" + getPrefix() + ")" + " has " + getPhoneCount() + " Phones.";
	}

	public boolean conflicts(Operator operator) {

		return (operator.getPrefix().equals(this.getPrefix())) && (operator.getName().equals(this.getName()));
	}

	public boolean hasPhone(String nr) {
		return getPhoneByNr(nr) != null;
	}

	/*
	 * ------------------- - Phones Methods - -------------------
	 */

	/**
	 * Search a phone by his number
	 * 
	 * @param nr
	 *            The phone number to find
	 * 
	 */
	public Phone getPhoneByNr(String nr) {

		for (Phone phone : getPhoneSet())
			if (phone.getNr().equals(nr))
				return phone;

		return null;
	}

	public void removePhoneByNumber(String phoneNumber) {

		for (Phone phone : getPhoneSet())
			if (phone.getNr().equals(phoneNumber)) {
				removePhone(phone);
				return;
			}
	}

	/**
	 * Print all the Phones objects that belongs to the operator.
	 * 
	 */
	public void printPhones() {
		for (Phone phone : getPhoneSet())
			System.out.println(phone);
	}

}
