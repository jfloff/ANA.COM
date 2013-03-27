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

    public abstract void startSentVoiceCall(Voice voice);

    public abstract void startReceivedVoiceCall(Voice voice);

    public abstract void endSentVoiceCall(Voice voice);

    public abstract void endReceivedVoiceCall(Voice voice);

    public abstract void sentVideo(Video video);

    public abstract void receivedVideo(Video video);
}
