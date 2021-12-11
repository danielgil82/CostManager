package il.ac.hit.model;

import il.ac.hit.Expense;
import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;
import il.ac.hit.exceptions.CostManagerException;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CostManagerModel implements IModel , IErrorAndExceptionsHandlingStrings
{
    private final String driverFullQualifiedName = "com.mysql.jdbc.Driver";
    private final String connectionStringToDB = "jdbc:mysql://localhost:3306/costmanagerproj";
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;


    public CostManagerModel() throws CostManagerException
    {
        try
        {
            Class.forName(driverFullQualifiedName);
        }
        catch (ClassNotFoundException ex)
        {
            throw new CostManagerException("Problem with registering driver to the driver manager", ex);
        }
    }

    @Override
    public void addNewCategory(String category) throws CostManagerException
    {

    }

    @Override
    public void removeExistingCategory(int id) throws CostManagerException
    {
        throw new CostManagerException("Bad");
    }

    @Override
    public void addNewCost(Expense cost) throws CostManagerException
    {
        throw new CostManagerException("Bad");
    }

    @Override
    public void removeExistingCost(int id) throws CostManagerException
    {
        throw new CostManagerException("Bad");
    }

    @Override
    public Collection<Expense> getReportByDates(Date startDate, Date endDate) throws CostManagerException
    {
        throw new CostManagerException("Bad");
    }

    @Override
    public Collection<Expense> getAllExpenses() throws CostManagerException
    {
        try
        {
            //Creating a connection string
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");

            //Getting a statement object
            statement = connection.createStatement();

            //performing simple query
            resultSet = statement.executeQuery("SELECT * FROM costs");

            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next())
            {
                costExpensesList.add(new Expense
                                (resultSet.getInt("id"),
                                resultSet.getString("category"),
                                resultSet.getInt("sum_cost"),
                                resultSet.getString("currency"),
                                resultSet.getString("description"),
                                resultSet.getDate("date")));
            }

            return costExpensesList;
        }
        catch (SQLException ex)
        {
            throw new CostManagerException("Couldn't get all expenses", ex);
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch (SQLException ex)
                {
                    throw new CostManagerException("Couldn't get all results", ex);
                }
            }
            if (statement != null)
            {
                try
                {
                    statement.close();
                }
                catch (SQLException ex)
                {
                    throw new CostManagerException("problem with statement", ex);
                }
            }

            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException ex)
                {
                    throw new CostManagerException("Something went wrong.", ex);
                }
            }
        }
    }
}
