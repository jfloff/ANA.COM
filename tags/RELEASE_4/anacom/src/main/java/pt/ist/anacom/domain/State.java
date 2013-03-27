package pt.ist.anacom.domain;

public abstract class State extends State_Base {

    public State() {
        super();
    }

    public String getStateName() {
        return this.getClass().getName().toString();
    }

    public boolean compareStateName(State state) {
        return this.getStateName().equals(state.getStateName());
    }

    public abstract void sentSMS(SMS sms);

    public abstract void receivedSMS(SMS sms);

    public abstract void sentVoice(Voice voice);

    public abstract void receivedVoice(Voice voice);

    public abstract void sentVideo(Video video);

    public abstract void receivedVideo(Video video);
}
