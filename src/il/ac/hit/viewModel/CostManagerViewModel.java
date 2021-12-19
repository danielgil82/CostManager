package il.ac.hit.viewModel;

import il.ac.hit.User;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.exceptions.CostManagerException;
import il.ac.hit.model.CostManagerModel;
import il.ac.hit.model.IModel;
import il.ac.hit.view.CostManagerView;
import il.ac.hit.view.IView;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CostManagerViewModel implements IViewModel
{

    private IView view;
    private IModel model;
    private ExecutorService service;

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
                    User user = model.getUser(fullName, password);
                    ((CostManagerView)view).setLoggedInUser(user);
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
        public void setView (IView view)
        {
            this.view = view;
        }

        @Override
        public void setModel (IModel model)
        {
            this.model = model;
        }
    }
