package il.ac.hit.model;

import il.ac.hit.Expense;
import il.ac.hit.exceptions.CostManagerException;

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

            for (Expense expense: expenseList)
            {
                System.out.println(expense);
                System.out.println();
            }

            int numberOfRowsThatAffected = model.addNewCategory("clothes", 1500);
            System.out.println(numberOfRowsThatAffected);
        }
        catch (CostManagerException ex)
        {
            System.out.println(ex.getMessage() + '\n' + ex.getCause());
        }
    }

}
