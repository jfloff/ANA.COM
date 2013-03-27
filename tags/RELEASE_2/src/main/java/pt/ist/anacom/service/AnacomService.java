package pt.ist.anacom.service;

import pt.ist.anacom.exception.AnacomException;
import jvstm.Atomic;

public abstract class AnacomService {

	@Atomic
	public void execute() throws AnacomException {
		dispatch();
	}

	public abstract void dispatch() throws AnacomException;
}
