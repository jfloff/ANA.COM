package pt.ist.phonebook.presentationserver;

import java.util.ArrayList;
import java.util.List;

import pt.ist.phonebook.replication.FrontEnd;
import pt.ist.phonebook.service.bridge.ApplicationServerBridge;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.dto.PhoneBookDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;
import pt.ist.phonebook.shared.stubs.server.ContactDoesNotExistException_Exception;
import pt.ist.phonebook.shared.stubs.server.ContactSimpleReqType;
import pt.ist.phonebook.shared.stubs.server.PhoneBookApplicationServerPortType;
import pt.ist.phonebook.shared.stubs.server.Phonebook;

public class ApplicationServerBridgeDistSoft implements ApplicationServerBridge {

    private PhoneBookApplicationServerPortType getPort() {
        Phonebook phonebook = new Phonebook();
        PhoneBookApplicationServerPortType port = phonebook.getPhoneBookApplicationServicePort();
        return port;
    }

    @Override
    public void createContact(ContactDetailedDto dto) throws NameAlreadyExistsException {
        FrontEnd fe = new FrontEnd();
        fe.createContact(dto);
    }

    @Override
    public ContactDetailedDto getContact(ContactSimpleDto dto) throws ContactDoesNotExistException {
        FrontEnd fe = new FrontEnd();
        return fe.getContact(dto);
    }

    @Override
    public void removeContact(ContactSimpleDto dto) throws ContactDoesNotExistException {
        PhoneBookApplicationServerPortType port = getPort();

        ContactSimpleReqType r = new ContactSimpleReqType();
        r.setName(dto.getName());

        try {
            port.removeContact(r);
        } catch (ContactDoesNotExistException_Exception e) {
            pt.ist.phonebook.shared.stubs.server.ContactDoesNotExistException exc = e.getFaultInfo();
            throw new ContactDoesNotExistException(exc.getName());
        }
    }

    // not implemented yet
    public List<ContactSimpleDto> search(String token) {
        return new ArrayList<ContactSimpleDto>();
    }

    // not implemented yet
    public PhoneBookDto listContacts() {
        return null;
    }
}
