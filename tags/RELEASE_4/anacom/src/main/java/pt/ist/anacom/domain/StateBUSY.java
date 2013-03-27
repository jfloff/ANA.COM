package pt.ist.anacom.domain;

import pt.ist.anacom.shared.exception.PhoneIsBUSYException;

public class StateBUSY extends StateBUSY_Base {

    public StateBUSY() {
        super();
        super.setName("BUSY");
    }

    @Override
    public void sentSMS(SMS sms) {
        throw new PhoneIsBUSYException(sms.getNrSource());
    }

    @Override
    public void receivedSMS(SMS sms) {
        this.getPhoneState().addCommunicationReceived(sms);
    }

    @Override
    public void sentVoice(Voice voice) {
        throw new PhoneIsBUSYException(voice.getNrSource());

    }

    @Override
    public void receivedVoice(Voice voice) {
        throw new PhoneIsBUSYException(voice.getNrDest());
    }

    @Override
    public void sentVideo(Video video) {
        throw new PhoneIsBUSYException(video.getNrSource());

    }

    @Override
    public void receivedVideo(Video video) {
        throw new PhoneIsBUSYException(video.getNrDest());
    }

}
