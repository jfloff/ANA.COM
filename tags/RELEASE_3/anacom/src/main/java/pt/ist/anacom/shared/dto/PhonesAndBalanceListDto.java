package pt.ist.anacom.shared.dto;

import java.util.ArrayList;
public class PhonesAndBalanceListDto {

	private String operatorPrefix;
	private ArrayList<BalanceDto> phonesList;

	public PhonesAndBalanceListDto(String operatorPrefix) {
		this.phonesList = new ArrayList<BalanceDto>();
		this.operatorPrefix = operatorPrefix;
	}
	
	public PhonesAndBalanceListDto(String operatorPrefix, ArrayList<BalanceDto> phonesList) {
		this.phonesList = phonesList;
		this.operatorPrefix = operatorPrefix;
	}

	public String getOperatorPrefix() {
		return operatorPrefix;
	}

	public void add(String phoneNumber, int balance) {
		phonesList.add(new BalanceDto(phoneNumber, balance));
	}
	
	public ArrayList<BalanceDto> getPhoneList() {
		return this.phonesList;
	}

	@Override
	public String toString() {
		return phonesList.toString();
	}

}
