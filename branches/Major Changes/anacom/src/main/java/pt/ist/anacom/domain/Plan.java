package pt.ist.anacom.domain;

import pt.ist.anacom.shared.exception.InvalidTaxEception;


public class Plan extends Plan_Base {

    public Plan(int tax, int taxVoice, int taxSMS, int taxVideo, int taxBonus) {
        super();

        if (tax < 0 || taxBonus < 0)
            throw new InvalidTaxEception();

        setTax(tax);
        setCostVoice(taxVoice);
        setCostSMS(taxSMS);
        setCostVideo(taxVideo);
        setTaxBonus(taxBonus);
    }

    /**
     * Calculate the cost of a SMS communication.
     * 
     * @param SMSText The SMS text
     * @return The cost of the SMS
     */
    public int calcCostSMS(String SMSText, boolean sameOperator) {

        int length = SMSText.length() - 1;
        int cost = ((length / 100) + 1) * getCostSMS();

        if (!sameOperator)
            cost = (cost * (100 + getTax())) / 100;

        return cost;
    }

    /**
     * Calculate the Voice cost.
     * 
     * @param sameOperator
     * @param SMSText The SMS text
     * @return The cost of the SMS
     */
    public int calcCostVoice(int duration, boolean sameOperator) {

        int cost = getCostVoice() * duration;

        if (!sameOperator)
            cost = (cost * (100 + getTax())) / 100;

        return cost;

    }

    /**
     * Calculate the SMS cost.
     * 
     * @param SMSText The SMS text
     * @return The cost of the SMS
     */
    public int calcCostVideo(int duration) {

        return 0;
    }

}
