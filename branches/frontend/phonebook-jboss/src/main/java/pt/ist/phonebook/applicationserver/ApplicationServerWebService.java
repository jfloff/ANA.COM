package pt.ist.phonebook.applicationserver;

// import javax.annotation.PostConstruct;
import java.math.BigInteger;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.service.CreateNewContactService;
import pt.ist.phonebook.service.GetContactService;
import pt.ist.phonebook.service.GetReplicaVersionService;
import pt.ist.phonebook.service.RemoveContactService;
import pt.ist.phonebook.service.SetReplicaVersionService;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.dto.ReplicaVersionDto;
import pt.ist.phonebook.shared.stubs.server.ContactDetailedReqType;
import pt.ist.phonebook.shared.stubs.server.ContactDoesNotExistException;
import pt.ist.phonebook.shared.stubs.server.ContactDoesNotExistException_Exception;
import pt.ist.phonebook.shared.stubs.server.ContactSimpleReqType;
import pt.ist.phonebook.shared.stubs.server.NameAlreadyExistsException;
import pt.ist.phonebook.shared.stubs.server.NameAlreadyExistsException_Exception;
import pt.ist.phonebook.shared.stubs.server.PhoneBookApplicationServerPortType;

@javax.jws.WebService(endpointInterface = "pt.ist.phonebook.shared.stubs.server.PhoneBookApplicationServerPortType", wsdlLocation = "/phonebook.wsdl", name = "PhoneBookApplicationServerPortType", portName = "PhoneBookApplicationServicePort", targetNamespace = "http://phonebook", serviceName = "phonebook")
public class ApplicationServerWebService implements PhoneBookApplicationServerPortType {

    public static void init(final String operatorPrefix, final String replicaId) {
        System.out.println("....STARTING Phonebook SERVER.. [" + operatorPrefix + " - S" + replicaId + "]");

        // initializes the Fenix Framework
        try {

            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "/tmp/db/" + operatorPrefix + "-" + replicaId;
                    domainModelPath = "/tmp/phonebook.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = PhoneBook.class;
                }
            });

        } catch (Exception e) {
            System.out.println("Failed to initialize the operator server.\n");
        }
    }

    public BigInteger replicaVersion() {

        GetReplicaVersionService versionService = new GetReplicaVersionService();
        versionService.execute();
        ReplicaVersionDto versionDto = versionService.getReplicaVersionServiceResult();
        Integer replicaVersion = versionDto.getVersion();
        return new BigInteger("" + replicaVersion);

    }

    public boolean replicaIsUpToDate(BigInteger newVersionBigInt) {

        Integer newVersion = newVersionBigInt.intValue();
        Integer replicaVersion = replicaVersion().intValue();

        if (newVersion > replicaVersion)
            return false;
        else
            return true;

    }

    @Override
    public BigInteger createContact(ContactDetailedReqType parameters) throws NameAlreadyExistsException_Exception {

        try {

            if (parameters != null) {

                System.out.println("[jUDDI] CRIAR CONTACTO NO SERVER: " + parameters.getPhoneNumber());

                if (!replicaIsUpToDate(parameters.getVersion())) {

                    // if(parameters.getPhoneNumber()parameters.toString().substring(0,
                    // 2).equals(""))
                    // Simulate Delay
                    // Random random = new Random();
                    // try {
                    // Thread.sleep(300*random.nextInt(5));
                    // } catch (InterruptedException e) {
                    // e.printStackTrace();
                    // }

                    // Create DTOs
                    String contactName = parameters.getName();
                    int phoneNumber = parameters.getPhoneNumber().intValue();
                    ContactDetailedDto contactDetailedDto = new ContactDetailedDto(contactName, phoneNumber);

                    // Run command
                    CreateNewContactService createNewContactService = new CreateNewContactService(contactDetailedDto);
                    createNewContactService.execute();

                    // Update replica version
                    ReplicaVersionDto replicaVersionDto = new ReplicaVersionDto(parameters.getVersion().intValue());
                    SetReplicaVersionService setReplicaVersionService = new SetReplicaVersionService(replicaVersionDto);
                    setReplicaVersionService.execute();

                }

                // Send ACK back
                return getReplicaVersion();

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }

            return null;

        } catch (pt.ist.phonebook.shared.exception.NameAlreadyExistsException e) {
            NameAlreadyExistsException remoteExc = new NameAlreadyExistsException();
            remoteExc.setDuplicateName(e.getConflictingName());
            throw new NameAlreadyExistsException_Exception("Contact Exception", remoteExc);
        }

    }

    @Override
    public ContactDetailedReqType getContact(ContactSimpleReqType parameters) throws ContactDoesNotExistException_Exception {

        try {

            // Simulate Delay
            java.util.Random random = new java.util.Random();
            try {
                Thread.sleep(300 * random.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (parameters != null) {

                System.out.println("[jUDDI]  GET CONTACT NO SERVER: " + parameters.getName());

                // Create DTOs
                String contactName = parameters.getName();
                ContactSimpleDto contactSimpleDto = new ContactSimpleDto(contactName);

                // Run command
                GetContactService getContactService = new GetContactService(contactSimpleDto);
                getContactService.execute();

                // Return type
                ContactDetailedDto returnDto = getContactService.getContactServiceResult();
                ContactDetailedReqType returnType = new ContactDetailedReqType();
                returnType.setName(returnDto.getName());
                returnType.setPhoneNumber(new BigInteger("" + returnDto.getPhoneNumber()));
                returnType.setVersion(getReplicaVersion());

                // Send return
                return returnType;

            } else {
                System.err.println("[ERR] You should not be here. Parameters equals null.");
            }

            return null;

        } catch (pt.ist.phonebook.shared.exception.ContactDoesNotExistException e) {
            ContactDoesNotExistException remoteExc = new ContactDoesNotExistException();
            remoteExc.setName(e.getContactName());
            throw new ContactDoesNotExistException_Exception("Contact Exception", remoteExc);
        }


    }

    @Override
    public void removeContact(ContactSimpleReqType parameters) throws ContactDoesNotExistException_Exception {
        ContactSimpleDto r = null;
        if (parameters != null) {
            r = new ContactSimpleDto(parameters.getName());
        }
        try {
            RemoveContactService s = new RemoveContactService(r);
            s.execute();
        } catch (pt.ist.phonebook.shared.exception.ContactDoesNotExistException e) {
            ContactDoesNotExistException remoteExc = new ContactDoesNotExistException();
            remoteExc.setName(e.getContactName());
            throw new ContactDoesNotExistException_Exception("Contact Exception", remoteExc);
        }
    }

    @Override
    public BigInteger getReplicaVersion() {

        try {

            System.out.println("[jUDDI] PEDIDO DE VERSAO!");

            GetReplicaVersionService versionService = new GetReplicaVersionService();
            versionService.execute();
            ReplicaVersionDto versionDto = versionService.getReplicaVersionServiceResult();
            Integer replicaVersion = versionDto.getVersion();

            System.out.println("[jUDDI] My version: " + replicaVersion.intValue());

            return new BigInteger("" + replicaVersion);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
