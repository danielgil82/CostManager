package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.viewmodel.ViewModel;

import java.util.Collection;

public interface View
{
    void setIViewModel(ViewModel vm);
    void displayMessage(Message message);
    void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag);
    void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag);
    void init();
    void start();
    void changeFrameFromLoginViewToAppView();
    void changeFrameFromAppViewToLoginView();
    void setSpecificUsersCategories(Collection<String> listOfCategories);
}
