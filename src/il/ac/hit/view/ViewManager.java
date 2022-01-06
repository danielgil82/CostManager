package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.Expense;
import il.ac.hit.model.User;
import il.ac.hit.view.appcontent.AppUtils;
import il.ac.hit.view.appcontent.AppView;
import il.ac.hit.view.login.LoginUtils;
import il.ac.hit.view.login.LoginView;
import il.ac.hit.viewmodel.ViewModel;

import java.util.List;

/**
 * An actual class that represents a concrete view.
 * an also this class is a kind of LoginUtils interface because it has a methods that are going to be used in the
 * LoginView Class.
 */
public class ViewManager implements View , LoginUtils , AppUtils {

    /** different frames of the application*/
    private LoginView loginView;
    private AppView appView;


    /** viewModel mediates between the view and model parts */
    private ViewModel viewModel;


    /**
     * Constructor that inits the loginView member
     */
    public ViewManager() {

    }

    /** getters */
    public ViewModel getViewModel() {
        return viewModel;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    /**
     * here we set the view model
     * @param vm the view model
     */
    @Override
    public void setIViewModel(ViewModel vm) {
        viewModel = vm;
    }

    /**
     *  this method suppose to send the full name and the password of the user
     *  to the viewModel, because of a reuse thinking, because if the in the future
     *  we'd change the gui part to another one, the logic of validating users credentials
     *  will still be relevant.
     * @param fullName of the user
     * @param password of the user
     */
    @Override
    public void validateUserCredentials(String fullName, String password) {
        viewModel.validateUserCredentialsForLoginPanel(fullName, password);
    }

    /**
     * this method will get invoked when the button ok in the loginPanel will get clicked
     * the method that will get invoked when the ok button in loginPanel clicked
     * @param fullName users name
     * @param password users password
     */
    @Override
    public void validateUserExistence(String fullName, String password) {
        viewModel.validateUserExistence(fullName, password);
    }

    /** this method adds new user to database.
     * @param userToAdd to the database
     */
    @Override
    public void addNewUser(User userToAdd){
        viewModel.addNewUser(userToAdd);
    }

    /**
     * @param fullName of the user
     * @param password password the user choose
     * @param confirmedPassword confirmation of the users password
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
     * this method receives message and displays it to the user via the loginView
     * @param message the relevant message to display
     */
    @Override
    public void displayMessage(Message message) {
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /**
     * this method receives message and displays it to the user via the loginView
     * and updates the flag that indicates the validity of the user's credentials
     *
     * @param message the relevant message to display
     * @param flag that indicates if the credentials of the user are valid
     */
    @Override
    public void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInSignUpPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /**
     * this method receives message and displays it to the user via the loginView
     * and updates the flag that indicates the validity of the user's credentials
     * @param message the relevant message to display
     * @param flag that indicates if the credentials of the user are valid
     */
    @Override
    public void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInLoginPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    /**
     * this method changes the frame from app page to the login page.
     */
    @Override
    public void changeFrameFromAppViewToLoginView() {
        appView.dispose();
        loginView.setVisible(true);
    }

    /**
     * this method resets the user
     */
    @Override
    public void resetUser() {
        viewModel.resetUser();
    }

    /**
     * this method changes the frame from the login page to the app page.
     */
    @Override
    public void changeFrameFromLoginViewToAppView() {
        loginView.setVisible(false);
        appView = new AppView(this);
        appView.setVisible(true);
    }

    @Override
    public void getCategoriesThatBelongToSpecificUser() {
        viewModel.getCategoriesBySpecificUser();
    }

    @Override
    public void setCategoriesAccordingToTheLoggedInUser(List<String> listOfCategories) {
        appView.setTheCategoriesList(listOfCategories);
    }

    @Override
    public void getExpensesByCategory(String categoryType) {
        viewModel.getExpensesBySpecificCategory(categoryType);
    }

    @Override
    public void setExpensesTableByCategoryInAppView(List<Expense> listOfExpenses) {
        appView.setTableInAllExpensesPanel(listOfExpenses);
    }

    @Override
    public void validateAndSetNewCategory(String category) {
        viewModel.addNewCategory(category);
    }
}