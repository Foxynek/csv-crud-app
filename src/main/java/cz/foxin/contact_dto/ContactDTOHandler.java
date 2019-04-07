package cz.foxin.contact_dto;

import cz.foxin.utils.CSVHelper;
import cz.foxin.utils.PropertiesFileCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ContactDTOHandler implements ContactDTO {

    @Override
    public Contact createContact(Contact contact) {
        CSVHelper.saveUniqueDataRowToCSV(PropertiesFileCreator.getFilepathFromProperties(), contact);
        return getContact(contact);
    }

    @Override
    public Contact getContact(Contact contact) {
        return CSVHelper.getDataRowFromCSV(PropertiesFileCreator.getFilepathFromProperties(), contact);
    }

    @Override
    public Contact updateContact(Contact newContact, Contact oldContact) {
        return CSVHelper.updateRowInCSV(PropertiesFileCreator.getFilepathFromProperties(), newContact, oldContact, false);
    }

    @Override
    public void deleteContact(Contact contact) {
        CSVHelper.updateRowInCSV(PropertiesFileCreator.getFilepathFromProperties(), null, contact, true);
    }
}
