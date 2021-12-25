package il.ac.hit.model;

import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CostManagerModel implements IModel, IErrorAndExceptionsHandlingStrings
{

    private final String driverFullQualifiedName = "com.mysql.jdbc.Driver";
    private final String connectionStringToDB = "jdbc:mysql://localhost:3306/costmanagerproj";
    private final List<User> listOfUsers = new ArrayList<User>();
    //   private Connection connection = null;
//    private Statement statement = null;
//    private ResultSet resultSet = null;

    public CostManagerModel() throws CostManagerException
    {
        try
        {
            Class.forName(driverFullQualifiedName);
            getAllUsersFromTheDB();
        }
        catch (ClassNotFoundException ex)
        {
            throw new CostManagerException("Problem with registering driver to the driver manager", ex);
        }
    }

    private void getAllUsersFromTheDB() throws CostManagerException
    {
        String getAllUsersQuery = "SELECT * from users";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);

            while (resultSet.next())
            {
                listOfUsers.add(new User(resultSet.getInt("user_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("password")));
            }
        }
        catch (SQLException exception)
        {
            throw new CostManagerException(PROBLEM_WITH_GETTING_THE_USERS);
        }
    }

    @Override
    public int addNewCategory(Category category) throws CostManagerException
    {
        String addNewCategoryQuery = "insert into categories (category, user_id)"
                + "value(?,?)";
        Connection connection = null;
        PreparedStatement addNewCategory = null;
        try
        {
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            addNewCategory = connection.prepareStatement(addNewCategoryQuery);
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
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                throw new CostManagerException("problem with closing the connection");
            }
        }
    }

    @Override
    public int removeExistingCategory(Category categoryToDelete) throws CostManagerException
    {
        String removeExistingCategoryQuery = "delete from categories where category = ? AND " +
                "user_id = ?";
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        //Creating a connection string
        try
        {
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            prepareStatement = connection.prepareStatement(removeExistingCategoryQuery);
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
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                throw new CostManagerException("problem with closing the connection");
            }
        }
    }

    private int removeCostsBySpecificCategoryAndBySpecificUser(Category expensesToDeleteByCategory) throws CostManagerException
    {
        String removeExpensesQuery = "delete from costs where category = ? AND " + "user_id = ?";
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        //Creating a connection string
        try
        {
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            prepareStatement = connection.prepareStatement(removeExpensesQuery);
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
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                throw new CostManagerException("problem with closing the connection");
            }
        }
    }

    @Override
    public int addNewExpense(Expense expense) throws CostManagerException
    {
        String addNewExpenseQuery = "insert into costs (category, sum_cost, currency, description, date, user_id)"
                + "value(?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement addNewExpense = null;
        try
        {
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            addNewExpense = connection.prepareStatement(addNewExpenseQuery);
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
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                throw new CostManagerException("problem with closing the connection");
            }
        }
    }

    @Override
    public int removeExistingExpense(int expenseID) throws CostManagerException
    {
        String removeExistingExpenseQuery = "delete from costs where cost_id = ?";
        Connection connection = null;
        PreparedStatement prepareStatement = null;

        try
        {
            //Creating a connection string
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            prepareStatement = connection.prepareStatement(removeExistingExpenseQuery);
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
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                throw new CostManagerException("problem with closing the connection");
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
        finally
        {
        }
    }

    @Override
    public User getUser(String userFullName, String userPassword) throws CostManagerException
    {

        for (User user : listOfUsers)
        {
            if(user.getFullName().equals(userFullName) && user.getUsersPassword().equals(userPassword))
            {
                return user;
            }
        }

        throw new CostManagerException(USER_DOES_NOT_EXISTS);
//        Connection connection = null;
//        PreparedStatement prepareStatement = null;
//        ResultSet resultSet = null;
//        User wantedUser = null;
//        String getUserQuery = "select * from users WHERE full_name = ? AND" +
//                " password = ?";
//        try
//        {
//            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
//            prepareStatement = connection.prepareStatement(getUserQuery);
//            prepareStatement.setString(1, userFullName);
//            prepareStatement.setString(2, userPassword);
//            resultSet = prepareStatement.executeQuery();
//            if (resultSet != null)
//            {
//                wantedUser = new User(
//                        resultSet.getInt("user_id"),
//                        resultSet.getString("full_name"),
//                        resultSet.getString("password")
//                );
//            }
//
//            return wantedUser;
//        }
//        catch (SQLException exception)
//        {
//            throw new CostManagerException(USER_DOES_NOT_EXISTS, exception);
//        }
//        finally
//        {
//            if (resultSet != null)
//            {
//                try
//                {
//                    resultSet.close();
//                }
//                catch (SQLException ex)
//                {
//                    throw new CostManagerException(PROBLEM_WITH_THE_RESULT_SET, ex);
//                }
//            }
//            if (prepareStatement != null)
//            {
//                try
//                {
//                    prepareStatement.close();
//                }
//                catch (SQLException ex)
//                {
//                    throw new CostManagerException(PROBLEM_WITH_THE_STATEMENT, ex);
//                }
//            }
//            if (connection != null)
//            {
//                try
//                {
//                    connection.close();
//                }
//                catch (SQLException ex)
//                {
//                    throw new CostManagerException(PROBLEM_WITH_THE_CONNECTION, ex);
//                }
//            }
//        }
    }

    private boolean checkIfTheUserExists(User user)
    {
        for (User currentUser : listOfUsers)
        {
            if (currentUser.getFullName().equals(user.getFullName()) &&
                    currentUser.getUsersPassword().equals(user.getUsersPassword()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int addNewUserToDB(User newUser) throws CostManagerException
    {
        if (checkIfTheUserExists(newUser))
        {
            return 0;
        }

        String addNewUserQuery = "insert into users (full_name, password)"
                + "value(?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement preparedStatement = connection.prepareStatement(addNewUserQuery))
        {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, newUser.getFullName());
            preparedStatement.setString(2, newUser.getUsersPassword());

            int numberOfRowsAffected = preparedStatement.executeUpdate();
            connection.commit();

            if (numberOfRowsAffected != 1)
            {
                throw new CostManagerException(SOMETHING_WENT_WRONG);
            }

            return numberOfRowsAffected;
        }
        catch (SQLException exception)
        {
            throw new CostManagerException(PROBLEM_WITH_ADDING_NEW_USER, exception);
        }
    }


    @Override
    public Collection<Expense> getAllExpenses(int userID) throws CostManagerException
    {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try
        {
            //Creating a connection string
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
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

    public void addNewUserToTheListOfUsers(User userToAddToListOfUsers) throws CostManagerException
    {
        String getUserIDQuery = "select user_id from users WHERE full_name = ? AND" +
                " password = ?";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement preparedStatement = connection.prepareStatement(getUserIDQuery))
        {

            preparedStatement.setString(1, userToAddToListOfUsers.getFullName());
            preparedStatement.setString(2, userToAddToListOfUsers.getUsersPassword());

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet != null)
            {
                listOfUsers.add( new User(
                        resultSet.getInt("user_id"),
                        userToAddToListOfUsers.getFullName(),
                        userToAddToListOfUsers.getUsersPassword()
                ));
            }

        }
        catch (SQLException exception)
        {
            throw new CostManagerException(PROBLEM_WITH_GETTING_THE_USERS);
        }
    }
}