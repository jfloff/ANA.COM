package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessRegisterNewOperatorService extends AnacomService {

	private OperatorDto operatorDto;

	public ProcessRegisterNewOperatorService(OperatorDto operatorDto) {
		this.operatorDto = operatorDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		anacom.addOperator(new Operator(this.operatorDto.getOperatorPrefix(), this.operatorDto.getName(), this.operatorDto.getTax(), this.operatorDto.getTaxVoice(),
				this.operatorDto.getTaxSMS(), this.operatorDto.getTaxVideo()));
	}
}
