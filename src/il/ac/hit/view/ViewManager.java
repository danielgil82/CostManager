package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.Expense;
import il.ac.hit.view.appcontent.AppUtils;
import il.ac.hit.view.appcontent.AppView;
import il.ac.hit.view.login.LoginUtils;
import il.ac.hit.view.login.LoginView;
import il.ac.hit.viewmodel.ViewModel;

import javax.swing.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TimerTask;

/**
 * The purpose of this class is the manage all the traffic between the gui components and the viewModel.
 * An actual class that represents a concrete view.
 * And also this class is a kind of LoginUtils interface and AppUtils interface, since it implements them both,
 * and it has a methods that are going to be used within the LoginView and AppView classes respectively.
 */
public class ViewManager implements View , LoginUtils , AppUtils {

    /** Different frames of the application*/
    private LoginView loginView;
    private AppView appView;

    /** ViewModel mediates between the view and model parts. */
    private ViewModel viewModel;

    /*** Constructor that inits the loginView member. */
    public ViewManager() {}

    /**
     * Here we set the view model.
     *
     * @param vm - the view model.
     */
    @Override
    public void setIViewModel(ViewModel vm) {
        viewModel = vm;
    }

    /**
     *  This method suppose to send the full name and the password of the user
     *  to the viewModel, because of a reuse thinking, because if the in the future
     *  we'd change the gui part to another one, the logic of validating users credentials
     *  will still be relevant.
     *
     * @param fullName - of the user.
     * @param password - of the user.
     */
    @Override
    public void validateUserCredentials(String fullName, String password) {
        viewModel.validateUserCredentialsForLoginPanel(fullName, password);
    }

    /**
     * This method will be invoked when the buttonOk in the loginPanel will be clicked
     *
     * @param fullName - users name.
     * @param password - users password.
     */
    @Override
    public void validateUserExistence(String fullName, String password) {
        viewModel.validateUserExistence(fullName, password);
    }

    /**
     * This method adds sends the fullName and the password to the viewModel.
     *
     * @param fullName - user's full name.
     * @param password - user's password.
     */
    @Override
    public void addNewUser(String fullName, String password){
        viewModel.addNewUser(fullName, password);
    }

    /**
     * This method is responsible for moving the fullName and the password to the viewModel.
     *
     * @param fullName - of the user.
     * @param password - password the user choose.
     * @param confirmedPassword - confirmation of the users' password.
     */
    @Override
    public void validateUsersFullNameAndPasswords(String fullName, String password, String confirmedPassword) {
        viewModel.validateUserCredentialsForSignUpPanel(fullName, password, confirmedPassword);
    }

    /**
     * Init method creates the loginView part of the app and displays it.
     */
    @Override
    public void init() {
        loginView = new LoginView(this);
    }

    /**
     * Displaying the loginView part of the app by setting its visibility to true.
     */
    @Override
    public void start() {
        loginView.setVisible(true);
    }

    /**
     * This method receives message and displays it to the user via the *loginView*.
     * and also sets a timer for displaying a message for a specific period of time.
     *
     * @param message - the relevant message to display.
     */
    @Override
    public void displayMessageForLoginSection(Message message) {
        loginView.getLabelInvalidDescription().setText(message.getMessage());

        // creating a timer for the display message to appear for a specific period of time.
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> loginView.getLabelInvalidDescription().setText(null));
            }
        }, 4000);
    }

