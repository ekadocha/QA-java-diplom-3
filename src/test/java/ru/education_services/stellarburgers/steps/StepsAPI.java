package ru.education_services.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.education_services.stellarburgers.clients.ApiClient;
import ru.education_services.stellarburgers.model.User;

public class StepsAPI {

    private ApiClient apiClient = new ApiClient();

    @Step("Отправить DELETE запрос на /api/auth/user")
    public Response sendDeleteRequestAuthUser(String token) {
        return apiClient.deleteUser(token);
    }

    @Step("Отправить POST запрос на /api/auth/login")
    public Response sendPostRequestAuthLogin(String email, String password) {
        return apiClient.loginUser(email, password);
    }

    @Step("Отправить POST запрос на /api/auth/register")
    public Response sendPostRequestAuthRegister(User user) {
        return apiClient.createUser(user);
    }
}
