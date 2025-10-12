package ru.education_services.stellarburgers.pom;
import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public class AccountProfilePage {

    private final By hrefProfile = By.xpath(".//a[text()='Профиль']");
    private final By hrefConstructor = By.xpath(".//p[text()='Конструктор']/parent::a");
    private final By hrefSiteLogo = By.xpath(".//div[starts-with(@class, 'AppHeader_header__logo')]/a");
    private final By buttonExit = By.xpath(".//button[text()='Выход']");

}
