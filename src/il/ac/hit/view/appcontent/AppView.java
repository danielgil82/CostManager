package il.ac.hit.view.appcontent;

import il.ac.hit.view.ComponentUtils;
import il.ac.hit.view.View;
import il.ac.hit.view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * class that represents AppView itself
 */
public class AppView extends JFrame {

    /** list of all categories */
    private final Collection<String> listOfCategories = new ArrayList<>();

    /** viewManger is the mediator between the view and the model */
    private View viewManager;

    private AppUtils appUtils;
    /**
     * swing components
     */
    private JButton buttonExpenses;
    private JButton buttonOperations;
    private JButton buttonReport;
    private JButton buttonLogout;
    private JButton buttonCurrencies;
    private JLabel labelCostManagerTitle;
    private JLabel labelFeedbackMessage;
    private JPanel panelAppContent;
    private JPanel panelNorth;
    private JPanel panelExpenses;
    private JPanel panelOperations;
    private JPanel panelReport;
    private JPanel panelCurrencies;

    private JPanel panelSouth;
    private JLayeredPane layeredPaneCenter;

    private FlowLayout panelNorthFlowLayout;
    private BorderLayout borderLayoutPanelContent;

    private ExpensesPanel expensesPanel;
//    private OperationsPanel operationsPanel;
//    private ReportPanel reportPanel;
//    private CurrenciesPanel currenciesPanel;


    /**
     * ctor that receives the viewManager
     *
     * @param appUtils
     */
    public AppView(AppUtils appUtils) {
        setAppUtils(appUtils);
        initAppView();
        startAppView();
    }

    /**
     * this method returns the listOfCategories
     * @return all the categories
     */
    public Collection<String> getListOfCategories() {
        return listOfCategories;
    }

    /**
     * this method return the expense panel
     * @return expense panel
     */
    public ExpensesPanel getExpensesPanel() {
        return expensesPanel;
    }

