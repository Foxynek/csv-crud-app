package cz.foxin.contact_service;

import cz.foxin.contact_dto.ContactDTO;
import cz.foxin.contact_dto.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ContactServiceHandler implements ContactService {

    @Autowired
    private ContactDTO contactDTO;

    private final Set<String> keySet = Stream.of("firstName", "lastName", "email").collect(Collectors.toSet());

    /**
     * Method below is used to validate query
     * Query should be valid only when user inputs all 3 required parameters to identify contact
     */
    private Contact getContactByQuery(Map<String,String> query) {
        if (query.keySet().equals(keySet)) {
            String firstName = query.get("firstName");
            String lastName = query.get("lastName");
            String email = query.get("email");
            if (!(firstName.equals("") || lastName.equals("") || email.equals(""))){
                Contact contact = new Contact();
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
                contact.setEmail(email);
                return contact;
            }
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Contact createContact(Contact contact) {
        return contactDTO.createContact(contact);
    }

    @Override
    public Contact getContact(Map<String,String> query) {
        return contactDTO.getContact(getContactByQuery(query));
    }

    @Override
    public Contact updateContact(Contact newContact, Map<String,String> query) {
        return contactDTO.updateContact(newContact, getContactByQuery(query));
    }

    @Override
    public void deleteContact(Map<String,String> query) {
        contactDTO.deleteContact(getContactByQuery(query));
    }
}
