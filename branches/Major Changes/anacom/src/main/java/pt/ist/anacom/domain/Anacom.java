package pt.ist.anacom.domain;

import java.util.List;

import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhonePrefixDoesNotMatchException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;

public class Anacom extends Anacom_Base {

    /*
     * ------------------- Anacom Class -------------------
     */

    public Anacom() {
        super();
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
            throw new PhonePrefixDoesNotMatchException(operator.getPrefix(), operator.getPrefix(), phoneNumber, phonePrefix);

        if (phoneGen == AnacomData.PhoneType.GEN2) {
            Phone phone = new OldGenPhone(phoneNumber);
            operator.addPhone(phone);
        }

        else if (phoneGen == AnacomData.PhoneType.GEN3) {
            Phone phone = new NewGenPhone(phoneNumber);
            operator.addPhone(phone);
        }
    }

    public Phone getPhone(String number) {

        Operator operator = getOperatorByPrefix(getPhonePrefixByNumber(number));
        return operator.getPhoneByNr(number);
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

    public List<SMSIn> getSMSPhoneReceivedList(String phoneNumber) {
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
        SMSIn smsIn = new SMSIn(sourcePhoneNumber, message, receiverPhone);
        receiverPhone.addReceivedSMS(smsIn);
    }

    public boolean sameOperator(String sourceNumber, String destNumber) {

        String srcPrefix = getPhonePrefixByNumber(sourceNumber);
        String destPrefix = getPhonePrefixByNumber(destNumber);

        return srcPrefix.equals(destPrefix);
    }

    public void sendSMS(String sourceNumber, String destinationNumber, String message) {
        if (message == null)
            throw new SMSInvalidTextException();
        Phone senderPhone = this.getPhone(sourceNumber);
        boolean sameOperator = sameOperator(sourceNumber, destinationNumber);
        int cost = senderPhone.getOperator().getPlan().calcCostSMS(message, sameOperator);
        if (cost > senderPhone.getBalance())
            throw new InsuficientBalanceException(senderPhone.getPhoneNumber(), senderPhone.getBalance());

        SMSOut smsOut = new SMSOut(destinationNumber, message, cost, senderPhone);
        senderPhone.addSentSMS(smsOut);
        senderPhone.decreaseBalanceBy(cost);
    }

    public AnacomData.CommunicationType getCommunicationType(Communication communication) {
        return communication.getType();
    }

    public void startSourceVoiceCall(String sourcePhoneNumber) {
        // Guardar estado anterior
        Phone sourcePhone = getPhone(sourcePhoneNumber);
        if (sourcePhone.getBalance() <= 0)
            throw new InsuficientBalanceException(sourcePhoneNumber, sourcePhone.getBalance());
        sourcePhone.startMadeVoiceCall(); // Checks if is possible to make call and sets state.
    }

    public void startDestinationVoiceCall(String destinationPhoneNumber) {
        // Guardar estado anterior
        Phone destinationPhone = getPhone(destinationPhoneNumber);
        destinationPhone.startReceivedVoiceCall();
    }

    public void endSourceVoiceCall(String sourcePhoneNumber, String destinationPhoneNumber, int duration) {
        Phone sourcePhone = getPhone(sourcePhoneNumber);
        boolean sameOperator = sameOperator(sourcePhoneNumber, destinationPhoneNumber);
        int cost = sourcePhone.getOperator().getPlan().calcCostVoice(duration, sameOperator);
        sourcePhone.decreaseBalanceBy(cost);
        sourcePhone.endMadeVoiceCall(destinationPhoneNumber, duration, cost);
    }

    public void endDestinationVoiceCall(String sourcePhoneNumber, String destinationPhoneNumber, int duration) {
        Phone destinationPhone = getPhone(destinationPhoneNumber);
        destinationPhone.endReceivedVoiceCall(sourcePhoneNumber, duration);
    }
}
