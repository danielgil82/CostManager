package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.Expense;
import il.ac.hit.viewmodel.ViewModel;

import java.util.Hashtable;
import java.util.List;

/**
 * View interface is an interface, that has the methods that are going
 * to be implemented by a concrete view object which is in our case would be
 * an object of the ViewManager class.
 */
public interface View
{
    /**
     * Here we set the view model.
     *
     * @param vm - the view model.
     */
    void setIViewModel(ViewModel vm);


    /**
     * This method receives message and displays it to the user via the *loginView*.
     * and also sets a timer for displaying a message for a specific period of time.
     *
     * @param message - the relevant message to display.
     */
    void displayMessageForLoginSection(Message message);


    /**
     * This method receives message and displays it to the user via the *appView*.
     *
     * @param message - the relevant message to display.
     */
    void displayMessageForAppSection(Message message);


    /**
     * This method receives message and displays it to the user via the loginView
     * and updates the flag that indicates the validity of the user's credentials.
     *
     * @param message - the relevant message to display.
     * @param flag - that indicates if the credentials of the user are valid.
     */
    void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag);


    /**
     * This method receives message and displays it to the user via the loginView
     * and updates the flag that indicates the validity of the user's credentials.
     *
     * @param message - the relevant message to display.
     * @param flag - that indicates if the credentials of the user are valid.
     */
    void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag);


    /**
     * Init method creates the loginView part of the app and displays it.
     */
    void init();


    /**
     * Displaying the loginView part of the app by setting its visibility to true.
     */
    void start();


    /**
     * Changing the frame from the login page to the app page.
     */
    void changeFrameFromLoginViewToAppView();


    /**
     * This method call the setTheCategoriesList in appView
     * that responsible for setting the categories to the combobox.
     *
     * @param listOfCategories - category list.
     */
    void setCategories(List<String> listOfCategories);


    /**
     * This method calls the setExpensesTableByCategoryInAppView in appView
     * that responsible for setting the expense table.
     *
     * @param listOfExpenses - expenses list.
     */
    void setExpensesTableByCategoryInAppView(List<Expense> listOfExpenses);


    /**
     * Receiving a category from view model, and sending it to the AppView.
     *
     * @param category - the category that should be added to the CategoryComboBox
     */
    void addNewCategoryToComboBox(String category);


//    void removeCategoryFromComboBox(String category);


    /**
     * Getting the cost's id's from the view model and moving to forward to the appView.
     *
     * @param costsID list of cost id's
     */
    void setCostsID(List<Integer> costsID);


    /**
     * This method is responsible for calling methods in AppView,
     * that responsible for updating the category combo boxes and cost id combo box.
     *
     * @param category - category to remove from category combo boxes, and the related cost id's that relate to that
     *                   category in the cost id combo box.
     */
    void updateCategoriesComboBoxes(String category);


    /**
     * Getting a list of costs that suppose to be removed from the CostIDComboBox.
     *
     * @param costsIDToRemove - list of costs id's to be removed
     */
    void removeCostsFromCostIDComboBox(List<Integer> costsIDToRemove);


    /**
     * This method gets back a list of expenses between 2 chosen dates and sends it to initPieChart.
     *
     * @param expensesBetweenGivenDates - list of expense between chosen dates.
     */
    void setPieChart(Hashtable<String,Float> expensesBetweenGivenDates);


    /**
     * This method gets back a list of expenses between 2 chosen dates and sends it to initTableReportPanel.
     *
     * @param expensesBetweenChosenDates - list of expense between chosen dates.
     */
    void setCostsTableInReportPanel(List<Expense> expensesBetweenChosenDates);
}
