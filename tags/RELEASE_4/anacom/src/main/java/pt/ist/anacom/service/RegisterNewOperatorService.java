package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.fenixframework.FenixFramework;

public class RegisterNewOperatorService extends AnacomService {

    private OperatorDetailedDto operatorDto;

    public RegisterNewOperatorService(OperatorDetailedDto operatorDto) {
        this.operatorDto = operatorDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.addOperator(this.operatorDto.getOperatorPrefix(),
                           this.operatorDto.getOperatorName(),
                           this.operatorDto.getOperatorTax(),
                           this.operatorDto.getOperatorTaxVoice(),
                           this.operatorDto.getOperatorTaxSMS(),
                           this.operatorDto.getOperatorTaxVideo());
    }
}
