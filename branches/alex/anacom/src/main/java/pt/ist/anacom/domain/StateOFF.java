package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.PhoneStateException;

public class StateOFF extends StateOFF_Base {

    public StateOFF() {
        super();
    }

    @Override
    public void sentSMS(SMS sms) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public void receivedSMS(SMS sms) {
        this.getPhone().addCommunicationReceived(sms);
    }

    @Override
    public void startSentVoiceCall(Voice voice) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public void startReceivedVoiceCall(Voice voice) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public void endSentVoiceCall(Voice voice) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public void endReceivedVoiceCall(Voice voice) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public void sentVideo(Video video) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public void receivedVideo(Video video) {
        throw new PhoneStateException(this.getPhone().getPhoneNumber(), this.getStateType());
    }

    @Override
    public AnacomData.State getStateType() {
        return AnacomData.State.OFF;
    }
}
