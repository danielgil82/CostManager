package il.ac.hit.view.appcontent;

import java.util.Date;

/**
 * An auxiliary class that have methods that be executed from the AppView class.
 */
public interface AppUtils {

    /**
     * This method resets the user.
     */
    void resetUser();

    /**
     * This method changes the frame from app page to the login page.
     */
    void changeFrameFromAppViewToLoginView();


    /**
     * This method calls the getCategoriesBySpecificUser in the viewModel
     * that responsible for getting the categories that belong to specific user.
     */
    void getCategoriesThatBelongToSpecificUser();


    /**
     * This method call the getExpensesBySpecificCategory in viewModel
     * that responsible for getting expenses by given category.
     *
     * @param categoryType - the expenses that would return are going to be based on the categoryType we receive here.
     */
    void getExpensesByCategory(String categoryType);


    /**
     * This method calls the validateAndAddNewCategory in viewModel
     * that responsible for validating and adding a new category to the database and to the
     * list of categories.
     *
     * @param categoryName - category's name.
     */
    void validateAndSetNewCategory(String categoryName);


    /**
     * This method calls the removeCategory in viewModel
     * that responsible for remove the selected category from the database and from the list of categories.
     *
     * @param categoryName - selected category to remove.
     */
    void removeCategory(String categoryName);


    /**
     * This method sends all the parameters it got to the viewModel by calling validateAndAddNewCost
     * the actual validation would take place there.
     *
     * @param categorySelected - category the user selected.
     * @param sumCost - how much the expense cost.
     * @param currency - type of currency.
     * @param description - the description of the cost.
     * @param date - the date the cost was made.
     */
    void validateAndAddNewCost(String categorySelected, String sumCost, String currency, String description, Date date);


    /**
     * Giving the view model the costID that suppose to be removed.
     *
     * @param costID - the id of a cost that suppose to be removed.
     */
    void removeCost(int costID);


    /**
     * asking from the view model the cost's id's of the loggedIn user.
     */
    void getCostsID();

    /**
     * This method calls to removeCostsThatReferToSpecificCategory in the viewModel and sends it the chosen category.
     *
     * @param category - the costs are going to be deleted according to
     *                 this category.
     */
    void removeCostsThatReferToChosenCategory(String category);

    /**
     * This method is responsible for calling another method in viewModel that has to bring back from the model
     * all the costs that were made between the given date.
     *
     * @param startDate - start date.
     * @param endDate - end date.
     */
    void getCostsBetweenChosenDates(Date startDate, Date endDate);
}
