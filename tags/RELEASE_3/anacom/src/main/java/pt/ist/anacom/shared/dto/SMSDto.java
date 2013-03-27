package pt.ist.anacom.shared.dto;

public class SMSDto {

	private String message, sourceNumber, destinationNumber;

	public SMSDto(String sourceNumber, String destinationNumber, String text) {
		this.sourceNumber = sourceNumber;
		this.destinationNumber = destinationNumber;
		this.message = text;
	}

	public String getSourceNumber() {
		return this.sourceNumber;
	}

	public String getDestinationNumber() {
		return this.destinationNumber;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return "[From: " + sourceNumber + ", To: " + destinationNumber + ", Msg: " + message + "]";
	}
	
}