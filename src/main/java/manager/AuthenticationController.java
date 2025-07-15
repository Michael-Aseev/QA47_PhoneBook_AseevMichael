package manager;

import dto.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.BaseAPI;

import static io.restassured.RestAssured.given;
import static utils.PropertiesReader.*;

public class AuthenticationController implements BaseAPI {

    /* given()
        .header("Authorisation", "value")
        .body(user)
    .when()
        .post(url)
    .then()   thenReturn()  --> response

    Request
    Response
     */

    public Response requestRegLogin(User user, String url) {
        return given()
                .baseUri(getProperty("login.properties", "baseUri"))
                .contentType(ContentType.JSON)// Content-type : App/json
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(url)
                .thenReturn()
                ;

    }
}
