import org.openqa.selenium.WebElement;

public abstract class BaseObject {
    protected WebElement root;

    public BaseObject(WebElement root) {
        this.root = root;
    }
}