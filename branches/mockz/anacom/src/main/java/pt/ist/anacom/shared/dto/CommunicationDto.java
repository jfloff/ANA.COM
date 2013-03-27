package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class CommunicationDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sourcePhoneNumber;
    private String destinationPhoneNumber;

    public CommunicationDto() {
    }

    public CommunicationDto(String sourcePhoneNumber, String destinationPhoneNumber) {
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
        return "From [" + this.sourcePhoneNumber + "] To [" + this.destinationPhoneNumber + "].";
    }



}
