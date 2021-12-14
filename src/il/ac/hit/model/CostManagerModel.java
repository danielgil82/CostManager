package il.ac.hit.model;

import il.ac.hit.Category;
import il.ac.hit.Expense;
import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;
import il.ac.hit.exceptions.CostManagerException;

import javax.xml.transform.Result;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CostManagerModel implements IModel, IErrorAndExceptionsHandlingStrings
{

    private final String driverFullQualifiedName = "com.mysql.jdbc.Driver";
    private final String connectionStringToDB = "jdbc:mysql://localhost:3306/costmanagerproj";
    private Connection connection = null;
//    private Statement statement = null;
//    private ResultSet resultSet = null;

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
        String addNewCategoryQuery = "insert into categories (category, user_id)"
                + "value(?,?)";

        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement addNewCategory = connection.prepareStatement(addNewCategoryQuery))
        {
            connection.setAutoCommit(false);
            addNewCategory.setString(1, category.getCategoryName());
            addNewCategory.setInt(2, category.getUserID());
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
    public int removeExistingCategory(Category categoryToDelete) throws CostManagerException
    {
        String removeExistingCategoryQuery = "delete from categories where category = ? AND " +
                "user_id = ?";

        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement prepareStatement = connection.prepareStatement(removeExistingCategoryQuery))
        {
            connection.setAutoCommit(false);
            prepareStatement.setString(1, categoryToDelete.getCategoryName());
            prepareStatement.setInt(2, categoryToDelete.getUserID());

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

    private int removeCostsBySpecificCategoryAndBySpecificUser(Category expensesToDeleteByCategory) throws CostManagerException
    {
        String removeExpensesQuery = "delete from costs where category = ? AND " + "user_id = ?";

        //Creating a connection string
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement prepareStatement = connection.prepareStatement(removeExpensesQuery))
        {
            connection.setAutoCommit(false);
            prepareStatement.setString(1, expensesToDeleteByCategory.getCategoryName());
            prepareStatement.setInt(2, expensesToDeleteByCategory.getUserID());

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
                throw new CostManagerException("problem with removing expenses by specific category.", exception);
            }
        }
    }

    @Override
    public int addNewExpense(Expense expense) throws CostManagerException
    {
        String addNewExpenseQuery = "insert into costs (category, sum_cost, currency, description, date, user_id)"
                + "value(?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement addNewExpense = connection.prepareStatement(addNewExpenseQuery))
        {
            connection.setAutoCommit(false);
            addNewExpense.setString(1, expense.getCategory());
            addNewExpense.setInt(2, expense.getCostSum());
            addNewExpense.setString(3, expense.getCurrency());
            addNewExpense.setString(4, expense.getExpenseDescription());
            addNewExpense.setDate(5, expense.getPurchaseDate());
            addNewExpense.setInt(6, expense.getUserID());

            int numberOfRowsAffected = addNewExpense.executeUpdate();
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
        String removeExistingExpenseQuery = "delete from costs where cost_id = ?";

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
    public Collection<Expense> getReportByDates(int userID, Date startDate, Date endDate) throws CostManagerException
    {
        String getReportByDatesQuery = "select * from costs WHERE userID = ? AND" +
                " date BETWEEN ? AND ? ";
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement prepareStatement = connection.prepareStatement(getReportByDatesQuery))
        {
            prepareStatement.setInt(1, userID);
            prepareStatement.setDate(2, startDate);
            prepareStatement.setDate(3, endDate);

            ResultSet resultSet = prepareStatement.executeQuery();

            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next())
            {
                costExpensesList.add(new Expense
                               (resultSet.getInt("cost_id"),
                                resultSet.getString("category"),
                                resultSet.getInt("sum_cost"),
                                resultSet.getString("currency"),
                                resultSet.getString("description"),
                                resultSet.getDate("date"),
                                resultSet.getInt("user_id")));
            }

            return costExpensesList;
        }
        catch (SQLException exception)
        {
            throw new CostManagerException("Couldn't find any expenses between these two dates", exception);
        }
    }

    @Override
    public Collection<Expense> getAllExpenses(int userID) throws CostManagerException
    {
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try
        {
            //Creating a connection string
             Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");

            //performing simple query
            String querySql = "SELECT * FROM costs " +
                            "where user_id = ?";

            prepareStatement = connection.prepareStatement(querySql);

            prepareStatement.setInt(1, userID);

             resultSet = prepareStatement.executeQuery();


            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next())
            {
                costExpensesList.add(new Expense
                        (resultSet.getInt("cost_id"),
                                resultSet.getString("category"),
                                resultSet.getInt("sum_cost"),
                                resultSet.getString("currency"),
                                resultSet.getString("description"),
                                resultSet.getDate("date"),
                                resultSet.getInt("user_id")));
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
            if (prepareStatement != null)
            {
                try
                {
                    prepareStatement.close();
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
