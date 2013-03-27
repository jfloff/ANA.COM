package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.PhoneStateException;

public class StateBUSY extends StateBUSY_Base {

    public StateBUSY(State state) {
        super();
        super.setPreviousState(state);
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
        this.getPhone().addCommunicationSent(voice);
        this.getPhone().setLastMadeCommunication(voice);
        this.getPhone().setState(this.getPreviousState());
    }

    @Override
    public void endReceivedVoiceCall(Voice voice) {
        this.getPhone().addCommunicationReceived(voice);
        this.getPhone().setState(this.getPreviousState());
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
        return AnacomData.State.BUSY;
    }
}
