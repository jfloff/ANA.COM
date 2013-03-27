package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class Voice extends Voice_Base {

    public Voice(int duration) {
        super();
        super.setDuration(duration);
        super.setCost(0);
    }

    public Voice(String sourcePhoneNumber, String destinationPhoneNumber, int duration, int cost) {
        super();
        super.setSourcePhoneNumber(sourcePhoneNumber);
        super.setDestinationPhoneNumber(destinationPhoneNumber);
        super.setDuration(duration);
        super.setCost(cost);
    }

    @Override
    public String toString() {
        return super.toString() + " for " + this.getDuration();
    }

    @Override
    public AnacomData.CommunicationType getType() {

        return AnacomData.CommunicationType.VOICE;
    }

    @Override
    public int getLength() {
        return this.getDuration();
    }
}
