package pt.ist.phonebook.presentationserver;

import java.util.List;

import pt.ist.phonebook.service.CreateNewContactService;
import pt.ist.phonebook.service.ListPhoneBookContactsService;
import pt.ist.phonebook.service.RemoveContactService;
import pt.ist.phonebook.service.SearchContactService;
import pt.ist.phonebook.service.bridge.ApplicationServerBridge;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.dto.PhoneBookDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;

public class ApplicationServerBridgeSoftEng implements ApplicationServerBridge {

    @Override
    public void createContact(ContactDetailedDto dto) throws NameAlreadyExistsException {
        CreateNewContactService service = new CreateNewContactService(dto);
        service.execute();
    }

    @Override
    public List<ContactSimpleDto> search(String token) {
        SearchContactService service = new SearchContactService(token);
        service.execute();
        return service.getResult();
    }

    @Override
    public void removeContact(ContactSimpleDto dto) throws ContactDoesNotExistException {
        RemoveContactService service = new RemoveContactService(dto);
        service.execute();
    }

    @Override
    public PhoneBookDto listContacts() {
        ListPhoneBookContactsService service = new ListPhoneBookContactsService();
        service.execute();
        return service.getContactList();
    }

    @Override
    public ContactDetailedDto getContact(ContactSimpleDto dto) throws ContactDoesNotExistException {
        // TODO Auto-generated method stub
        return null;
    }
}
