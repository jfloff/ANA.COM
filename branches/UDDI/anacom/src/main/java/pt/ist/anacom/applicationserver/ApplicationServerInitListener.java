package pt.ist.anacom.applicationserver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import pt.ist.fenixframework.pstm.repository.Repository;

public class ApplicationServerInitListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

        System.out.println("\n.........Entrou no context destroyed listener\n");
        Repository rep = Repository.getRepository();
        rep.closeRepository();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            String operatorName = arg0.getServletContext().getInitParameter("operatorName");
            ApplicationServerWebService.init(operatorName);
        } catch (Exception e) {
            System.out.println("Error starting up the webapp: \n");
            e.printStackTrace();
        }
    }
}
