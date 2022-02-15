package il.ac.hit.model;

import il.ac.hit.auxiliary.HandlingMessage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * This class represents the interaction with the database and the logic behind that,
 * and the concrete model object of the project.
 */
public class CostManagerModel implements Model {
    /**
     * This string represents the driver full qualified name.
     */
    private final String driverFullQualifiedName = "com.mysql.jdbc.Driver";

    /**
     * This String represents the user's name of the db.
     */
    private final String user = "******";

    /**
     * This String represents the password of the db.
     */
    private final String password = "******";

    /**
     * This string represents the connection to the database.
     */
    private final String connectionStringToDB = "jdbc:mysql://localhost:3306/costmanagerproj";

    /**
     * List of all users that signed up to our application.
     */
    private final List<User> listOfUsers = new ArrayList<>();

    /**
     * Categories name that belong to the signed-in user.
     */
    private final List<String> listOfCategoriesNames = new ArrayList<>();

    /**
     * Categories that belong to the signed-in user.
     */
    private final List<Category> listOfCategories = new ArrayList<>();

    /**
     * Ctor of the CostManagerModel.
     * The constructor sets the list of users list.
     * A CostManagerException might be thrown from getAllUsersFromTheDB
     * and that's why there is a signature around the method declaration.
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
     * Getter for getting the listOfUser list.
     *
     * @return listOfUsers.
     */
    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    /**
     * This method add a new user when he signed up, to the listOfUsers we defined.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    private void getAllUsersFromTheDB() throws CostManagerException {
        //query to select all the rows from the user table
        String getAllUsersQuery = "SELECT * from users";
        if (listOfUsers.size() != 0) {
            listOfUsers.clear();
        }

        // we've used try with resources because it manages the releasion off the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);

            // extracting all data from the resultSet
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
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void addNewCategory(Category category) throws CostManagerException {
        //query to insert a new category to the categories' table in the database
        String addNewCategoryQuery = "insert into categories (category, user_id)"
                + "value(?,?)";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement addNewCategory = connection.prepareStatement(addNewCategoryQuery)) {

            connection.setAutoCommit(false);

            addNewCategory.setString(1, category.getCategoryName());
            addNewCategory.setInt(2, category.getUserID());

            // get the number of rows that were affected and check it
            int numberOfRowsAffected = addNewCategory.executeUpdate();

            connection.commit();

            //throw an exception if the number of rows that were affected is different from 1
            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ADDING_NEW_CATEGORY);
            }

            listOfCategories.add(category);

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }


    /**
     * This method deletes a category from the database.
     *
     * @param categoryToDelete - the category that is going to be deleted.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void removeExistingCategory(Category categoryToDelete) throws CostManagerException {
        //query to remove an existing category from the db
        String removeExistingCategoryQuery = "delete from categories where category = ? AND user_id = ?";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement prepareStatement = connection.prepareStatement(removeExistingCategoryQuery);) {

            connection.setAutoCommit(false);
            prepareStatement.setString(1, categoryToDelete.getCategoryName());
            prepareStatement.setInt(2, categoryToDelete.getUserID());

            // get the number of rows that were affected and check it
            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();

            //throw an exception if the number of rows that were affected is different from 1
            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REMOVING_AN_EXISTING_CATEGORY);
            }

            listOfCategoriesNames.remove(categoryToDelete.getCategoryName());

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }


    /**
     * This method responsible for removing costs that belong to a specific category.
     * There is no need for checking the number of rows that was affected, because there
     * might be 0 or more rows that were affected.
     *
     * @param category - the category which belong to some costs.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void removeCostsBySpecificCategory(Category category) throws CostManagerException {
        //query to remove all costs that belong by the given category.
        String removeExpensesQuery = "delete from costs where category = ? AND user_id = ?";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement prepareStatement = connection.prepareStatement(removeExpensesQuery);) {

            connection.setAutoCommit(false);
            prepareStatement.setString(1, category.getCategoryName());
            prepareStatement.setInt(2, category.getUserID());

            //When removing an expenses we don't know the exact amount of expenses that are going to be removed
            // that's why we didn't check the executeUpdate returned value.
            prepareStatement.executeUpdate();
            connection.commit();

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }

    /**
     * This method responsible for add new cost to the database.
     *
     * @param cost - the cost that should be added to the database.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void addNewCost(Expense cost) throws CostManagerException {
        //query to add new expense
        String addNewExpenseQuery = "insert into costs (category, sum_cost, currency, description, date, user_id)"
                + "value(?, ?, ?, ?, ?, ?)";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement prepareStatement = connection.prepareStatement(addNewExpenseQuery);) {

            connection.setAutoCommit(false);
            prepareStatement.setString(1, cost.getCategory());
            prepareStatement.setFloat(2, cost.getCostSum());
            prepareStatement.setString(3, cost.getCurrency());
            prepareStatement.setString(4, cost.getExpenseDescription());
            prepareStatement.setDate(5, cost.getPurchaseDate());
            prepareStatement.setInt(6, cost.getUserID());

            // get the number of rows that were affected and check it
            int numberOfRowsAffected = prepareStatement.executeUpdate();

            connection.commit();

            //throw an exception if the number of rows that were affected is different from 1
            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ADDING_NEW_EXPENSE);
            }

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }

    /**
     * This method responsible for removing the chosen cost.
     *
     * @param costID - the cost that should be removed.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void removeExistingCost(int costID) throws CostManagerException {
        //query to remove an existing cost
        String removeExistingExpenseQuery = "delete from costs where cost_id = ? ";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement prepareStatement = connection.prepareStatement(removeExistingExpenseQuery);) {
            connection.setAutoCommit(false);
            prepareStatement.setInt(1, costID);

            // get the number of rows that were affected and check it
            int numberOfRowsAffected = prepareStatement.executeUpdate();
            connection.commit();

            //throw an exception if the number of rows that were affected is different from 1
            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_REMOVING_EXISTING_EXPENSE);
            }

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }

    /**
     * This method responsible for get all costs between two chosen
     * dates and return list that contains them.
     *
     * @param userID - the user that we want to get his all costs between those two dates.
     * @param startDate - first date.
     * @param endDate - second date.
     *
     * @return list of all costs between two dates.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public List<Expense> getReportByDates(int userID, Date startDate, Date endDate) throws CostManagerException {
        //query to filter costs between 2 given dates.
        String getReportByDatesQuery = "select * from costs WHERE user_id = ? AND date BETWEEN ? AND ? ";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement prepareStatement = connection.prepareStatement(getReportByDatesQuery)) {

            prepareStatement.setInt(1, userID);
            prepareStatement.setDate(2, startDate);
            prepareStatement.setDate(3, endDate);

            //get all the data to the result set
            ResultSet resultSet = prepareStatement.executeQuery();

            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next()) {
                costExpensesList.add(new Expense
                        (resultSet.getInt("cost_id"),
                                resultSet.getString("category"),
                                resultSet.getFloat("sum_cost"),
                                resultSet.getString("currency"),
                                resultSet.getString("description"),
                                resultSet.getDate("date"),
                                resultSet.getInt("user_id")));
            }

            return costExpensesList;

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.COULD_NOT_FIND_EXPENSES_BETWEEN_THESE_DATES, exception);
        }
    }

    /**
     * Optional gives the opportunity to return an "Optional" user or empty one.
     *
     * @param userFullName - represent the name of the user.
     * @param userPassword - represent the password of the user.
     *
     * @return the user if the there is one.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public User getUser(String userFullName, String userPassword) throws CostManagerException {
        /*
         * first we get the listOfUsers stream, and then we try to filter the relevant user
         * according to the given filters which are userFullName, and userPassword.
         *
         * afterwards if a user was found, so we'll retrieve it from the userOptional local member.
         * else, we'll throw an exception which indicates that there is no such user.
         */
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
     * This method checks if the user already exists,
     * using Optional gives you the opportunity to return true or false according to it
     * or throw an exception if needed.
     *
     * @param user - is the user we want to check about if he exists or not.
     *
     * @return true if exists else throws exception that the user already exists.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    public boolean checkIfTheUserExists(User user) throws CostManagerException {
        /*
         * this method returns an exception if the user exits,
         * or otherwise return false.
         */
        Optional<User> checkIfExists = listOfUsers
                .stream()
                .filter(currentUser -> currentUser.getFullName().equals(user.getFullName()) &&
                        currentUser.getUsersPassword().equals(user.getUsersPassword()))
                .findFirst();

        if (checkIfExists.isPresent()) {
            throw new CostManagerException(HandlingMessage.USER_ALREADY_EXISTS);
        } else {
            return false;
        }
    }

