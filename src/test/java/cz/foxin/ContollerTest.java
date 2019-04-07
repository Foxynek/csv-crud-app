package cz.foxin;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.foxin.contact_dto.Contact;
import cz.foxin.contact_service.ContactController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(ContactController.class)
public class ContollerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ContactController contactController;

    private Contact contact;
    private Contact contact2;
    private Map<String,String> contactData;

    @Before
    public void loadContact(){
        contact = new Contact("userFirstName", "userLastName", "userEmail");
        contact2 = new Contact("userFirstNameUpdated", "userLastNameUpdated", "userEmailUpdated");
        contactData = new HashMap<>();
        contactData.put("firstName", "userFirstName");
        contactData.put("lastName", "userLastName");
        contactData.put("email", "userEmail");
    }

    @Test
    public void createContact() throws Exception{
        String json = mapper.writeValueAsString(contact);

        mvc.perform(post("/crud/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

        given(contactController.createContact(contact)).willReturn(contact);
    }

    @Test
    public void getContact() throws Exception{
        mvc.perform(get("/crud/contact")
            .param("firstName", contact.getFirstName())
            .param("lastName", contact.getLastName())
            .param("email", contact.getEmail()))
                .andExpect(status().isOk());

        given(contactController.getContact(contactData)).willReturn(contact);
    }

    @Test
    public void updateContact() throws Exception{
        String json = mapper.writeValueAsString(contact);

        mvc.perform(put("/crud/contact")
            .param("firstName", contact.getFirstName())
            .param("lastName", contact.getLastName())
            .param("email", contact.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        given(contactController.updateContact(contact2, contactData)).willReturn(contact2);
    }

    @Test
    public void deleteContact() throws Exception{
        mvc.perform(delete("/crud/contact")
                .param("firstName", contact.getFirstName())
                .param("lastName", contact.getLastName())
                .param("email", contact.getEmail()))
                    .andExpect(status().isOk());
    }
}
