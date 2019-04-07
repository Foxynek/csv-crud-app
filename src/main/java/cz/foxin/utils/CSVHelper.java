package cz.foxin.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import cz.foxin.contact_dto.Contact;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Null;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Class is used to handle all requests to input/read data from CSV
 *
 * CSV charset is set to CP1250, can be changed by changing string value of charset
 * File to store data is generated automatically, there is no need to create it beforehand
 */

@Slf4j
public class CSVHelper {

    private static Charset charset = Charset.forName("cp1250");

    /**
     * Method below is used to store unique data to CSV
     */
    public static void saveUniqueDataRowToCSV(String filePath, Contact contact){
        try{
            final CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset)));
            csvReader.close();
        } catch (FileNotFoundException e){
            log.error("File not found");
        } catch (IOException e){
            log.error("Error in saving data", e);
        }
        if (!findDataRowInCSV(filePath, contact)){
            saveNewRowToCSV(filePath, contact);
        }
    }

    /**
     * Method below is used to get data from CSV
     */
    public static Contact getDataRowFromCSV(String filePath, Contact contact){
        try{
            final CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset)));

            Map<String,String> mapping = new HashMap<>();
            mapping.put("firstName", "firstName");
            mapping.put("lastName", "lastName");
            mapping.put("email", "email");
            HeaderColumnNameTranslateMappingStrategy<Contact> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Contact.class);
            strategy.setColumnMapping(mapping);

            CsvToBean<Contact> csvToBean = new CsvToBean<>();
            csvToBean.setMappingStrategy(strategy);
            csvToBean.setCsvReader(csvReader);
            List<Contact> contacts = csvToBean.parse();

            for (Contact singleContact : contacts){
                if (singleContact.getFirstName().equals(contact.getFirstName()) &&
                        singleContact.getLastName().equals(contact.getLastName()) && singleContact.getEmail().equals(contact.getEmail())){
                    return singleContact;
                }
            }

            csvReader.close();
        } catch (FileNotFoundException e){
            log.error("File not found");
        } catch (IOException e) {
            log.error("Error reading data", e);
        }
        throw new NullPointerException("Contact not found");
    }

    /**
     * Method below is used to update/delete data in CSV
     */
    public static Contact updateRowInCSV(String filePath, Contact newContact, Contact oldContact, boolean delete) {
        try {
            final CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset)));
            csvReader.close();
        } catch (FileNotFoundException e) {
            log.error("File not found");
        } catch (IOException e) {
            log.error("Error updating data", e);
        }
        if (findDataRowInCSV(filePath, oldContact)){
            replaceRowInCSV(filePath, newContact, oldContact, delete);
            log.info("Requested data found and updated/deleted");
            if (!delete) {
                return getDataRowFromCSV(filePath, newContact);
            }
            else return null;
        }
        else throw new NullPointerException("Data not found");
    }

    public static void saveNewRowToCSV(String filePath, Contact contact){
        try {
            final CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), charset)));

            ColumnPositionMappingStrategy<Contact> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Contact.class);
            String[] columns = {"firstName", "lastName", "email"};
            strategy.setColumnMapping(columns);

            StatefulBeanToCsvBuilder<Contact> builder = new StatefulBeanToCsvBuilder<>(csvWriter);
            StatefulBeanToCsv<Contact> beanWriter = builder.withMappingStrategy(strategy).build();

            beanWriter.write(contact);
            csvWriter.close();
        } catch (FileNotFoundException e){
            log.info("File not found", e);
        } catch (Exception f){
            log.error("Error in saving data", f);
        }
    }

    private static boolean findDataRowInCSV(String filePath, Contact contact){
        try{
            final CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset)));
            String[] contactData = new String[]{contact.getFirstName(), contact.getLastName(), contact.getEmail()};
            String[] line;
            while((line = csvReader.readNext()) != null){
                if (Arrays.equals(line, contactData)){
                    csvReader.close();
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found");
        } catch (IOException e) {
            log.error("Error reading data", e);
        }
        return false;
    }

    private static List<Contact> getAllDataFromCSV(String filePath){
        try{
            final CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset)));

            Map<String,String> mapping = new HashMap<>();
            mapping.put("firstName", "firstName");
            mapping.put("lastName", "lastName");
            mapping.put("email", "email");
            HeaderColumnNameTranslateMappingStrategy<Contact> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Contact.class);
            strategy.setColumnMapping(mapping);

            CsvToBean<Contact> csvToBean = new CsvToBean<>();
            csvToBean.setMappingStrategy(strategy);
            csvToBean.setCsvReader(csvReader);
            List<Contact> contacts = csvToBean.parse();
            csvReader.close();
            return contacts;

        } catch (FileNotFoundException e) {
            log.error("File not found");
        } catch (IOException e) {
            log.error("Error reading data", e);
        }
        throw new NullPointerException("Contacts not found!");
    }

    private static void replaceRowInCSV(String filePath, Contact newContact, Contact oldContact, boolean delete) {
        try {
            List<Contact> contacts = getAllDataFromCSV(filePath);

            final CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false), charset)));
            csvWriter.writeNext(new String[]{"firstName", "lastName", "email"});
            csvWriter.close();

            for (Contact contact : contacts){
                if (contact.equals(oldContact) && delete) {
                    log.info("Contact deleted");
                }
                else if (contact.equals(oldContact)){
                    saveNewRowToCSV(filePath, newContact);
                }
                else saveNewRowToCSV(filePath, contact);
            }
        } catch (FileNotFoundException e) {
            log.error("File not found");
        } catch (IOException e) {
            log.error("Error updating data", e);
        }
    }
}
