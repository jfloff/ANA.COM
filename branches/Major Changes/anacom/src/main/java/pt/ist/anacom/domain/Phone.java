package pt.ist.anacom.domain;

import java.util.ArrayList;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.CommunicationListIsEmptyException;
import pt.ist.anacom.shared.exception.InvalidBalanceOperationException;
import pt.ist.anacom.shared.exception.PhoneNumberIncorrectException;
import pt.ist.anacom.shared.exception.UnsuportedOperationException;

public abstract class Phone extends Phone_Base {

    public Phone() {
        super();
        super.setStateOFF(new StateOFF());
        super.setStateON(new StateON());
        super.setStateBUSY(new StateBUSY());
        super.setStateSILENCE(new StateSILENCE());
        super.setState(getStateOFF());
        super.getState().setPhoneState(this);
    }

    public void setState(AnacomData.State state) {

        switch (state) {
        case OFF:
            super.setState(getStateOFF());
            break;
        case SILENCE:
            super.setState(getStateSILENCE());
            break;
        case ON:
            super.setState(getStateON());
            break;
        case BUSY:
            super.setState(getStateBUSY());
            break;
        }
    }

    public AnacomData.State getStateType() {

        return this.getState().getStateType();
    }

    @Override
    public void setPhoneNumber(String number) {
        if (!(number.length() == AnacomData.NUMBER_LENGTH))
            throw new PhoneNumberIncorrectException(number);
        super.setPhoneNumber(number);
    }

    @Override
    public String toString() {
        return "N. Telemovel: " + this.getPhoneNumber() + " Saldo: " + this.getBalance() + " - " + getReceivedSMS() + "|" + getSentSMS();
    }

    /**
     * Get the Phone prefix.
     * 
     * @return Returns the Phone prefix.
     */
    public String getPrefix() {
        return this.getPhoneNumber().substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    public void increaseBalanceBy(int valueToIncrease) {
        int balance = getBalance();
        if (valueToIncrease < 0 || (balance + valueToIncrease) > 10000)
            throw new InvalidBalanceOperationException(this.getPhoneNumber(), this.getBalance());

        // Bonus formula
        Operator op = this.getOperator();
        int taxBonus = op.getPlan().getTaxBonus(); //
        balance += (valueToIncrease * ((101 + taxBonus) / 100)); // saldo +=
                                                                 // valueToincrease *
                                                                 // (1+bonus%)

        if (balance > 10000)
            throw new InvalidBalanceOperationException(this.getPhoneNumber(), this.getBalance());

        setBalance(balance); //
    }

    public void decreaseBalanceBy(int valueToDecrease) {
        int balance = getBalance();
        if (balance < valueToDecrease || valueToDecrease < 0)
            throw new InvalidBalanceOperationException(this.getPhoneNumber(), this.getBalance());
        setBalance(balance - valueToDecrease);
    }

    public Communication getLastCommunicationMade() {
        if (getCommunicationSent().isEmpty())
            throw new CommunicationListIsEmptyException(this.getPhoneNumber());

        return getCommunicationSent().get(getCommunicationSentCount() - 1);
    }

    /*
     * ==================================================================== =============================SMS Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent SMS.
     * 
     * @param smsOut SMS to be added
     */
    public void addSentSMS(SMSOut smsOut) {
        this.getState().sentSMS(smsOut);
    }

    /**
     * Store a received SMS.
     * 
     * @param smsIn SMS to be added
     */
    public void addReceivedSMS(SMSIn smsIn) {
        this.getState().receivedSMS(smsIn);
    }


    /**
     * Gets all the messages this phone has. (Does not callgetAllSMS because it had to search in the lists twice)
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMSIn> getReceivedSMS() {
        ArrayList<SMSIn> receivedSMS = new ArrayList<SMSIn>();
        for (Communication communication : getCommunicationReceived())
            if (communication instanceof SMSIn) {
                receivedSMS.add((SMSIn) communication);
            }
        return receivedSMS;
    }

    /**
     * Gets all the messages this phone has. (Does not call getAllSMS because it had to search in the lists twice).
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMSOut> getSentSMS() {
        ArrayList<SMSOut> sentSMS = new ArrayList<SMSOut>();
        for (Communication communication : getCommunicationSent())
            if (communication instanceof SMSOut) {
                sentSMS.add((SMSOut) communication);
            }
        return sentSMS;
    }

    // ====================================================================
    // ===========================Voice Operations=========================
    // ====================================================================

    public void startMadeVoiceCall() {
        getState().startSentVoiceCall();
    }

    public void startReceivedVoiceCall() {
        getState().startReceivedVoiceCall();
    }

    /**
     * @param cost
     * @param duration
     * @param destinationPhoneNumber
     */
    public void endMadeVoiceCall(String destinationPhoneNumber, int duration, int cost) {
        setState(AnacomData.State.ON);
        addCommunicationSent(new VoiceOut(destinationPhoneNumber, duration, cost, this));
    }

    public void endReceivedVoiceCall(String sourcePhoneNumber, int duration) {
        setState(AnacomData.State.ON);
        addCommunicationReceived(new VoiceIn(sourcePhoneNumber, duration, this));
    }

    /**
     * Gets all the received voice calls this phone has. (Does not call getAllVoiceCalls because it had to search in the lists twice)
     * 
     * @return an ArrayList of all the voice calls received.
     */
    public ArrayList<VoiceIn> getReceivedVoiceCalls() {
        ArrayList<VoiceIn> receivedVoiceCalls = new ArrayList<VoiceIn>();
        for (Communication communication : getCommunicationReceived())
            if (communication instanceof VoiceIn) {
                receivedVoiceCalls.add((VoiceIn) communication);
            }
        return receivedVoiceCalls;
    }

    /**
     * Gets all the voice calls this phone has. (Does not call getAllVoiceCalls because it had to search in the lists twice).
     * 
     * @return an ArrayList of all the voice calls made.
     */
    public ArrayList<VoiceOut> getSentVoiceCalls() {
        ArrayList<VoiceOut> sentVoiceCalls = new ArrayList<VoiceOut>();
        for (Communication communication : getCommunicationSent())
            if (communication instanceof VoiceOut) {
                sentVoiceCalls.add((VoiceOut) communication);
            }
        return sentVoiceCalls;
    }

    /*
     * ==================================================================== ===========================Video Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent video call.
     * 
     * @param video Video call to be added
     */
    public abstract void addSentVideo(VideoIn video) throws UnsuportedOperationException;

    /**
     * Store a received video call.
     * 
     * @param video Video call to be added
     */
    public abstract void addReceivedVideo(VideoOut video) throws UnsuportedOperationException;



}
