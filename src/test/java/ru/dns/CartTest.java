package ru.dns;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartTest {
    static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().browserVersion("112").setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 1000));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    /**
     * Тест на добавление товара в корзину
     */
    @Test
    public void testAddCart() {
        //открыть карточку товара
        driver.get("https://www.dns-shop.ru/product/14ce97f73a32ed20/mys-besprovodnaa-lamzu-atlantis-belyj/");

        //добавить в корзину
        try {
            driver.findElement(By.cssSelector(".button-ui_brand")).click();
        } catch (NoSuchElementException e) {
            Assertions.fail("Кнопка \"Купить\" не найдена на странице!");
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,\"cart-link-counter__badge\")]")));

        //перейти в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        //проверка, что товар добавлен в корзину
        WebElement element = null;
        try {
            element = driver.findElement(By.cssSelector(".cart-link-counter__badge"));
        } catch (NoSuchElementException e) {
            Assertions.fail("Элемент \"Кол-во товаров над иконкой корзины\"не найден на странице");
        }
        String textElement = element.getText();
        String message = String.format("В корзине не верное кол-во товаров. Ожидалось %s. Получили %s.", "1", textElement);
        Assertions.assertEquals("1", textElement, message);

        //очистить корзину
        try {
            driver.findElement(By.cssSelector(".count-buttons__button_minus")).click();
        } catch (NoSuchElementException e) {
            Assertions.fail("Элемент \"-\", удаляющий товар из корзины, не найден на странице");
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".buttons__basket-text")));
    }

    /**
     * Тест на удаление товара из корзины
     */
    @Test
    public void testDeleteCart() {
        //открыть карточку товара
        driver.get("https://www.dns-shop.ru/product/14ce97f73a32ed20/mys-besprovodnaa-lamzu-atlantis-belyj/");

        //добавить в корзину
        try {
            driver.findElement(By.cssSelector(".button-ui_brand")).click();
        } catch (NoSuchElementException e) {
            Assertions.fail("Кнопка \"Купить\" не найдена на странице!");
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,\"cart-link-counter__badge\")]")));

        //перейти в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        //проверка, что в корзине есть 1 товар
        WebElement element = null;
        try {
            element = driver.findElement(By.cssSelector(".cart-link-counter__badge"));
        } catch (NoSuchElementException e) {
            Assertions.fail("Элемент \"Кол-во товаров над иконкой корзины\"не найден на странице");
        }
        String textElement = element.getText();
        String message = String.format("В корзине не верное кол-во товаров. Ожидалось %s. Получили %s.", "1", textElement);
        Assertions.assertEquals("1", textElement, message);

        //очистить корзину
        try {
            driver.findElement(By.cssSelector(".count-buttons__button_minus")).click();
        } catch (NoSuchElementException e) {
            Assertions.fail("Элемент \"-\", удаляющий товар из корзины, не найден на странице");
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".buttons__basket-text")));

        //проверка, что корзина очищена
        try {
            element = driver.findElement(By.cssSelector(".empty-message__title-empty-cart"));
        } catch (NoSuchElementException e) {
            Assertions.fail("Текстовая подпись \"Корзина пуста\" не найдена на странице");
        }
        textElement = element.getText();
        message = String.format("В корзине не верное кол-во товаров. Ожидалось %s. Получили %s.", "Корзина пуста", textElement);
        Assertions.assertEquals("Корзина пуста", textElement, message);
    }

    @AfterAll
    public static void close() {
        driver.quit();
    }
}
