package il.ac.hit;

public class Category
{
    private String categoryName;
    private int monthlyBudget;

    public Category(String categoryName, int monthlyBudget)
    {
        this.categoryName = categoryName;
        this.monthlyBudget = monthlyBudget;
    }

    @Override
    public String toString()
    {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                ", monthlyBudget=" + monthlyBudget +
                '}';
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public int getMonthlyBudget()
    {
        return monthlyBudget;
    }

    public void setMonthlyBudget(int monthlyBudget)
    {
        this.monthlyBudget = monthlyBudget;
    }
}
