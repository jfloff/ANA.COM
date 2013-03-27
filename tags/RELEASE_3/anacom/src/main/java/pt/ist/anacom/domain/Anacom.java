package pt.ist.anacom.domain;

import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorDoesNotExistException;
import pt.ist.anacom.shared.exception.PhonePrefixDoesNotMatchException;
import pt.ist.anacom.shared.exception.PrefixDoesNotExistException;

public class Anacom extends Anacom_Base {

	/*
	 * ------------------- Data Values -------------------
	 */

	public static final int prefixLength = 2;
	public static final int numberLength = 9;

	/*
	 * ------------------- Anacom Class -------------------
	 */

	public Anacom() {
		super();
	}

	/*
	 * ------------------- Operator Methods -------------------
	 */

	@Override
	public void addOperator(Operator operator) {
		if (hasOperator(operator))
			throw new OperatorAlreadyExistsException(operator.getPrefix(), operator.getPrefix());
		super.addOperator(operator);
	}

	public boolean hasOperator(Operator operator) {
		for (Operator aux : this.getOperatorSet())
			if (operator.conflicts(aux))
				return true;

		return false;
	}

	/**
	 * Return the operator responsible by a specific prefix
	 * 
	 * @param prefix
	 *            The prefix of the operator
	 * @return The operator responsible
	 */
	public Operator getOperatorByPrefix(String prefix) {
		for (Operator operator : this.getOperator())
			if (operator.getPrefix().equals(prefix))
				return operator;

		return null;
	}

	/**
	 * Return the operator with a specific name
	 * 
	 * @param operatorName
	 *            The name of the operator
	 * @return The operator with that name
	 */
	public Operator getOperatorByString(String operatorName) {
		for (Operator operator : getOperatorSet())
			if (operator.getPrefix().equals(operatorName))
				return operator;

		return null;
	}

	/*
	 * ------------------- Phones Methods -------------------
	 */

	/**
	 * Add a phone to the correct operator responsible by his prefix
	 * 
	 * @param phone
	 *            The phone object to be added
	 * 
	 */
	public void addPhone(String operatorPrefix, Phone phone) {

		Operator operator = this.getOperatorByPrefix(operatorPrefix);

		if (operator == null)
			throw new OperatorDoesNotExistException(operatorPrefix);

		if (!(operator.getPrefix().equals(phone.getPrefix())))
			throw new PhonePrefixDoesNotMatchException(operator.getPrefix(), operator.getPrefix(), phone.getNr(), phone.getPrefix());

		operator.addPhone(phone);
	}

	public Phone getPhone(String number) {
		if (number == null)
			throw new NoSuchPhoneException(number);
		String prefix = number.substring(0, Anacom.prefixLength);
		Operator operator = getOperatorByPrefix(prefix);
		if (operator == null)
			/* Mudar para OperatorDoesNotExist?*/
			throw new PrefixDoesNotExistException(prefix);

		Phone phone = operator.getPhoneByNr(number);

		if (phone == null)
			throw new NoSuchPhoneException(number);

		return phone;
	}

	public void removePhone(String operatorPrefix, String phoneNumber) {

		Operator operator = this.getOperatorByPrefix(operatorPrefix);
		
		if (operator == null)
			throw new OperatorDoesNotExistException(operatorPrefix);

		Phone phone = operator.getPhoneByNr(phoneNumber);
		if (phone == null)
			throw new NoSuchPhoneException(phoneNumber);

		operator.removePhone(phone);
	}

}
