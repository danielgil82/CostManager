package il.ac.hit.model;

import il.ac.hit.Category;
import il.ac.hit.Expense;
import il.ac.hit.exceptions.CostManagerException;

import java.util.Collection;
import java.util.Date;

public interface IModel
{
     int addNewCategory(Category newCategoryToAdd) throws CostManagerException;
     int removeExistingCategory(Category categoryToDelete) throws CostManagerException;
     int addNewExpense(Expense cost) throws CostManagerException;
     int removeExistingExpense(int id) throws CostManagerException;
     Collection<Expense> getReportByDates(int userID, java.sql.Date startDate, java.sql.Date endDate) throws CostManagerException;
     Collection<Expense> getAllExpenses(int userID) throws CostManagerException;
}
