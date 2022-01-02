package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.User;
import il.ac.hit.viewmodel.CostManagerViewModel;
import il.ac.hit.viewmodel.ViewModel;

import java.util.Collection;

public class ViewManager implements View , LoginUtils {

    private ViewModel viewModel;
    private LoginView loginView;
    private AppView appView;

    public ViewModel getViewModel() {
        return viewModel;
    }

    public LoginView getLoginView() {
        return loginView;
    }


    public ViewManager() {
      //  loginView = new LoginView(this::viewManagerAddNewUser , this::viewManagerValidateUserExistence);
        loginView = new LoginView(this);
        loginView.setVisible(true);


        //AppView = new CostManagerAppView(user);
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
        viewModel.userCredentialsForLoginPanel(fullName, password);
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
     *
     * @param fullName of the user
     * @param password password the user choose
     * @param confirmedPassword confirmation of the users password
     */
    @Override
    public void validateUsersFullNameAndPasswords(String fullName, String password, String confirmedPassword) {
        viewModel.userCredentialsForSignUpPanel(fullName, password, confirmedPassword);
    }

    @Override
    public void setIViewModel(ViewModel vm) {
        viewModel = vm;
    }

    @Override
    public void init() {
        //   loginPageFrame = new LoginPageFrame();
    }

    @Override
    public void start() {
    }

    @Override
    public void displayMessage(Message message) {
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    @Override
    public void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInSignUpPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    @Override
    public void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag) {
        loginView.setAreUserCredentialsValidInLoginPanel(flag);
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    @Override
    public void changeFrameFromAppViewToLoginView() {
        appView.dispose();
        loginView.setVisible(true);
    }

    public void resetUser() {
        ((CostManagerViewModel) viewModel).resetUser();
    }

    @Override
    public void changeFrameFromLoginViewToAppView() {
        loginView.setVisible(false);
        appView = new AppView(this);
        appView.setVisible(true);
    }

    @Override
    public void setSpecificUsersCategories(Collection<String> listOfCategories) {
        for (String category : listOfCategories) {
            appView.getListOfCategories().add(category);
        }
        appView.getExpensesPanel().getPanelCategorySelector().auxiliaryAddCategoriesIntoComboBox(listOfCategories);
    }
}