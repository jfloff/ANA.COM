package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class Video extends Video_Base {

    public Video(int duration) {
        super();
        super.setDuration(duration);
        super.setCost(0);
    }

    public Video(int duration, int cost) {
        super();
        super.setDuration(duration);
        super.setCost(cost);
    }

    @Override
    public AnacomData.CommunicationType getType() {

        return AnacomData.CommunicationType.VIDEO;
    }

    @Override
    public int getLength() {
        return this.getDuration();
    }

    @Override
    public String toString() {
        return "Video duration : " + this.getDuration();
    }

}
