package il.ac.hit.model;

import il.ac.hit.Category;
import il.ac.hit.Expense;
import il.ac.hit.exceptions.CostManagerException;

import java.util.Collection;
import java.util.Date;

public interface IModel
{
    public int addNewCategory(Category newCategoryToAdd) throws CostManagerException;
    public void removeExistingCategory(int id) throws CostManagerException;
    public int addNewExpense(Expense cost) throws CostManagerException;
    public void removeExistingExpense(int id) throws CostManagerException;
    public Collection<Expense> getReportByDates(Date startDate, Date endDate) throws CostManagerException;
    public Collection<Expense> getAllExpenses() throws CostManagerException;
}
