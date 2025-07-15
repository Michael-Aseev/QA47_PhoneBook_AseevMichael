package api_tests;

import dto.Contact;
import dto.ResponseMessageDto;
import dto.TokenDto;
import io.restassured.response.Response;
import manager.ContactController;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static utils.RandomUtils.*;
import static utils.RandomUtils.generateString;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class DeleteContactByIdTestsRest extends ContactController {

    Contact contact;

    @BeforeClass(alwaysRun = true)
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

    @Test(groups = "smoke")
    public void deleteContactByIdPositiveTest(){
        Response response = deleteContactsById(contact, tokenDto);
        response
                .then()
                .log().ifValidationFails()
                .statusCode(200)  //Contact was deleted!
                .body(matchesJsonSchemaInClasspath("ResponseMessageDtoSchema.json"))
        ;

    }

    @Test
    public void deleteContactByIdNegativeTest_401_invalidToken(){
        TokenDto tokenDto1 = TokenDto.builder().token("invalid").build();
        Response response = deleteContactsById(contact, tokenDto1);
        response
                .then()
                .log().all()
                .statusCode(401)  //Contact was deleted!
                .body(matchesJsonSchemaInClasspath("ErrorMessageDtoSchema.json"))
        ;

    }
}
