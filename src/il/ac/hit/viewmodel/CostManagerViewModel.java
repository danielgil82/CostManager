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

                if (user != null) {
                    view.changeFrameFromLoginViewToAppView();
                } else {
                    throw new CostManagerException(USER_DOES_NOT_EXISTS);
                }
            } catch (CostManagerException ex) {
                //lambda expression because Runnable is a functional interface
                SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
            }
        });
    }

    @Override
    public void addNewUser(User user) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    int affectedRows = model.addNewUserToDB(user);

                    if (affectedRows == 0) {
                        throw new CostManagerException(USER_ALREADY_EXISTS);
                    } else if (affectedRows == 1) {
                        SwingUtilities.invokeLater(() -> view.displayMessage
                                (new Message(HandlingMessage.SIGNED_UP_SUCCESSFULLY.toString())));
                    }
                } catch (CostManagerException ex) {
                    //lambda expression because Runnable is a functional interface
                    SwingUtilities.invokeLater(() -> view.displayMessage(new Message(ex.getMessage())));
                }
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
