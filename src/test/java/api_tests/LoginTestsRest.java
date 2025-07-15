package api_tests;

import dto.ErrorMessageDto;
import dto.TokenDto;
import dto.User;
import io.restassured.response.Response;
import manager.AuthenticationController;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseAPI;

import java.time.LocalDate;

public class LoginTestsRest extends AuthenticationController implements BaseAPI {

    SoftAssert softAssert = new SoftAssert();

    @Test(groups = "smoke")
    public void loginPositiveTest() {
        User user = new User("qa_mail@mail.com", "Qwerty123!");
        Response response = requestRegLogin(user, LOGIN_URL);
        System.out.println(response.getStatusLine());
        softAssert.assertEquals(response.getStatusCode(), 200);
        TokenDto tokenDto = response.body().as(TokenDto.class);
        System.out.println(tokenDto);
        softAssert.assertTrue(tokenDto.toString().contains("token"));
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_wrongPassword() {
        User user = new User("qa_mail@mail.com", "Qwerty123");
        Response response = requestRegLogin(user, LOGIN_URL);
        System.out.println(response.getStatusLine());
        softAssert.assertEquals(response.getStatusCode(), 401);
        ErrorMessageDto errorMessageDto = response.body().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
//        "error": "Unauthorized",
//                "message": "Login or Password incorrect",
//                "path": "/v1/user/login/usernamepassword"
        softAssert.assertTrue(errorMessageDto.getMessage().equals("Login or Password incorrect"));
        softAssert.assertTrue(errorMessageDto.getError().equals("Unauthorized"));
        softAssert.assertTrue(errorMessageDto.getPath().contains("login/usernamepassword"));
        //"timestamp": "2025-07-06T15:51:27"
        System.out.println(LocalDate.now().toString());
        System.out.println(errorMessageDto.getTimestamp().substring(0, 10));
        softAssert.assertEquals(LocalDate.now().toString(), errorMessageDto.getTimestamp().substring(0, 10));
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_wrongEmailFormat() {
        User user = new User("qa_mail", "Qwerty123!");
        Response response = requestRegLogin(user, LOGIN_URL);
        System.out.println(response.getStatusLine());
        softAssert.assertEquals(response.getStatusCode(), 401);
        ErrorMessageDto errorMessageDto = response.body().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getMessage().equals("Login or Password incorrect"));
        softAssert.assertTrue(errorMessageDto.getError().equals("Unauthorized"));
        softAssert.assertTrue(errorMessageDto.getPath().contains("login/usernamepassword"));
        System.out.println(LocalDate.now().toString());
        System.out.println(errorMessageDto.getTimestamp().substring(0, 10));
        softAssert.assertEquals(LocalDate.now().toString(), errorMessageDto.getTimestamp().substring(0, 10));
        softAssert.assertAll();
    }
}
