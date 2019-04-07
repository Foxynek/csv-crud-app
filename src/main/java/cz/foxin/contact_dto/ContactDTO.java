package cz.foxin.contact_dto;

public interface ContactDTO {

    Contact createContact(Contact contact);
    Contact getContact(Contact contact);
    Contact updateContact(Contact newContact, Contact oldContact);
    void deleteContact(Contact contact);
}
