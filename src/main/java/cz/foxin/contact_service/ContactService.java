package cz.foxin.contact_service;

import cz.foxin.contact_dto.Contact;

import java.util.Map;

public interface ContactService {

    Contact createContact(Contact contact);
    Contact getContact(Map<String,String> query);
    Contact updateContact(Contact newContact, Map<String,String> query);
    void deleteContact(Map<String,String> query);

}
