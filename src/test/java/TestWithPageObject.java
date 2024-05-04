import org.checkerframework.checker.units.qual.C;
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

import java.lang.ref.Cleaner;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class TestWithPageObject {

    private  WebDriver driver;
    //private LoggedInPage userProfile;
    //private ListPage listPage;
    private static ListPage listPage;
    private static LoggedInPage userProfilePage;
    private static String connectionString;
    private static DatabaseConnection dbConnection;
    private static Cleaner cleaner;
    private static String login;
    private static String password;
    private static int userId;

    @BeforeAll
    public static void mainSetUp()
    {
        login = "login2";
        password = "Haslo123!";
        userId = 9;
        //cleaner = Cleaner.create();
        listPage = new ListPage(null);
        userProfilePage = new LoggedInPage(null, login, password, userId);
        connectionString = "jdbc:sqlite:C:\\Users\\krzys\\projects\\aplikacja_lista_zakopow\\aplikacja_lista_zakopow\\lista_zakopow\\db.sqlite3";

        try {
            //dbConnection = DatabaseConnection.getInstance(cleaner, connectionString);
            DatabaseConnection.setConnectionString(connectionString);
            dbConnection = DatabaseConnection.getInstance();

            System.out.println("Connection to database established");
        } catch (SQLException e) {
            System.out.println("Error connecting to database" + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp()
    {
        driver = new ChromeDriver();
        listPage.setDriver(driver);
        userProfilePage.setDriver(driver);
        userProfilePage.login();
    }

    @AfterEach
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void testLogin()
    {
        WebElement logoutButton = userProfilePage.getLogoutButton();
        WebElement myListsHeader = userProfilePage.getMyListsHeader();
        boolean elementsFromUserProfileFound = (logoutButton != null) && (myListsHeader != null);
        Assertions.assertTrue(elementsFromUserProfileFound);
    }

    private List<DBShoppingList> listsPresentInLeftAndNotPresentInRight(List<DBShoppingList> leftList, List<DBShoppingList> rightList)
    {
        return leftList.stream().filter( lst -> !rightList.contains(lst) ).collect(Collectors.toList());

    }

    @Test
    public void testAddList()
    {

        String newListName = "newList";
        List<DBShoppingList> listsInDBBefore = null;

        //get user lists before creating new one
        try {
            listsInDBBefore = dbConnection.getListsOfUserWithId(userId);
        } catch (SQLException e) {
            System.out.println("Error querying db - lists before: " + e.getMessage());
        }
        System.out.println("Lists names before:");
        listsInDBBefore.forEach(lst -> System.out.println(lst.getName()));
        System.out.println("===========================");

        //interaction with page - create new list
        int listsWithNameBefore = userProfilePage.getListWithName(newListName).size();
        userProfilePage.getNewListNameInput().sendKeys(newListName);
        userProfilePage.getCreateNewListButton().click();
        listPage.waitUntilTitleVisible();

        //get user lists after creating new lists and find the diffrence between before and after creating new list
        List<DBShoppingList> listsInDBAfter = null;
        try {
            listsInDBAfter = dbConnection.getListsOfUserWithId(userId);
        } catch (SQLException e) {
            System.out.println("Error querying db - lists after: " + e.getMessage());
        }

        System.out.println("Lists names After:");
        listsInDBAfter.forEach(lst -> System.out.println(lst.getName()));
        System.out.println("===========================");

        final List<DBShoppingList> finalListBefore = listsInDBBefore;
        List<DBShoppingList> listsDifference = listsInDBAfter.stream().filter( lst -> !finalListBefore.contains(lst) ).collect(Collectors.toList());


        Assertions.assertEquals(newListName ,listsDifference.get(0).getName(), "New list has incorrect name");
        Assertions.assertEquals(1, listsDifference.size(), "User has more than 1 newly created list");
        boolean listHasCorrectName =  listPage.listHasGivenTitle(newListName);
        listPage.returnToUserProfile();
        userProfilePage.waitUntilTitleVisible();
        int listsWithNameAfter = userProfilePage.getListWithName(newListName).size();
        Assertions.assertTrue(listHasCorrectName);
        Assertions.assertEquals(listsWithNameBefore + 1, listsWithNameAfter);

    }

    @AfterAll
    public static void mainTearDown()
    {
        try {
            dbConnection.manualConnectionClose();
            System.out.println("connection to db manually closed");
        } catch (SQLException e) {
            System.out.println("SQL exception when closing connection to db: " + e.getMessage());
        }
    }

}
