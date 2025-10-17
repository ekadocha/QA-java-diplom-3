package ru.education_services.stellarburgers.pom;
import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public class ForgotPasswordPage {

    private final By hrefLogin = By.xpath(".//a[@href='/login']");

}
