package il.ac.hit.model;

import java.util.List;
import java.sql.Date;
/**
 * The interface that represents the model functionality.
 */
public interface Model
{
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
     User getUser(String userFullName, String userPassword) throws CostManagerException;



     /**
      * This function adding new user to the database by checking the number of rows
      * that were affected.
      * If the number of rows that affected is not 1, it means there was a problem
      * with adding this user otherwise, everything gone okay.
      *
      * @param user - is the new user we want to add to the database.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     void addNewUserToDBAndUpdateTheListOfUsers(User user) throws CostManagerException;



     /**
      * This method add new category to the categories' table in the database.
      *
      * @param category - the category that the user choose to add to the database.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     void addNewCategory(Category category) throws CostManagerException;


     /**
      * This method deletes a category from the database.
      *
      * @param categoryToDelete - the category that is going to be deleted.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     void removeExistingCategory(Category categoryToDelete) throws CostManagerException;



     /**
      * This method responsible for removing costs that belong to a specific category.
      * There is no need for checking the number of rows that was affected, because there
      * might be 0 or more rows that were affected.
      *
      * @param category - the category which belong to some costs.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     void removeCostsBySpecificCategory(Category category) throws CostManagerException;

     /**
      * This method responsible for add new cost to the database.
      *
      * @param cost - the cost that should be added to the database.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     void addNewCost(Expense cost) throws CostManagerException;



     /**
      * This method responsible for removing the chosen cost.
      *
      * @param costID - the cost that should be removed.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     void removeExistingCost(int costID) throws CostManagerException;



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
     List<Expense> getReportByDates(int userID, Date startDate, Date endDate) throws CostManagerException;


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
     List<Expense> getExpensesByCategory(int userID, String categoryType) throws CostManagerException;



     /**
      * This method recieves a user id and by it gets the categories that belong him.
      *
      * @param userId - identifies the user, thus we can get the categories according to that user.
      *
      * @return all categories of a specific user.
      *
      * @throws CostManagerException - the exception we defined to our application.
      */
     List<String> getCategoriesNamesBySpecificUser(int userId) throws CostManagerException;



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
     boolean checkIfTheUserExists(User user) throws CostManagerException;
}
