package pt.ist.phonebook.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;

public class PhoneBook extends PhoneBook_Base {

    public PhoneBook() {
        super();
        this.setReplicaVersion(1);
    }

    private Contact getContactByName(String name) {
        for (Contact contact : getContactSet()) {
            if (contact.getName().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public boolean hasContact(String contactName) {
        return getContactByName(contactName) != null;
    }

    public Contact getPhoneNumberByName(String contactToSearch) throws ContactDoesNotExistException {
        Contact returnContact = getContactByName(contactToSearch);

        if (returnContact == null) {
            throw new ContactDoesNotExistException(contactToSearch);
        }

        return returnContact;
    }

    @Override
    public void addContact(Contact contactToBeAdded) throws NameAlreadyExistsException {
        if (hasContact(contactToBeAdded.getName()))
            throw new NameAlreadyExistsException(contactToBeAdded.getName());
        
        Logger.getLogger(this.getClass()).info("Hello WORLD!");

        super.addContact(contactToBeAdded);
    }

    public void removeContact(String contactName) throws ContactDoesNotExistException {
        Contact toRemove = getContactByName(contactName);

        if (toRemove == null)
            throw new ContactDoesNotExistException(contactName);

        super.removeContact(toRemove);
    }

    public List<Contact> searchContact(String token) {
        List<Contact> matchingContacts = new ArrayList<Contact>();
        for (Contact existingContact : this.getContactSet()) {
            if (existingContact.getName().contains(token)) {
                matchingContacts.add(existingContact);
            }
        }
        return matchingContacts;
    }

}
