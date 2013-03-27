package pt.ist.anacom.domain;

import java.util.ArrayList;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.PhoneNumberWrongLengthException;
import pt.ist.anacom.shared.exception.UnsuportedOperationException;

public abstract class Phone extends Phone_Base {

    public Phone() {
        super();
        // super.setStateOFF(new StateOFF());
        // super.setStateON(new StateON());
        // super.setStateBUSY(new StateBUSY());
        // super.setStateSILENCE(new StateSILENCE());
        super.setState(new StateOFF());
        super.getState().setPhone(this);
    }

    public void setState(AnacomData.State state) {
        switch (state) {
        case OFF:
            super.setState(new StateOFF());
            break;
        case SILENCE:
            super.setState(new StateSILENCE());
            break;
        case ON:
            super.setState(new StateON());
            break;
        case BUSY:
            super.setState(new StateBUSY(this.getState()));
            break;
        }
    }

    public AnacomData.State getStateType() {

        return this.getState().getStateType();
    }

    @Override
    public void setPhoneNumber(String number) {
        if (!(number.length() == AnacomData.NUMBER_LENGTH))
            throw new PhoneNumberWrongLengthException(number);
        super.setPhoneNumber(number);
    }

    @Override
    public String toString() {
        return "N. Telemovel: " + this.getPhoneNumber() + " Saldo: " + this.getBalance() + " - " + getAllSMS();
    }

