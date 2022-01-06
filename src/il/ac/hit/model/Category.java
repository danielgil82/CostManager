package il.ac.hit.model;

/**
 * The category class represents a mapping class that represents the categories' table in the database.
 */
public class Category
{
    /** Data members that represent the category table attributes. */
    private String categoryName;
    private int userID;

    /**
     * Ctor that gets the name of the category, and the id of the user.
     * @param categoryName - category's name
     * @param userID - user id
     */
    public Category(String categoryName, int userID)
    {
        setCategoryName(categoryName);
        setUserID(userID);
    }

    /**
     * Get the category name.
     * @return categoryName - category name
     */
    public String getCategoryName()
    {
        return categoryName;
    }

    /**
     * Get the user id.
     * @return userID - the user id
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     * Set the category name.
     * @param categoryName - the category name
     */
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    /**
     * Set the user id.
     * @param userID - the user id
     */
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