package pt.ist.anacom.applicationserver;

import pt.ist.anacom.domain.Anacom;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;

public class DatabaseBootstrap {
    private static boolean notInitialized = true;

    public static void init() {
        if (notInitialized) {
            FenixFramework.initialize(new Config() {
                {
                    dbAlias = "test-db";
                    domainModelPath = "src/main/dml/anacom.dml";
                    repositoryType = RepositoryType.BERKELEYDB;
                    rootClass = Anacom.class;
                }
            });
        }
        notInitialized = false;
    }
}
