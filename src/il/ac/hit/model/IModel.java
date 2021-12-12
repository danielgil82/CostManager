package il.ac.hit.model;

import il.ac.hit.Expense;
import il.ac.hit.exceptions.CostManagerException;

import java.util.Collection;
import java.util.Date;

public interface IModel
{
    public int addNewCategory(String category, int monthlyBudget) throws CostManagerException;
    public void removeExistingCategory(int id) throws CostManagerException;
    public void addNewCost(Expense cost) throws CostManagerException;
    public void removeExistingCost(int id) throws CostManagerException;
    public Collection<Expense> getReportByDates(Date startDate, Date endDate) throws CostManagerException;
    public Collection<Expense> getAllExpenses() throws CostManagerException;
}
