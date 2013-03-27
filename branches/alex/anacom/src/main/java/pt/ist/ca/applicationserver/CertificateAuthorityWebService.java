package pt.ist.ca.applicationserver;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Properties;

import pt.ist.ca.domain.CertificateAuthority;
import pt.ist.ca.service.GetCAPublicKeyService;
import pt.ist.ca.service.GetRevokedCertificateListService;
import pt.ist.ca.service.RevokeCertificateService;
import pt.ist.ca.service.SignCertificateService;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.OperatorCertificateInfoDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.ca.shared.exception.CertificateAuthorityException;
import pt.ist.ca.shared.exception.CertificateDoesNotExistException;
import pt.ist.ca.shared.exception.EmptyOperatorException;
import pt.ist.ca.shared.exception.EmptyPublicKeyException;
import pt.ist.ca.shared.exception.InvalidValidityException;
import pt.ist.ca.shared.exception.UnableToProvideServiceException;
import pt.ist.ca.shared.stubs.CertificateAuthorityPortType;
import pt.ist.ca.shared.stubs.CertificateDoesNotExistRemoteException;
import pt.ist.ca.shared.stubs.CertificateListType;
import pt.ist.ca.shared.stubs.CertificateType;
import pt.ist.ca.shared.stubs.EmptyOperatorRemoteException;
import pt.ist.ca.shared.stubs.EmptyPublicKeyRemoteException;
import pt.ist.ca.shared.stubs.InvalidValidityRemoteException;
import pt.ist.ca.shared.stubs.OperatorCertificateInfoType;
import pt.ist.ca.shared.stubs.OperatorType;
import pt.ist.ca.shared.stubs.PublicKeyType;
import pt.ist.ca.shared.stubs.UnableToProvideServiceRemoteException;
import pt.ist.ca.shared.stubs.ValidityType;
import pt.ist.ca.shared.stubs.VoidType;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;
import pt.ist.shared.SecurityData;

@javax.jws.WebService(endpointInterface = "pt.ist.ca.shared.stubs.CertificateAuthorityPortType", wsdlLocation = "/ca.wsdl", name = "CertificateAuthorityPortType", portName = "CertificateAuthorityPort", targetNamespace = "http://pt.ist.ca.essd.0403", serviceName = "CertificateAuthorityService")
@javax.jws.HandlerChain(file = "ca-handler-chain.xml")
public class CertificateAuthorityWebService implements
		CertificateAuthorityPortType {

	public static String name;
	public static String filePath;

	public static void init(final String caName, final String file) {
		System.out.println(".............STARTING Certificate Authority "
				+ caName + " SERVER...........");
		name = caName;
		filePath = file;

		// initializes the Fenix Framework
		try {

			FenixFramework.initialize(new Config() {
				{
					dbAlias = "/tmp/db" + caName;
					domainModelPath = "/tmp/ca.dml";
					repositoryType = RepositoryType.BERKELEYDB;
					rootClass = CertificateAuthority.class;
				}
			});

			Properties properties = SecurityData.readPropertiesFile(file);
			String publicPath = properties.getProperty("publicKeyCAPath")
					+ "CApublic.dat";
			String privatePath = properties.getProperty("privateKeyCAPath")
					+ "CAprivate.dat";
			PublicKey pubKey = SecurityData.readPublicKeys(publicPath);
			PrivateKey privKey = SecurityData.readPrivateKeys(privatePath);

			boolean committed = false;
			try {
				Transaction.begin();
				CertificateAuthority ca = FenixFramework.getRoot();
				ca.addKeys(pubKey, privKey);
				Transaction.commit();
				committed = true;
			} finally {
				if (!committed) {
					Transaction.abort();
				}
			}

		} catch (Exception e) {
			System.out
					.println("Failed to initialize the Certificate Authority server.\n");
		}
	}

	@Override
	public CertificateType signCertificate(
			OperatorCertificateInfoType signCertificateInput)
			throws UnableToProvideServiceRemoteException,
			EmptyPublicKeyRemoteException, EmptyOperatorRemoteException,
			InvalidValidityRemoteException {

		OperatorCertificateInfoDto dto = new OperatorCertificateInfoDto(
				signCertificateInput.getOperatorID(),
				signCertificateInput.getPublicKey(),
				signCertificateInput.getValidity());
		SignCertificateService service = new SignCertificateService(dto);
		CertificateType certType = new CertificateType();
		try {
			service.execute();
		} catch (UnableToProvideServiceException e) {
			throw new UnableToProvideServiceRemoteException();
		} catch (EmptyOperatorException e) {
			throw new EmptyOperatorRemoteException();
		} catch (EmptyPublicKeyException e) {
			throw new EmptyOperatorRemoteException();
		} catch (InvalidValidityException e) {
			ValidityType validityType = new ValidityType();
			validityType.setValidity(e.getValidity());
			throw new InvalidValidityRemoteException(e.getMessage(),
					validityType);
		} catch (CertificateAuthorityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CertificateDto cert = service.getSignCertificateServiceResult();
		certType.setCertificateContents(cert.getCertificate());
		certType.setSignature(cert.getSignature());
		return certType;
	}

	@Override
	public void revokeCertificate(OperatorType revokeCertificateInput)
			throws CertificateDoesNotExistRemoteException {
		OperatorDto dto = new OperatorDto(
				revokeCertificateInput.getOperatorID());
		RevokeCertificateService service = new RevokeCertificateService(dto);

		try {
			service.execute();
		} catch (CertificateDoesNotExistException e) {
			OperatorType operatorType = new OperatorType();
			operatorType.setOperatorID(e.getOperatorID());
			throw new CertificateDoesNotExistRemoteException(e.getMessage(),
					operatorType);
		} catch (CertificateAuthorityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CertificateListType getRevokedCertificateList(
			VoidType getRevokedCertificateListInput)
			throws UnableToProvideServiceRemoteException {
		GetRevokedCertificateListService service = new GetRevokedCertificateListService();
		CertificateListType certListType = new CertificateListType();
		try {
			service.execute();
		} catch (UnableToProvideServiceException e) {
			throw new UnableToProvideServiceRemoteException();
		} catch (CertificateAuthorityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// fill in dto arrayList
		for (CertificateDto certificate : service
				.getGetRevokedListServiceResult().getCertificateList()) {
			CertificateType certificateType = new CertificateType();
			certificateType
					.setCertificateContents(certificate.getCertificate());
			certificateType.setSignature(certificate.getSignature());
			certListType.getCertificateList().add(certificateType);
		}

		return certListType;
	}

	@Override
	public PublicKeyType getCAPublicKey(VoidType getCAPublicKeyInput) {
		GetCAPublicKeyService service = new GetCAPublicKeyService();
		PublicKeyType keyType = new PublicKeyType();
		try {
			service.execute();
		} catch (CertificateAuthorityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PublicKeyDto keyDto = service.getGetCAServerPublicKeyServiceResult();
		keyType.setPublicKey(keyDto.getPublicKey());
		return keyType;
	}
}
