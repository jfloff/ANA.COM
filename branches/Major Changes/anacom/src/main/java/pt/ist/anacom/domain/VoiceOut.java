package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class VoiceOut extends VoiceOut_Base {

    public VoiceOut(String destinationPhoneNumber, int duration, int cost, Phone phoneSent) {
        super();
        setDestinationPhoneNumber(destinationPhoneNumber);
        setDuration(duration);
        setCost(cost);
        setPhoneSent(phoneSent);
    }

    @Override
    public AnacomData.CommunicationType getType() {
        return AnacomData.CommunicationType.VOICE_OUT;
    }

    @Override
    public String toString() {
        return "Voice " + super.toString() + "\"" + getDestinationPhoneNumber() + "\" with message \"" + getDuration() + "\"  and with cost "
                + getCost() + ".";
    }

    @Override
    public int getLength() {
        return getDuration();
    }

}
