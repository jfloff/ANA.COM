package pt.ist.anacom.shared.exception;

public class NoCommunicationsMadeYetException extends PhoneException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoCommunicationsMadeYetException() {

    }

    public NoCommunicationsMadeYetException(String phoneNumber) {
        super("Phone [" + phoneNumber + "] hasn't made any communication yet.", phoneNumber);
    }
}
