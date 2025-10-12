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

import static org.junit.jupiter.api.Assertions.*;
import static ru.education_services.stellarburgers.WebDriverCreator.createWebDriver;
import static ru.education_services.stellarburgers.generators.UserGenerator.randomUser;

public class MainPageTests {
    private static final AppConfig appConfig = ConfigFactory.create(AppConfig.class);
    private static final String MAIN_PAGE_URL = appConfig.baseUrl();
    private static final String LOGIN_URL = appConfig.baseUrl() + "login";
    private static final MainPage mainPage = new MainPage();
    private static final LoginPage loginPage = new LoginPage();
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
    @MethodSource("placeForLoginData")
    @DisplayName("Главная страница. Можно авторизоваться на главной странице через Личный Кабинет или кнопкой 'Войти в аккаунт'")
    public void successLoginFromMainPageTest(By placeForLogin) {

        steps.open(MAIN_PAGE_URL)
                .click(placeForLogin)
                .waitUntilVisible(loginPage.getHeaderLogin());

        steps.inputTextField(loginPage.getInputEmail(), user.getEmail())
                .inputTextField(loginPage.getInputPassword(), user.getPassword())
                .click(loginPage.getButtonLogin())
                .waitUntilVisible(mainPage.getButtonCreateOrder());

        assertEquals(appConfig.baseUrl(), driver.getCurrentUrl());

    }

    static Stream<Arguments> placeForLoginData() {
        return Stream.of(
                Arguments.of(mainPage.getButtonLogin()),
                Arguments.of(mainPage.getHrefPersonalCabinet())
        );
    }

    @Test
    @DisplayName("Главная страница. Можно перейти в личный кабинет авторизованным пользователем")
    public void openProfileFromMainPageTest() {

        steps.open(LOGIN_URL)
                .inputTextField(loginPage.getInputEmail(), user.getEmail())
                .inputTextField(loginPage.getInputPassword(), user.getPassword())
                .click(loginPage.getButtonLogin())
                .waitUntilVisible(mainPage.getButtonCreateOrder());

        steps.click(mainPage.getHrefPersonalCabinet())
                .waitUntilVisible(accountProfilePage.getHrefProfile());

        assertEquals(appConfig.baseUrl() + "account/profile", driver.getCurrentUrl());

    }

    @ParameterizedTest
    @MethodSource("placeForBunTransitionData")
    @DisplayName("Главная страница. Можно перейти к разделу 'Булки' из раздела 'Cоусы' и 'Начинки'")
    public void transitToBunListTest(By placeForBunTransition) {

        steps.open(MAIN_PAGE_URL)
                .click(placeForBunTransition)
                .click(mainPage.getButtonBun())
                .waitUntilVisible(mainPage.getButtonBunCurrent());

        assertTrue(steps.isChildInParentCoordinates(mainPage.getMenuContainer(), mainPage.getFirstBun()));

    }

    static Stream<Arguments> placeForBunTransitionData() {
        return Stream.of(
                Arguments.of(mainPage.getButtonSauce()),
                Arguments.of(mainPage.getButtonMainIngredient())
        );
    }

    @ParameterizedTest
    @MethodSource("placeForSauceTransitionData")
    @DisplayName("Главная страница. Можно перейти к разделу 'Соусы' из раздела 'Булки' и 'Начинки'")
    public void transitToSauceListTest(By placeForSauceTransition) {

        steps.open(MAIN_PAGE_URL)
                .click(mainPage.getButtonSauce())
                .click(placeForSauceTransition)
                .click(mainPage.getButtonSauce())
                .waitUntilVisible(mainPage.getButtonSauceCurrent());

        assertTrue(steps.isChildInParentCoordinates(mainPage.getMenuContainer(), mainPage.getFirstSauce()));

    }

    static Stream<Arguments> placeForSauceTransitionData() {
        return Stream.of(
                Arguments.of(mainPage.getButtonBun()),
                Arguments.of(mainPage.getButtonMainIngredient())
        );
    }

    @ParameterizedTest
    @MethodSource("placeForMainIngredientTransitionData")
    @DisplayName("Главная страница. Можно перейти к разделу 'Начинки' из раздела 'Булки' и 'Соусы'")
    public void transitToMainIngredientListTest(By placeForMainIngredientTransition) {

        steps.open(MAIN_PAGE_URL)
                .click(mainPage.getButtonMainIngredient())
                .click(placeForMainIngredientTransition)
                .click(mainPage.getButtonMainIngredient())
                .waitUntilVisible(mainPage.getButtonMainIngredientCurrent());

        assertTrue(steps.isChildInParentCoordinates(mainPage.getMenuContainer(), mainPage.getFirstMainIngredient()));

    }

    static Stream<Arguments> placeForMainIngredientTransitionData() {
        return Stream.of(
                Arguments.of(mainPage.getButtonBun()),
                Arguments.of(mainPage.getButtonSauce())
        );
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        if (token != null) {
            stepsAPI.sendDeleteRequestAuthUser(token);
        }
    }
}
