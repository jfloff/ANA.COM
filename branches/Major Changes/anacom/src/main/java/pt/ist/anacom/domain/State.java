package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public abstract class State extends State_Base {

    public State() {
        super();
    }

    public abstract AnacomData.State getStateType();

    // Comunicacoes

    public abstract void sentSMS(SMSOut smsOut);

    public abstract void receivedSMS(SMSIn smsIn);

    public abstract void startSentVoiceCall();

    public abstract void startReceivedVoiceCall();


    // To remove

    public abstract void sentVideo(VideoOut videoOut);

    public abstract void receivedVideo(VideoIn videoIn);

}
