package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.exception.InvalidTaxException;
import pt.ist.anacom.shared.exception.OperatorNameAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixWrongLengthException;
import pt.ist.fenixframework.FenixFramework;

public class RegisterNewOperatorService extends AnacomService {

    private OperatorDetailedDto operatorDto;

    public RegisterNewOperatorService(OperatorDetailedDto operatorDto) {
        this.operatorDto = operatorDto;
    }

    @Override
    public final void dispatch() throws OperatorPrefixAlreadyExistsException,
            OperatorPrefixWrongLengthException,
            OperatorNullNameException,
            InvalidTaxException,
            OperatorNameAlreadyExistsException {

        Anacom anacom = FenixFramework.getRoot();
        anacom.addOperator(this.operatorDto.getOperatorPrefix(),
                           this.operatorDto.getOperatorName(),
                           this.operatorDto.getOperatorTax(),
                           this.operatorDto.getOperatorTaxVoice(),
                           this.operatorDto.getOperatorTaxSMS(),
                           this.operatorDto.getOperatorTaxVideo(),
                           this.operatorDto.getOperatorTaxBonus());
    }
}
