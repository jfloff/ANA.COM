package pt.ist.anacom.shared.exception;

public class SMSInvalidTextException extends CommunicationException {

    private static final long serialVersionUID = 1L;

    public SMSInvalidTextException() {

    }

    public SMSInvalidTextException(String sourcePhoneNumber, String destinationPhoneNumber) {
        super("SMS from [" + sourcePhoneNumber + "] to [" + destinationPhoneNumber + "] has an empty message.", sourcePhoneNumber, destinationPhoneNumber);
    }
}
