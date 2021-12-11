package il.ac.hit;

import java.sql.Date;

public class Expense {
    private int id;
    private int costSum;
    private String category;
    private String currency;
    private String descriptionOfExpense;
    private Date purchaseDate;

    public Expense(String category, int costSum, String currency, String descriptionOfExpense, Date purchaseDate) {
        this.category = category;
        this.costSum = costSum;
        this.currency = currency;
        this.descriptionOfExpense = descriptionOfExpense;
        this.purchaseDate = purchaseDate;
    }

    public Expense(int id, String category, int costSum, String currency, String descriptionOfExpense, Date purchaseDate) {
        this.id = id;
        this.category = category;
        this.costSum = costSum;
        this.currency = currency;
        this.descriptionOfExpense = descriptionOfExpense;
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCost_sum() {
        return costSum;
    }

    public void setCost_sum(int cost_sum) {
        this.costSum = cost_sum;
    }

    @Override
    public String toString()
    {
        return "Expense{" +
                "id=" + id +
                ", costSum=" + costSum +
                ", category='" + category + '\'' +
                ", currency='" + currency + '\'' +
                ", descriptionOfExpense='" + descriptionOfExpense + '\'' +
                ", purchaseDate=" + purchaseDate +
                '}';
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescriptionOfExpense() {
        return descriptionOfExpense;
    }

    public void setDescriptionOfExpense(String descriptionOfExpense) {
        this.descriptionOfExpense = descriptionOfExpense;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
