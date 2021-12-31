package il.ac.hit.view;

import il.ac.hit.auxiliary.Message;
import il.ac.hit.viewmodel.CostManagerViewModel;
import il.ac.hit.viewmodel.ViewModel;

import java.util.Collection;

public class ViewManager implements View {

    private ViewModel viewModel;
    private LoginView loginView;
    private AppView appView;

    public ViewModel getViewModel() {
        return viewModel;
    }

    public LoginView getLoginView() {
        return loginView;
    }


    public ViewManager() {
        loginView = new LoginView(this, viewModel::validateUserExistence);
        loginView.setVisible(true);
        //AppView = new CostManagerAppView(user);
    }

    @Override
    public void setIViewModel(ViewModel vm) {
        viewModel = vm;
    }

    @Override
    public void init() {
        //   loginPageFrame = new LoginPageFrame();
    }

    @Override
    public void start() {
    }

    @Override
    public void displayMessage(Message message) {
        loginView.getLabelInvalidDescription().setText(message.getMessage());
    }

    @Override
    public void changeFrameFromAppViewToLoginView() {
        appView.dispose();
        loginView.setVisible(true);
    }

    public void resetUser() {
        ((CostManagerViewModel) viewModel).resetUser();
    }

    @Override
    public void changeFrameFromLoginViewToAppView() {
        loginView.setVisible(false);
        appView = new AppView(this);
        appView.setVisible(true);
    }

    @Override
    public void setSpecificUsersCategories(Collection<String> listOfCategories) {
        for (String category : listOfCategories) {
            appView.getListOfCategories().add(category);
        }
        appView.getExpensesPanel().getPanelCategorySelector().auxiliaryAddCategoriesIntoComboBox(listOfCategories);
    }
}