    /**
     * Get the Phone prefix.
     * 
     * @return Returns the Phone prefix.
     */
    public String getPrefix() {
        return this.getPhoneNumber().substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    public void checkNegativeBalance() {
        if (this.getBalance() <= 0)
            throw new InsuficientBalanceException(this.getPhoneNumber(), this.getBalance());
    }

    public void increaseBalanceBy(int valueToIncrease) {
        int balance = getBalance();

        if ((balance + valueToIncrease) > AnacomData.MAX_BALANCE)
            throw new BalanceLimitExceededException(this.getPhoneNumber(), this.getBalance());

        if (valueToIncrease < 0)
            throw new NegativeBalanceValueException(this.getPhoneNumber(), valueToIncrease);

        // Bonus formula
        Operator op = this.getOperator();
        int taxBonus = op.getPlan().getTaxBonus(); //
        balance += (valueToIncrease * (101 + taxBonus)) / 100; // saldo += valueToincrease
                                                               // * (1+bonus%)

        // if balance exceed balance limit set balance with maximum amount
        if (balance > AnacomData.MAX_BALANCE)
            setBalance(AnacomData.MAX_BALANCE);
        else
            setBalance(balance);
    }

    public void checkCostAndBalance(int cost) {

        int balance = this.getBalance();

        if (cost > balance)
            throw new InsuficientBalanceException(this.getPhoneNumber(), balance);
    }

    public void decreaseBalanceBy(int valueToDecrease) {
        int balance = getBalance();

        if (valueToDecrease < 0)
            throw new NegativeBalanceValueException(this.getPhoneNumber(), valueToDecrease);

        checkNegativeBalance();

        setBalance(balance - valueToDecrease);
    }

    public Communication getPhoneLastMadeCommunication() {

        Communication lastCommunication = this.getLastMadeCommunication();

        // Havent made any communications
        if (lastCommunication == null)
            throw new NoCommunicationsMadeYetException(this.getPhoneNumber());

        return lastCommunication;
    }

    /*
     * ====================================================================
     * =============================SMS Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent SMS.
     * 
     * @param sms SMS to be added
     */
    public void addSentSMS(SMS sms) {
        this.getState().sentSMS(sms);
    }

    /**
     * Store a received SMS.
     * 
     * @param sms SMS to be added
     */
    public void addReceivedSMS(SMS sms) {
        this.getState().receivedSMS(sms);
    }

    /**
     * Gets all the messages this phone has.
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMS> getAllSMS() {
        ArrayList<SMS> allSMS = new ArrayList<SMS>();

        allSMS.addAll(getSentSMS());
        allSMS.addAll(getReceivedSMS());

        return allSMS;
    }

    /**
     * Gets all the messages this phone has. (Does not callgetAllSMS because it had to
     * search in the lists twice)
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMS> getReceivedSMS() {
        ArrayList<SMS> receivedSMS = new ArrayList<SMS>();
        for (Communication communication : getCommunicationReceived())
            if (communication instanceof SMS) {
                receivedSMS.add((SMS) communication);
            }
        return receivedSMS;
    }

    /**
     * Gets all the messages this phone has. (Does not call getAllSMS because it had to
     * search in the lists twice).
     * 
     * @return an ArrayList of all SMS's.
     */
    public ArrayList<SMS> getSentSMS() {
        ArrayList<SMS> sentSMS = new ArrayList<SMS>();
        for (Communication communication : getCommunicationSent())
            if (communication instanceof SMS) {
                sentSMS.add((SMS) communication);
            }
        return sentSMS;
    }

    /*
     * ====================================================================
     * ===========================Voice Operations=========================
     * ====================================================================
     */

    public void startReceivedVoiceCall(Voice voice) {
        this.getState().startReceivedVoiceCall(voice);
    }

    public void startSentVoiceCall(Voice voice) {
        this.getState().startSentVoiceCall(voice);
    }

    public Communication checkActiveCommunication(String sourcePhoneNumber, String destinationPhoneNumber, AnacomData.CommunicationType type) {
        Communication activeCommunication = this.getActiveCommunication();

        if (activeCommunication == null)
            throw new NoActiveCommunicationException(sourcePhoneNumber, destinationPhoneNumber);

        if (!(sourcePhoneNumber.equals(activeCommunication.getSourcePhoneNumber()) && destinationPhoneNumber.equals(activeCommunication.getDestinationPhoneNumber())))
            throw new NoActiveCommunicationException(sourcePhoneNumber, destinationPhoneNumber);

        if (activeCommunication.getType() != type)
            throw new NoActiveCommunicationException(sourcePhoneNumber, destinationPhoneNumber);

        this.removeActiveCommunication();

        return activeCommunication;
    }

    public void endReceivedVoiceCall(Voice voice) {
        this.getState().endReceivedVoiceCall(voice);
    }

    public void endSentVoiceCall(Voice voice) {
        this.getState().endSentVoiceCall(voice);
    }

    /**
     * Gets all the voice calls this phone has. (Does not call allVoice because it had to
     * search in the lists twice)
     * 
     * @return an ArrayList of all the voice calls received.
     */
    public ArrayList<Voice> getAllVoiceCalls() {
        ArrayList<Voice> allVoiceCalls = new ArrayList<Voice>();
        allVoiceCalls.addAll(getSentVoiceCalls());
        allVoiceCalls.addAll(getReceivedVoiceCalls());

        return allVoiceCalls;
    }

    /**
     * Gets all the received voice calls this phone has. (Does not call getAllVoiceCalls
     * because it had to search in the lists twice)
     * 
     * @return an ArrayList of all the voice calls received.
     */
    public ArrayList<Voice> getReceivedVoiceCalls() {
        ArrayList<Voice> receivedVoiceCalls = new ArrayList<Voice>();
        for (Communication communication : getCommunicationReceived())
            if (communication instanceof Voice) {
                receivedVoiceCalls.add((Voice) communication);
            }
        return receivedVoiceCalls;
    }

    /**
     * Gets all the voice calls this phone has. (Does not call getAllVoiceCalls because it
     * had to search in the lists twice).
     * 
     * @return an ArrayList of all the voice calls made.
     */
    public ArrayList<Voice> getSentVoiceCalls() {
        ArrayList<Voice> sentVoiceCalls = new ArrayList<Voice>();
        for (Communication communication : getCommunicationSent())
            if (communication instanceof Voice) {
                sentVoiceCalls.add((Voice) communication);
            }
        return sentVoiceCalls;
    }

    /*
     * ====================================================================
     * ===========================Video Operations=========================
     * ====================================================================
     */

    /**
     * Store a sent video call.
     * 
     * @param video Video call to be added
     */
    public abstract void addSentVideo(Video video) throws UnsuportedOperationException;

    /**
     * Store a received video call.
     * 
     * @param video Video call to be added
     */
    public abstract void addReceivedVideo(Video video) throws UnsuportedOperationException;



}
