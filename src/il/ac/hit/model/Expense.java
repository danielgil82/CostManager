package il.ac.hit.model;

import java.sql.Date;

/**
 * The Expense class represents a mapping class that represents the costs' table in the database.
 */
public class Expense
{
    private int expenseID;
    private int costSum;
    private String category;
    private String currency;
    private String expenseDescription;
    private Date purchaseDate;
    private int userID;

    /**
     * ctor that recieves parameter of an Expense excluded the expenseID.
     * @param category category of the expense.
     * @param costSum sum of the expense.
     * @param currency currency of the expense.
     * @param descriptionOfExpense description of the expense.
     * @param purchaseDate date of the expense.
     * @param userID the user that made the purchase.
     */
    public Expense(String category, int costSum, String currency, String descriptionOfExpense, Date purchaseDate, int userID)
    {
        setCategory(category);
        setCostSum(costSum);
        setCurrency(currency);
        setExpenseDescription(descriptionOfExpense);
        setPurchaseDate(purchaseDate);
        setUserID(userID);
    }

    /**
     * ctor that recieves parameter of an Expense included the expenseID .
     * @param expenseID the id of the expense.
     * @param category category of the expense.
     * @param costSum sum of the expense.
     * @param currency currency of the expense.
     * @param descriptionOfExpense description of the expense.
     * @param purchaseDate date of the expense.
     * @param userID the user that made the purchase.
     */
    public Expense(int expenseID, String category, int costSum, String currency, String descriptionOfExpense, Date purchaseDate, int userID)
    {
        this(category, costSum, currency, descriptionOfExpense, purchaseDate, userID);
        setExpenseID(expenseID);
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getExpenseID()
    {
        return expenseID;
    }

    public String getCategory()
    {
        return category;
    }

    public int getCostSum()
    {
        return costSum;
    }

    public String getCurrency()
    {
        return currency;
    }

    public String getExpenseDescription()
    {
        return expenseDescription;
    }

    public Date getPurchaseDate()
    {
        return purchaseDate;
    }

    public void setExpenseID(int expenseID)
    {
        this.expenseID = expenseID;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setCostSum(int cost_sum)
    {
        this.costSum = cost_sum;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public void setExpenseDescription(String expenseDescription)
    {
        this.expenseDescription = expenseDescription;
    }

    public void setPurchaseDate(Date purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString()
    {
        return "Expense{" +
                "id=" + expenseID +
                ", costSum=" + costSum +
                ", category='" + category + '\'' +
                ", currency='" + currency + '\'' +
                ", descriptionOfExpense='" + expenseDescription + '\'' +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}