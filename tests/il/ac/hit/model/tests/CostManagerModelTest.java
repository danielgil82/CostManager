package il.ac.hit.model.tests;

import il.ac.hit.Category;
import il.ac.hit.Expense;
import il.ac.hit.exceptions.CostManagerException;
import il.ac.hit.model.CostManagerModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class CostManagerModelTest
{
    private CostManagerModel costManagerModel;

    @Before
    public void setUp() throws Exception
    {
        costManagerModel = new CostManagerModel();
    }

    @After
    public void tearDown() throws Exception
    {
        costManagerModel = null;
    }

    @Test
    public void addNewCategory() throws Exception
    {
        int expected = 1;

        try
        {
            int actual = costManagerModel.addNewCategory(new Category("bills", 5000));
            assertEquals(expected, actual, 0);
        }
        catch (CostManagerException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void removeExistingCategory()
    {
        int expected = 1;

        try
        {
            String categoryToRemove = "bills";
            int actual = costManagerModel.removeExistingCategory(categoryToRemove);
            assertEquals(expected, actual, 0);
        }
        catch (CostManagerException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void addNewExpense()
    {
        int expected = 1;

        try
        {
            Expense newExpense = new Expense("food", 30, "eur", "cake, burekas",  new Date(new java.util.Date().getTime()));
            int actual = costManagerModel.addNewExpense(newExpense);
            assertEquals(expected, actual, 0);
        }
        catch (CostManagerException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void removeExistingExpense()
    {
        int expected = 1;

        try
        {
            int idToRemove = 10;
            int actual = costManagerModel.removeExistingExpense(idToRemove);
            assertEquals(expected, actual, 0);
        }
        catch (CostManagerException e)
        {
            e.printStackTrace();
        }
    }

//    @Test
//    public void getReportByDates()
//    {
//    }
//
//    @Test
//    public void getAllExpenses()
//    {
//    }
}