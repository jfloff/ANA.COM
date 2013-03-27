package pt.ist.anacom.shared.exception;

public class PhonePrefixDoesNotMatchException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String operatorName, operatorPrefix;
	private String phoneNumber, phonePrefix;

	public PhonePrefixDoesNotMatchException(String operatorName, String operatorPrefix, String phoneNumber, String phonePrefix) {
		super("Phone " + phoneNumber + " prefix (" + phonePrefix + ") does not match operator " + operatorName + " prefix (" + operatorPrefix + ")");
		this.operatorName = operatorName;
		this.operatorPrefix = operatorPrefix;
		this.phoneNumber = phoneNumber;
		this.phonePrefix = phonePrefix;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public String getOperatorPrefix() {
		return operatorPrefix;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPhonePrefix() {
		return phonePrefix;
	}

}
