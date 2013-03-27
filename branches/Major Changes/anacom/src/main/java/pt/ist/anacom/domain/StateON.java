package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;

public class StateON extends StateON_Base {

    public StateON() {
        super();
    }

    @Override
    public void sentSMS(SMSOut smsOut) {
        this.getPhoneState().addCommunicationSent(smsOut);
    }

    @Override
    public void receivedSMS(SMSIn smsIn) {
        this.getPhoneState().addCommunicationReceived(smsIn);
    }

    @Override
    public void startSentVoiceCall() {
        getPhoneState().setState(getPhoneState().getStateBUSY());
    }

    @Override
    public void startReceivedVoiceCall() {
        getPhoneState().setState(getPhoneState().getStateBUSY());
    }

    @Override
    public State getStateType() {
        return State.ON;
    }

    // To change later

    @Override
    public void sentVideo(VideoOut video) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivedVideo(VideoIn video) {
        // TODO Auto-generated method stub

    }

}
