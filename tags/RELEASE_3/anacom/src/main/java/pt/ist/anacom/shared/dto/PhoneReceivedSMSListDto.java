package pt.ist.anacom.shared.dto;

import java.util.ArrayList;

public class PhoneReceivedSMSListDto {

	private String operatorPrefix, phoneNumber;
	private ArrayList<SMSDto> smsList;

	public PhoneReceivedSMSListDto(String operatorPrefix, String phoneNumber) {
		this.operatorPrefix = operatorPrefix;
		this.phoneNumber = phoneNumber;
		smsList = new ArrayList<SMSDto>();
	}

	public void add(String sourceNumber, String destinationNumber, String text){
		smsList.add(new SMSDto(sourceNumber, destinationNumber, text));
	}
	
	public String getOperatorPrefix() {
		return operatorPrefix;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public ArrayList<SMSDto> getSmsList() {
		return smsList;
	}
	
	@Override
	public String toString() {
		return smsList.toString();
	}
		
}
