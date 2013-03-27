package pt.ist.anacom.shared.exception;

public class CommunicationListIsEmptyException extends AnacomException {

    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public CommunicationListIsEmptyException() {

    }

    public CommunicationListIsEmptyException(String phoneNumber) {
        super(phoneNumber + "has not made any communications yet.");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


}
