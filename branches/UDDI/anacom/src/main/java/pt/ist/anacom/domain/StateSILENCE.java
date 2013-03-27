package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData.State;
import pt.ist.anacom.shared.exception.PhoneIsSILENCEException;

public class StateSILENCE extends StateSILENCE_Base {

    public StateSILENCE() {
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
        throw new PhoneIsSILENCEException(voice.getDestinationPhoneNumber());

    }

    @Override
    public void sentVideo(Video video) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivedVideo(Video video) {
        throw new PhoneIsSILENCEException(video.getDestinationPhoneNumber());
    }

    @Override
    public void throwStateException(String phoneNumber) {
        throw new PhoneIsSILENCEException(phoneNumber);
    }

    @Override
    public State getStateType() {
        return State.SILENCE;
    }

}
