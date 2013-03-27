package pt.ist.anacom.service.dto;

public class OperatorDto {

	private String prefix;
	private String name;
	private int tax, taxVoice, taxSMS, taxVideo;

	public OperatorDto(String prefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {

		this.prefix = prefix;
		this.name = name;
		this.tax = tax;
		this.taxVoice = taxVoice;
		this.taxVideo = taxVideo;
		this.taxSMS = taxSMS;
	}

	public String getPrefix() {
		return prefix;
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
