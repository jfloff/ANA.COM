package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;

public class StateBUSY extends StateBUSY_Base {

    public StateBUSY() {
        super();
    }

    // ----------------------------------
    // ----------SMS Operations----------
    // ----------------------------------

    @Override
    public void sentSMS(SMSOut smsOut) {
        throw new PhoneIsBUSYException(smsOut.getPhoneSent().getPhoneNumber());
    }

    @Override
    public void receivedSMS(SMSIn smsIn) {
        this.getPhoneState().addCommunicationReceived(smsIn);
    }

    // ----------------------------------
    // ---------Voice Operations---------
    // ----------------------------------

    @Override
    public void startSentVoiceCall() {
        throw new PhoneIsBUSYException(getPhoneState().getPhoneNumber());
    }

    @Override
    public void startReceivedVoiceCall() {
        throw new PhoneIsBUSYException(getPhoneState().getPhoneNumber());
    }


    // ----------------------------------
    // ---------Video Operations---------
    // ----------------------------------

    @Override
    public State getStateType() {
        return State.BUSY;
    }

    // To change

    @Override
    public void sentVideo(VideoOut videoOut) {
        throw new PhoneIsBUSYException(getPhoneState().getPhoneNumber());
    }

    @Override
    public void receivedVideo(VideoIn videoIn) {
        throw new PhoneIsBUSYException(getPhoneState().getPhoneNumber());
    }

}
