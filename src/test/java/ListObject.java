import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ListObject extends BaseObject{

    public ListObject(WebElement elem)
    {
        super(elem);
    }

    public String getName()
    {
        return root.findElement(By.xpath("input[@type='submit']")).getAttribute("value");
    }
    public String getId()
    {
        return root.findElement(By.xpath("input[@name='list_id']")).getAttribute("value");
    }
}
