package ru.education_services.stellarburgers;

import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import ru.education_services.stellarburgers.model.User;
import ru.education_services.stellarburgers.pom.LoginPage;
import ru.education_services.stellarburgers.pom.MainPage;
import ru.education_services.stellarburgers.pom.RegisterPage;
import ru.education_services.stellarburgers.steps.StepsAPI;
import ru.education_services.stellarburgers.steps.StepsUI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static ru.education_services.stellarburgers.generators.UserGenerator.randomUser;
import static ru.education_services.stellarburgers.generators.UserGenerator.randomUserWithShortPassword;

public class RegisterPageTests {

    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String REGISTER_URL = appConfig.baseUrl() + "register";
    private static final RegisterPage registerPage = new RegisterPage();
    private static final LoginPage loginPage = new LoginPage();
    private static final MainPage mainPage = new MainPage();
    private final StepsAPI stepsAPI = new StepsAPI();
    private WebDriver driver;
    private String token;
    private StepsUI steps;
    private User user;

    @BeforeEach
    public void setUp() {
        driver = createWebDriver();
        steps = new StepsUI(driver);
        user = randomUser();
    }

    @Test
    @DisplayName("Страница регистрации. Можно зарегистрироваться с валидными данными пользователя")
    public void successRegistrationWithValidCredsRedirectsToLoginPageTest() {

        steps.open(REGISTER_URL)
                .inputTextField(registerPage.getInputName(), user.getName())
                .inputTextField(registerPage.getInputEmail(), user.getEmail())
                .inputTextField(registerPage.getInputPassword(), user.getPassword())
                .click(registerPage.getButtonRegister())
                .waitUntilVisible(loginPage.getHeaderLogin());

        assertEquals(appConfig.baseUrl() + "login", driver.getCurrentUrl());

        Response responseLogin = stepsAPI.sendPostRequestAuthLogin(user.getEmail(), user.getPassword());
        responseLogin.then().statusCode(200);
        token = responseLogin.jsonPath().getString("accessToken");

    }

    @Test
    @DisplayName("Страница регистрации. Срабатывает валидация при попытке ввести пароль короче 6 символов")
    public void unsuccessRegistrationWithShortPasswordTest() {

        user = randomUserWithShortPassword();

        steps.open(REGISTER_URL)
                .inputTextField(registerPage.getInputName(), user.getName())
                .inputTextField(registerPage.getInputEmail(), user.getEmail())
                .inputTextField(registerPage.getInputPassword(), user.getPassword())
                .click(registerPage.getButtonRegister());

        assertTrue(steps.isShown(registerPage.getErrorMessageUnderPassword()), "Некорректный пароль");

    }

    @Test
    @DisplayName("Страница регистрации. Можно авторизоваться при клике на ссылку 'Войти'")
    public void successLoginFromRegisterPageTest() {

        Response responseRegister = stepsAPI.sendPostRequestAuthRegister(user);
        responseRegister.then().statusCode(200);
        token = responseRegister.jsonPath().getString("accessToken");

        steps.open(REGISTER_URL)
                .click(registerPage.getHrefLogin())
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
