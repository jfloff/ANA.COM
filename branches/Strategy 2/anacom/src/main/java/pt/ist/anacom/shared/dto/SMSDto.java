package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class SMSDto extends CommunicationDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String message;

    public SMSDto() {

    }

    public SMSDto(String sourcePhoneNumber, String destinationPhoneNumber, String text) {
        super(sourcePhoneNumber, destinationPhoneNumber);
        this.message = text;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return super.toString() + ", Msg: [\"" + message + "\"]";
    }

}
