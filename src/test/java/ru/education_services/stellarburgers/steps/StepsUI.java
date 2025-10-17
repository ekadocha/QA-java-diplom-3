package ru.education_services.stellarburgers.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class StepsUI {

    private final WebDriver webDriver;

    public StepsUI(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Step("Открыть страницу {0}")
    public StepsUI open(String url) {
        webDriver.get(url);
        return this;
    }

    @Step("Кликнуть на элемент {0}")
    public StepsUI click(By element) {
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView();", webDriver.findElement(element));
        new WebDriverWait(webDriver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
        return this;
    }

    @Step("Ввести в поле {0} текст '{1}'")
    public StepsUI inputTextField(By element, String text) {
        webDriver.findElement(element).sendKeys(text);
        return this;
    }

    @Step("Проверить, что элемент {0} отображается на странице")
    public boolean isShown(By element) {
        return !webDriver.findElements(element).isEmpty();
    }

    @Step("Подождать, пока элемент {0} не появится на странице")
    public StepsUI waitUntilVisible (By element) {
        new WebDriverWait(webDriver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(element));
        return this;
    }

    @Step("Проверить, находится ли элемент {1} внутри элемента {0}")
    public boolean isChildInParentCoordinates (By parent, By child) {
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(4))
                    .until(driver -> {
                        Rectangle parentRect = webDriver.findElement(parent).getRect();
                        Rectangle childRect = webDriver.findElement(child).getRect();
                        return childRect.x >= parentRect.x &&
                                childRect.y >= parentRect.y &&
                                childRect.x + childRect.width <= parentRect.x + parentRect.width &&
                                childRect.y + childRect.height <= parentRect.y + parentRect.height;
                    });
            return true; // условие выполнилось
        } catch (TimeoutException e) {
            return false; // не дождались за отведённое время
        }
    }
}
