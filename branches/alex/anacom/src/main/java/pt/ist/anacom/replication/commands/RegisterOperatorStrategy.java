package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.OperatorDetailedType;

public class RegisterOperatorStrategy extends WriteCommandStrategy {

    private final OperatorDetailedDto operatorDetailedDto;
    private final OperatorDetailedType operatorDtoType;

    public RegisterOperatorStrategy(BusinessQueryManager businessQueryManager, OperatorDetailedDto operatorDetailedDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.operatorDetailedDto = operatorDetailedDto;

        operatorDtoType = new OperatorDetailedType();

        operatorDtoType.setOperatorName(operatorDetailedDto.getOperatorName());
        operatorDtoType.setOperatorPrefix(prefix);
        operatorDtoType.setTax(operatorDetailedDto.getOperatorTax());
        operatorDtoType.setTaxVoice(operatorDetailedDto.getOperatorTaxVoice());
        operatorDtoType.setTaxSMS(operatorDetailedDto.getOperatorTaxSMS());
        operatorDtoType.setTaxVideo(operatorDetailedDto.getOperatorTaxVideo());
        operatorDtoType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async registerOperator : " + operatorDetailedDto.getOperatorName() + " | New version : "
                    + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                port.registerOperatorAsync(operatorDtoType, handlerResponse);
            }
        }
    }

}
