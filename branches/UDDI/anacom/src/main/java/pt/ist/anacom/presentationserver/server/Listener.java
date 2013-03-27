package pt.ist.anacom.presentationserver.server;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;

public class Listener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			ConnectionFactory connFactory = org.apache.ws.scout.registry.ConnectionFactoryImpl.newInstance();
				
			////////////////////////////////////////////////////////
			// Liga��o ao UDDI registry
			////////////////////////////////////////////////////////
			
			// Para uma correcta liga��o ao UDDI registry, � necess�rio definir v�rios
			// par�metros (properties), desde a vers�o do UDDI aos URLs de publica��o e pesquisa do registry
			Properties props = new Properties();
			// Localiza��o do ficheiro de configura��o da liga��o,
			// que deve estar na directoria WEB-INF/classes do .war
			props.setProperty("scout.juddi.client.config.file", "uddi.xml");
			// URL para pesquisas ao UDDI registry
			props.setProperty("javax.xml.registry.queryManagerURL", "http://localhost:8081/juddiv3/services/inquiry");
			// URL para publicar dados no UDDI registry
			props.setProperty("javax.xml.registry.lifeCycleManagerURL", "http://localhost:8081/juddiv3/services/publish");
			// URL do gestor de seguran�a do UDDI registry
			props.setProperty("javax.xml.registry.securityManagerURL", "http://localhost:8081/juddiv3/services/security");
			// Vers�o UDDI que o registry usa
			props.setProperty("scout.proxy.uddiVersion", "3.0");
			// Protocolo de transporte usado para invoca��es ao UDDI registry
			props.setProperty("scout.proxy.transportClass", "org.apache.juddi.v3.client.transport.JAXWSTransport");
			connFactory.setProperties(props);

			// Finalmente, estabelece a liga��o ao UDDI registry
			Connection connection = connFactory.createConnection();

			// Define credenciais de autentica��o a usar para interac��o com o UDDI registry
			// Nota: o jUDDI fornecido para utiliza��o no projecto est� configurado 
			// para aceitar qualquer par username/password; no mundo real, n�o � assim 
			PasswordAuthentication passwdAuth = new PasswordAuthentication("username", "password".toCharArray());  
			Set<PasswordAuthentication> creds = new HashSet<PasswordAuthentication>();  
			creds.add(passwdAuth);
			connection.setCredentials(creds);

			// Obter objecto RegistryService
			RegistryService rs = connection.getRegistryService();

			// Obter objecto QueryManager da JAXR Business API 
			// (caso se pretenda fazer pesquisas)
			BusinessQueryManager businessQueryManager = rs.getBusinessQueryManager();
			// Obter objecto BusinessLifeCycleManager da JAXR Business API 
			// (caso se pretenda registar/alterar informa��o no UDDI registry)
			BusinessLifeCycleManager businessLifeCycleManager = rs.getBusinessLifeCycleManager();

			
			////////////////////////////////////////////////////////
			// Pesquisa de organiza��o j� registada
			////////////////////////////////////////////////////////
			Collection<String> findQualifiers = new ArrayList<String>();
			findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

			String organizationName = "A Minha Organiza��o";
			String serviceName = "O Meu Servi�o";
			String bindingDescription = "Informa��o para acesso ao servi�o";
			String bindingURL = "http://localhost:8080/base-server/AnacomServiceImpl";

			// Pretendemos criar nova organiza��o apenas caso ainda n�o exista
			// Logo, vamos primeiro pesquisar por organiza��o com nome "MinhaOrganizacao"
			Organization org = null;
			
			Collection<String> namePatterns = new ArrayList<String>();
			namePatterns.add("%"+organizationName+"%");
			// Efectua a pesquisa
			BulkResponse r = businessQueryManager.findOrganizations(findQualifiers, namePatterns, null, null, null, null);
			@SuppressWarnings("unchecked")
			Collection<Organization> orgs = r.getCollection();
			                       
			for (Organization o : orgs) {
				if (o.getName().getValue().equals(organizationName)) {
					// TO DO: Caso a organization/service/serviceBinding
					// j� tenham sido registados anteriormente, basta
					// aplicar-lhes as altera��es desejadas e registar
					// essas altera��es atrav�s de 
				    // businessLifeCycleManager.saveOrganization
				}
			}

			
			////////////////////////////////////////////////////////
			// Registo de novas entidades/altera��es a entidades j�
			// existentes no UDDI registry
			////////////////////////////////////////////////////////

			if (org==null) {
				// N�o se encontrou a organiza��o j� registada, logo vamos cri�-la
			    org = businessLifeCycleManager.createOrganization(organizationName);				

			    // Cria servi�o
			    Service service = businessLifeCycleManager.createService(serviceName);
			    service.setDescription(businessLifeCycleManager.createInternationalString(serviceName));
			    // Adiciona o servi�o � organiza��o
			    org.addService(service);
			    
			    // Cria serviceBinding
			    ServiceBinding serviceBinding = businessLifeCycleManager.createServiceBinding();
			    serviceBinding.setDescription(businessLifeCycleManager.
			            createInternationalString(bindingDescription));
			    serviceBinding.setValidateURI(false);
			    // Aqui define-se o URL do endpoint do Web Service
			    serviceBinding.setAccessURI(bindingURL);				
			    // Adiciona o serviceBinding ao servi�o
			    service.addServiceBinding(serviceBinding);			    
			}
			
		    Collection<Organization> orgs2 = new ArrayList<Organization>();
		    orgs2.add(org);
		     
		    // Finalmente, regista a nova organization/service/serviceBinding
		    // (ou as novas altera��es) no UDDI registry
		    BulkResponse br = businessLifeCycleManager.saveOrganizations(orgs2);
		     
		    if (br.getStatus() == JAXRResponse.STATUS_SUCCESS)
		            System.out.println("Registo completado com sucesso.");
		    else
		            System.out.println("Erro durante o registo no UDDI.");
		    
		} catch (JAXRException e) {
			System.err.println("Erro ao contactar o UDDI registry.");
			e.printStackTrace();
		}		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		return;
	}

}
