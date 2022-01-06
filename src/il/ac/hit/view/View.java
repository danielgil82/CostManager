package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.model.Expense;
import il.ac.hit.viewmodel.ViewModel;

import java.util.Collection;
import java.util.List;

/**
 * View interface is an interface, that has the methods that are going
 * to be implemented by a concrete view object which is in our case would be an object of the ViewManager class.
 */
public interface View
{
    void setIViewModel(ViewModel vm);
    void displayMessage(Message message);
    void displayMessageAndSetTheFlagValidatorForSignUpPanel(Message message, boolean flag);
    void displayMessageAndSetTheFlagValidatorForLoginPanel(Message message, boolean flag);
    void init();
    void start();
    void changeFrameFromLoginViewToAppView();
    void setCategoriesAccordingToTheLoggedInUser(List<String> listOfCategories);
    void setExpensesTableByCategoryInAppView(List<Expense> listOfExpenses);
}
