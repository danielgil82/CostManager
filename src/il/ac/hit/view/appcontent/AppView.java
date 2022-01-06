package il.ac.hit.view.appcontent;

import il.ac.hit.model.Expense;
import il.ac.hit.view.ComponentUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

/**
 * class that represents AppView itself
 */
public class AppView extends JFrame {

    /** list of all categories that belongs to a specific user*/
    //private final List<String> categoriesListBySpecificUser = new ArrayList<>();

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
    private JPanel panelNorthAppView;
    private JPanel panelAllExpenses;
    private JPanel panelOperations;
    private JPanel panelReport;
    private JPanel panelCurrencies;
    private JPanel panelSouthAppView;
    private JLayeredPane layeredPaneCenterAppView;
    private JComboBox<String> comboBoxCategories;
    private FlowLayout panelNorthFlowLayout;
    private BorderLayout borderLayoutPanelContent;

    private ExpensesByCategory expensesByCategory;
//    private OperationsPanel operationsPanel;
//    private ReportPanel reportPanel;
//    private CurrenciesPanel currenciesPanel;


    /**
     * ctor that receives an appUtils object which is implemented by ViewManager
     * @param appUtils
     */
    public AppView(AppUtils appUtils) {
        setAppUtils(appUtils);
        initAppView();
        startAppView();
    }

    /*** this method initialize the swing components */
    private void initAppView() {
        comboBoxCategories = new JComboBox<>();
        expensesByCategory = new ExpensesByCategory();
        buttonExpenses = new JButton("Expenses");
        buttonOperations = new JButton("Operations");
        buttonReport = new JButton("Report");
        buttonLogout = new JButton("LogOut");
        buttonCurrencies = new JButton("Currencies");
        labelCostManagerTitle = new JLabel("Cost Manager");
        labelFeedbackMessage = new JLabel();
        borderLayoutPanelContent = new BorderLayout();
        panelAppContent = new JPanel(borderLayoutPanelContent);
        panelNorthAppView = new JPanel();
        panelAllExpenses = new JPanel();
        panelOperations = new JPanel();
        panelReport = new JPanel();
        panelCurrencies = new JPanel();
        panelSouthAppView = new JPanel();
        layeredPaneCenterAppView = new JLayeredPane();
        panelNorthFlowLayout = new FlowLayout(0, 25, 0);
    }

    /**
     * this method settings the panels, buttons and their attributes
     */
    private void startAppView() {
        setApplicationFrame();
        setNorthPanel();
        setNorthPanelComponentAttributes();
        setButtonsActionListeners();
        setComboBoxCategory();
        setLayeredPane();

        //get all the categories that belong to the loggedIn user.
        appUtils.getCategoriesThatBelongToSpecificUser();

        setSouthPanel();
    }

    /**
     * this method recieves the categories that belong to the loggedIn user , and then
     * calls to a method that sets the categories to the combobox
     * @param listOfCategories that are going to be added to the categoriesListBySpecificUser
     */
    public void setTheCategoriesList(List<String> listOfCategories) {
        //insert categories to the combo box
        listOfCategories.add("all");
        insertCategoriesToComboBox(listOfCategories);
    }

    /**
     * this method builds the expenses' table of the user that logged in
     * @param expensesByCategory list of the expenses according to the chosen category.
     */
    public void setTableInAllExpensesPanel(List<Expense> expensesByCategory) {
        // create the titles for the tables
        DefaultTableModel tableDataModel = new DefaultTableModel(new String[]{
                "cost_id", "category", "sum_cost", "currency", "description", "date", "user_id"},
                0);

        // add each cost to the table model
        for (Expense expense : expensesByCategory)
        {
            tableDataModel.addRow(new Object[]{
                    expense.getExpenseID(),
                    expense.getCategory(),
                    expense.getCostSum(),
                    expense.getCurrency(),
                    expense.getExpenseDescription(),
                    expense.getPurchaseDate(),
                    expense.getUserID()
            });
        }
        // link the model and the table
       this.expensesByCategory.tableData.setModel(tableDataModel);
    }

