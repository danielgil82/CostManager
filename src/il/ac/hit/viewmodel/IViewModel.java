package il.ac.hit.viewmodel;

import il.ac.hit.model.IModel;
import il.ac.hit.model.User;
import il.ac.hit.view.IView;

public interface IViewModel
{
    public void setView(IView view);
    public void setModel(IModel model);
    public void getUser(String fullName, String password);
    public void addNewUser(User user);

}
