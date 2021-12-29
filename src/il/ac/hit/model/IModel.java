package il.ac.hit.model;

import java.util.Collection;

public interface IModel
{
     User getUser(String userFullName, String userPassword) throws CostManagerException;
     int addNewUserToDB(User user) throws CostManagerException;
     int addNewCategory(Category newCategoryToAdd) throws CostManagerException;
     int removeExistingCategory(Category categoryToDelete) throws CostManagerException;
     int addNewExpense(Expense cost) throws CostManagerException;
     int removeExistingExpense(int id) throws CostManagerException;
     Collection<Expense> getReportByDates(int userID, java.sql.Date startDate, java.sql.Date endDate) throws CostManagerException;
     Collection<Expense> getAllExpenses(int userID) throws CostManagerException;
     Collection<String> getCategoriesBySpecificUser(int userId) throws CostManagerException;
}
