package pt.ist.anacom.service.dto;

import java.util.ArrayList;

public class PhoneAndBalanceListDto {

	private String operatorName;
	private ArrayList<SimplePhone> phonesList;

	public PhoneAndBalanceListDto(String operatorName) {
		phonesList = new ArrayList<SimplePhone>();
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void add(String phone, int balance) {
		phonesList.add(new SimplePhone(phone, balance));
	}

	@Override
	public String toString() {
		return phonesList.toString();
	}

	private class SimplePhone {

		String phoneNumber;
		int balance;

		public SimplePhone(String phoneNumber, int balance) {
			this.phoneNumber = phoneNumber;
			this.balance = balance;
		}

		// public int getPhoneNumber(){ return phoneNumber; }

		// public int getBalance(){ return balance; }

		@Override
		public String toString() {
			return phoneNumber + " - " + balance;
		}

	}

}
