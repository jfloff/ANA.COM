package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class SMSDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String message, sourceNumber, destinationNumber;

    public SMSDto() {

    }

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
