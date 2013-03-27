package pt.ist.anacom.shared.dto;

import java.io.Serializable;

import pt.ist.anacom.shared.data.AnacomData;


public class PhoneDetailedDto extends PhoneSimpleDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int phoneGen;
    private String operatorPrefix;

    public PhoneDetailedDto() {
    }

    public PhoneDetailedDto(String phoneNumber) {
        super(phoneNumber);
    }

    public PhoneDetailedDto(String operatorPrefix, String phoneNumber, AnacomData.PhoneType phoneGen) {
        super(phoneNumber);
        this.phoneGen = phoneGen.ordinal();
        this.operatorPrefix = operatorPrefix;
    }

    public AnacomData.PhoneType getPhoneGen() {
        return AnacomData.convertIntToPhoneTypeEnum(this.phoneGen);
    }

    @Override
    public String getOperatorPrefix() {
        return operatorPrefix;
    }

    @Override
    public String toString() {
        return "[Phone: " + super.getPhoneNumber() + ", Op:" + this.operatorPrefix + ", phoneGen:" + this.getPhoneGen() + "]";
    }
}
