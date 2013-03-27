package pt.ist.anacom.service.dto;

public class PhoneDto {

	private String phoneNumber;
	private String operator;

	public PhoneDto(String operator, String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.operator = operator;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getOperator() {
		return this.operator;
	}

}