    /**
     * This function adding new user to the database by checking the number of rows
     * that were affected.
     * If the number of rows that affected is not 1, it means there was a problem
     * with adding this user otherwise, everything gone okay.
     *
     * @param userToAdd - is the new user we want to add to the database.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public void addNewUserToDBAndUpdateTheListOfUsers(User userToAdd) throws CostManagerException {
        //query add the new user to database and add him to the listOfUsers we defined
        String addNewUserQuery = "insert into users (full_name, password) value(?, ?)";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(addNewUserQuery)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, userToAdd.getFullName());
            preparedStatement.setString(2, userToAdd.getUsersPassword());

            // get the number of rows that were affected and check it
            int numberOfRowsAffected = preparedStatement.executeUpdate();

            connection.commit();

            //throw an exception if the number of rows that were affected is different from 1
            if (numberOfRowsAffected != 1) {
                throw new CostManagerException(HandlingMessage.PROBLEM_WITH_ADDING_NEW_USER);
            }

            // get all the users of the database because another one was added.
            //and we wanted to hold an updated list of users in this method.
            getAllUsersFromTheDB();

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.SOMETHING_WENT_WRONG, exception);
        }
    }


    /**
     * This method responsible for getting all costs that belong to a specific category.
     *
     * @param userID - the user id we want to get all his costs by chosen category.
     * @param categoryType - the chosen category.
     *
     * @return list of all costs that belong to a specific user and category.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public List<Expense> getExpensesByCategory(int userID, String categoryType) throws CostManagerException {
        /*
         * here we have a boolean that represents if the expenses must be filtered or not
         * if the category is "all" which means there is no filter , we're getting all the costs from the db
         * otherwise, we get the filtered costs by the category.
         */
        String getExpensesQuery;
        boolean isCategoryEqualsToAll = false;

