package il.ac.hit.model;

import il.ac.hit.Category;
import il.ac.hit.Expense;
import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;
import il.ac.hit.exceptions.CostManagerException;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class CostManagerModel implements IModel, IErrorAndExceptionsHandlingStrings
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
    public int addNewCategory(Category category) throws CostManagerException
    {
        String addNewCategoryQuery = "insert into categories (category_name, monthly_budget)"
                + "value(?,?)";
        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement addNewCategory = connection.prepareStatement(addNewCategoryQuery))
        {
            connection.setAutoCommit(false);
            addNewCategory.setString(1, category.getCategoryName());
            addNewCategory.setInt(2, category.getMonthlyBudget());
            int numberOfRowsAffected = addNewCategory.executeUpdate();
            connection.commit();
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
                throw new CostManagerException("Oops seems like this category already exists, try another one", exception);
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
    public int removeExistingCategory(String categoryToDelete) throws CostManagerException
    {
        String removeExistingCategoryQuery = "delete from categories where category_name = ?";
        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement prepareStatement = connection.prepareStatement(removeExistingCategoryQuery))
        {
            connection.setAutoCommit(false);
            prepareStatement.setString(1, categoryToDelete);

            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();

            return numberOfRowsAffected;
        }
        catch (SQLException exception)
        {
            if (connection != null)
            {
                System.err.print("Transaction is being rolled back");
                try
                {
                    connection.rollback();

                    return -1;
                }
                catch (SQLException ex)
                {
                    throw new CostManagerException("problem with rolling back.", ex);
                }
            }
            else
            {
                throw new CostManagerException("problem with removing an existing category.", exception);
            }
        }
    }

    @Override
    public int addNewExpense(Expense cost) throws CostManagerException
    {
        String addNewExpenseQuery = "insert into costs (category, sum_cost, currency, description, date)"
                + "value(?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement addNewExpense = connection.prepareStatement(addNewExpenseQuery))
        {
            connection.setAutoCommit(false);
            addNewExpense.setString(1, cost.getCategory());
            addNewExpense.setInt(2, cost.getCost_sum());
            addNewExpense.setString(3, cost.getCurrency());
            addNewExpense.setString(4, cost.getDescriptionOfExpense());
            addNewExpense.setDate(5, cost.getPurchaseDate());
            int numberOfRowsAffected = addNewExpense.executeUpdate();
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
                throw new CostManagerException("Oops seems like this expense already exists, try another one", exception);
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
                throw new CostManagerException("problem with adding new expense.", exception);
            }
        }
    }

    @Override
    public int removeExistingExpense(int expenseID) throws CostManagerException
    {
        String removeExistingExpenseQuery = "delete from costs where id = ?";

        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement prepareStatement = connection.prepareStatement(removeExistingExpenseQuery))
        {
            connection.setAutoCommit(false);
            prepareStatement.setInt(1, expenseID);

            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();

            return numberOfRowsAffected;
        }
        catch (SQLException exception)
        {
            if (connection != null)
            {
                System.err.print("Transaction is being rolled back");
                try
                {
                    connection.rollback();

                    return -1;
                }
                catch (SQLException ex)
                {
                    throw new CostManagerException("problem with rolling back.", ex);
                }
            }
            else
            {
                throw new CostManagerException("problem with removing an existing expense.", exception);
            }
        }
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
