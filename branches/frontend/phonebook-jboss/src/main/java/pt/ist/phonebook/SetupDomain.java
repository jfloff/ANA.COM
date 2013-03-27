package pt.ist.phonebook;

import jvstm.Atomic;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.Config.RepositoryType;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.Contact;
import pt.ist.phonebook.domain.PhoneBook;

public class SetupDomain {

    public static void main(String[] args) {
        FenixFramework.initialize(new Config() {
            {
                dbAlias = "db";
                domainModelPath = "src/main/dml/phonebook.dml";
                repositoryType = RepositoryType.BERKELEYDB;
                rootClass = PhoneBook.class;

            }
        });
        populateDomain();
    }

    @Atomic
    public static void populateDomain() {
        PhoneBook pb = FenixFramework.getRoot();
        pb.addContact(new Contact("SOS", 112));
    }

}
