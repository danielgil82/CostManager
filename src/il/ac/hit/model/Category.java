package il.ac.hit.model;

/**
 * The category class represents a mapping class that represents the categories' table in the database.
 */
public class Category
{
    private String categoryName;
    private int userID;

    public Category(String categoryName, int userID)
    {
        setCategoryName(categoryName);
        setUserID(userID);
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    @Override
    public String toString()
    {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                ", UserID= " + userID +
                '}';
    }
}