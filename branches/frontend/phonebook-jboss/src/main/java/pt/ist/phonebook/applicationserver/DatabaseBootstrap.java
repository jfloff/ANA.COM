package pt.ist.phonebook.applicationserver;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.Config.RepositoryType;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.phonebook.domain.PhoneBook;

public class DatabaseBootstrap {
    private static boolean notInitialized = true;

    public static void init() {
        if (notInitialized) {
            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "test-db";
                    domainModelPath = "src/main/dml/phonebook.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = PhoneBook.class;
                }
            });
        }
        notInitialized = false;
    }

    public static void setup() {
        try {
            pt.ist.phonebook.SetupDomain.populateDomain();
        } catch (pt.ist.phonebook.shared.exception.PhoneBookException ex) {
            System.out.println("Error while populating phonebook application: " + ex);
        }
    }
}
