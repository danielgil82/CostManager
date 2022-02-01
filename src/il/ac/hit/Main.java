package il.ac.hit;

import il.ac.hit.model.CostManagerException;
import il.ac.hit.model.CostManagerModel;
import il.ac.hit.model.Model;
import il.ac.hit.view.ViewManager;
import il.ac.hit.view.View;
import il.ac.hit.viewmodel.CostManagerViewModel;
import il.ac.hit.viewmodel.ViewModel;

import javax.swing.*;

public class Main
{
    public static void main(String args[]) throws CostManagerException
    {
        //construct the MVVM parts of the project.
        Model model = new CostManagerModel();
        ViewModel viewModel = new CostManagerViewModel();
        View view = new ViewManager();

        SwingUtilities.invokeLater(() -> {
            view.init();
            view.start();
        });

        //set the different parts of MVVM to the according place.
        viewModel.setModel(model);
        viewModel.setView(view);
        view.setIViewModel(viewModel);
    }
}
