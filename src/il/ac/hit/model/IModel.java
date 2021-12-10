package il.ac.hit.model;

import il.ac.hit.CostManager;
import il.ac.hit.exceptions.CostManagerException;

import java.util.Collection;
import java.util.Date;

public interface IModel
{
    public void addNewCategory(String category) throws CostManagerException;
    public void removeExistingCategory(int id) throws CostManagerException;
    public void addNewCost(CostManager cost) throws CostManagerException;
    public void removeExistingCost(int id) throws CostManagerException;
    public Collection<CostManager> getReportByDates(Date startDate, Date endDate) throws CostManagerException;
    public Collection<CostManager> getAllExpenses() throws CostManagerException;
}
