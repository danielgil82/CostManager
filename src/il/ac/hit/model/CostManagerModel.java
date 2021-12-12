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
    public int addNewCategory(String category, int monthlyBudget) throws CostManagerException
    {
        String addNewCategoryQuery = "insert into categories (category_name, monthly_budget)"
                                                    + "value(?,?)";
        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            PreparedStatement addNewCategory = connection.prepareStatement(addNewCategoryQuery))
        {
            connection.setAutoCommit(false);
            addNewCategory.setString(1, category);
            addNewCategory.setInt(2, monthlyBudget);

            int numberOfRowsAffected = addNewCategory.executeUpdate();
            connection.commit();
//
            if (numberOfRowsAffected != 1)
            {
                throw new CostManagerException("Something went wrong.");
            }

            return numberOfRowsAffected;
        }
        catch (SQLException exception)
        {
            if (exception instanceof SQLIntegrityConstraintViolationException)
            {
                throw new CostManagerException("Oops seems like this category exists already, try another one", exception);
            }
            else if (connection != null)
            {
                System.err.print("Transaction is being rolled back");

                try
                {
                    connection.rollback();

                    return 0;
                }
                catch (SQLException ex)
                {
                    throw new CostManagerException("problem with rolling back.", ex);
                }
            }
            else
            {
                throw new CostManagerException("problem with adding new category.", exception);
            }
        }
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
