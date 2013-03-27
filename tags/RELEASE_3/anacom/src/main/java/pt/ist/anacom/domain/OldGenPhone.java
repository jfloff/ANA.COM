package pt.ist.anacom.domain;

import pt.ist.anacom.shared.exception.UnsuportedOperationException;

public class OldGenPhone extends OldGenPhone_Base {

	public OldGenPhone(String number) {
		super();
		this.setNr(number);
		this.setBalance(0);
		this.setState(State.MobileStateON);
	}

	public OldGenPhone(String number, int balance) {
		super();
		this.setNr(number);
		this.setBalance(balance);
		this.setState(State.MobileStateON);
	}

	@Override
	public void addSentVideo(Video video) {
		throw new UnsuportedOperationException();
	}

	@Override
	public void addReceivedVideo(Video video) {
		throw new UnsuportedOperationException();
	}

}
