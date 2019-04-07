package cz.foxin.contact_service;

import cz.foxin.contact_dto.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * This is basic REST Controller
 * Default port - 5000 (URL - http://localhost:5000/crud/)
 * You can change this setting in resources/application.properties
 */

@RestController
@Slf4j
@RequestMapping(value = "/crud")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * CREATE contact
     * URL - http://localhost:5000/crud/contact
     * Contact body is required to create contact
     * Use POST request method
     */
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public Contact createContact(@RequestBody Contact contact){
        try{
            return contactService.createContact(contact);
        } catch(ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create contact with these parameters!", e);
        }
    }

    /**
     * GET contact
     * URL - http://localhost:5000/crud/contact?firstName={YOUR INPUT}&lastName={YOUR INPUT}&email={YOUR INPUT}
     * Query above is required to find contact
     * Use GET request method
     */
    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public Contact getContact(@RequestParam Map<String,String> query){
        try{
            return contactService.getContact(query);
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot perform search with these parameters!", e);
        }catch(NullPointerException f){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queried data not found!", f);
        }
    }

    /**
     * UPDATE contact
     * URL - http://localhost:5000/crud/contact?firstName={YOUR INPUT}&lastName={YOUR INPUT}&email={YOUR INPUT}
     * Query above is required to specify existing contact you want to change
     * Contact body is used to specify changes for this contact (won't check for duplicates, so this should be the only way to create duplicates)
     * Use PUT request method
     */
    @RequestMapping(value = "/contact", method = RequestMethod.PUT)
    public Contact updateContact(@RequestBody Contact newContact, @RequestParam Map<String,String> query){
        try{
            return contactService.updateContact(newContact, query);
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update contact with these parameters!", e);
        }catch(NullPointerException f){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queried data not found!", f);
        }
    }

    /**
     * DELETE contact
     * URL - http://localhost:5000/crud/contact?firstName={YOUR INPUT}&lastName={YOUR INPUT}&email={YOUR INPUT}
     * Query above is required to delete contact
     * Use DELETE request method
     */
    @RequestMapping(value = "/contact", method = RequestMethod.DELETE)
    public void deleteContact(@RequestParam Map<String,String> query){
        try{
            contactService.deleteContact(query);
        }catch(ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete contact with these parameters!", e);
        }catch(NullPointerException f){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queried data not found!", f);
        }
    }

}
