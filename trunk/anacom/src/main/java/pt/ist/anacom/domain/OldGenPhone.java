package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.UnsuportedOperationException;

public class OldGenPhone extends OldGenPhone_Base {

    public OldGenPhone(String number) {
        super();
        this.setPhoneNumber(number);
        this.setBalance(0);
        this.setState(AnacomData.State.OFF);
    }

    public OldGenPhone(String number, int balance) {
        super();
        this.setPhoneNumber(number);
        this.setBalance(balance);
        this.setState(AnacomData.State.OFF);
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
