package pt.ist.anacom.domain;

public class Video extends Video_Base {

	public Video() {
		super();
	}

	@Override
	public String toString() {
		return "Video duration : " + getDuration();
	}

}
