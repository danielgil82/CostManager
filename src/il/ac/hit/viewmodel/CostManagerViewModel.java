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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;

public class CostManagerViewModel implements ViewModel {

    private View view;
    private Model model;
    private ExecutorService service;
    private User user;


    public CostManagerViewModel() {
        this.service = Executors.newFixedThreadPool(3);
    }

    @Override
    public void validateUserExistence(String fullName, String password) {
        service.submit(() -> {
            try {
                user = model.getUser(fullName, password);

//                if (user != null) {
                //Doesn't require a check on the user because if the user exists it will return into the user, and then
                //frames will be changed, else it will be caught by the catch
                view.changeFrameFromLoginViewToAppView();
//                }
//                else {
//                    throw new CostManagerException(USER_DOES_NOT_EXISTS);
//                }
            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
            }
        });
    }

    /**
     * first we check if the user exists, if it does exist we catch the exception
     * that tells that the user already exists. else we add a new user to the data base
     *
     * @param user
     */
    @Override
    public void addNewUser(User user) {
        service.submit(() -> {

            try {

                model.checkIfTheUserExists(user);

                model.addNewUserToDBAndUpdateTheListOfUsers(user);

                SwingUtilities.invokeLater(() ->
                        view.displayMessage
                                (new Message(HandlingMessage.SIGNED_UP_SUCCESSFULLY.toString())));

//                if (affectedRows == 0) {
//                    throw new CostManagerException(USER_ALREADY_EXISTS);
//                } else if (affectedRows == 1) {
//                    SwingUtilities.invokeLater(() -> view.displayMessage
//                            (new Message(HandlingMessage.SIGNED_UP_SUCCESSFULLY.toString())));

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
                    Collection<String> categoryCollection = new ArrayList<>();

                    categoryCollection = model.getCategoriesBySpecificUser(user.getUserID());

                    view.setSpecificUsersCategories(categoryCollection);
                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
                }
            }
        });
    }

    /**
     *
     * @param fullName fullName of the user
     * @param password password of the user
     * @param confirmedPassword confirmed password of the user
     */
    @Override
    public void validateUsersFullNameAndPassword(String fullName, String password, String confirmedPassword) {

        if (SwingUtilities.isEventDispatchThread())
        {
            loginValidator(fullName, password, confirmedPassword);
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    loginValidator(fullName, password, confirmedPassword);
                }
            });
        }
    }

    /**
     * this method does all the validations, the reason for the validations to appear here at the
     * view model class is because of a reuse, if in the future we'll change the GUI to another type of GUI
     * this kind of logic validations would still be in need , that's why it is important to implement it here.
     * @param fullName fullName of the user
     * @param password password of the user
     * @param confirmedPassword confirmed password of the user
     */
    private void loginValidator(String fullName, String password, String confirmedPassword) {
        if(fullName.equals("") || password.equals("") || confirmedPassword.equals("")){
            view.displayMessage(new Message(HandlingMessage.EMPTY_FIELDS.toString()));
        }
        else if (!validateUsersFullName(fullName)) {
            view.displayMessage(new Message(HandlingMessage.INVALID_FULL_NAME.toString()));
        }
        else if(!confirmPasswords(password, confirmedPassword, String::equals)){
            view.displayMessage(new Message(HandlingMessage.PASSWORDS_DO_NOT_MATCH.toString()));
        }
    }

    /**
     * this method use the functional interface as BiPredicate inorder to test if the 2 passwords are equal.
     * @param firstPassword first password
     * @param secondPassword second password
     * @param passwordsMatchTest functional interface which gets the lambda function
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


    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    public void resetUser() {
        user = null;
    }
}
