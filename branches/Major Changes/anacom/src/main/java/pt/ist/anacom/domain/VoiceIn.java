package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class VoiceIn extends VoiceIn_Base {

    public VoiceIn(String sourcePhoneNumber, int duration, Phone phoneReceived) {
        super();
        setSourcePhoneNumber(sourcePhoneNumber);
        setDuration(duration);
        setPhoneReceived(phoneReceived);
    }

    @Override
    public AnacomData.CommunicationType getType() {
        return AnacomData.CommunicationType.VOICE_IN;
    }

    @Override
    public String toString() {
        return "Voice " + super.toString() + "\"" + getSourcePhoneNumber() + "\" and with duration \"" + getDuration() + "\".";
    }

    @Override
    public int getLength() {
        return getDuration();
    }
}
