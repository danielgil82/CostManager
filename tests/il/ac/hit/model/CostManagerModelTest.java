package il.ac.hit.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CostManagerModelTest {

    private CostManagerModel costManagerModel;

    /**
     * This test will happen before all tests every time we run the test.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @BeforeEach
    void setUp() throws CostManagerException {
        costManagerModel = new CostManagerModel();
    }

    /**
     * This test will happen after all tests and realise the allocated memory.
     *
     * @throws CostManagerException - - custom exception of our project.
     */
    @AfterEach
    void tearDown() throws CostManagerException {
        costManagerModel = null;
    }

    /**
     * Testing the add new category action - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void addNewCategory()throws CostManagerException {
        List<String> listOfCategories;
        Category categoryTest = new Category("test_name_2", 12);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int expected = listOfCategories.size() + 1;
        costManagerModel.addNewCategory(categoryTest);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int actual = listOfCategories.size();

        assertEquals(expected, actual);
    }

    /**
     * Testing the remove existing category action - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void removeExistingCategory()throws CostManagerException {
        List<String> listOfCategories;
        Category categoryTest = new Category("test_name_2", 12);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int expected = listOfCategories.size() - 1;
        costManagerModel.removeExistingCategory(categoryTest);
        listOfCategories = costManagerModel.getCategoriesNamesBySpecificUser(12);
        int actual = listOfCategories.size();

        assertEquals(expected, actual);
    }

    /**
     * Testing the remove costs by specific category - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void removeCostsBySpecificCategory()throws CostManagerException {
        List<Expense> listOfExpenses;
        Category category = new Category("food", 12);
        listOfExpenses = costManagerModel.getExpensesByCategory(12, category.getCategoryName());
        int expected = listOfExpenses.size() - 1;
        costManagerModel.removeCostsBySpecificCategory(category);
        listOfExpenses = costManagerModel.getExpensesByCategory(12, category.getCategoryName());
        int actual = listOfExpenses.size();

        assertEquals(expected, actual);
    }

    /**
     * Test add new cost - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void addNewCost() throws CostManagerException{
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

    /**
     * Testing if remove exiting cost works - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void removeExistingCost()throws CostManagerException {
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

    /**
     * Testing if we get the right costs between 2 given dates - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void getReportByDates() throws CostManagerException{
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

    /**
     * Testing the get user action - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void getUser()throws CostManagerException {
        User expected = new User(12, "marina", "1234");
        User actual = costManagerModel.getUser("marina", "1234");

        assertEquals(expected.getUserID(), actual.getUserID());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getUsersPassword(), actual.getUsersPassword());
    }

    /**
     * This test tests if we receive CostManagerException type of exception.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void checkIfTheUserExists() throws CostManagerException {
        User user = new User(12, "marina", "1234");

        assertThrows(CostManagerException.class, () -> costManagerModel.checkIfTheUserExists(user));
    }

    /**
     * Testing that a new user was added to the DB - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void addNewUserToDBAndUpdateTheListOfUsers()throws CostManagerException {
        User testUser = new User("shimi", "1234");
        int expected = costManagerModel.getListOfUsers().size() + 1;
        costManagerModel.addNewUserToDBAndUpdateTheListOfUsers(testUser);
        int actual  = costManagerModel.getListOfUsers().size();

        assertEquals(expected, actual);
    }

    /**
     * Testing the get expenses by a specific category - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void getExpensesByCategory() throws CostManagerException{
        String purchaseDate = "2022-01-19";
        Date date = Date.valueOf(purchaseDate);
        Expense expected = new Expense(21,"food", 12.5f,
                "nis", "pizza", date ,12);
        List<Expense> actual = costManagerModel.getExpensesByCategory(12, expected.getCategory());

        assertEquals(expected.getExpenseID(), actual.get(0).getExpenseID());
        assertEquals(expected.getCategory(), actual.get(0).getCategory());
        assertEquals(expected.getCostSum(), actual.get(0).getCostSum());
        assertEquals(expected.getCurrency(), actual.get(0).getCurrency());
        assertEquals(expected.getExpenseDescription(), actual.get(0).getExpenseDescription());
        assertEquals(expected.getPurchaseDate(), actual.get(0).getPurchaseDate());
        assertEquals(expected.getUserID(), actual.get(0).getUserID());
    }

    /**
     * Testing the get categories action by specific user - should work.
     *
     * @throws CostManagerException - custom exception of our project.
     */
    @Test
    void getCategoriesNamesBySpecificUser() throws CostManagerException {
        List<String> expected = Arrays.asList("electricty", "food");
        List<String> actual = costManagerModel.getCategoriesNamesBySpecificUser(12);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}