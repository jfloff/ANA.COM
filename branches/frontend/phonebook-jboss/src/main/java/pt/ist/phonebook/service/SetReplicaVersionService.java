package pt.ist.phonebook.service;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.Contact;
import pt.ist.phonebook.domain.PhoneBook;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ReplicaVersionDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;

public class SetReplicaVersionService extends PhoneBookService {

    private ReplicaVersionDto replicaVersionDto;

    public SetReplicaVersionService(ReplicaVersionDto replicaVersionDto) {
        this.replicaVersionDto = replicaVersionDto;
    }

    public final void dispatch() {
        System.out.println("OIII!!!!! ESTOU AQUI PAHHHHHHHHHHHHHHHHHHHHHHHHH");
        PhoneBook phoneBook = FenixFramework.getRoot();
        phoneBook.setReplicaVersion(replicaVersionDto.getVersion());
        System.out.println("NOVA VERSAO: " + replicaVersionDto.getVersion());
    }
}
