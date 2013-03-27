package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;

public class StateOFF extends StateOFF_Base {

    public StateOFF() {
        super();
    }

    @Override
    public void sentSMS(SMS sms) {
        throw new PhoneIsOFFException(sms.getSourcePhoneNumber());
    }

    @Override
    public void receivedSMS(SMS sms) {
    }

    @Override
    public void sentVoice(Voice voice) {
        throw new PhoneIsOFFException(voice.getSourcePhoneNumber());
    }

    @Override
    public void receivedVoice(Voice voice) {
        throw new PhoneIsOFFException(voice.getDestinationPhoneNumber());
    }

    @Override
    public void sentVideo(Video video) {
        throw new PhoneIsOFFException(video.getSourcePhoneNumber());
    }

    @Override
    public void receivedVideo(Video video) {
        throw new PhoneIsOFFException(video.getDestinationPhoneNumber());
    }

    @Override
    public void throwStateException(String phoneNumber) {
        throw new PhoneIsOFFException(phoneNumber);
    }

    @Override
    public State getStateType() {
        return State.OFF;
    }

}
