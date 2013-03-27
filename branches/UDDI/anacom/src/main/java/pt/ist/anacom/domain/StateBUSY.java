package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;

public class StateBUSY extends StateBUSY_Base {

    public StateBUSY() {
        super();
    }

    @Override
    public void sentSMS(SMS sms) {
        throw new PhoneIsBUSYException(sms.getSourcePhoneNumber());
    }

    @Override
    public void receivedSMS(SMS sms) {
        this.getPhoneState().addCommunicationReceived(sms);
    }

    @Override
    public void sentVoice(Voice voice) {
        throw new PhoneIsBUSYException(voice.getSourcePhoneNumber());

    }

    @Override
    public void receivedVoice(Voice voice) {
        throw new PhoneIsBUSYException(voice.getDestinationPhoneNumber());
    }

    @Override
    public void sentVideo(Video video) {
        throw new PhoneIsBUSYException(video.getSourcePhoneNumber());

    }

    @Override
    public void receivedVideo(Video video) {
        throw new PhoneIsBUSYException(video.getDestinationPhoneNumber());
    }

    @Override
    public void throwStateException(String phoneNumber) {
        throw new PhoneIsBUSYException(phoneNumber);
    }

    @Override
    public State getStateType() {
        return State.BUSY;
    }

}
