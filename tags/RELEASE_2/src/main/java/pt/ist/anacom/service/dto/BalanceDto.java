package pt.ist.anacom.service.dto;

public class BalanceDto {

	private String phoneNumber;
	public int balance;

	public BalanceDto(String phoneNumber, int balance) {
		this.phoneNumber = phoneNumber;
		this.balance = balance;
	}

	public String getNumber() {
		return this.phoneNumber;
	}

	public int getBalance() {
		return this.balance;
	}
}
