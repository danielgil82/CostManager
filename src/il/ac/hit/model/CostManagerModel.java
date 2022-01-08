package il.ac.hit.model;

import il.ac.hit.auxiliary.HandlingMessage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * This class represents the interaction with the database and the logic behind that.
 */
public class CostManagerModel implements Model {
    /** This string represents the driver full qualified name. */
    private final String driverFullQualifiedName = "com.mysql.jdbc.Driver";

    /** This string represents the connection to the database. */
    private final String connectionStringToDB = "jdbc:mysql://localhost:3306/costmanagerproj";

    /** List of all users that signed up to our application. */
    private final List<User> listOfUsers = new ArrayList<>();

    /**
     * Ctor of the CostManagerModel.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    public CostManagerModel() throws CostManagerException {
        try {
            Class.forName(driverFullQualifiedName);
            getAllUsersFromTheDB();
        } catch (ClassNotFoundException ex) {
            throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REGISTERING_THE_DRIVER, ex);
        }
    }

    /**
     * This method add a new user when he signed up, to the listOfUsers we defined.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    private void getAllUsersFromTheDB() throws CostManagerException {
        //query to select all the rows from the user table
        String getAllUsersQuery = "SELECT * from users";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);

            while (resultSet.next()) {
                listOfUsers.add
                        (new User(resultSet.getInt("user_id"),
                                resultSet.getString("full_name"),
                                resultSet.getString("password")));
            }

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.PROBLEM_WITH_GETTING_THE_USERS);
        }
    }

    /**
     * This method add new category to the categories' table in the database.
     *
     * @param category - the category that the user choose to add to the database.
     * @return the number of rows were affected.
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public int addNewCategory(Category category) throws CostManagerException {
        //query to insert a new category to the categories' table in the database
        String addNewCategoryQuery = "insert into categories (category, user_id)"
                + "value(?,?)";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement addNewCategory = connection.prepareStatement(addNewCategoryQuery)) {

            connection.setAutoCommit(false);
            addNewCategory.setString(1, category.getCategoryName());
            addNewCategory.setInt(2, category.getUserID());
            int numberOfRowsAffected = addNewCategory.executeUpdate();
            connection.commit();

            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ADDING_NEW_CATEGORY);
            }

            return numberOfRowsAffected;

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }


    /**
     * This method deletes a category from the database.
     * @param categoryToDelete - the category that is going to be deleted.
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void removeExistingCategory(Category categoryToDelete) throws CostManagerException {
        String removeExistingCategoryQuery = "delete from categories where category = ? AND " +
                "user_id = ?";

        try(Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            PreparedStatement prepareStatement= connection.prepareStatement(removeExistingCategoryQuery);) {

            connection.setAutoCommit(false);
            prepareStatement.setString(1, categoryToDelete.getCategoryName());
            prepareStatement.setInt(2, categoryToDelete.getUserID());
            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();

            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY);
            }

        } catch (SQLException exception) {

            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }

//        try {
//            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
//            prepareStatement = connection.prepareStatement(removeExistingCategoryQuery);
//            connection.setAutoCommit(false);
//            prepareStatement.setString(1, categoryToDelete.getCategoryName());
//            prepareStatement.setInt(2, categoryToDelete.getUserID());
//            int numberOfRowsAffected = prepareStatement.executeUpdate();
//            connection.commit();
//            return numberOfRowsAffected;
//        } catch (SQLException exception) {
//            if (connection != null) {
//                //  System.err.print("Transaction is being rolled back");
//                try {
//                    connection.rollback();
//                    return -1;
//                } catch (SQLException ex) {
//                    throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ROLLING_BACK, ex);
//                }
//            } else {
//                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY
//                        , exception);
//            }
//        } finally {
//            try {
//                connection.close();
//            } catch (SQLException ex) {
//                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_CLOSING_THE_CONNECTION);
//            }
//        }
    }

    private int removeCostsBySpecificCategoryAndBySpecificUser(Category expensesToDeleteByCategory) throws CostManagerException {
        String removeExpensesQuery = "delete from costs where category = ? AND " + "user_id = ?";
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        //Creating a connection string
        try {
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            prepareStatement = connection.prepareStatement(removeExpensesQuery);
            connection.setAutoCommit(false);
            prepareStatement.setString(1, expensesToDeleteByCategory.getCategoryName());
            prepareStatement.setInt(2, expensesToDeleteByCategory.getUserID());
            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();
            return numberOfRowsAffected;
        } catch (SQLException exception) {
            if (connection != null) {
                //     System.err.print("Transaction is being rolled back");
                try {
                    connection.rollback();
                    return -1;
                } catch (SQLException ex) {
                    throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ROLLING_BACK, ex);
                }
            } else {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY
                        , exception);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_CLOSING_THE_CONNECTION);
            }
        }
    }

    @Override
    public int addNewExpense(Expense expense) throws CostManagerException {
        String addNewExpenseQuery = "insert into costs (category, sum_cost, currency, description, date, user_id)"
                + "value(?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement addNewExpense = null;
        try {
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
            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG);
            }
            return numberOfRowsAffected;
        } catch (SQLException exception) {
            if (exception instanceof SQLIntegrityConstraintViolationException) {
                throw new CostManagerException(HandlingMessage.EXPENSE_ALREADY_EXISTS, exception);
            } else if (connection != null) {
                // System.err.print("Transaction is being rolled back");
                try {
                    connection.rollback();
                    return 0;
                } catch (SQLException ex) {
                    throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ROLLING_BACK, ex);
                }
            } else {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ADDING_NEW_EXPENSE,
                        exception);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_CLOSING_THE_CONNECTION);
            }
        }
    }

    @Override
    public int removeExistingExpense(int expenseID) throws CostManagerException {
        String removeExistingExpenseQuery = "delete from costs where cost_id = ?";
        Connection connection = null;
        PreparedStatement prepareStatement = null;

        try {
            //Creating a connection string
            connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
            prepareStatement = connection.prepareStatement(removeExistingExpenseQuery);
            connection.setAutoCommit(false);
            prepareStatement.setInt(1, expenseID);
            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();
            return numberOfRowsAffected;
        } catch (SQLException exception) {
            if (connection != null) {
                // System.err.print("Transaction is being rolled back");
                try {
                    connection.rollback();
                    return -1;
                } catch (SQLException ex) {
                    throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ROLLING_BACK, ex);
                }
            } else {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REMOVING_EXISTING_EXPENSE, exception);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_CLOSING_THE_CONNECTION);
            }
        }
    }

    @Override
    public Collection<Expense> getReportByDates(int userID, Date startDate, Date endDate) throws CostManagerException {
        String getReportByDatesQuery = "select * from costs WHERE userID = ? AND" +
                " date BETWEEN ? AND ? ";
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement prepareStatement = connection.prepareStatement(getReportByDatesQuery)) {

            prepareStatement.setInt(1, userID);
            prepareStatement.setDate(2, startDate);
            prepareStatement.setDate(3, endDate);
            ResultSet resultSet = prepareStatement.executeQuery();
            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next()) {
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

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.COULD_NOT_FIND_EXPENSES_BETWEEN_THESE_DATES, exception);
        } finally {
        }
    }

    /**
     * Optional gives the opportunity to return an "Optional" user or empty one.
     *
     * @param userFullName - represent the name of the user.
     * @param userPassword - represent the password of the user.
     * @return the user if the there is one.
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public User getUser(String userFullName, String userPassword) throws CostManagerException {
        Optional<User> userOptional = listOfUsers
                .stream()
                .filter(user -> user.getFullName().equals(userFullName) && user.getUsersPassword().equals(userPassword))
                .findFirst();

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new CostManagerException(HandlingMessage.USER_DOES_NOT_EXISTS);
        }
    }

    /**
     * CheckIfTheUserExists method checks if the user already exists,
     * using Optional that gives the opportunity to return true or false according to it.
     *
     * @param user - is the user we want to check about if he exists or not.
     * @return true if exists else throws exception that the user already exists.
     * @throws CostManagerException - the exception we defined to our application.
     */
    public boolean checkIfTheUserExists(User user) throws CostManagerException {
        Optional<User> checkIfExists = listOfUsers
                .stream()
                .filter(currentUser -> currentUser.getFullName().equals(user.getFullName()) &&
                        currentUser.getUsersPassword().equals(user.getUsersPassword()))
                .findFirst();

        if (checkIfExists.isPresent()) {
            throw new CostManagerException(HandlingMessage.USER_ALREADY_EXISTS);
        } else {
            return true;
        }
    }

