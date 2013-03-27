package pt.ist.anacom.domain;

public abstract class CommunicationOut extends CommunicationOut_Base {

    public CommunicationOut() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + " to";
    }

}
