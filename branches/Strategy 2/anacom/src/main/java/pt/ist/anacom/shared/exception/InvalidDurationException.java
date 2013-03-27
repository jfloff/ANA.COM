package pt.ist.anacom.shared.exception;

public class InvalidDurationException extends CommunicationException {

    private static final long serialVersionUID = 1L;

    public InvalidDurationException() {

    }

    public InvalidDurationException(String sourcePhoneNumber, String destinationPhoneNumber) {
        super("Communication from [" + sourcePhoneNumber + "] to [" + destinationPhoneNumber + "] has an invalid duration.", sourcePhoneNumber, destinationPhoneNumber);
    }
}
