package il.ac.hit.viewModel;

import il.ac.hit.model.IModel;
import il.ac.hit.view.IView;

public interface IViewModel
{
    public void setView(IView view);
    public void setModel(IModel model);
    public void getUser(String fullName, String password);


}
