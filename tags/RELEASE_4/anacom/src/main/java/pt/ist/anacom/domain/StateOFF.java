package pt.ist.anacom.domain;

import pt.ist.anacom.shared.exception.PhoneIsOFFException;

public class StateOFF extends StateOFF_Base {

    public StateOFF() {
        super();
        super.setName("OFF");
    }

    @Override
    public void sentSMS(SMS sms) {
        throw new PhoneIsOFFException(sms.getNrSource());
    }

    @Override
    public void receivedSMS(SMS sms) {
        throw new PhoneIsOFFException(sms.getNrDest());
    }

    @Override
    public void sentVoice(Voice voice) {
        throw new PhoneIsOFFException(voice.getNrSource());
    }

    @Override
    public void receivedVoice(Voice voice) {
        throw new PhoneIsOFFException(voice.getNrDest());
    }

    @Override
    public void sentVideo(Video video) {
        throw new PhoneIsOFFException(video.getNrSource());
    }

    @Override
    public void receivedVideo(Video video) {
        throw new PhoneIsOFFException(video.getNrDest());
    }

}
