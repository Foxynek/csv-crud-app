package cz.foxin.contact_dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * Data Transfer Object class to handle contacts
 */

@Data
public class Contact {

    @CsvBindByName(column = "firstName")
    private String firstName;
    @CsvBindByName(column = "lastName")
    private String lastName;
    @CsvBindByName(column = "email")
    private String email;

    public Contact(){}
    public Contact(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
