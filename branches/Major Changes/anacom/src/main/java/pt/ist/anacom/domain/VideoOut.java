package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class VideoOut extends VideoOut_Base {

    public VideoOut() {
        super();
    }

    @Override
    public AnacomData.CommunicationType getType() {
        return AnacomData.CommunicationType.VIDEO_OUT;
    }

    @Override
    public int getLength() {
        return getDuration();
    }

}
