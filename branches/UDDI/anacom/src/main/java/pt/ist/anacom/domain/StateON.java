package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;

public class StateON extends StateON_Base {

    public StateON() {
        super();
    }

    @Override
    public void sentSMS(SMS sms) {
        this.getPhoneState().addCommunicationSent(sms);
    }

    @Override
    public void receivedSMS(SMS sms) {
        this.getPhoneState().addCommunicationReceived(sms);
    }

    @Override
    public void sentVoice(Voice voice) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivedVoice(Voice voice) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sentVideo(Video video) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivedVideo(Video video) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canStartMadeVoiceCall() {
        return true;
    }

    @Override
    public boolean canStartReceivedVoiceCall() {
        return true;
    }

    @Override
    public void throwStateException(String phoneNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public State getStateType() {
        return State.ON;
    }

}
