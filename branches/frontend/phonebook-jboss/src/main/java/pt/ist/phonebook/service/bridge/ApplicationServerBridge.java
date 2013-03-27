package pt.ist.phonebook.service.bridge;

import java.util.List;

import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.dto.PhoneBookDto;
import pt.ist.phonebook.shared.dto.ReplicaVersionDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;

public interface ApplicationServerBridge {

    public void createContact(ContactDetailedDto dto) throws NameAlreadyExistsException;

    public List<ContactSimpleDto> search(String token);

    public void removeContact(ContactSimpleDto dto) throws ContactDoesNotExistException;

    public ContactDetailedDto getContact(ContactSimpleDto dto) throws ContactDoesNotExistException;

    public PhoneBookDto listContacts();

}
