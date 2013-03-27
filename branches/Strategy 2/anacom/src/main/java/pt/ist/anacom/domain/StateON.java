package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.data.AnacomData.State;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.PhoneStateException;

public class StateON extends StateON_Base {

    public StateON() {
        super();
    }

    @Override
    public void sentSMS(SMS sms) {
        this.getPhone().addCommunicationSent(sms);
        this.getPhone().setLastMadeCommunication(sms);
    }

    @Override
    public void receivedSMS(SMS sms) {
        this.getPhone().addCommunicationReceived(sms);
    }

    @Override
    public void startSentVoiceCall(Voice voice) {
        this.getPhone().setActiveCommunication(voice);
        this.getPhone().setState(AnacomData.State.BUSY);
    }

    @Override
    public void startReceivedVoiceCall(Voice voice) {
        this.getPhone().setActiveCommunication(voice);
        this.getPhone().setState(AnacomData.State.BUSY);
    }

    @Override
    public void endReceivedVoiceCall(Voice voice) {
        throw new NoActiveCommunicationException(voice.getSourcePhoneNumber(), voice.getDestinationPhoneNumber());
    }

    @Override
    public void endSentVoiceCall(Voice voice) {
        throw new NoActiveCommunicationException(voice.getSourcePhoneNumber(), voice.getDestinationPhoneNumber());
    }

    @Override
    public void sentVideo(Video video) {
    }

    @Override
    public void receivedVideo(Video video) {
    }

    @Override
    public AnacomData.State getStateType() {
        return AnacomData.State.ON;
    }
}
