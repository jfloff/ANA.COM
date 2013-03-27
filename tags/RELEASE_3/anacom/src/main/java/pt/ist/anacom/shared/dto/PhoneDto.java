package pt.ist.anacom.shared.dto;

public class PhoneDto {

	private String phoneNumber;
	private String operatorPrefix;

	public PhoneDto(String operatorPrefix, String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.operatorPrefix = operatorPrefix;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getOperatorPrefix() {
		return this.operatorPrefix;
	}

	@Override
	public String toString() {
		return "[Phone: " + phoneNumber + ", Op:" + operatorPrefix + "]";
	}
}
