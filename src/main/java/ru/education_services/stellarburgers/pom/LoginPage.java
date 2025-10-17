package ru.education_services.stellarburgers.pom;
import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public class LoginPage {

    private final By headerLogin = By.xpath(".//h2[text()='Вход']");
    private final By inputEmail = By.xpath(".//label[text()='Email']/following-sibling::input");
    private final By inputPassword = By.xpath(".//label[text()='Пароль']/following-sibling::input");
    private final By buttonLogin = By.xpath(".//button[text()='Войти']");

}
