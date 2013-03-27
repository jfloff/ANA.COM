package pt.ist.anacom.domain;

import pt.ist.anacom.shared.data.AnacomData;

public class SMSIn extends SMSIn_Base {

    public SMSIn(String sourcePhoneNumber, String message, Phone phoneReceived) {
        super();
        setPhoneReceived(phoneReceived);
        setSourcePhoneNumber(sourcePhoneNumber);
        setMessage(message);
    }

    public boolean equals(Communication communication) {
        if (communication instanceof SMSIn) {
            SMSIn smsIn = (SMSIn) communication;
            return getSourcePhoneNumber() == smsIn.getSourcePhoneNumber() && getMessage().equals(smsIn.getMessage());
        }
        return false;
    }


    @Override
    public AnacomData.CommunicationType getType() {
        return AnacomData.CommunicationType.SMS_IN;
    }

    @Override
    public String toString() {
        return "SMS " + super.toString() + "\"" + getSourcePhoneNumber() + "\" and with message \"" + getMessage() + "\".";
    }

    @Override
    public int getLength() {
        return getMessage().length();
    }

}
