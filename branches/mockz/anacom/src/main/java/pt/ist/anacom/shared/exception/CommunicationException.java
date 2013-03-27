package pt.ist.anacom.shared.exception;

public class CommunicationException extends AnacomException {

    private static final long serialVersionUID = 1L;

    private String sourcePhoneNumber, destinationPhoneNumber;

    public CommunicationException() {

    }

    public CommunicationException(String message, String sourcePhoneNumber, String destinationPhoneNumber) {
        super("COMMUNICATION:" + message);
        this.sourcePhoneNumber = sourcePhoneNumber;
        this.destinationPhoneNumber = destinationPhoneNumber;
    }

    public String getSourcePhoneNumber() {
        return this.sourcePhoneNumber;
    }

    public String getDestinationPhoneNumber() {
        return this.destinationPhoneNumber;
    }
}
