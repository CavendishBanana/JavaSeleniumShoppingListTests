import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected By mainTitleLocator;
    protected WebDriver driver;
    protected int defaultDuraionMiliseconds;

    public void setDriver(WebDriver driver)
    {
        this.driver = driver;
    }
    protected BasePage(WebDriver driver)
    {
        this.driver = driver;
        defaultDuraionMiliseconds = 5000;
        mainTitleLocator = By.id("page_title_id");

    }

    protected void waitUntilTitleVisible(int durationMilliseconds)
    {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofMillis(durationMilliseconds));
        //wait.until(d -> revealed.isDisplayed());
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainTitleLocator));
    }

    protected void waitUntilTitleVisible()
    {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofMillis(defaultDuraionMiliseconds));
        //wait.until(d -> revealed.isDisplayed());
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainTitleLocator));
    }
}
