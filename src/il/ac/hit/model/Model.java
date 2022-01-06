package il.ac.hit.model;

import java.util.Collection;
import java.util.List;

/**
 * the interface that represents the model functionality
 */
public interface Model
{
     User getUser(String userFullName, String userPassword) throws CostManagerException;
     void addNewUserToDBAndUpdateTheListOfUsers(User user) throws CostManagerException;
     int addNewCategory(Category newCategoryToAdd) throws CostManagerException;
     int removeExistingCategory(Category categoryToDelete) throws CostManagerException;
     int addNewExpense(Expense cost) throws CostManagerException;
     int removeExistingExpense(int id) throws CostManagerException;
     Collection<Expense> getReportByDates(int userID, java.sql.Date startDate, java.sql.Date endDate) throws CostManagerException;
     Collection<Expense> getAllExpenses(int userID) throws CostManagerException;
     List<String> getCategoriesBySpecificUser(int userId) throws CostManagerException;
     boolean checkIfTheUserExists(User user) throws CostManagerException;
}
