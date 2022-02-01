package il.ac.hit.viewmodel;

import il.ac.hit.model.Model;
import il.ac.hit.model.User;
import il.ac.hit.view.View;

import java.util.Date;

/**
 * This interface has the methods that will be implemented by a concrete ViewModel object
 * and all the methods here are asynchronous because we want the awt event thread to be free
 * when a method from here is invoked. That's why all the methods in ViewModel interface return void.
 */
public interface ViewModel
{
    /**
     * Here we set the view data member.
     *
     * @param view - part of the project.
     */
    void setView(View view);

    /**
     * Here we set the model data member.
     *
     * @param model - the model of our application.
     */
    void setModel(Model model);

    /**
     * Here we ensure that the user exists in the database.
     *
     * @param fullName - of the user.
     * @param password - of the user.
     */
    void validateUserExistence(String fullName, String password);

    /**
     *Adding the user to the database, but first ensuring that he doesn't already exist.
     *
     * @param fullName - user's full name.
     * @param password - user's password.
     */
    void addNewUser(String fullName, String password);

    /**
     * getting a list of categories that belong to the loggedIn user.
     */
    void getCategoriesBySpecificUser();

    /**
     * First we ensure that the awt event thread is the one that run right now.
     * Then we validate users credentials when he signs up.
     *
     * @param fullName          - fullName of the user.
     * @param password          - password of the user.
     * @param confirmedPassword - confirmed password of the user.
     */
    void validateUserCredentialsForSignUpPanel(String fullName, String password, String confirmedPassword);

    /**
     * First we ensure that the awt event thread is the one that run right now.
     * Then we validate users credentials when he logs in.
     *
     * @param fullName - fullName of the user
     * @param password - password of the user
     */
    void validateUserCredentialsForLoginPanel(String fullName, String password);

    /**
     * Here we reset the user.
     */
    void resetUser();

    /**
     * getting back all costs by given category.
     *
     * @param categoryType - the costs will be filtered according to the category.
     */
    void getExpensesBySpecificCategory(String categoryType);

    /**
     * This method first validates the input of the user, by ensuring that only letters inserted,
     * afterwards checks that category doesn't already exist, if it does then the user gets a feedback respectively
     * message.
     * Lastly if the input is good, and the category doesn't exist , the method sends the category to the database,
     * to be added.
     *
     * @param category to add
     */
    void validateAndAddNewCategory(String category);

    /**
     * removing a specific category.
     *
     * @param categoryToRemove - the category that the db supposed to remove.
     */
    void removeSpecificCategory(String categoryToRemove);

    /**
     * Validating the inputs of the user, and adding a new cost.
     *
     * @param categorySelected - the category the user chose.
     * @param sumCost - the amount of money the user spent on that cost.
     * @param currency - the currency he used.
     * @param description - an explanation of the cost.
     * @param date - when it took place.
     */
    void validateAndAddNewCost(String categorySelected, String sumCost, String currency, String description, Date date);

    /**
     * removing a cost.
     *
     * @param costID - the id of the cost that supposed to be removed.
     */
    void removeCost(int costID);

    /**
     * Getting all the cost id's of the loggedIn user.
     */
    void getCostsID();

    /**
     * removing costs that refer to the given category.
     *
     * @param category - the costs are going to be removed by the given category.
     */
    void removeCostsThatReferToSpecificCategory(String category);

    /**
     * This method is responsible for getting back to the view all the costs that we're purchased between the given
     * dates.
     *
     * @param startDate - start date.
     * @param endDate   - end date.
     */
    void getCostsBetweenGivenDates(Date startDate, Date endDate);
}