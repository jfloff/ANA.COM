package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class CommunicationDurationDto extends CommunicationDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private int duration;

    public CommunicationDurationDto() {
    }

    public CommunicationDurationDto(String sourcePhoneNumber, String destinationPhoneNumber, int duration) {
        super(sourcePhoneNumber, destinationPhoneNumber);
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return super.toString() + " and duration [" + this.duration + "].";
    }



}