        if (categoryType.equals("all")) {
            getExpensesQuery = "SELECT * FROM costs " + "where user_id = ? ";
            isCategoryEqualsToAll = true;
        } else {
            getExpensesQuery = "SELECT * FROM costs " +
                    "where user_id = ? and category = ? ";
        }

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getExpensesQuery)) {

            preparedStatement.setInt(1, userID);

            // if the category is not all , then we must place the second parameter in the right place holder.
            if (!isCategoryEqualsToAll) {
                preparedStatement.setString(2, categoryType);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Expense> costExpensesList = new LinkedList<>();

            while (resultSet.next()) {
                costExpensesList.add(new Expense
                        (resultSet.getInt("cost_id"),
                                resultSet.getString("category"),
                                resultSet.getFloat("sum_cost"),
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

    /**
     * This method recieves a user id and by it gets the categories that belong him.
     *
     * @param userId - identifies the user, thus we can get the categories according to that user.
     *
     * @return all categories of a specific user.
     *
     * @throws CostManagerException - the exception we defined to our application.
     */
    @Override
    public List<String> getCategoriesNamesBySpecificUser(int userId) throws CostManagerException {
        //query that gets all the categories that belong to a specific user
        String getCategoriesByUserQuery = "SELECT category FROM categories where user_id = ?";

        // we've used try with resources because it manages the release of the allocated resources.
        try (Connection connection = DriverManager.getConnection(connectionStringToDB, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getCategoriesByUserQuery)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if listOfCategoriesNames isn't empty , we clear it.
            if (listOfCategoriesNames.size() > 0) {
                listOfCategoriesNames.clear();
            }

            // add all the data that we got from the resultSet to the list of categories.
            while (resultSet.next()) {
                listOfCategoriesNames.add(resultSet.getString("category"));
            }

            return listOfCategoriesNames;

        } catch (SQLException exception) {
            throw new CostManagerException(HandlingMessage.COULD_NOT_GET_THE_CATEGORIES);
        }
    }
}
