package pt.ist.anacom.domain;

import pt.ist.anacom.exception.UnsuportedOperationException;

public class OldGenPhone extends OldGenPhone_Base {

	public OldGenPhone(String number) {
		super();
		this.setNr(number);
		this.setBalance(0);
		this.setState(State.MobileStateOFF);
	}

	public OldGenPhone(String number, int balance) {
		super();
		this.setNr(number);
		this.setBalance(balance);
		this.setState(State.MobileStateOFF);
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
