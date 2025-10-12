package ru.education_services.stellarburgers;

import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.education_services.stellarburgers.model.User;
import ru.education_services.stellarburgers.pom.AccountProfilePage;
import ru.education_services.stellarburgers.pom.LoginPage;
import ru.education_services.stellarburgers.pom.MainPage;
import ru.education_services.stellarburgers.steps.StepsAPI;
import ru.education_services.stellarburgers.steps.StepsUI;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static ru.education_services.stellarburgers.generators.UserGenerator.randomUser;

public class AccountProfileTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String LOGIN_URL = appConfig.baseUrl() + "login";
    private static final LoginPage loginPage = new LoginPage();
    private static final MainPage mainPage = new MainPage();
    private static final AccountProfilePage accountProfilePage = new AccountProfilePage();
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

    @ParameterizedTest
    @MethodSource("placeForConstructorTransitionData")
    @DisplayName("Личный кабинет. Можно перейти на страницу конструктора из личного кабинета")
    public void openMainPageFromPersonalCabinetTest(By placeForConstructorTransition) {

        steps.open(LOGIN_URL)
                .inputTextField(loginPage.getInputEmail(), user.getEmail())
                .inputTextField(loginPage.getInputPassword(), user.getPassword())
                .click(loginPage.getButtonLogin())
                .waitUntilVisible(mainPage.getButtonCreateOrder());

        steps.click(mainPage.getHrefPersonalCabinet())
                .click(placeForConstructorTransition)
                .waitUntilVisible(mainPage.getButtonCreateOrder());

        assertEquals(appConfig.baseUrl(), driver.getCurrentUrl());

    }

    static Stream<Arguments> placeForConstructorTransitionData() {
        return Stream.of(
                Arguments.of(accountProfilePage.getHrefConstructor()),
                Arguments.of(accountProfilePage.getHrefSiteLogo())
        );
    }

    @Test
    @DisplayName("Личный кабинет. Можно выйти из личного кабинета")
    public void exitFromPersonalCabinetTest() {

        steps.open(LOGIN_URL)
                .inputTextField(loginPage.getInputEmail(), user.getEmail())
                .inputTextField(loginPage.getInputPassword(), user.getPassword())
                .click(loginPage.getButtonLogin())
                .waitUntilVisible(mainPage.getButtonCreateOrder());

        steps.click(mainPage.getHrefPersonalCabinet())
                .waitUntilVisible(accountProfilePage.getButtonExit());

        steps.click(accountProfilePage.getButtonExit())
                .waitUntilVisible(loginPage.getHeaderLogin());

        assertEquals(LOGIN_URL, driver.getCurrentUrl());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        if (token != null) {
            stepsAPI.sendDeleteRequestAuthUser(token);
        }
    }
}
