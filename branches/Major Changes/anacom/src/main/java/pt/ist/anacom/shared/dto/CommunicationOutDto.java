package pt.ist.anacom.shared.dto;

import java.io.Serializable;

import pt.ist.anacom.shared.data.AnacomData;

public class CommunicationOutDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int communicationType;
    private String destinationPhoneNumber;
    private int cost;
    private int length;

    public CommunicationOutDto() {

    }

    public CommunicationOutDto(AnacomData.CommunicationType communicationType, String destinationPhoneNumber, int cost, int length) {
        this.communicationType = communicationType.ordinal();
        this.destinationPhoneNumber = destinationPhoneNumber;
        this.cost = cost;
        this.length = length;
    }

    public String getDestinationPhoneNumber() {
        return destinationPhoneNumber;
    }

    public AnacomData.CommunicationType getCommunicationType() {
        return AnacomData.CommunicationType.values()[this.communicationType];
    }

    public int getCost() {
        return this.cost;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Last communication made was a " + this.getCommunicationType().name() + " to: " + this.destinationPhoneNumber + " with cost of "
                + this.cost + " and length of " + this.length + ".";
    }
}
