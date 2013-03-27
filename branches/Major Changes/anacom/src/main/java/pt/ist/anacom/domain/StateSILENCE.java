package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;

public class StateSILENCE extends StateSILENCE_Base {

    public StateSILENCE() {
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
        // TODO Auto-generated method stub

    }

    @Override
    public void startReceivedVoiceCall() {
        // TODO Auto-generated method stub

    }

    @Override
    public State getStateType() {
        return State.SILENCE;
    }

    // TODO: To change Later

    @Override
    public void sentVideo(VideoOut video) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivedVideo(VideoIn video) {
        // TODO Auto-generated method stub

    }

}
