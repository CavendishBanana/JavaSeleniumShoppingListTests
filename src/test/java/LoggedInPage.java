import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LoggedInPage extends BasePage {

    //private WebDriver driver;
    private int userId;
    private String login, password, webpageURL;
    //private int defaultDuraionMiliseconds;
    private By  newListNameInputBy, newListCreateButtonBy, myListsHeaderBy, logoutButtonBy, sharedListsBy, myListsHeaderFollowingSiblingsBy;


    /*
    private void waitUntilTitleVisible(int durationMilliseconds)
    {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofMillis(durationMilliseconds));
        //wait.until(d -> revealed.isDisplayed());
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainTitleLocator));
    }
    */
    public LoggedInPage(WebDriver driver, String userLogin, String userPassword, int userId)
    {
        super(driver);
        defaultDuraionMiliseconds = 5000;

        login = userLogin;
        password = userPassword;
        userId = userId;
        webpageURL="http://127.0.0.1:8000/";
        this.driver = driver;
        mainTitleLocator = By.id("page_title_id");
        newListNameInputBy = By.id("new_list_name");
        newListCreateButtonBy = By.id("create_new_list_button");
        myListsHeaderBy = By.xpath("//h2[contains(text(),'Moje listy')]");
        sharedListsBy = By.xpath("//h2[contains(text(),'Udostępnione listy')]");
        myListsHeaderFollowingSiblingsBy = By.xpath( "//h2[text()='Moje listy']/following-sibling::*" );
        logoutButtonBy = By.id("logout_submit_button_id");
    }
    private void clickCookiesConsent()
    {
        String cookiesPopupId = "simple-cookie-consent";
        String cookiesPopupButtonId = "cookie-consent-button-id";
        By cookiesPopupBy = By.id(cookiesPopupId);
        By cookiesPopupButtonBy = By.id(cookiesPopupButtonId);
        List<WebElement> cookiePopupList =  driver.findElements(cookiesPopupBy);
        if(!cookiePopupList.isEmpty())
        {
            driver.findElement(cookiesPopupButtonBy).click();
        }
    }
    public void login()
    {


        String loginInputId = "user_login_login";
        String passwordInputId = "user_password_1_login";
        String loginButtonXpath = "//table[@id='login-table']//input[@type='submit']";

        By loginInputBy = By.id(loginInputId);
        By passwordInputBy = By.id(passwordInputId);
        By loginButtonBy = By.xpath(loginButtonXpath);

        driver.get(webpageURL);
        waitUntilTitleVisible(defaultDuraionMiliseconds);
        clickCookiesConsent();
        driver.findElement(loginInputBy).sendKeys(login);
        driver.findElement(passwordInputBy).sendKeys(password);
        driver.findElement(loginButtonBy).click();
        waitUntilTitleVisible(defaultDuraionMiliseconds);
    }

    public List<ListObject> getUsersOwnLists()
    {
        //WebElement myListsHeader = driver.findElement(myListsHeaderBy);
        List<WebElement> myListsHeaderFollowingSiblings = driver.findElements(myListsHeaderFollowingSiblingsBy);
        List<ListObject> lists = new LinkedList<>();
        for( WebElement elem : myListsHeaderFollowingSiblings )
        {
            if(elem.getTagName().equals("h2"))
            {
                break;
            }
            lists.add(new ListObject(elem));
        }
        return lists;
    }

    public List<ListObject> getListWithName(String searchedListName)
    {
        List<ListObject> userLists = getUsersOwnLists();
        return userLists.stream().filter(lst -> lst.getName().equals(searchedListName)).collect(Collectors.toList());
    }

    public WebElement getLogoutButton()
    {
        List<WebElement> logoutButtonList = driver.findElements(logoutButtonBy);
        if(!logoutButtonList.isEmpty())
        {
            return logoutButtonList.get(0);
        }
        return null;
    }

    public WebElement getMyListsHeader()
    {
        List<WebElement> myListsHeaderList = driver.findElements(myListsHeaderBy);
        if(!myListsHeaderList.isEmpty())
        {
            return myListsHeaderList.get(0);
        }
        return null;
    }

    public WebElement getNewListNameInput()
    {
        return driver.findElement(newListNameInputBy);
    }
    public WebElement getCreateNewListButton()
    {
        return driver.findElement(newListCreateButtonBy);
    }
}


/*
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
*/