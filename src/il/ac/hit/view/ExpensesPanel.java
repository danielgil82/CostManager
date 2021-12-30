package il.ac.hit.view;

import il.ac.hit.model.Category;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpensesPanel extends JPanel
{
    private CategorySelectorPanel panelCategorySelector;
   // private TableExpensesPanel panelTableExpenses;
    private GridLayout gridLayout;
    private IView viewManager;



    public ExpensesPanel(IView viewManager)
    {
        setViewManager(viewManager);
        panelCategorySelector = new CategorySelectorPanel();
       // panelTableExpenses = new TableExpensesPnael();
    }

    public CategorySelectorPanel getPanelCategorySelector()
    {
        return panelCategorySelector;
    }

    public IView getViewManager()
    {
        return viewManager;
    }

    public void setViewManager(IView viewManager)
    {
        this.viewManager = viewManager;
    }

    /**
     * package friendly class CategorySelectorPanel which
     * allows the user to choose the category in order to display
     * the expenses according to the chosen category
     */
    class CategorySelectorPanel extends JPanel
    {
        private JLabel labelCategorySelector;
        private JComboBox<String> comboBoxCategories;
        private JButton buttonDisplayData;
        private FlowLayout flowLayout;
        private JPanel panelUpCategorySelector;
        private JPanel panelDownCategorySelector;
    //    private final List<Category> listOfCategories = new ArrayList<>();

        public CategorySelectorPanel()
        {
            initCategorySelectorPanel();
            startCategorySelectorPanel();
        }

        private void initCategorySelectorPanel()
        {
            labelCategorySelector = new JLabel("Select Category:");
            comboBoxCategories = new JComboBox<String>();
            buttonDisplayData = new JButton("Display");
            flowLayout = new FlowLayout();
            flowLayout.setVgap(35);
            flowLayout.setHgap(50);
            panelUpCategorySelector = new JPanel(flowLayout);
            panelDownCategorySelector = new JPanel();
        }

        private void startCategorySelectorPanel()
        {
            setComboBoxCategories();
        }

        /**
         *
         * asking for the categories from the model via the ViewModel
         */
        private void setComboBoxCategories()
        {
            //Casting the viewManager from IView To ViewManager
            ((ViewManager)(viewManager)).getViewModel().getCategoriesBySpecificUser();
        }

        /**
         * @param listOfCategories the categories that are being put into the combo box
         * setting the combo box with the categories
         */
        private void addCategoriesIntoComboBox(Collection<String> listOfCategories)
        {

        }

        /**
         * Auxiliary public method for calling a private one here in the class because
         * we don't want this logic to be exposed to other classes outside.
         * @param listOfCategories the categories that are being put into the combo box
         */
        public void auxiliaryAddCategoriesIntoComboBox(Collection<String> listOfCategories)
        {
            addCategoriesIntoComboBox(listOfCategories);
        }

        /**
         * clear combo box  responsible for clearing all the category combo boxes on load
         */
        private void clearComboBoxesItems()
        {
            if (comboBoxCategories.getItemCount() > 0)
            {
                comboBoxCategories.removeAllItems();
            }
        }

    }

}
