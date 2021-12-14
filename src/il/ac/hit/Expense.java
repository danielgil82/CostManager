package il.ac.hit;

import java.sql.Date;

public class Expense
{
    private int expenseID;
    private int costSum;
    private String category;
    private String currency;
    private String expenseDescription;
    private Date purchaseDate;
    private int userID;

    public Expense(String category, int costSum, String currency, String descriptionOfExpense, Date purchaseDate, int userID)
    {
        setCategory(category);
        setCostSum(costSum);
        setCurrency(currency);
        setExpenseDescription(descriptionOfExpense);
        setPurchaseDate(purchaseDate);
        setUserID(userID);
    }

    public Expense(int expenseID, String category, int costSum, String currency, String descriptionOfExpense, Date purchaseDate, int userID)
    {
        this(category, costSum, currency, descriptionOfExpense, purchaseDate, userID);
        setExpenseID(expenseID);
//        setCategory(category);
//        setCostSum(costSum);
//        setCurrency(currency);
//        setExpenseDescription(descriptionOfExpense);
//        setPurchaseDate(purchaseDate);
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