    /**
     * this method set the viewManager data member
     * @param appUtils viewManager that mediate between the view and the  model
     */
    public void setAppUtils(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    /**
     * this method initialize the swing components
     */
    private void initAppView() {
        expensesPanel = new ExpensesPanel(viewManager);
        buttonExpenses = new JButton("Expenses");
        buttonOperations = new JButton("Operations");
        buttonReport = new JButton("Report");
        buttonLogout = new JButton("LogOut");
        buttonCurrencies = new JButton("Currencies");
        labelCostManagerTitle = new JLabel("Cost Manager");
        labelFeedbackMessage = new JLabel();
        panelAppContent = new JPanel();
        panelNorth = new JPanel();
        panelExpenses = new JPanel();
        panelOperations = new JPanel();
        panelReport = new JPanel();
        panelCurrencies = new JPanel();
        panelSouth = new JPanel();
        layeredPaneCenter = new JLayeredPane();
        panelNorthFlowLayout = new FlowLayout(0, 25, 0);
        borderLayoutPanelContent = new BorderLayout();
    }

    /**
     * this method settings the panels, buttons and their attributes
     */
    private void startAppView() {
        setApplicationFrame();
        setNorthPanel();
        setNorthPanelComponentAttributes();
        setButtonsActionListeners();




        setSouthPanel();
    }

    /**
     * this method settings the south panel of the application and defined the relevant text
     */
    private void setSouthPanel() {
        panelSouth.setBackground(new Color(190, 190, 230, 155));
        panelSouth.setBounds(0, 900, 1300, 100);

        ComponentUtils.setComponentsAttributes(labelFeedbackMessage,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(1240, 50));

        panelSouth.add(labelFeedbackMessage);
        panelAppContent.add(panelSouth, BorderLayout.SOUTH);
    }

    /**
     * this method settings the north panel of the application, which is acting like the client navigation bar.
     */
    private void setNorthPanel() {
        panelNorth.setLayout(panelNorthFlowLayout);
        panelNorth.setBackground(new Color(190, 190, 230, 155));
        panelNorth.setBounds(0, 0, 1300, 200);
        panelNorth.add(buttonExpenses);
        panelNorth.add(buttonOperations);
        panelNorth.add(buttonReport);
        panelNorth.add(buttonCurrencies);
        panelNorth.add(labelCostManagerTitle);
        panelNorth.add(buttonLogout);
        panelAppContent.add(panelNorth, BorderLayout.NORTH);
    }

    /**
     * this method represents the buttons ActionListeners
     */
    private void setButtonsActionListeners() {

        setActionListeners(buttonExpenses, panelExpenses);
        setActionListeners(buttonOperations, panelOperations);
        setActionListeners(buttonReport, panelReport);
        setActionListeners(buttonCurrencies, panelCurrencies);
//        buttonExpenses.addActionListener(e -> {
//            layeredPaneCenter.removeAll();
//            layeredPaneCenter.add(panelExpenses);
//            layeredPaneCenter.revalidate();
//        });
//
//        buttonOperations.addActionListener(e -> {
//            layeredPaneCenter.removeAll();
//            layeredPaneCenter.add(panelOperations);
//            layeredPaneCenter.revalidate();
//        });
//
//        buttonReport.addActionListener(e -> {
//            layeredPaneCenter.removeAll();
//            layeredPaneCenter.add(panelReport);
//            layeredPaneCenter.revalidate();
//        });

        buttonLogout.addActionListener(e -> {
            appUtils.resetUser();
            appUtils.changeFrameFromAppViewToLoginView();
        });
    }

    /** this method changes between different panels */
    private void setActionListeners(JButton button, JPanel panel) {
        button.addActionListener(e -> {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(panel);
            layeredPaneCenter.revalidate();
        });
    }

    /**
     * this method sets each of the north's panel components attributes
     */
    private void setNorthPanelComponentAttributes() {

        Component[] components = panelNorth.getComponents();

        for (Component component : components) {
            if (component instanceof JButton) {
                ComponentUtils.setComponentsAttributes(component,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(180, 70));
            }
        }

        labelCostManagerTitle.setFont(new Font("Narkisim", Font.BOLD, 50));
        labelCostManagerTitle.setBounds(1000, 0, 200, 100);
        labelCostManagerTitle.setForeground(Color.red);
    }

    /**
     * this method sets the application frame size
     */
    private void setApplicationFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1300, 1000);
        ComponentUtils.centralizeWindow(this);
        panelAppContent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.add(panelAppContent);
    }

    /**
     * this class represents the expenses panel
     */
    private class ExpensesPanel extends JPanel {
        /** data member which represents all the expenses by a specific category */
        private CategorySelectorPanel panelCategorySelector;

        /*** swing components*/
        // private TableExpensesPanel panelTableExpenses;
        private JPanel panelTableData;
        private JScrollPane scrollPaneTable;
        private JTable tableData;
        private GridLayout gridLayout;
        /** viewManger that mediate between the view and the model */
        private View viewManager;

        /**
         * ctor that receives a viewManager parameter
         * @param viewManager the data member that mediate between the view and the model
         */
        public ExpensesPanel(View viewManager)
        {
            setViewManager(viewManager);
            panelCategorySelector = new CategorySelectorPanel();
            // panelTableExpenses = new TableExpensesPnael();
        }

        /**
         * this method returns the panelCategorySelector
         * @return panel that represents the panel category selector
         */
        public CategorySelectorPanel getPanelCategorySelector()
        {
            return panelCategorySelector;
        }

        /**
         * this method returns the viewManager data member
         * @return viewManager data member
         */
        public View getViewManager()
        {
            return viewManager;
        }

        /**
         * this method sets the viewManager data member
         * @param viewManager data member that mediate between the view and the model
         */
        public void setViewManager(View viewManager)
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

            /**
             *
             */
            public CategorySelectorPanel()
            {
                initCategorySelectorPanel();
                startCategorySelectorPanel();
            }

            /**
             *
             */
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
}
