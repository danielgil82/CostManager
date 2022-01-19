package il.ac.hit.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CostManagerModelTest {
    private CostManagerModel costManagerModel;

    @Before
    public void setUp() throws CostManagerException {
        costManagerModel = new CostManagerModel();
    }

    @After
    public void tearDown() throws CostManagerException {
        costManagerModel = null;
    }

    @Test
    public void addNewCategory() throws CostManagerException {
        List<String> listOfCategories;
        Category categoryTest = new Category("test_name_2", 12);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int expected = listOfCategories.size() + 1;
        costManagerModel.addNewCategory(categoryTest);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int actual = listOfCategories.size();
        assertEquals(expected, actual);
    }

    @Test
    public void removeExistingCategory() throws CostManagerException {
        List<String> listOfCategories;
        Category categoryTest = new Category("test_name_2", 12);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int expected = listOfCategories.size() - 1;
        costManagerModel.removeExistingCategory(categoryTest);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int actual = listOfCategories.size();
        assertEquals(expected, actual);
    }

    @Test
    public void removeCostsBySpecificCategory()throws CostManagerException  {
        List<Expense> listOfExpenses;
        Category category = new Category("food", 12);
        listOfExpenses = costManagerModel.getExpensesByCategory(12, category.getCategoryName());
        int expected = listOfExpenses.size() - 1;
        costManagerModel.removeCostsBySpecificCategory(category);
        listOfExpenses = costManagerModel.getExpensesByCategory(12, category.getCategoryName());
        int actual = listOfExpenses.size();

        assertEquals(expected, actual);
    }

    @Test
    public void addNewCost()throws CostManagerException  {
        List<Expense> listOfExpenses;
        Expense expense = new Expense("food", 12.5f, "nis",  "pizza",
                new java.sql.Date(new java.util.Date().getTime()),12 );
        listOfExpenses = costManagerModel.getExpensesByCategory(12, "all");
        int expected = listOfExpenses.size() + 1;
        costManagerModel.addNewCost(expense);
        listOfExpenses = costManagerModel.getExpensesByCategory(12, "all");
        int actual = listOfExpenses.size();

        assertEquals(expected, actual);
    }

    @Test
    public void removeExistingCost() throws CostManagerException {
        List<Expense> listOfExpenses;
        Expense expense = new Expense("food", 12.5f, "nis",  "pizza",
                new java.sql.Date(new java.util.Date().getTime()),12 );
        listOfExpenses = costManagerModel.getExpensesByCategory(12, "all");
        int expected = listOfExpenses.size() + 1;
        costManagerModel.addNewCost(expense);
        listOfExpenses = costManagerModel.getExpensesByCategory(12, "all");
        int actual = listOfExpenses.size();

        assertEquals(expected, actual);
    }

    @Test
    public void getReportByDates()throws CostManagerException  {
        String firstDate = "2022-01-18";
        String secondDate = "2022-01-19";
        Date dateOne = Date.valueOf(firstDate);//converting string into sql date
        Date dateTwo = Date.valueOf(secondDate);

        Expense expected = new Expense(21,"food", 12.5f,
                "nis", "pizza", dateTwo ,12);

        List<Expense> actual = new LinkedList<>();
        actual = costManagerModel.getReportByDates(12, dateOne, dateTwo);

        assertEquals(expected.getExpenseID(), actual.get(0).getExpenseID());
        assertEquals(expected.getCategory(), actual.get(0).getCategory());
        assertEquals(expected.getCostSum(), actual.get(0).getCostSum(), 0.01);
        assertEquals(expected.getCurrency(), actual.get(0).getCurrency());
        assertEquals(expected.getExpenseDescription(), actual.get(0).getExpenseDescription());
        assertEquals(expected.getPurchaseDate(), actual.get(0).getPurchaseDate());
        assertEquals(expected.getUserID(), actual.get(0).getUserID());
    }

    @Test
    public void getUser() throws CostManagerException  {
        User expected = new User(12, "marina", "1234");
        User actual = costManagerModel.getUser("marina", "1234");
        assertEquals(expected.getUserID(), actual.getUserID());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getUsersPassword(), actual.getUsersPassword());

    }

    @Test
    public void checkIfTheUserExists() throws CostManagerException  {
        User user = new User(12, "marina", "1234");
        boolean expected = true;
        boolean actual = costManagerModel.checkIfTheUserExists(user);
//        assertThrows(expected, actual);
    }

    @Test
    public void addNewUserToDBAndUpdateTheListOfUsers() throws CostManagerException {
    }

    @Test
    public void getExpensesByCategory()throws CostManagerException  {
    }

    @Test
    public void getCategoriesBySpecificUser() throws CostManagerException {
    }
}