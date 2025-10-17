package ru.education_services.stellarburgers.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import ru.education_services.stellarburgers.AppConfig;
import ru.education_services.stellarburgers.model.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);

    private static final String API_USER_INFO = "api/auth/user";
    private static final String API_LOGIN_USER = "api/auth/login";
    private static final String API_CREATE_USER = "/api/auth/register";

    public ApiClient() {
        RestAssured.baseURI = appConfig.baseUrl();
    }

    public Response deleteUser(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .when()
                .delete(API_USER_INFO);
    }

    public Response loginUser(String email, String password) {
        Map<String, String> userCreds = new HashMap<>(Map.of("email", email, "password", password));
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCreds)
                .when()
                .post(API_LOGIN_USER);
    }

    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(API_CREATE_USER);
    }
}
