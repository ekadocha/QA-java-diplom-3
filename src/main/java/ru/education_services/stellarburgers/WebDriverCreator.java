package ru.education_services.stellarburgers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class WebDriverCreator {

        /*
    Переменные окружения, прописанные в системе:
    YANDEX_BROWSER_DRIVER_PATH - путь до файла драйвера Яндекс браузера (хромдрайвера нужной версии)
    YANDEX_BROWSER_PATH - путь к исполняемому файлу Яндекс браузера в системе
    Для применения изменения переменных окружения обязательно перезапустить IDEA
     */

    public static WebDriver createWebDriver() {
        String browser = System.getProperty("browser");
        if (browser == null) {
            return createChromeDriver();
        }

        switch (browser) {
            case "yandex":
                return createYandexDriver();
            case "chrome":
            default:
                return createChromeDriver();
        }
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        return webDriver;
    }

    private static WebDriver createYandexDriver() {
        System.setProperty("webdriver.chrome.driver", System.getenv("YANDEX_BROWSER_DRIVER_PATH"));
        ChromeOptions options = new ChromeOptions();
        options.setBinary(System.getenv("YANDEX_BROWSER_PATH"));
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        return webDriver;
    }
}
