package il.ac.hit.viewmodel;

import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;
import il.ac.hit.model.User;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.CostManagerException;
import il.ac.hit.model.IModel;
import il.ac.hit.view.CostManagerLoginView;
import il.ac.hit.view.ViewManager;
import il.ac.hit.view.IView;

import javax.swing.*;
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
                            ((ViewManager)view).getLoginView().getLabelInvalidDescription().setText(ex.getMessage());
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
                    int x = model.addNewUser(user);





                    //((ViewManager) view).setUser(user);
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
}
