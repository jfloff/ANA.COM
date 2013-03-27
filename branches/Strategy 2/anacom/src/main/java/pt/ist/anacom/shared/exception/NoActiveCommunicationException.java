package pt.ist.anacom.shared.exception;

public class NoActiveCommunicationException extends CommunicationException {

    private static final long serialVersionUID = 1L;

    public NoActiveCommunicationException() {
    }

    public NoActiveCommunicationException(String sourcePhoneNumber, String destinationPhoneNumber) {
        super("No on going communication between [" + sourcePhoneNumber + "] and [" + destinationPhoneNumber + "].", sourcePhoneNumber, destinationPhoneNumber);
    }
}
