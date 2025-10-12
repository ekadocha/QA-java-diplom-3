package ru.education_services.stellarburgers.pom;

import org.openqa.selenium.By;
import lombok.Getter;

@Getter
public class MainPage {

    private final By buttonCreateOrder = By.xpath(".//button[text()='Оформить заказ']");
    private final By buttonLogin = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By hrefPersonalCabinet = By.xpath(".//p[text()='Личный Кабинет']");
    private final By buttonBun = By.xpath(".//span[text()='Булки']/parent::div");
    private final By buttonBunCurrent = By.xpath(".//span[text()='Булки']/parent::div[contains(@class, 'current')]");
    private final By firstBun = By.xpath(".//h2[text()='Булки']/following-sibling::ul[1]/a");
    private final By buttonSauce = By.xpath(".//span[text()='Соусы']/parent::div");
    private final By buttonSauceCurrent = By.xpath(".//span[text()='Соусы']/parent::div[contains(@class, 'current')]");
    private final By firstSauce = By.xpath(".//h2[text()='Соусы']/following-sibling::ul[1]/a");
    private final By buttonMainIngredient = By.xpath(".//span[text()='Начинки']/parent::div");
    private final By buttonMainIngredientCurrent = By.xpath(".//span[text()='Начинки']/parent::div[contains(@class, 'current')]");
    private final By firstMainIngredient = By.xpath(".//h2[text()='Начинки']/following-sibling::ul[1]/a");
    private final By menuContainer = By.xpath(".//div[contains(@class, 'menuContainer')]");

}
