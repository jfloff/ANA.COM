package pt.ist.anacom;

import jvstm.Atomic;
import pt.ist.anacom.domain.Anacom;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;

public class SetupDomain {
	public static void main(final String[] args) {
		FenixFramework.initialize(new Config() {{ 
			dbAlias = "db/dbAnacom";
            domainModelPath = "src/main/dml/anacom.dml";
            repositoryType = RepositoryType.BERKELEYDB;
            rootClass = Anacom.class;
        }});
		populateDomain();
		        
    }
	
	@Atomic
	public static void populateDomain() {
		
	}
    
}