//    /**
//     * This method receives a category and then calls for
//     * removeChosenCategoryFromComboBox that responsible for removing the category
//     * from the combobox.
//     *
//     * @param category - to remove from combobox.
//     */
//    @Override
//    public void removeCategoryFromComboBox(String category) {
//        appView.removeChosenCategoryFromComboBox(category);
//    }

    /**
     * Receiving a category from view model, and sending it to the AppView.
     *
     * @param category - the category that should be added to the CategoryComboBox
     */
    @Override
    public void addNewCategoryToComboBox(String category) {
        appView.addCategoryToComboBoxes(category);
    }

    /**
     * This method receives message and displays it to the user via the loginView
     * and updates the flag that indicates the validity of the user's credentials.
     *
     * @param message - the relevant message to display.
     * @param flag - that indicates if the credentials of the user are valid.
     */
    @Override
    public void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInSignUpPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /**
     * This method receives message and displays it to the user via the loginView
     * and updates the flag that indicates the validity of the user's credentials.
     *
     * @param message - the relevant message to display.
     * @param flag - that indicates if the credentials of the user are valid.
     */
    @Override
    public void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInLoginPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /**
     * This method receives message and displays it to the user via the *appView*.
     *
     * @param message - the relevant message to display.
     */
    @Override
    public void displayMessageForAppSection(Message message) {
        appView.getLabelFeedbackMessage().setText(message.getMessage());

        // creating a timer for the display message to appear for a specific period of time.
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        appView.getLabelFeedbackMessage().setText(null);
                    }
                });
            }
        }, 4000);
    }

    /**
     * This method changes the frame from app page to the login page.
     */
    @Override
    public void changeFrameFromAppViewToLoginView() {
        appView.dispose();
        appView = null;
        loginView.setVisible(true);
    }

    /**
     * This method resets the user.
     */
    @Override
    public void resetUser() {
        viewModel.resetUser();
    }

    /**
     * Changing the frame from the login page to the app page.
     */
    @Override
    public void changeFrameFromLoginViewToAppView() {
        loginView.setVisible(false);
        appView = new AppView(this);
        appView.setVisible(true);
    }

    /**
     * This method calls the getCategoriesBySpecificUser in the viewModel
     * that responsible for getting the categories that belong to specific user.
     */
    @Override
    public void getCategoriesThatBelongToSpecificUser() {
        viewModel.getCategoriesBySpecificUser();
    }

    /**
     * This method call the setTheCategoriesList in appView
     * that responsible for setting the categories to the combobox.
     *
     * @param listOfCategories - category list.
     */
    @Override
    public void setCategories(List<String> listOfCategories) {
        appView.setTheCategoriesToCategoriesComboBox(listOfCategories);
    }

    /**
     * This method call the getExpensesBySpecificCategory in viewModel
     * that responsible for getting expenses by given category.
     *
     * @param categoryType - the expenses that would return are going to be based on the categoryType we receive here.
     */
    @Override
    public void getExpensesByCategory(String categoryType) {
        viewModel.getExpensesBySpecificCategory(categoryType);
    }

    /**
     * This method calls the setExpensesTableByCategoryInAppView in appView
     * that responsible for setting the expense table.
     *
     * @param listOfExpenses - expenses list.
     */
    @Override
    public void setExpensesTableByCategoryInAppView(List<Expense> listOfExpenses) {
        appView.setTableInAllExpensesPanel(listOfExpenses);
    }

    /**
     * This method calls the validateAndAddNewCategory in viewModel
     * that responsible for validating and adding a new category to the database and to the
     * list of categories.
     *
     * @param categoryName - category's name.
     */
    @Override
    public void validateAndSetNewCategory(String categoryName) {
        viewModel.validateAndAddNewCategory(categoryName);
    }

    /**
     * This method calls the removeCategory in viewModel
     * that responsible for remove the selected category from the database and from the list of categories.
     *
     * @param categoryName - selected category to remove.
     */
    @Override
    public void removeCategory(String categoryName) {
        viewModel.removeSpecificCategory(categoryName);
    }


    /**
     * This method calls to removeCostsThatReferToSpecificCategory in the viewModel and sends it the chosen category.
     *
     * @param category - the costs are going to be deleted according to
     *                 this category.
     */
    @Override
    public void removeCostsThatReferToChosenCategory(String category) {
        viewModel.removeCostsThatReferToSpecificCategory(category);
    }


    /**
     * Getting a list of costs that suppose to be removed from the CostIDComboBox.
     *
     * @param costsIDToRemove - list of costs id's to be removed
     */
    @Override
    public void removeCostsFromCostIDComboBox(List<Integer> costsIDToRemove) {
       appView.updateCostIDComboBox(costsIDToRemove);
    }

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
    @Override
    public void validateAndAddNewCost(String categorySelected, String sumCost, String currency,
                                      String description, Date date) {
        viewModel.validateAndAddNewCost(categorySelected, sumCost, currency,description ,date);
    }

    /**
     * asking from the view model the cost's id's of the loggedIn user.
     */
    @Override
    public void getCostsID() {
        viewModel.getCostsID();
    }

    /**
     * Getting the cost's id's from the view model and moving to forward to the appView.
     *
     * @param costsID list of cost id's
     */
    @Override
    public void setCostsID(List<Integer> costsID) {
        appView.setTheCostsIDToCostIDComboBox(costsID);
    }

    /**
     * Giving the view model the costID that suppose to be removed.
     *
     * @param costID - the id of a cost that suppose to be removed.
     */
    @Override
    public void removeCost(int costID) {
        viewModel.removeCost(costID);
    }

    /**
     * This method is responsible for calling methods in AppView,
     * that responsible for updating the category combo boxes and cost id combo box.
     *
     * @param category - category to remove from category combo boxes, and the related cost id's that relate to that
     *                   category in the cost id combo box.
     */
    @Override
    public void updateCategoriesComboBoxes(String category) {
        appView.updateCategoriesComboBoxes(category);
    }

    /**
     * This method is responsible for calling another method in viewModel that has to bring back from the model
     * all the costs that were made between the given date.
     *
     * @param startDate - start date.
     * @param endDate - end date.
     */
    @Override
    public void getCostsBetweenChosenDates(Date startDate, Date endDate) {
        viewModel.getCostsBetweenGivenDates(startDate, endDate);
    }

    /**
     * This method gets back a list of expenses between 2 chosen dates and sends it to initPieChart.
     *
     * @param expensesBetweenGivenDates - list of expense between chosen dates.
     */
    @Override
    public void setPieChart(Hashtable<String,Float> expensesBetweenGivenDates) {
        appView.initPieChart(expensesBetweenGivenDates);
    }

    /**
     * This method gets back a list of expenses between 2 chosen dates and sends it to initTableReportPanel.
     *
     * @param expensesBetweenChosenDates - list of expense between chosen dates.
     */
    @Override
    public void setCostsTableInReportPanel(List<Expense> expensesBetweenChosenDates) {
        appView.initTableReportPanel(expensesBetweenChosenDates);
    }
}