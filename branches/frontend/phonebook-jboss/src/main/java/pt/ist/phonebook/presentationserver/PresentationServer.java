package pt.ist.phonebook.presentationserver;

import java.util.Random;

import org.apache.log4j.Logger;

import pt.ist.phonebook.applicationserver.DatabaseBootstrap;
import pt.ist.phonebook.service.bridge.ApplicationServerBridge;
import pt.ist.phonebook.shared.dto.ContactDetailedDto;
import pt.ist.phonebook.shared.dto.ContactSimpleDto;
import pt.ist.phonebook.shared.exception.ContactDoesNotExistException;
import pt.ist.phonebook.shared.exception.NameAlreadyExistsException;

public class PresentationServer {

    static private ApplicationServerBridge bridge = null;

    public void init(String server) {
        if (server.equals("ES+SD")) {
            bridge = new ApplicationServerBridgeDistSoft();
        } else if (server.equals("ES-only")) {
            bridge = new ApplicationServerBridgeSoftEng();
            DatabaseBootstrap.init();
            DatabaseBootstrap.setup();
        } else {
            throw new RuntimeException("Servlet parameter error: ES+SD nor ES-only");
        }
    }

    static public ApplicationServerBridge getBridge() {
        return bridge;
    }

    public void doSomething() {
        
        Logger.getLogger(this.getClass()).info("Hello WORLD! 2222222");

        // SIMULATE GENERATE NEW CONTACT
        Random random = new Random();
        String name = "JOANA" + random.nextInt(999999999);
        ContactDetailedDto dto = new ContactDetailedDto(name, 912345678);

        // WRITE
        try {
            bridge.createContact(dto);
        } catch (NameAlreadyExistsException e) {
            System.out.println("Error: Cannot add new contact: " + e.getConflictingName());
        }

        System.out.println("[jUDDI] Write success");

        // GET
        try {
            ContactSimpleDto getDto = new ContactSimpleDto(name);
            ContactDetailedDto returnDto = bridge.getContact(getDto);
            System.out.println("PHONE_NUMBER: " + returnDto.getPhoneNumber());
        } catch (ContactDoesNotExistException e) {
            System.out.println("Error: Cannot get contact: " + e.getContactName());
        }

        System.out.println("[jUDDI] Get success");


        /*
         * // CLEAN try { bridge.removeContact(dto); } catch (ContactDoesNotExistException
         * e) { System.out.println("Cannot remove non-existing contact: " +
         * e.getContactName()); }
         */
    }

    public static void main(String args[]) {
        PresentationServer ps = new PresentationServer();
        String server = System.getProperty("server.type", "ES+SD"); // "ES+SD";

        ps.init(server);
        ps.doSomething();
    }
}
