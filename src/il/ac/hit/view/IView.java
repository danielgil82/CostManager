package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.viewModel.IViewModel;

public interface IView
{

    void setIViewModel(IViewModel vm);
    void displayMessage(Message message);
    void init();
    void start();
}
