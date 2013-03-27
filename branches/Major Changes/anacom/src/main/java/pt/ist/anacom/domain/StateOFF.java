package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;

public class StateOFF extends StateOFF_Base {

    public StateOFF() {
        super();
    }

    @Override
    public void sentSMS(SMSOut smsOut) {
        throw new PhoneIsOFFException(smsOut.getPhoneSent().getPhoneNumber());
    }

    @Override
    public void receivedSMS(SMSIn smsIn) {
    }

    @Override
    public void startSentVoiceCall() {
        throw new PhoneIsOFFException(getPhoneState().getPhoneNumber());
    }

    @Override
    public void startReceivedVoiceCall() {
        throw new PhoneIsOFFException(getPhoneState().getPhoneNumber());
    }

    @Override
    public State getStateType() {
        return State.OFF;
    }

    // To Change Later


    @Override
    public void sentVideo(VideoOut videoOut) {
        throw new PhoneIsOFFException(getPhoneState().getPhoneNumber());
    }

    @Override
    public void receivedVideo(VideoIn videoIn) {
        throw new PhoneIsOFFException(getPhoneState().getPhoneNumber());
    }


}
