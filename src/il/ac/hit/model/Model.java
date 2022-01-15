package il.ac.hit.model;

import java.util.Collection;
import java.util.List;
import java.sql.Date;
/**
 * The interface that represents the model functionality.
 */
public interface Model
{
     User getUser(String userFullName, String userPassword) throws CostManagerException;
     void addNewUserToDBAndUpdateTheListOfUsers(User user) throws CostManagerException;
     void addNewCategory(Category newCategoryToAdd) throws CostManagerException;
     void removeExistingCategory(Category categoryToDelete) throws CostManagerException;
     void removeCostsBySpecificCategory(Category category) throws CostManagerException;
     void addNewCost(Expense cost) throws CostManagerException;
     void removeExistingCost(int costID) throws CostManagerException;
     List<Expense> getReportByDates(int userID, Date startDate, Date endDate) throws CostManagerException;
     List<Expense> getExpensesByCategory(int userID, String categoryType) throws CostManagerException;
     List<String> getCategoriesBySpecificUser(int userId) throws CostManagerException;
     boolean checkIfTheUserExists(User user) throws CostManagerException;
}
