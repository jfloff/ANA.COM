package pt.ist.anacom.domain;


public abstract class CommunicationIn extends CommunicationIn_Base {

    public CommunicationIn() {
        super();
    }

    @Override
    public String toString() {
        return super.toString() + " from";
    }

}
