package ui_tests;

import data_providers.ContactDP;
import dto.Contact;
import dto.User;
import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddPage;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;
import utils.HeaderMenuItem;
import utils.TestNGListener;

import static pages.BasePage.clickButtonHeader;
import static utils.PropertiesReader.getProperty;
import static utils.RandomUtils.*;

@Listeners(TestNGListener.class)

public class AddNewContactsTest extends ApplicationManager {
    HomePage homePage;
    LoginPage loginPage;
    ContactsPage contactsPage;
    AddPage addPage;
    int sizeBeforeAdd;
    String existPhone;


    @BeforeMethod(alwaysRun = true)
    public void login() {
        //User user = new User("mamon300396@gmail.com", "Ercbdn300396$");
        User user = new User(getProperty("login.properties", "email"),
                getProperty("login.properties", "password"));
        homePage = new HomePage(getDriver());
        loginPage = clickButtonHeader(HeaderMenuItem.LOGIN);
        loginPage.typeLoginForm(user);
        contactsPage = new ContactsPage(getDriver());
        sizeBeforeAdd = contactsPage.getContactsListSize();
        existPhone = contactsPage.getPhoneFromList();
        addPage = clickButtonHeader(HeaderMenuItem.ADD);
    }

    @Test(invocationCount = 1, groups = "smoke")
    public void addNewContactPositiveTest(){
        Contact contact = Contact.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .phone(generatePhone(10))
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        int sizeAfterAdd = contactsPage.getContactsListSize();
        System.out.println(sizeAfterAdd + "X" + sizeAfterAdd);
        Assert.assertEquals(sizeBeforeAdd +1, sizeAfterAdd);
    }

    @Test(dataProvider = "dataProviderContactsFile", dataProviderClass = ContactDP.class)
    public void addNewContactPositiveTestDP(Contact contact) {
        addPage.typeAddNewContactFrom(contact);
        int sizeAfterAdd = contactsPage.getContactsListSize();
        System.out.println(sizeBeforeAdd + "X" + sizeAfterAdd);
        Assert.assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }

    @Test(invocationCount = 1)
    public void addNewContactPositiveTest_useFindElements() {
        Contact contact = Contact.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .phone("0123456789")
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        int sizeAfterAdd = contactsPage.getContactsListSizeUseFindElement();
        System.out.println(sizeBeforeAdd + "X" + sizeAfterAdd);
        Assert.assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }

    @Test
    public void addNewContactPositiveTest_validateContactNamePhone() {
        Contact contact = Contact.builder()
                .name("Name-" + generateString(8))
                .lastName(generateString(10))
                .phone(generatePhone(10))
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        Assert.assertTrue(contactsPage.validateContactNamePhone(contact.getName(),
                contact.getPhone()));
    }

    @Test(invocationCount = 1)
    public void addNewContactNegativeTest_emptyName() {
        Contact contact = Contact.builder()
                .name("")
                .lastName(generateString(10))
                .phone("0123456789")
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        Assert.assertTrue(addPage.validateURL("add"));
    }

    @Test(invocationCount = 1)
    public void addNewContactNegativeTest_emptyLast() {
        Contact contact = Contact.builder()
                .name(generateString(10))
                .lastName("")
                .phone("0123456789")
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        Assert.assertTrue(addPage.isUrlNotContains("contacts"));
    }

    @Test(invocationCount = 1)
    public void addNewContactNegativeTest_emptyPhone() {
        Contact contact = Contact.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone("")
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        //Assert.assertEquals(" Phone not valid: Phone number must contain only digits! And length min 10, max 15!",
        //addPage.closeAlertReturnText());
        Assert.assertTrue(addPage.closeAlertReturnText().contains("Phone number must contain only digits!"));
    }

    @Test(invocationCount = 1)
    public void addNewContactNegativeTest_emptyEmail() {
        Contact contact = Contact.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .phone("0123456789")
                .email("")
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        Assert.assertTrue(addPage.isUrlNotContains("contacts"));
    }

    @Test(invocationCount = 1)
    public void addNewContactNegativeTest_existPhone() {
        Contact contact = Contact.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .phone(existPhone)
                .email(generateEmail(10))
                .address("Haifa " + generateString(10))
                .description("desc " + generateString(15))
                .build();
        addPage.typeAddNewContactFrom(contact);
        Assert.assertTrue(addPage.isUrlNotContains("contacts"));
    }

}

