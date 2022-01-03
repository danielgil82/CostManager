package il.ac.hit.model;

/**
 * The category class represents a mapping class that represents the categories' table in the database.
 */
public class Category
{   // category name
    private String categoryName;
    // the id of the user that has that category.
    private int userID;

    /**
     * ctor that gets the name of the category, and the id of the user
     * @param categoryName category's name
     * @param userID user id
     */
    public Category(String categoryName, int userID)
    {
        setCategoryName(categoryName);
        setUserID(userID);
    }

    /**
     * get the category name
     * @return name
     */
    public String getCategoryName()
    {
        return categoryName;
    }

    /**
     * get the user id
     * @return the user id
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     * set the category name
     * @param categoryName the category name
     */
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    /**
     * set the user id
     * @param userID the user id
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