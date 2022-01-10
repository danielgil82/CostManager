package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.Expense;
import il.ac.hit.model.User;
import il.ac.hit.view.appcontent.AppUtils;
import il.ac.hit.view.appcontent.AppView;
import il.ac.hit.view.login.LoginUtils;
import il.ac.hit.view.login.LoginView;
import il.ac.hit.viewmodel.ViewModel;

import java.util.Date;
import java.util.List;

/**
 * An actual class that represents a concrete view.
 * And also this class is a kind of LoginUtils interface because it has a methods that are going to be used in the
 * LoginView Class.
 */
public class ViewManager implements View , LoginUtils , AppUtils {

    /** Different frames of the application*/
    private LoginView loginView;
    private AppView appView;


    /** ViewModel mediates between the view and model parts. */
    private ViewModel viewModel;


    /*** Constructor that inits the loginView member. */
    public ViewManager() {

    }

    /** Getters. */
    public ViewModel getViewModel() {
        return viewModel;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    /**
     * Here we set the view model.
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
     * @param fullName - of the user.
     * @param password - of the user.
     */
    @Override
    public void validateUserCredentials(String fullName, String password) {
        viewModel.validateUserCredentialsForLoginPanel(fullName, password);
    }

    /**
     * This method will get invoked when the button ok in the loginPanel will get clicked
     * the method that will get invoked when the ok button in loginPanel clicked.
     * @param fullName - users name.
     * @param password - users password.
     */
    @Override
    public void validateUserExistence(String fullName, String password) {
        viewModel.validateUserExistence(fullName, password);
    }

    /** This method adds new user to database.
     * @param userToAdd - to the database.
     */
    @Override
    public void addNewUser(User userToAdd){
        viewModel.addNewUser(userToAdd);
    }

    /**This method...
     * @param fullName - of the user.
     * @param password - password the user choose.
     * @param confirmedPassword - confirmation of the users password.
     */
    @Override
    public void validateUsersFullNameAndPasswords(String fullName, String password, String confirmedPassword) {
        viewModel.validateUserCredentialsForSignUpPanel(fullName, password, confirmedPassword);
    }

    @Override
    public void init() {
        loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    @Override
    public void start() {
    }

    /**
     * This method receives message and displays it to the user via the loginView.
     * @param message - the relevant message to display.
     */
    @Override
    public void displayMessageForLoginSection(Message message) {
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /**
     * This method receives message and displays it to the user via the appView.
     * @param message - the relevant message to display.
     */
    @Override
    public void displayMessageForAppSection(Message message) {
        appView.getLabelFeedbackMessage().setText(message.getMessage());
    }


    /**
     * This method receives a category and then calls for
     * removeChosenCategoryFromComboBox that responsible for removing the category
     * from the combobox.
     *
     * @param category - to remove from combobox.
     */
    @Override
    public void removeCategoryFromComboBox(String category) {
        appView.removeChosenCategoryFromComboBox(category);
    }


    //    @Override
//    public void isTheCategoryNameInputValid(boolean isValid) {
//        appView.setCategoryInputValid(isValid);
//    }

    @Override
    public void addNewCategoryToComboBox(String category) {
        appView.addCategoryToComboBox(category);
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
     * @param message - the relevant message to display.
     * @param flag - that indicates if the credentials of the user are valid.
     */
    @Override
    public void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInLoginPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /*** This method changes the frame from app page to the login page. */
    @Override
    public void changeFrameFromAppViewToLoginView() {
        appView.dispose();
        loginView.setVisible(true);
    }

    /** This method resets the user. */
    @Override
    public void resetUser() {
        viewModel.resetUser();
    }

    /*** This method changes the frame from the login page to the app page. */
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
     * @param listOfCategories - category list.
     */
    @Override
    public void setCategories(List<String> listOfCategories) {
        appView.setTheCategoriesToCategoriesComboBox(listOfCategories);
    }

    /**
     * This method call the getExpensesBySpecificCategory in viewModel
     * that responsible for getting expenses by given category.
     * @param categoryType - the expenses that would return are going to be based on the categoryType we receive here.
     */
    @Override
    public void getExpensesByCategory(String categoryType) {
        viewModel.getExpensesBySpecificCategory(categoryType);
    }

    /**
     *This method call the setExpensesTableByCategoryInAppView in appView
     * that responsible for setting the expense table.
     * @param listOfExpenses - expenses list.
     */
    @Override
    public void setExpensesTableByCategoryInAppView(List<Expense> listOfExpenses) {
        appView.setTableInAllExpensesPanel(listOfExpenses);
    }

    /**
     * This method call the validateAndAddNewCategory in viewModel
     * that responsible for validating and adding a new category to the database and to the list of categories.
     * @param categoryName - category's name.
     */
    @Override
    public void validateAndSetNewCategory(String categoryName) {
        viewModel.validateAndAddNewCategory(categoryName);
    }

    /**
     * This method call the removeCategory in viewModel
     * that responsible for remove the selected category from the database and from the list of categories.
     * @param categoryName - selected category to remove.
     */
    @Override
    public void removeCategory(String categoryName) {
        viewModel.removeSpecificCategory(categoryName);
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
    public void validateAndAddNewCost(String categorySelected, String sumCost,
                                      String currency, String description, Date date) {

        viewModel.validateAndAddNewCost(categorySelected, sumCost, currency,description ,date);
    }

    /**
     *
     */
    @Override
    public void getCostsID() {
        viewModel.getCostsID();
    }

    /**
     *
     * @param costsID
     */
    @Override
    public void setCostsID(List<Integer> costsID) {
        appView.setTheCostsIDToCostIDComboBox(costsID);
    }

    @Override
    public void removeCost(int costID) {
        viewModel.removeCost(costID);
    }


}