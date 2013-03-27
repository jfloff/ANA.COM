package pt.ist.anacom.domain;

public class Communication extends Communication_Base {

    public Communication() {
        super();
    }

    @Override
    public String toString() {
        return "Communication from " + getNrSource() + " to " + getNrDest() + " costed " + getCost();
    }

}
