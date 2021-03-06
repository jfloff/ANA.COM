package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class OperatorDetailedDto extends OperatorSimpleDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String name;
    private int tax, taxVoice, taxSMS, taxVideo;

    public OperatorDetailedDto() {

    }

    public OperatorDetailedDto(String operatorPrefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {

        super(operatorPrefix);
        this.name = name;
        this.tax = tax;
        this.taxVoice = taxVoice;
        this.taxVideo = taxVideo;
        this.taxSMS = taxSMS;
    }

    public String getOperatorName() {
        return name;
    }

    public int getOperatorTaxVoice() {
        return taxVoice;
    }

    public int getOperatorTaxSMS() {
        return taxSMS;
    }

    public int getOperatorTaxVideo() {
        return taxVideo;
    }

    public int getOperatorTax() {
        return tax;
    }

    @Override
    public String toString() {
        return "[Prefix: " + super.getOperatorPrefix() + ", Name: " + this.name + ", Tax Voice: " + this.taxVoice + ", Tax Video: " + this.taxVideo
                + ", Tax SMS: " + this.taxSMS + ", Tax: " + this.tax + "]";
    }
}
