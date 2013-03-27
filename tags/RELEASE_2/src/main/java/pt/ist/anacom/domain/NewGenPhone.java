package pt.ist.anacom.domain;

public class NewGenPhone extends NewGenPhone_Base {

	public NewGenPhone(String nr) {
		super();
		this.setNr(nr);
		this.setBalance(0);
		this.setState(State.MobileStateOFF);
	}

	public NewGenPhone(String number, int balance) {
		super();
		this.setNr(number);
		this.setBalance(balance);
		this.setState(State.MobileStateOFF);
	}

	@Override
	public void addSentVideo(Video video) {
		addCommunicationSent(video);
	}

	@Override
	public void addReceivedVideo(Video video) {
		addCommunicationReceived(video);
	}
}
