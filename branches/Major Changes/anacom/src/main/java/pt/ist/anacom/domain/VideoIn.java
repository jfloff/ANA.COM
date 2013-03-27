package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class VideoIn extends VideoIn_Base {

    public VideoIn() {
        super();
    }

    @Override
    public AnacomData.CommunicationType getType() {
        return AnacomData.CommunicationType.VIDEO_IN;
    }

    @Override
    public int getLength() {
        return getDuration();
    }

}
