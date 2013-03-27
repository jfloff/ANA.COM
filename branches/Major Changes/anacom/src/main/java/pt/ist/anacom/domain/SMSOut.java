package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class SMSOut extends SMSOut_Base {

    public SMSOut(String destinationPhoneNumber, String message, int cost, Phone phoneSent) {
        super();
        setPhoneSent(phoneSent);
        setDestinationPhoneNumber(destinationPhoneNumber);
        setMessage(message);
        setCost(cost);
    }

    public boolean equals(Communication communication) {
        if (communication instanceof SMSOut) {
            SMSOut smsOut = (SMSOut) communication;
            return getDestinationPhoneNumber() == smsOut.getDestinationPhoneNumber() && getCost() == smsOut.getCost()
                    && getMessage().equals(smsOut.getMessage());
        }
        return false;
    }

    @Override
    public AnacomData.CommunicationType getType() {
        return AnacomData.CommunicationType.SMS_OUT;
    }

    @Override
    public String toString() {
        return "SMS " + super.toString() + "\"" + getDestinationPhoneNumber() + "\" with message \"" + getMessage() + "\"  and with cost "
                + getCost() + ".";
    }

    @Override
    public int getLength() {
        return getMessage().length();
    }
}
