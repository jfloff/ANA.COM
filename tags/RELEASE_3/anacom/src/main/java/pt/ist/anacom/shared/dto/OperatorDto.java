package pt.ist.anacom.shared.dto;

public class OperatorDto {

	private String operatorPrefix;
	private String name;
	private int tax, taxVoice, taxSMS, taxVideo;

	public OperatorDto(String operatorPrefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {

		this.operatorPrefix = operatorPrefix;
		this.name = name;
		this.tax = tax;
		this.taxVoice = taxVoice;
		this.taxVideo = taxVideo;
		this.taxSMS = taxSMS;
	}

	public String getOperatorPrefix() {
		return operatorPrefix;
	}

	public String getName() {
		return name;
	}

	public int getTaxVoice() {
		return taxVoice;
	}

	public int getTaxSMS() {
		return taxSMS;
	}

	public int getTaxVideo() {
		return taxVideo;
	}

	public int getTax() {
		return tax;
	}

}
