package il.ac.hit.view;

import il.ac.hit.model.User;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.viewmodel.IViewModel;

import javax.swing.*;

public class ViewManager implements IView
{

    private IViewModel viewModel;


    private CostManagerLoginView loginView;
    private CostManagerAppView appView;


    public ViewManager()
    {
        loginView = new CostManagerLoginView(this);
        loginView.setVisible(true);
        //AppView = new CostManagerAppView(user);
    }

    @Override
    public void setIViewModel(IViewModel vm)
    {
        viewModel = vm;
    }

    @Override
    public void init()
    {
        //   loginPageFrame = new LoginPageFrame();
    }

    @Override
    public void start()
    {
    }

    @Override
    public void displayMessage(Message message)
    {
       // JOptionPane.showMessageDialog(null, message.getText());
    }

    @Override
    public void changeFrameFromLoginViewToAppView()
    {
        loginView.setVisible(false);
        appView = new CostManagerAppView();
        appView.setVisible(true);
    }

    public IViewModel getViewModel()
    {
        return viewModel;
    }


    public CostManagerLoginView getLoginView()
    {
        return loginView;
    }
}
