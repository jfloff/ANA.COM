package base;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import base.stubs.BaseServiceImpl;
import base.stubs.BaseServiceImplService;

public class BaseServiceClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BaseServiceImplService bs = new BaseServiceImplService();

		BaseServiceImpl server = bs.getBaseServiceImplPort();

		Binding binding = ((BindingProvider) server).getBinding();

		List<Handler> handlerList = new ArrayList<Handler>();
		handlerList.add(new ClientHandler());
		// inserir outros handlers
		binding.setHandlerChain(handlerList);

		// System.out.println(server.echo("Sistemas Distribuidos"));
		System.out.println(server.echo("O JBOSS É UMA MERDA!", 5213461, "TRINTA"));

	}

}
