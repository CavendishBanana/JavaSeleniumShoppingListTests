import java.time.LocalDate;
import java.time.LocalDateTime;

public class DBShoppingList {
    private String name;
    private int ownerId;
    private int id;
    private LocalDate createDate;

    public int getId() { return  id;}
    public int getOwnerId() { return ownerId; }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public String getName() {
        return name;
    }

    public DBShoppingList(int id, int ownerId, LocalDate createDate, String name)
    {
        this.id = id;
        this.ownerId = ownerId;
        this.createDate = createDate;
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(!(o instanceof DBShoppingList))
        {
            return false;
        }

        DBShoppingList other = (DBShoppingList) o;
        return other.getId() == getId();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
