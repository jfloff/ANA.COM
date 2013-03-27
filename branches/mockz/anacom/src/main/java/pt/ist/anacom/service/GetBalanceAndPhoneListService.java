package pt.ist.anacom.service;

import java.util.List;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class GetBalanceAndPhoneListService extends AnacomService {

    private OperatorSimpleDto operatorDto;
    private BalanceAndPhoneListDto result;

    public GetBalanceAndPhoneListService(OperatorSimpleDto operatorDto) {
        this.operatorDto = operatorDto;
    }

    @Override
    public final void dispatch() throws OperatorPrefixDoesNotExistException {
        Anacom anacom = FenixFramework.getRoot();
        List<Phone> phoneList = anacom.getBalanceAndPhoneList(operatorDto.getOperatorPrefix());

        result = new BalanceAndPhoneListDto();

        for (Phone phone : phoneList)
            result.add(phone.getPhoneNumber(), phone.getBalance());
    }

    public BalanceAndPhoneListDto getBalanceAndPhoneListServiceResult() {
        return this.result;
    }

}
