import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;


class DemoTest {

    @BeforeAll
    public static void setUp()
    {
        System.out.println("setUp method with beforeAll label");
    }

    @BeforeAll
    public static void setUp2()
    {
        System.out.println("!!! setUp2 method with beforeAll label");
    }

    @BeforeEach
    public void init()
    {
        System.out.println("init method with beforeEach label");
    }

    @AfterEach
    public void tearDown()
    {
        System.out.println("tearDown method with AfterEach label");
    }

    @AfterAll
    public static void finalTearDown()
    {
        System.out.println("finalTearDown with AfterAll label");
    }

    @Test
    @Order(2)
    void testPassed()
    {
        System.out.println("Start tests");
        Assertions.assertTrue(true);
        System.out.println("Tests done");
    }
    @Test
    @Order(1)
    void simpleTestCase()
    {
        System.out.println("Start test 2");
        WebDriver driver = new ChromeDriver();
        driver.get("http://127.0.0.1:8000/");
        String title = driver.getTitle();
        Assertions.assertEquals("Lista zakupów", title, "Title is not equal to expected title");
        Assertions.assertNotEquals("Lista zakupów!", title, "Title is not equal to expected title");
        driver.close();
        System.out.println("Test 2 done");
    }

    @Test
    void checkLoginFunctionality()
    {
        WebDriver driver = new ChromeDriver();
        driver.get("http://127.0.0.1:8000/");
        String cookiesPopupId = "simple-cookie-consent";
        String cookiesPopupButtonId = "cookie-consent-button-id";
        List<WebElement> cookiePopupList =  driver.findElements(By.id(cookiesPopupId));
        if(!cookiePopupList.isEmpty())
        {
            driver.findElement(By.id(cookiesPopupButtonId)).click();
            driver.findElement(By.id("user_login_login")).sendKeys("login2137");
            driver.findElement(By.id("user_password_1_login")).sendKeys("!Q2w3e4r5t6y");
            driver.findElement(By.xpath("//table[@id='login-table']//input[@type='submit']")).click();
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            //wait.until(d -> revealed.isDisplayed());
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Utwórz nową listę')]")));
            List<WebElement> h2CreateNewListElems = driver.findElements(By.xpath("//h2[contains(text(),'Utwórz nową listę')]"));
            List<WebElement> logoutButtonList = driver.findElements(By.id("logout_submit_button_id"));
            boolean h2Located = !h2CreateNewListElems.isEmpty();
            boolean logoutButtonLocated = !logoutButtonList.isEmpty();
            Assertions.assertTrue(h2Located && logoutButtonLocated, "Elements h2 Create new list and logout button were not found after login");
        }
        driver.close();
    }

}
