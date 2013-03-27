package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class NewGenPhone extends NewGenPhone_Base {

    public NewGenPhone(String phoneNumber) {
        super();
        this.setPhoneNumber(phoneNumber);
        this.setBalance(0);
        this.setState(AnacomData.State.OFF);
    }

    public NewGenPhone(String number, int balance) {
        super();
        this.setPhoneNumber(number);
        this.setBalance(balance);
        this.setState(AnacomData.State.OFF);
    }

    @Override
    public void addSentVideo(Video video) {
        this.getState().sentVideo(video);
    }

    @Override
    public void addReceivedVideo(Video video) {
        this.getState().receivedVideo(video);
    }
}
