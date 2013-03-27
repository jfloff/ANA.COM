package pt.ist.anacom.domain;

import pt.ist.anacom.exception.NoSuchPhoneException;
import pt.ist.anacom.exception.OperatorAlreadyExistsException;
import pt.ist.anacom.exception.OperatorDoesNotExistException;
import pt.ist.anacom.exception.PhonePrefixDoesNotMatchException;
import pt.ist.anacom.exception.PrefixDoesNotExistException;

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
			throw new OperatorAlreadyExistsException(operator);
		super.addOperator(operator);
	}

	public boolean hasOperator(Operator operator) {
		for (Operator aux : this.getOperatorSet())
			if (operator.conflicts(aux))
				return true;

		return false;
	}

	/**
	 * Prints all the Operators information.
	 * 
	 */
	public void printOperators() {
		for (Operator operator : getOperator()) {
			System.out.println(operator);
			operator.printPhones();
		}
	}

	/**
	 * Return the operator responsible by a specific prefix
	 * 
	 * @param prefix
	 *            The prefix of the operator
	 * @return The operator responsible
	 */
	public Operator getOperatorByPrefix(String prefix) {
		for (Operator operator : getOperator())
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
			if (operator.getName().equals(operatorName))
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
	public void addPhone(String operatorName, Phone phone) {

		Operator operator = this.getOperatorByString(operatorName);

		if (operator == null)
			throw new OperatorDoesNotExistException(operatorName);

		if (!(operator.getPrefix().equals(phone.getPrefix())))
			throw new PhonePrefixDoesNotMatchException(operator, phone);

		operator.addPhone(phone);
	}

	public Phone getPhone(String number) {
		if (number == null)
			throw new NoSuchPhoneException(number);
		String prefix = number.substring(0, Anacom.prefixLength);
		Operator operator = getOperatorByPrefix(prefix);
		if (operator == null)
			throw new PrefixDoesNotExistException(prefix);

		Phone phone = operator.getPhoneByNr(number);

		if (phone == null)
			throw new NoSuchPhoneException(number);

		return phone;
	}

	public void removePhone(String operatorName, String phoneNumber) {

		Operator operator = this.getOperatorByString(operatorName);
		
		if (operator == null)
			throw new OperatorDoesNotExistException(operatorName);

		Phone phone = operator.getPhoneByNr(phoneNumber);
		if (phone == null)
			throw new NoSuchPhoneException(phoneNumber);

		operator.removePhone(phone);
	}

}
