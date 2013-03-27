package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class VoiceEndCallDto extends VoiceCallDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private int duration;

	public VoiceEndCallDto() {  }
	
	public VoiceEndCallDto(String sourcePhoneNumber, String destinationPhoneNumber, int duration){
		super(sourcePhoneNumber, destinationPhoneNumber);
		this.duration = duration;
	}

	public int getDuration() {
		return this.duration;
	}

	@Override
	public String toString() {
		return super.toString() + " and duration " + this.duration;
	}
	
	
	
}