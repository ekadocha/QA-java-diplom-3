package ru.education_services.stellarburgers;

import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import ru.education_services.stellarburgers.model.User;
import ru.education_services.stellarburgers.pom.ForgotPasswordPage;
import ru.education_services.stellarburgers.pom.LoginPage;
import ru.education_services.stellarburgers.pom.MainPage;
import ru.education_services.stellarburgers.steps.StepsAPI;
import ru.education_services.stellarburgers.steps.StepsUI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static ru.education_services.stellarburgers.generators.UserGenerator.randomUser;

public class ForgotPasswordTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String FORGOT_PASSWORD_URL = appConfig.baseUrl() + "forgot-password";
    private static final LoginPage loginPage = new LoginPage();
    private static final ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
    private static final MainPage mainPage = new MainPage();
    private final StepsAPI stepsAPI = new StepsAPI();
    private WebDriver driver;
    private String token;
    private User user;
    private StepsUI steps;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        steps = new StepsUI(driver);
        user = randomUser();
        Response responseRegister = stepsAPI.sendPostRequestAuthRegister(user);
        responseRegister.then().statusCode(200);
        token = responseRegister.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Страница восстановления пароля. Можно авторизоваться при клике на ссылку 'Войти'")
    public void successLoginFromRegisterPageTest() {

        steps.open(FORGOT_PASSWORD_URL)
                .click(forgotPasswordPage.getHrefLogin())
                .waitUntilVisible(loginPage.getHeaderLogin());

        steps.inputTextField(loginPage.getInputEmail(), user.getEmail())
                .inputTextField(loginPage.getInputPassword(), user.getPassword())
                .click(loginPage.getButtonLogin())
                .waitUntilVisible(mainPage.getButtonCreateOrder());

        assertEquals(appConfig.baseUrl(), driver.getCurrentUrl());

    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        if (token != null) {
            stepsAPI.sendDeleteRequestAuthUser(token);
        }
    }
}