    /**
     * this method set the viewManager data member
     * @param appUtils viewManager that mediate between the view and the  model
     */
    public void setAppUtils(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    /**
     * setting the layered pane.
     */
    private void setLayeredPane() {
        layeredPaneCenterAppView.setBounds(0, 200, 1200, 650);
        layeredPaneCenterAppView.setLayout(new CardLayout(0, 0));
        setPanelAllExpensesPartOfTheLayeredPane();
       // setSignUpPanelPartOfTheLayeredPane();
        panelAppContent.add(layeredPaneCenterAppView, BorderLayout.CENTER);
    }

    /**
     * added to the panelAllExpenses the expensesPanel object ,
     * and added the panelAllExpenses to the LayeredPane
     */
    private void setPanelAllExpensesPartOfTheLayeredPane() {

        panelAllExpenses.add(expensesByCategory);
        layeredPaneCenterAppView.add(panelAllExpenses);
    }

    /** first we clear all the items in the combo box, and then we add each of the categories to the combo box.*/
    private void insertCategoriesToComboBox(List<String> listOfCategories) {
        clearComboBoxesItems();

        for (String category : listOfCategories) {
            comboBoxCategories.addItem(category);
        }
    }

    /*** Setting the combo box */
    private void setComboBoxCategory()
    {
        ComponentUtils.setComponentsAttributes(
                comboBoxCategories,
                new Font("Narkisim", Font.BOLD, 25),
                new Dimension(200, 35));
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

    /**
     * this method settings the south panel of the application and defined the relevant text
     */
    private void setSouthPanel() {
        panelSouthAppView.setBackground(new Color(190, 190, 230, 155));
        panelSouthAppView.setBounds(0, 900, 1400, 100);

        ComponentUtils.setComponentsAttributes(labelFeedbackMessage,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(1350, 50));

        panelSouthAppView.add(labelFeedbackMessage);
        panelAppContent.add(panelSouthAppView, BorderLayout.SOUTH);
    }

    /**
     * this method settings the north panel of the application, which is acting like the client navigation bar.
     */
    private void setNorthPanel() {
        panelNorthAppView.setLayout(panelNorthFlowLayout);
        panelNorthAppView.setBackground(new Color(190, 190, 230, 155));
        panelNorthAppView.setBounds(0, 0, 1400, 200);
        panelNorthAppView.add(buttonExpenses);
        panelNorthAppView.add(buttonOperations);
        panelNorthAppView.add(buttonReport);
        panelNorthAppView.add(buttonCurrencies);
        panelNorthAppView.add(labelCostManagerTitle);
        panelNorthAppView.add(buttonLogout);
        panelAppContent.add(panelNorthAppView, BorderLayout.NORTH);
    }

    /**
     * this method represents the buttons ActionListeners
     */
    private void setButtonsActionListeners() {

        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonExpenses, panelAllExpenses);
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonOperations, panelOperations);
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonReport, panelReport);
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonCurrencies, panelCurrencies);

        //different action listener for the buttonLogout.
        buttonLogout.addActionListener(e -> {
            appUtils.resetUser();
            appUtils.changeFrameFromAppViewToLoginView();
        });
    }

    /**
     * this method sets each of the north's panel components attributes
     * by first extracting all the buttons from the northPanel
     * and then their attributes, and separately set the labelCostManagerTitle attributes
     *
     */
    private void setNorthPanelComponentAttributes() {

        Component[] components = panelNorthAppView.getComponents();

        for (Component component : components) {
            if (component instanceof JButton) {
                ComponentUtils.setComponentsAttributes(component,
                        new Font("Narkisim", Font.BOLD, 25),
                        new Dimension(170, 50));
            }
        }

        labelCostManagerTitle.setFont(new Font("Narkisim", Font.BOLD, 55));
        labelCostManagerTitle.setBounds(1000, 0, 200, 100);
        labelCostManagerTitle.setForeground(Color.red);
    }

    /**
     * this method sets the application frame size
     */
    private void setApplicationFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 1000);
        ComponentUtils.centralizeWindow(this);
        panelAppContent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.add(panelAppContent);
    }

    /**
     * this class represents the expenses panel
     */
    private class ExpensesByCategory extends JPanel {
        /** data member which represents all the expenses by a specific category */
        private CategorySelectorPanel panelCategorySelector;

        /*** swing components*/
        private JPanel panelTableData;
        private JScrollPane scrollPaneTable;
        private JTable tableData;
        private GridLayout gridLayoutExpensesPanel;

        /*** ctor*/
        public ExpensesByCategory() {
            initExpensesPanel();
            startExpensesPanel();
        }

        /** this method inits the all the data members in "this" */
        private void initExpensesPanel() {
            panelCategorySelector = new CategorySelectorPanel();
            panelTableData = new JPanel();
            scrollPaneTable = new JScrollPane();
            tableData = new JTable();
            gridLayoutExpensesPanel = new GridLayout(2, 1, 0, 150);
        }

        /**
         *  this method adding different components to "this",
         *  and calling to a method that builds the expenses table.
         */
        private void startExpensesPanel() {
            this.setLayout(gridLayoutExpensesPanel);
            this.add(panelCategorySelector);
            setExpensesTableBySpecificCategory();
            this.add(panelTableData);
        }

        /** this method builds the expenses table*/
        private void setExpensesTableBySpecificCategory() {
            panelTableData.setLayout(new BorderLayout());
            panelTableData.add(scrollPaneTable);
            tableData.setFont(new Font("Narkisim", Font.PLAIN, 20));
            tableData.setRowHeight(30);
            tableData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scrollPaneTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPaneTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneTable.setViewportView(tableData);
            panelTableData.setPreferredSize(new Dimension(500, 270));
        }

        /**
         * CategorySelectorPanel class allows the user to choose the category in order to display
         * the expenses according to the chosen category
         */
        private class CategorySelectorPanel extends JPanel {
            private JLabel labelFilterExpensesByCategory;
            private JLabel labelCategorySelector;
            private JButton buttonDisplayExpenses;
            private FlowLayout flowLayoutPanelCenterLabelAndComboBox;
            private JPanel panelNorthTitleCategorySelector;
            private JPanel panelCenterLabelAndComboBox;
            private JPanel panelSouthOfTheCategorySelectorPanel;


            /*** ctor */
            public CategorySelectorPanel() {
                initCategorySelectorPanel();
                startCategorySelectorPanel();
            }

            /** this method inits the all the data members in "this" */
            private void initCategorySelectorPanel() {
                labelFilterExpensesByCategory = new JLabel("Filter your expenses by specific category");
                labelCategorySelector = new JLabel("Select Category:");
                buttonDisplayExpenses = new JButton("Display");
                flowLayoutPanelCenterLabelAndComboBox = new FlowLayout();
                flowLayoutPanelCenterLabelAndComboBox.setVgap(35);
                flowLayoutPanelCenterLabelAndComboBox.setHgap(80);
                panelNorthTitleCategorySelector = new JPanel();
                panelCenterLabelAndComboBox = new JPanel(flowLayoutPanelCenterLabelAndComboBox);
                panelSouthOfTheCategorySelectorPanel = new JPanel();
            }

            /**
             * this method calls different methods that set different
             * components attributes.
             */
            private void startCategorySelectorPanel() {
                setLabelFilterByCategoryTitle();
                setLabelSelectCategoryAttributes();
                setBtnDisplayCategoryAttributes();
                locateComponentsOnTheCategorySelectorPanel();
                setButtonDisplayListener();
            }

            /**
             * Setting the label which acts as the title of the panel
             */
            private void setLabelFilterByCategoryTitle() {
                ComponentUtils.setComponentsAttributes(
                        labelFilterExpensesByCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(600, 50));
            }

            /**
             * Setting the label next to the combo box
             */
            private void setLabelSelectCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(
                        labelCategorySelector,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(150, 50));
            }

            /**
             * Setting the button in charge of sending the desired category
             */
            private void setBtnDisplayCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(
                        buttonDisplayExpenses,
                        new Font("Narkisim", Font.BOLD, 25),
                        new Dimension(130, 30));
            }

            /**
             * this method locates all the components and adding them to the appropriate panel
             */
            private void locateComponentsOnTheCategorySelectorPanel() {
                this.setLayout(new BorderLayout());
                panelNorthTitleCategorySelector.add(labelFilterExpensesByCategory);
                this.add(panelNorthTitleCategorySelector, BorderLayout.NORTH);
                panelCenterLabelAndComboBox.add(labelCategorySelector);
                panelCenterLabelAndComboBox.add(comboBoxCategories);
                this.add(panelCenterLabelAndComboBox, BorderLayout.CENTER);
                panelSouthOfTheCategorySelectorPanel.add(buttonDisplayExpenses);
                this.add(panelSouthOfTheCategorySelectorPanel, BorderLayout.SOUTH);
            }

            /** this method adds action listener to the display button.*/
            private void setButtonDisplayListener() {
                buttonDisplayExpenses.addActionListener(e ->
                        appUtils.getExpensesByCategory(comboBoxCategories.getSelectedItem().toString()));
            }
        }
    }
}
