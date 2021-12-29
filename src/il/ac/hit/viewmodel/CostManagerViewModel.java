package il.ac.hit.viewmodel;

import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;
import il.ac.hit.model.User;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.CostManagerException;
import il.ac.hit.model.IModel;
import il.ac.hit.view.IView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CostManagerViewModel implements IViewModel , IErrorAndExceptionsHandlingStrings
{

    private IView view;
    private IModel model;
    private ExecutorService service;
    private User user;


    public CostManagerViewModel()
    {
        this.service = Executors.newFixedThreadPool(3);
    }

    @Override
    public void getUser(String fullName, String password)
    {
        service.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    user = model.getUser(fullName, password);

                    if (user != null)
                    {
                        view.changeFrameFromLoginViewToAppView();
                    }

                    else
                    {
                        throw new CostManagerException(USER_DOES_NOT_EXISTS);
                    }
                }
                catch (CostManagerException ex)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.displayMessage(new Message(ex.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void addNewUser(User user)
    {
        service.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    int affectedRows = model.addNewUserToDB(user);

                    if (affectedRows == 0)
                    {
                        throw new CostManagerException(USER_ALREADY_EXISTS);
                    }
                    else if (affectedRows == 1)
                    {
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                view.displayMessage(new Message(SIGNED_UP_SUCCESSFULLY));
                            }
                        });
                    }
                }

                catch (CostManagerException ex)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.displayMessage(new Message(ex.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getCategoriesBySpecificUser()
    {
        service.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Collection<String> categoryCollection = new ArrayList<>();

                    categoryCollection = model.getCategoriesBySpecificUser(user.getUserID());

                    view.setSpecificUsersCategories(categoryCollection);
                }

                catch (CostManagerException ex)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.displayMessage(new Message(ex.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setView(IView view)
    {
        this.view = view;
    }

    @Override
    public void setModel(IModel model)
    {
        this.model = model;
    }

    public void resetUser()
    {
        user = null;
    }
}
