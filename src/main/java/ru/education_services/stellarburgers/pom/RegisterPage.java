package ru.education_services.stellarburgers.pom;

import org.openqa.selenium.By;
import lombok.Getter;

@Getter
public class RegisterPage {

    private final By inputName = By.xpath(".//label[text()='Имя']/following-sibling::input");
    private final By inputEmail = By.xpath(".//label[text()='Email']/following-sibling::input");
    private final By inputPassword = By.xpath(".//label[text()='Пароль']/following-sibling::input");
    private final By buttonRegister = By.xpath(".//button[text()='Зарегистрироваться']");
    private final By errorMessageUnderPassword = By.xpath(".//p[contains(@class, 'input__error')]");
    private final By hrefLogin = By.xpath(".//a[@href='/login']");

}
