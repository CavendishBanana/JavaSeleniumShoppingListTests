import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ListPage extends BasePage{
    private By returnToUserProfilePageBy;
    private By h2TagBy;

    public ListPage(WebDriver driver)
    {
        super(driver);
        returnToUserProfilePageBy = By.xpath("//a[contains(text(),'powrót')]");
        h2TagBy = By.tagName("h2");
    }

    public void returnToUserProfile()
    {
        driver.findElement(returnToUserProfilePageBy).click();
        waitUntilTitleVisible(defaultDuraionMiliseconds);
    }
    public boolean listHasGivenTitle(String title)
    {
        List<WebElement> headers = driver.findElements(h2TagBy);
        return headers.get(0).getText().equals(title);
    }
}
