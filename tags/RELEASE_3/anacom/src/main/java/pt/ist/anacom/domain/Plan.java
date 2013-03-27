package pt.ist.anacom.domain;

import org.joda.time.DateTime;

public class Plan extends Plan_Base {

	public Plan(int tax, int taxVoice, int taxSMS, int taxVideo) {
		super();

		setTax(tax);
		setTaxVoice(taxVoice);
		setTaxSMS(taxSMS);
		setTaxVideo(taxVideo);
	}

	/**
	 * Calculate the cost of a SMS communication.
	 * 
	 * @param SMSText
	 *            The SMS text
	 * @return The cost of the SMS
	 */
	public int calcCostSMS(String SMSText, boolean sameOperator) {
		// N�o sei se a conta est� bem feita.
		int taxSMS = 100 + getTaxSMS();
		int length = SMSText.length() - 1;
		int cost = ((length / 100 + 1) * taxSMS) / 100;

		if (!sameOperator)
			cost *= getTax();

		return cost;
	}

	/**
	 * Calculate the Voice cost.
	 * 
	 * @param SMSText
	 *            The SMS text
	 * @return The cost of the SMS
	 */
	public int calcCostVoice(DateTime time) {

		return 0;
	}

	/**
	 * Calculate the SMS cost.
	 * 
	 * @param SMSText
	 *            The SMS text
	 * @return The cost of the SMS
	 */
	public int calcCostVideo(DateTime time) {

		return 0;
	}

}
