package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class VoiceCallDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String destinationPhoneNumber;
    private String sourcePhoneNumber;

    public VoiceCallDto() {
    }

    public VoiceCallDto(String sourcePhoneNumber, String destinationPhoneNumber) {
        this.sourcePhoneNumber = sourcePhoneNumber;
        this.destinationPhoneNumber = destinationPhoneNumber;
    }

    public String getDestinationPhoneNumber() {
        return this.destinationPhoneNumber;
    }

    public String getSourcePhoneNumber() {
        return this.sourcePhoneNumber;
    }

    @Override
    public String toString() {
        return "From :" + destinationPhoneNumber + " To:" + sourcePhoneNumber + ".";
    }



}
