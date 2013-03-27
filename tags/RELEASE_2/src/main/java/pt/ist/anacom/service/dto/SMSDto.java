package pt.ist.anacom.service.dto;

public class SMSDto {

	private String text, senderNumber, receiverNumber;

	public SMSDto(String senderNumber, String receiverNumber, String text) {
		this.senderNumber = senderNumber;
		this.receiverNumber = receiverNumber;
		this.text = text;
	}

	public String getSenderNumber() {
		return this.senderNumber;
	}

	public String getReceiverNumber() {
		return this.receiverNumber;
	}

	public String getText() {
		return this.text;
	}

}
