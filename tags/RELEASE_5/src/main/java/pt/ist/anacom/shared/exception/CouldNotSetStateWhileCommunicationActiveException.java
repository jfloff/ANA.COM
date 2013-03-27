package pt.ist.anacom.shared.exception;

public class CouldNotSetStateWhileCommunicationActiveException extends PhoneException {

    private static final long serialVersionUID = 1L;

	
	public CouldNotSetStateWhileCommunicationActiveException() {
	}
	
	public CouldNotSetStateWhileCommunicationActiveException(String phoneNumber) {
		super("Cant change phone [" + phoneNumber + "] state. In a communication", phoneNumber);
	}

}
