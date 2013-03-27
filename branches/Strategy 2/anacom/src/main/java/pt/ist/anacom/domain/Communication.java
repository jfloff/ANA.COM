package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public abstract class Communication extends Communication_Base {

    public Communication() {
        super();
    }

    public abstract AnacomData.CommunicationType getType();

    public abstract int getLength();

    @Override
    public String toString() {
        return "Communication from " + getSourcePhoneNumber() + " to " + getDestinationPhoneNumber() + " costed " + getCost();
    }

}
