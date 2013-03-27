package base;

@javax.jws.WebService
@javax.jws.HandlerChain(file = "handler-chain.xml")
public class BaseServiceImpl {

	@javax.jws.WebMethod
	public String echo(String name, int x, String aaa) {
		System.out.println("AQUI BOI");
		if (x % 2 == 0)
			return ("Bom dia " + name + "!");
		else
			return "PAROU!" + aaa;
	}

}
