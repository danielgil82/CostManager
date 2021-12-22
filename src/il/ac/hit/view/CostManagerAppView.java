package il.ac.hit.view;

import il.ac.hit.model.User;

import javax.swing.*;

public class CostManagerAppView extends JFrame
{
    private User loggedInUser;

    public CostManagerAppView(User user)
    {
        setLoggedInUser(user);
    }

    public User getLoggedInUser()
    {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser)
    {
        this.loggedInUser = loggedInUser;
    }
}
