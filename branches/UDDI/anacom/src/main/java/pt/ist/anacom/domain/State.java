package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public abstract class State extends State_Base {

    public State() {
        super();
    }

    public abstract AnacomData.State getStateType();

    // Comunicacoes

    public abstract void sentSMS(SMS sms);

    public abstract void receivedSMS(SMS sms);

    public abstract void sentVoice(Voice voice);

    public abstract void receivedVoice(Voice voice);

    public abstract void sentVideo(Video video);

    public abstract void receivedVideo(Video video);

    // New

    public boolean canStartMadeVoiceCall() {
        return false;
    }

    public boolean canStartReceivedVoiceCall() {
        return false;
    }

    public abstract void throwStateException(String phoneNumber);
}
