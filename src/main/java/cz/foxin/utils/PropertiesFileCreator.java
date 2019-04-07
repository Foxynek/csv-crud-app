package cz.foxin.utils;

import cz.foxin.contact_dto.Contact;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static cz.foxin.utils.CSVHelper.saveNewRowToCSV;
import static cz.foxin.utils.CSVHelper.saveUniqueDataRowToCSV;

/**
 * Class is used to handle creation of CSV file in a location based on set property
 *
 * Default storage is set to {PROJECT DIRECTORY}/csv/contacts.csv
 * Storage location can be changed in resources/application.properties file to system temp folder
 * Value {projectdir} - used to set location in project folder
 * Value {tmp} - used to set location to system temp folder
 */

@Slf4j
public class PropertiesFileCreator {

    private static String projectPath = System.getProperty("user.dir");
    private static Path filePath = null;

    private static String readFilepathFromProperties(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(projectPath + "\\src\\main\\resources\\application.properties"));
            return properties.getProperty("filepath");
        } catch (FileNotFoundException e){
            log.error("File not found", e);
        } catch (IOException e){
            log.error("Error reading properties", e);
        }
        throw new NullPointerException();
    }

    public static void createFile(){
        try {
            if (readFilepathFromProperties().equals("tmp")) {
                filePath = Files.createTempFile("contacts", ".csv");
            } else if (readFilepathFromProperties().equals("projectdir")) {
                Files.createDirectory(Paths.get(projectPath+"\\csv"));
                filePath = Files.createFile(Paths.get(projectPath+"\\csv\\contacts.csv"));
            }
        } catch (FileAlreadyExistsException e){
            log.info("File/folder already created, will skip creation");
            filePath = Paths.get(projectPath+"\\csv\\contacts.csv");
        } catch (IOException e){
            log.error("Error creating file");
        }
    }

    public static String getFilepathFromProperties(){
        if (filePath == null){
            createFile();
            saveUniqueDataRowToCSV(filePath.toString(), new Contact("firstName", "lastName", "email"));
        }
        return filePath.toString();
    }
}
