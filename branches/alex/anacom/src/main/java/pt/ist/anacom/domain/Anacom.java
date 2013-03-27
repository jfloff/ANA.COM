package pt.ist.anacom.domain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneAndOperatorPrefixDoNotMatchException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;
import pt.ist.shared.SecurityData;

public class Anacom extends Anacom_Base {

    /*
     * ------------------- Anacom Class -------------------
     */

    public Anacom() {
        super();
        this.setReplicaVersion(1);
    }

    /**
     * Add Operator Keys
     * 
     * @param privKey
     * @param pubKey
     */
    public void addKeys(PrivateKey privKey, PublicKey pubKey) {
        this.setPublicKey(SecurityData.encode64(pubKey.getEncoded()));
        this.setPrivateKey(SecurityData.encode64(privKey.getEncoded()));
    }

    public String getPhonePrefixByNumber(String number) {

        return number.substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    /*
     * ------------------- Operator Methods -------------------
     */

    public void addOperator(String operatorPrefix, String operatorName, int tax, int taxVoice, int taxSMS, int taxVideo, int taxBonus) {

        Operator operator = new Operator(operatorPrefix, operatorName, tax, taxVoice, taxSMS, taxVideo, taxBonus);

        if (hasOperator(operator))
            throw new OperatorPrefixAlreadyExistsException(operatorPrefix);

        super.addOperator(operator);
    }

    @Override
    public boolean hasOperator(Operator operator) {
        for (Operator aux : this.getOperatorSet())
            if (operator.conflicts(aux))
                return true;

        return false;
    }

    public Operator hasOperatorByPrefix(String prefix) {
        for (Operator operator : this.getOperator())
            if (operator.getPrefix().equals(prefix))
                return operator;

        return null;
    }

    /**
     * Return the operator responsible by a specific prefix
     * 
     * @param prefix The prefix of the operator
     * @return The operator responsible
     */
    public Operator getOperatorByPrefix(String prefix) {
        Operator result = hasOperatorByPrefix(prefix);

        if (result == null)
            throw new OperatorPrefixDoesNotExistException(prefix);

        return result;
    }

    /*
     * ------------------- Phones Methods -------------------
     */

    /**
     * Add a phone to the correct operator responsible by his prefix
     * 
     * @param phoneNumber The phone object to be added
     */
    public void addPhone(String operatorPrefix, String phoneNumber, AnacomData.PhoneType phoneGen) {

        Operator operator = this.getOperatorByPrefix(operatorPrefix);

        String phonePrefix = getPhonePrefixByNumber(phoneNumber);

        if (!(operator.getPrefix().equals(phonePrefix)))
            throw new PhoneAndOperatorPrefixDoNotMatchException(operator.getPrefix(), phonePrefix);

        if (phoneGen == AnacomData.PhoneType.GEN2) {
            Phone phone = new OldGenPhone(phoneNumber);
            operator.addPhone(phone);
            phone.setOperator(operator);
        } else if (phoneGen == AnacomData.PhoneType.GEN3) {
            Phone phone = new NewGenPhone(phoneNumber);
            operator.addPhone(phone);
            phone.setOperator(operator);
        }
    }

    public Phone getPhone(String number) {

        Operator operator = getOperatorByPrefix(getPhonePrefixByNumber(number));
        return operator.getPhoneByNumber(number);
    }

    public void removePhone(String phoneNumber) {

        Phone phone = this.getPhone(phoneNumber);
        Operator operator = this.getOperatorByPrefix(getPhonePrefixByNumber(phoneNumber));
        operator.removePhone(phone);
    }

    public void increasePhoneBalance(String number, int balance) {
        this.getPhone(number).increaseBalanceBy(balance);
    }

    public int getPhoneBalance(String number) {
        return this.getPhone(number).getBalance();
    }

    public List<SMS> getSMSPhoneReceivedList(String phoneNumber) {
        return this.getPhone(phoneNumber).getReceivedSMS();
    }

    public List<Phone> getBalanceAndPhoneList(String operatorPrefix) {
        return this.getOperatorByPrefix(operatorPrefix).getPhoneList();
    }

    public AnacomData.State getPhoneState(String phoneNumber) {
        return this.getPhone(phoneNumber).getStateType();
    }

    public void setPhoneState(String phoneNumber, AnacomData.State state) {
        this.getPhone(phoneNumber).setState(state);
    }

    public void receiveSMS(String sourcePhoneNumber, String destinationPhoneNumber, String message) {
        Phone receiverPhone = this.getPhone(destinationPhoneNumber);
        SMS sms = new SMS(sourcePhoneNumber, destinationPhoneNumber, message);
        receiverPhone.addReceivedSMS(sms);
    }

    public boolean sameOperator(String sourceNumber, String destNumber) {

        String srcPrefix = getPhonePrefixByNumber(sourceNumber);
        String destPrefix = getPhonePrefixByNumber(destNumber);

        return srcPrefix.equals(destPrefix);
    }

    public void sendSMS(String sourceNumber, String destinationNumber, String message) {

        if (message == null)
            throw new SMSInvalidTextException(sourceNumber, destinationNumber);

        Phone senderPhone = this.getPhone(sourceNumber);

        boolean sameOperator = sameOperator(sourceNumber, destinationNumber);
        int smsCost = senderPhone.getOperator().getPlan().calcCostSMS(message, sameOperator);
        senderPhone.checkCostAndBalance(smsCost);
        senderPhone.decreaseBalanceBy(smsCost);

        SMS sms = new SMS(sourceNumber, destinationNumber, message, smsCost);
        senderPhone.addSentSMS(sms);
    }

    public AnacomData.CommunicationType getCommunicationType(Communication communication) {

        return communication.getType();
    }

    public int getCommunicationLength(Communication communication, AnacomData.CommunicationType type) {

        return communication.getLength();
    }

    public Communication getPhoneLastCommunicationMade(String phoneNumber) {
        return this.getPhone(phoneNumber).getPhoneLastMadeCommunication();
    }

    // Voice Call related processes

    public void startSourceVoiceCall(String sourcePhoneNumber, String destinationPhoneNumber) {
        Phone sourcePhone = getPhone(sourcePhoneNumber);

        /* MUDAR */
        sourcePhone.checkNegativeBalance();
        // Checks if its possible to make call, sets state, and sets active call
        sourcePhone.startSentVoiceCall(new Voice(sourcePhoneNumber, destinationPhoneNumber));
    }

    public void startDestinationVoiceCall(String sourcePhoneNumber, String destinationPhoneNumber) {
        Phone destinationPhone = getPhone(destinationPhoneNumber);
        // Checks if its possible to make call, sets state, and sets active call
        destinationPhone.startReceivedVoiceCall(new Voice(sourcePhoneNumber, destinationPhoneNumber));
    }

    public void endSourceVoiceCall(String sourcePhoneNumber, String destinationPhoneNumber, int duration) {
        if (duration < 0)
            throw new InvalidDurationException(sourcePhoneNumber, destinationPhoneNumber);

        Phone sourcePhone = getPhone(sourcePhoneNumber);

        Voice activeCommunication = (Voice) sourcePhone.checkActiveCommunication(sourcePhoneNumber,
                                                                                 destinationPhoneNumber,
                                                                                 AnacomData.CommunicationType.VOICE);

        boolean sameOperator = sameOperator(sourcePhoneNumber, destinationPhoneNumber);
        int cost = sourcePhone.getOperator().getPlan().calcCostVoice(duration, sameOperator);

        sourcePhone.decreaseBalanceBy(cost);

        activeCommunication.setCost(cost);
        activeCommunication.setDuration(duration);

        sourcePhone.endSentVoiceCall(activeCommunication);
    }

    public void endDestinationVoiceCall(String sourcePhoneNumber, String destinationPhoneNumber, int duration) {

        if (duration < 0)
            throw new InvalidDurationException(sourcePhoneNumber, destinationPhoneNumber);

        Phone destinationPhone = getPhone(destinationPhoneNumber);

        Voice activeCommunication = (Voice) destinationPhone.checkActiveCommunication(sourcePhoneNumber,
                                                                                      destinationPhoneNumber,
                                                                                      AnacomData.CommunicationType.VOICE);
        activeCommunication.setDuration(duration);

        destinationPhone.endReceivedVoiceCall(activeCommunication);
    }
}
