import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallbackTest {

    private WebDriver driver;

    @BeforeAll
    public static void SetUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void SetUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Ильхом Додов");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79997778844");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expected, actualText);
    }

    @Test
    public void shouldShowValidationErrorWrongNameField() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Ilkhom Dodov");
        driver.findElement(By.tagName("button")).click();
        String validationErrorTextForWrongName = driver.findElement(By.cssSelector("span.input_invalid .input__sub")).getText();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(validationErrorTextForWrongName, expected);
    }

    @Test
    public void shouldShowValidationErrorWrongNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Ильхом Додов");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+7999777884");
        driver.findElement(By.tagName("button")).click();
        String validationErrorTextForWrongPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expected, validationErrorTextForWrongPhone);
    }

    @Test
    public void shouldShowValidationErrorUncheckedCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Ильхом Додов");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79997778844");
        driver.findElement(By.tagName("button")).click();

        String validationErrorTextForUncheckedCheckbox = driver.findElement(By.cssSelector(".input_invalid .checkbox__text")).getText().trim();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";

        assertEquals(expected, validationErrorTextForUncheckedCheckbox);
    }

    @Test
    public void shouldShowValidationErrorForBlankField() {
        driver.get("http://localhost:9999");
        driver.findElement(By.tagName("button")).click();
        String validationErrorTextForBlankField = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText().trim();

        String expected = "Поле обязательно для заполнения";

        assertEquals(expected, validationErrorTextForBlankField);
    }
}
