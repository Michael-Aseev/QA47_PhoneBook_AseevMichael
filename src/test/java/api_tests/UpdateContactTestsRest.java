package api_tests;

import dto.Contact;
import dto.ResponseMessageDto;
import io.restassured.response.Response;
import manager.ContactController;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static utils.RandomUtils.*;


public class UpdateContactTestsRest extends ContactController {

    Contact contact;

    @BeforeClass(groups = "smoke")
    public void createContact() {
        contact = Contact.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .phone(generatePhone(10))
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        Response response = addNewContactRequest(contact, tokenDto);
        ResponseMessageDto responseMessageDto;
        if (response.getStatusCode() != 200)
            System.out.println("Contact doesn't create");
        else {
            responseMessageDto = response.body().as(ResponseMessageDto.class);
            //Contact was added! ID: 62b99d86-4961-4318-ae52-01f8470f493b
            contact.setId(responseMessageDto.getMessage().split("ID: ")[1]);
        }
    }


    @Test
    public void UpdateContactPositiveTest() {
        System.out.println(contact.toString());
        contact.setName("New name");
        Response response = updateContactRequest(contact, tokenDto);
        response
                .then()
                .log().all()//ifValidationFails()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("ResponseMessageDtoSchema.json"))
        ;
        ResponseMessageDto responseMessageDto = response.body().as(ResponseMessageDto.class);
        Assert.assertTrue(responseMessageDto.getMessage().contains("Contact was updated"));
    }
}
