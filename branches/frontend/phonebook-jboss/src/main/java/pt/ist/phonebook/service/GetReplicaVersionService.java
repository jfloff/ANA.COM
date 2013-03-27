package pt.ist.phonebook.service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.Contact;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ReplicaVersionDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;

public class GetReplicaVersionService extends PhoneBookService {

    private ReplicaVersionDto resultReplicaVersionDto;

    public GetReplicaVersionService() {

    }

    public final void dispatch() {
        PhoneBook phoneBook = FenixFramework.getRoot();
        Integer replicaVersion = phoneBook.getReplicaVersion();
        resultReplicaVersionDto = new ReplicaVersionDto(replicaVersion);
    }

    public ReplicaVersionDto getReplicaVersionServiceResult() {
        return this.resultReplicaVersionDto;
    }
}
