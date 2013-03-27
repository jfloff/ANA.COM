package pt.ist.anacom.shared.dto;

public class BalanceDto {

	private String phoneNumber;
	private int balance;

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
	
	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "[Phone: " + phoneNumber + ", Bal: " + balance + "]";
	}
	
	
}