    /**
     * This function adding new user to the database by
     * checking the number of rows that were affected.
     * If the number of rows that affected is not 1,
     * it means there was a problem with adding this user.
     * Otherwise, everything gone okay.
     *
     * @param userToAdd - is the new user we want to add to the database.
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void addNewUserToDBAndUpdateTheListOfUsers(User userToAdd) throws CostManagerException {
        //query add the new user to database and add him to the listOfUsers we defined
        String addNewUserQuery = "insert into users (full_name, password)"
                + "value(?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement preparedStatement = connection.prepareStatement(addNewUserQuery)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, userToAdd.getFullName());
            preparedStatement.setString(2, userToAdd.getUsersPassword());

            int numberOfRowsAffected = preparedStatement.executeUpdate();
            connection.commit();

            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ADDING_NEW_USER);
            }

            listOfUsers.add(userToAdd);

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }


    @Override
    public List<Expense> getExpensesByCategory(int userID, String categoryType) throws CostManagerException {

        String getExpensesQuery;
        boolean isCategoryEqualsToAll = false;

        if (categoryType.equals("all")) {
            getExpensesQuery = "SELECT * FROM costs " + "where user_id = ? ";
            isCategoryEqualsToAll = true;
        } else {
            getExpensesQuery = "SELECT * FROM costs " +
                    "where user_id = ? and category = ? ";
        }

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement preparedStatement = connection.prepareStatement(getExpensesQuery)) {

            preparedStatement.setInt(1, userID);

            if (!isCategoryEqualsToAll) {
                preparedStatement.setString(2, categoryType);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next()) {
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

        } catch (SQLException ex) {
            throw new CostManagerException(HandlingMessage.COULD_NOT_GET_ALL_EXPENSES, ex);
        }

    }

    /** This method recieves a user id and by it gets the categories that belong him.
     * @param userId - identifies the user, thus we can get the categories according to that user.
     * @return all categories of a specific user.
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public List<String> getCategoriesBySpecificUser(int userId) throws CostManagerException {
        //query that gets all the categories that belong to a specific user
        String getCategoriesByUserQuery = "SELECT category FROM categories where user_id = ?";

        try (Connection connection = DriverManager.getConnection(connectionStringToDB, "sigalit", "leybman");
             PreparedStatement preparedStatement = connection.prepareStatement(getCategoriesByUserQuery)) {

            preparedStatement.setInt(1, userId);
            List<String> listOfCategories = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listOfCategories.add(resultSet.getString("category"));
            }

            return listOfCategories;

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.COULD_NOT_GET_THE_CATEGORIES);
        }
    }
}