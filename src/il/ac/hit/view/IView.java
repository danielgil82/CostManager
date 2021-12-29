package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.viewmodel.IViewModel;

import javax.swing.*;
import java.util.Collection;

public interface IView
{

    void setIViewModel(IViewModel vm);
    void displayMessage(Message message);
    void init();
    void start();
    void changeFrameFromLoginViewToAppView();
    void changeFrameFromAppViewToLoginView();
    void setSpecificUsersCategories(Collection<String> listOfCategories);
}
