package il.ac.hit.model;

import il.ac.hit.Category;
import il.ac.hit.Expense;
import il.ac.hit.exceptions.CostManagerException;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

public class SimpleProgramForModel
{

    private static IModel model;

    public static void main(String args[])
    {
        try
        {
            model = new CostManagerModel();
            Collection<Expense> expenseList = model.getAllExpenses();
            for (Expense expense : expenseList)
            {
                System.out.println(expense);
                System.out.println();
            }

            int numberOfRowsThatAffected;
//            numberOfRowsThatAffected = model.addNewCategory(new Category("leisure", 2500));
//            System.out.println(numberOfRowsThatAffected);

//            numberOfRowsThatAffected = model.addNewExpense(new Expense("clothes", 180, "nis", "pizza",  new Date(new java.util.Date().getTime())));
//            System.out.println(numberOfRowsThatAffected);
//
            numberOfRowsThatAffected = model.removeExistingCategory("food");
            if (numberOfRowsThatAffected == 0)
            {
                System.out.println("0 rows affected because there is no such row with such category");
            }
            else if (numberOfRowsThatAffected == 1)
            {
                System.out.println("1 row was deleted successfully");
            }

            numberOfRowsThatAffected = model.removeExistingExpense(5);
            if (numberOfRowsThatAffected == 0)
            {
                System.out.println("0 rows affected because there is no such row with such id");
            }
            else if (numberOfRowsThatAffected == 1)
            {
                System.out.println("1 row was deleted successfully");
            }


        }
        catch (CostManagerException ex)
        {
            System.out.println(ex.getMessage() + '\n' + ex.getCause());
        }
    }
}
