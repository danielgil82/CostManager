package il.ac.hit.viewmodel;

import il.ac.hit.auxiliary.HandlingMessage;
import il.ac.hit.model.User;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.CostManagerException;
import il.ac.hit.model.Model;
import il.ac.hit.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;

/**
 * CostManagerViewModel is the class that mediates between the view and the model parts.
 * In this class we have overridden methods that were declared in the ViewModel interface.
 * A prat of these methods we have a private methods that do some logic.
 */
public class CostManagerViewModel implements ViewModel {

    private View view;
    private Model model;
    private User user;
    private ExecutorService service;
    private List<String> categoriesOfTheUser = new ArrayList<>();
    /**
     * ctor of the CostManagerViewModel, it constructs the number of the thread
     * that are going to be in the thread pool
     */
    public CostManagerViewModel() {
        this.service = Executors.newFixedThreadPool(3);
    }

    /**
     * here we ensure that the user exists in the database.
     * @param fullName of the user.
     * @param password of the user.
     */
    @Override
    public void validateUserExistence(String fullName, String password) {
        service.submit(() -> {
            try {
                user = model.getUser(fullName, password);

                //Doesn't require a check on the user because if the user exists it will return into the user, and then
                //frames will be changed, else it will be caught by the catch
               SwingUtilities.invokeLater(() -> view.changeFrameFromLoginViewToAppView());

            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
            }
        });
    }

    /**
     * first we check if the user exists, if it does exist, we catch the exception
     * that tells that the user already exists, else we add a new user to the database,
     * and display a feedback message to the user.
     * @param user to add to the database.
     */
    @Override
    public void addNewUser(User user) {
        service.submit(() -> {

            try {
                model.checkIfTheUserExists(user);

                model.addNewUserToDBAndUpdateTheListOfUsers(user);

                SwingUtilities.invokeLater(() ->
                        view.displayMessage(new Message(HandlingMessage.SIGNED_UP_SUCCESSFULLY.toString())));

            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
            }
        });
    }

    @Override
    public void getCategoriesBySpecificUser() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    categoriesOfTheUser = model.getCategoriesBySpecificUser(user.getUserID());

                    SwingUtilities.invokeLater(() -> view.setCategoriesAccordingToTheLoggedInUser(categoriesOfTheUser));

                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
                }
            }
        });
    }



    /**
     * first we ensure that the awt event thread is the one that run right now.
     * Then we validate users credentials when he signs up.
     * @param fullName fullName of the user
     * @param password password of the user
     * @param confirmedPassword confirmed password of the user
     */
    @Override
    public void userCredentialsForSignUpPanel(String fullName, String password, String confirmedPassword) {

        if (SwingUtilities.isEventDispatchThread())
        {
            signUpValidatorCredentials(fullName, password, confirmedPassword);
        }
        else
        {
            SwingUtilities.invokeLater(() -> signUpValidatorCredentials(fullName, password, confirmedPassword));
        }
    }

    /**
     * first we ensure that the awt event thread is the one that run right now.
     * Then we validate users credentials when he logs in.
     * @param fullName fullName of the user
     * @param password password of the user
     */
    @Override
    public void userCredentialsForLoginPanel(String fullName, String password) {
        if (SwingUtilities.isEventDispatchThread())
        {
            loginValidatorCredentials(fullName, password);
        }
        else
        {
            SwingUtilities.invokeLater(() -> loginValidatorCredentials(fullName, password));
        }
    }

    /**
     * this method suppose to validate the credentials of the user
     * @param fullName of the user
     * @param password of the user
     */
    private void loginValidatorCredentials(String fullName, String password) {
        if (fullName.equals("") || password.equals("")) {
            view.displayMessageAndSetTheFlagValidatorForLoginPanel(
                    new Message(HandlingMessage.EMPTY_FIELDS.toString()), false);
        } else if (!validateUsersFullName(fullName)) {
            view.displayMessageAndSetTheFlagValidatorForLoginPanel
                    (new Message(HandlingMessage.INVALID_FULL_NAME.toString()), false);
        } else {
            view.displayMessageAndSetTheFlagValidatorForLoginPanel
                    (new Message(""), true);
        }
    }

    /**
     * Here we validate users credentials when he signs up. The reason for the validations to appear here ,at the
     * view model class is because of a reuse, if in the future we'll change the GUI to another type of GUI
     * this kind of logic would still be in need, that's why it is important to implement it here.
     * @param fullName fullName of the user
     * @param password password of the user
     * @param confirmedPassword confirmed password of the user
     */
    private void signUpValidatorCredentials(String fullName, String password, String confirmedPassword) {

        if(fullName.equals("") || password.equals("") || confirmedPassword.equals("")){
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel(
                    new Message(HandlingMessage.EMPTY_FIELDS.toString()), false);
        }
        else if (!validateUsersFullName(fullName)) {
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel
                    (new Message(HandlingMessage.INVALID_FULL_NAME.toString()), false);
        }
        else if(!confirmPasswords(password, confirmedPassword, String::equals)){
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel(
                    new Message(HandlingMessage.PASSWORDS_DO_NOT_MATCH.toString()), false);
        }
        else{
            view.displayMessageAndSetTheFlagValidatorForSignUpPanel(
                    new Message(""), true);
        }
    }

    /**
     * this method use the functional interface as BiPredicate inorder to test if the 2 passwords are equal.
     * @param firstPassword first password
     * @param secondPassword second password
     * @param passwordsMatchTest functional interface which gets the lambda function and uses the test abstract function.
     * @return true if the passwords match else false.
     */
    private boolean confirmPasswords(String firstPassword, String secondPassword, BiPredicate<String, String> passwordsMatchTest) {
        return passwordsMatchTest.test(firstPassword, secondPassword);
    }

    /**
     * @param fullName users input for full name
     * @return checks if the full name consists of letters and spaces.
     */
    private boolean validateUsersFullName(String fullName) {
        char[] chars = fullName.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }

        return true;
    }

    /**
     * Here we set the view data member.
     * @param view
     */
    @Override
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Here we set the model data member.
     * @param model the model of our application.
     */
    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Here we reset the user.
     */
    @Override
    public void resetUser() {
        user = null;
    }
}