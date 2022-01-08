package il.ac.hit.view.appcontent;

import com.toedter.calendar.JDateChooser;
import il.ac.hit.model.Expense;
import il.ac.hit.view.ComponentUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Class that represents AppView itself.
 */
public class AppView extends JFrame {

    /** List of all categories that belongs to a specific user. */
    //private final List<String> categoriesListBySpecificUser = new ArrayList<>();
    private boolean isCategoryInputValid;
    private AppUtils appUtils;
    /*** Swing components. */
    private JButton buttonExpenses;
    private JButton buttonOperations;
    private JButton buttonReport;
    private JButton buttonLogout;
    private JButton buttonCurrencies;
    private JLabel labelExpenseManagerTitle;
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
    private CategoryAndExpenseOperations categoryAndExpenseOperations;
//    private ReportPanel reportPanel;
//    private CurrenciesPanel currenciesPanel;


    /**
     * Ctor that receives an appUtils object which is implemented by ViewManager.
     * @param appUtils - ...
     */
    public AppView(AppUtils appUtils) {
        setAppUtils(appUtils);
        initAppView();
        startAppView();
    }

    /*** This method initialize the swing components. */
    private void initAppView() {
        comboBoxCategories = new JComboBox<>();
        expensesByCategory = new ExpensesByCategory();
        categoryAndExpenseOperations = new CategoryAndExpenseOperations();
        buttonExpenses = new JButton("Expenses");
        buttonOperations = new JButton("Operations");
        buttonReport = new JButton("Report");
        buttonLogout = new JButton("LogOut");
        buttonCurrencies = new JButton("Currencies");
        labelExpenseManagerTitle = new JLabel("Cost Manager");
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
     * This method settings the panels, buttons and their attributes.
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
     * This method removes the selected category from the combobox.
     * @param category - to be removed.
     */
    public void removeChosenCategoryFromComboBox(String category) {
        comboBoxCategories.removeItem(comboBoxCategories.getSelectedItem());
    }

    /**
     * This method adds a new category to the combobox.
     * @param category - the chosen category.
     */
    public void addCategoryToComboBox(String category) {
        comboBoxCategories.addItem(category);
    }

    /**
     * setter for categoryInput
     * @param categoryInputValid indicates if the category name is valid or not
     */
    public void setCategoryInputValid(boolean categoryInputValid) {
        isCategoryInputValid = categoryInputValid;
    }

    /**
     * This method returns the labelFeedbackMessage.
     * @return a label that shows the description
     */
    public JLabel getLabelFeedbackMessage() {
        return labelFeedbackMessage;
    }

    /**
     * This method recieves the categories that belong to the loggedIn user , and then
     * calls to a method that sets the categories to the combobox.
     * @param listOfCategories - that are going to be added to the categoriesListBySpecificUser.
     */
    public void setTheCategoriesToComboBox(List<String> listOfCategories) {
        //insert categories to the combo box
//        listOfCategories.add("all");
        insertCategoriesToComboBox(listOfCategories);
    }

    /**
     * This method builds the expenses' table of the user that logged in.
     * @param expensesByCategory - list of the expenses according to the chosen category.
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
     * This method set the viewManager data member.
     * @param appUtils - viewManager that mediate between the view and the  model.
     */
    public void setAppUtils(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    /**
     * Setting the layered pane.
     */
    private void setLayeredPane() {
        layeredPaneCenterAppView.setBounds(0, 200, 1200, 650);
        layeredPaneCenterAppView.setLayout(new CardLayout(0, 0));
        setPanelAllExpensesPartOfTheLayeredPane();
        setPanelOperationsPartOfTheLayeredPane();
        //    setPanelOperationsPartOfTheLayeredPane();
        panelAppContent.add(layeredPaneCenterAppView, BorderLayout.CENTER);
    }

    /**
     * Added to the panelAllExpenses the expensesPanel object,
     * and added the panelAllExpenses to the LayeredPane.
     */
    private void setPanelAllExpensesPartOfTheLayeredPane() {
        panelAllExpenses.setBackground(new Color(230, 230, 230));
        panelAllExpenses.add(expensesByCategory);
        layeredPaneCenterAppView.add(panelAllExpenses);
    }

    private void setPanelOperationsPartOfTheLayeredPane() {
        panelOperations.setBackground(new Color(230, 230, 230));
        panelOperations.add(categoryAndExpenseOperations);
        layeredPaneCenterAppView.add(panelOperations);
    }

    /** First we clear all the items in the combo box, and then we add each of the categories to the combo box. */
    private void insertCategoriesToComboBox(List<String> listOfCategories) {
        clearComboBoxesItems();

        for (String category : listOfCategories) {
            comboBoxCategories.addItem(category);
        }

        comboBoxCategories.addItem("all");
    }

    /*** Setting the combo box. */
    private void setComboBoxCategory()
    {
        ComponentUtils.setComponentsAttributes(
                comboBoxCategories,
                new Font("Narkisim", Font.BOLD, 25),
                new Dimension(200, 35));
    }

    /**
     * Clear combo box  responsible for clearing all the category combo boxes on load.
     */
    private void clearComboBoxesItems()
    {
        if (comboBoxCategories.getItemCount() > 0)
        {
            comboBoxCategories.removeAllItems();
        }
    }

    /**
     * This method settings the south panel of the application and defined the relevant text.
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
     * This method settings the north panel of the application, which is acting like the client navigation bar.
     */
    private void setNorthPanel() {
        panelNorthAppView.setLayout(panelNorthFlowLayout);
        panelNorthAppView.setBackground(new Color(190, 190, 230, 155));
        panelNorthAppView.setBounds(0, 0, 1400, 200);
        panelNorthAppView.add(buttonExpenses);
        panelNorthAppView.add(buttonOperations);
        panelNorthAppView.add(buttonReport);
        panelNorthAppView.add(buttonCurrencies);
        panelNorthAppView.add(labelExpenseManagerTitle);
        panelNorthAppView.add(buttonLogout);
        panelAppContent.add(panelNorthAppView, BorderLayout.NORTH);
    }

    /**
     * This method represents the buttons ActionListeners.
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
     * This method sets each of the north's panel components attributes
     * by first extracting all the buttons from the northPanel
     * and then their attributes, and separately set the labelCostManagerTitle attributes.
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

        labelExpenseManagerTitle.setFont(new Font("Narkisim", Font.BOLD, 55));
        labelExpenseManagerTitle.setBounds(1000, 0, 200, 100);
        labelExpenseManagerTitle.setForeground(Color.red);
    }

    /**
     * This method sets the application frame size.
     */
    private void setApplicationFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 1000);
        ComponentUtils.centralizeWindow(this);
        panelAppContent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.add(panelAppContent);
    }

    /**
     * This class represents the expenses panel.
     */
    private class ExpensesByCategory extends JPanel {
        /** Data member which represents all the expenses by a specific category. */
        private CategorySelectorPanel panelCategorySelector;

        /*** Swing components*/
        private JPanel panelTableData;
        private JScrollPane scrollPaneTable;
        private JTable tableData;
        private GridLayout gridLayoutExpensesPanel;

        /*** Ctor*/
        public ExpensesByCategory() {
            initExpensesPanel();
            startExpensesPanel();
        }

        /** This method inits the all the data members in "this". */
        private void initExpensesPanel() {
            panelCategorySelector = new CategorySelectorPanel();
            panelTableData = new JPanel();
            scrollPaneTable = new JScrollPane();
            tableData = new JTable();
            gridLayoutExpensesPanel = new GridLayout(2, 1, 0, 150);
        }

        /**
         *  This method adding different components to "this",
         *  and calling to a method that builds the expenses table.
         */
        private void startExpensesPanel() {
            this.setLayout(gridLayoutExpensesPanel);
            this.add(panelCategorySelector);
            setExpensesTableBySpecificCategory();
            this.add(panelTableData);
        }

        /** This method builds the expenses table. */
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
         * the expenses according to the chosen category.
         */
        private class CategorySelectorPanel extends JPanel {
            private JLabel labelFilterExpensesByCategoryTitle;
            private JLabel labelCategorySelector;
            private JButton buttonDisplayExpenses;
            private FlowLayout flowLayoutPanelCenterLabelAndComboBox;
            private JPanel panelNorthTitleCategorySelector;
            private JPanel panelCenterLabelAndComboBox;
            private JPanel panelSouthOfTheCategorySelectorPanel;


            /*** Ctor */
            public CategorySelectorPanel() {
                initCategorySelectorPanel();
                startCategorySelectorPanel();
            }

            /** This method inits the all the data members in "this". */
            private void initCategorySelectorPanel() {
                labelFilterExpensesByCategoryTitle = new JLabel("Filter your expenses by specific category");
                labelCategorySelector = new JLabel("Select Category:");
                buttonDisplayExpenses = new JButton("Display");
                flowLayoutPanelCenterLabelAndComboBox = new FlowLayout();
                flowLayoutPanelCenterLabelAndComboBox.setVgap(35);
                flowLayoutPanelCenterLabelAndComboBox.setHgap(35);
                panelNorthTitleCategorySelector = new JPanel();
                panelCenterLabelAndComboBox = new JPanel(flowLayoutPanelCenterLabelAndComboBox);
                panelSouthOfTheCategorySelectorPanel = new JPanel();
            }

            /**
             * This method calls different methods that set different components attributes.
             */
            private void startCategorySelectorPanel() {
                setLabelFilterByCategoryTitle();
                setLabelSelectCategoryAttributes();
                setBtnDisplayCategoryAttributes();
                locateComponentsOnTheCategorySelectorPanel();
                setButtonDisplayListener();
            }

            /**
             * Setting the label which acts as the title of the panel.
             */
            private void setLabelFilterByCategoryTitle() {
                ComponentUtils.setComponentsAttributes(
                        labelFilterExpensesByCategoryTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(600, 50));
            }

            /**
             * Setting the label next to the combo box.
             */
            private void setLabelSelectCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(
                        labelCategorySelector,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(150, 50));
            }

            /**
             * Setting the button in charge of sending the desired category.
             */
            private void setBtnDisplayCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(
                        buttonDisplayExpenses,
                        new Font("Narkisim", Font.BOLD, 25),
                        new Dimension(130, 30));
            }

            /**
             * This method locates all the components and adding them to the appropriate panel.
             */
            private void locateComponentsOnTheCategorySelectorPanel() {
                this.setLayout(new BorderLayout());
                panelNorthTitleCategorySelector.add(labelFilterExpensesByCategoryTitle);
                panelNorthTitleCategorySelector.setBackground(new Color(207, 220, 218, 255));
                this.add(panelNorthTitleCategorySelector, BorderLayout.NORTH);
                panelCenterLabelAndComboBox.add(labelCategorySelector);
                panelCenterLabelAndComboBox.add(comboBoxCategories);
                this.add(panelCenterLabelAndComboBox, BorderLayout.CENTER);
                panelSouthOfTheCategorySelectorPanel.add(buttonDisplayExpenses);
                this.add(panelSouthOfTheCategorySelectorPanel, BorderLayout.SOUTH);
            }

            /** This method adds action listener to the display button. */
            private void setButtonDisplayListener() {
                buttonDisplayExpenses.addActionListener(e ->
                        appUtils.getExpensesByCategory(comboBoxCategories.getSelectedItem().toString()));
            }
        }
    }

    private class CategoryAndExpenseOperations extends JPanel {
        /** Swing components*/
        private JPanel panelLeftCategories;
        private JPanel panelRightExpenses;
        private GridLayout leftGridLayout;
        private GridLayout rightGridLayout;
        private FlowLayout flowLayoutOperationsPanel;
        private AddCategoryPanel addCategory;
        private RemoveCategoryPanel removeCategory;
        private AddCostPanel addCostPanel;
//        private RemoveCostPanel removeCostPanel;


        public CategoryAndExpenseOperations() {
            initOperationsPanel();
            startOperationsPanel();
        }

        private void initOperationsPanel() {
            panelLeftCategories = new JPanel();
            panelRightExpenses = new JPanel();
            leftGridLayout = new GridLayout(2, 1, 0, 200);
            rightGridLayout  = new GridLayout(2, 1, 0, 200);
            flowLayoutOperationsPanel = new FlowLayout(0, 10, 0);

            addCategory = new AddCategoryPanel();
            removeCategory = new RemoveCategoryPanel();
            addCostPanel = new AddCostPanel();
//            removeCostPanel;
        }

        private void startOperationsPanel() {
            this.setLayout(flowLayoutOperationsPanel);
            panelLeftCategories.setLayout(leftGridLayout);
            panelRightExpenses.setLayout(rightGridLayout);
            panelLeftCategories.add(addCategory);
            panelLeftCategories.add(removeCategory);
            this.add(panelLeftCategories);

           panelRightExpenses.add(addCostPanel);
            this.add(panelRightExpenses);
        }

        /*** add category panel class represents adding new category operation as a panel.*/
        private class AddCategoryPanel extends JPanel{

            /** Swing components*/
            private JLabel labelTitleAddCategory;
            private JLabel labelAddCategoryName;
            private JTextField textFieldAddCategory;
            private JButton buttonAddNewCategory;
            private JPanel panelNorthAddCategory;
            private JPanel panelCenterAddCategory;
            private JPanel panelSouthAddCategory;
            private FlowLayout flowLayoutAddCategory;

            private AddCategoryPanel() {
                initAddCategory();
                startAddCategory();
            }

            private void initAddCategory() {
                labelTitleAddCategory = new JLabel("Add Category");
                labelAddCategoryName = new JLabel("Category name:");
                textFieldAddCategory = new JTextField(10);
                buttonAddNewCategory = new JButton("Add");
                panelNorthAddCategory = new JPanel();
                panelCenterAddCategory = new JPanel();
                panelSouthAddCategory = new JPanel();
                flowLayoutAddCategory = new FlowLayout(0, 25, 25);
            }

            private void startAddCategory() {
                setLabelTitleAddCategoryAttributes();
                setLabelAddCategoryAttributes();
                setTextFieldAddCategoryAttributes();
                setBtnAddCategoryAttributes();
                locateComponentsOnAddCategoryPane();
                setButtonAddActionListener();
            }

            private void setLabelTitleAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelTitleAddCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }
            private void setLabelAddCategoryAttributes(){
                ComponentUtils.setComponentsAttributes(labelAddCategoryName,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(250, 50));
            }

            private void setTextFieldAddCategoryAttributes(){
                ComponentUtils.setComponentsAttributes(textFieldAddCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 40));
            }

            private void setBtnAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(buttonAddNewCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 50));
            }

            private void locateComponentsOnAddCategoryPane(){
                this.setLayout(new BorderLayout());
                panelNorthAddCategory.add(labelTitleAddCategory);
                panelNorthAddCategory.setBackground(new Color(207, 220, 218, 255));
                this.add(panelNorthAddCategory, BorderLayout.NORTH);
                panelCenterAddCategory.setLayout(flowLayoutAddCategory);
                panelCenterAddCategory.add(labelAddCategoryName);
                panelCenterAddCategory.add(textFieldAddCategory);
                this.add(panelCenterAddCategory, BorderLayout.CENTER);
                panelSouthAddCategory.add(buttonAddNewCategory);
                this.add(panelSouthAddCategory, BorderLayout.SOUTH);
            }

            private void setButtonAddActionListener(){
                buttonAddNewCategory.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        appUtils.validateAndSetNewCategory(textFieldAddCategory.getText());


                        textFieldAddCategory.setText("");
                    }
                });
            }
        }

        private class RemoveCategoryPanel extends JPanel{

            /** swing components*/
            private JLabel labelRemoveCategoryTitle;
            private JLabel labelRemoveCategory;
            private JButton buttonRemoveCategory;
            private JPanel panelNorthRemoveCategory;
            private JPanel panelCenterRemoveCategory;
            private JPanel panelSouthRemoveCategory;
            private FlowLayout flowLayoutRemoveCategory;

            private RemoveCategoryPanel() {
                initRemoveCategory();
                startRemoveCategory();
            }

            private void initRemoveCategory(){
                labelRemoveCategoryTitle = new JLabel("Remove Category");
                labelRemoveCategory = new JLabel("Select category:");
                buttonRemoveCategory = new JButton("Remove");
                panelNorthRemoveCategory = new JPanel();
                panelCenterRemoveCategory = new JPanel();
                panelSouthRemoveCategory = new JPanel();
                flowLayoutRemoveCategory = new FlowLayout(0, 25, 25);
            }

            private void startRemoveCategory(){
                setLabelTitleRemoveCategoryAttributes();
                setLabelRemoveCategoryAttributes();
                setButtonRemoveAttributes();
                locateComponentsOnRemovePane();
                setButtonRemoveActionListener();
            }

            private void setLabelTitleRemoveCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCategoryTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }

            private void setLabelRemoveCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(250, 50));
            }

            private void setButtonRemoveAttributes() {
                ComponentUtils.setComponentsAttributes(buttonRemoveCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 50));
            }

            private void locateComponentsOnRemovePane() {
                this.setLayout(new BorderLayout());
                panelNorthRemoveCategory.add(labelRemoveCategoryTitle);
                panelNorthRemoveCategory.setBackground(new Color(207, 220, 218, 255));
                this.add(panelNorthRemoveCategory, BorderLayout.NORTH);
                panelCenterRemoveCategory.setLayout(flowLayoutRemoveCategory);
                panelCenterRemoveCategory.add(labelRemoveCategory);
                panelCenterRemoveCategory.add(comboBoxCategories);
                this.add(panelCenterRemoveCategory, BorderLayout.CENTER);
                panelSouthRemoveCategory.add(buttonRemoveCategory);
                this.add(panelSouthRemoveCategory, BorderLayout.SOUTH);
            }

            private void setButtonRemoveActionListener() {
                buttonRemoveCategory.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        appUtils.removeCategory(comboBoxCategories.getSelectedItem().toString());
                    }
                });
            }
        }

        /**
         * Class that represents the Add Expense Panel Operation.
         */
        private class AddCostPanel extends JPanel{

            /**
             * Swing components
             */
            private JLabel labelAddCostTitle;
            private JLabel labelCategoryAddCostPanel;
            private JLabel labelSumCost;
            private JLabel labelCurrency;
            private JLabel labelDescription;
            private JLabel labelDate;
            private JTextField textFieldSumCost;
            private JTextField textFieldCurrency;
            private JTextArea textFieldDescription;

            private JDateChooser dateChooser;

//            private JComboBox<> comboBoxCategory;
//            private JComboBox<Currency> comboBoxCurrency;
            private JButton buttonClearInputsInAddCostPanel;
            private JButton buttonAddNewCost;
            private JPanel panelNorthAddCost;
            private JPanel panelCenterAddCost;
            private JPanel panelSouthAddCost;
            private GridLayout gridLayoutCenterPanelAddCost;
            private FlowLayout flowLayoutSouthPanelAddCost;

            private AddCostPanel() {
                initAddExpense();
                startAddExpense();
            }

            private void initAddExpense() {
                labelAddCostTitle = new JLabel("Add Cost");
                labelCategoryAddCostPanel = new JLabel("Category:");
                labelSumCost = new JLabel("Sum cost:");
                labelCurrency = new JLabel("Currency:");
                labelDescription = new JLabel("Description:");
                labelDate = new JLabel("Date:");
                textFieldSumCost = new JTextField(10);
                textFieldCurrency = new JTextField(10);
                textFieldDescription = new JTextArea(10,3);
                dateChooser = new JDateChooser();
                buttonClearInputsInAddCostPanel = new JButton("Clear");
                buttonAddNewCost = new JButton("Add");
                panelNorthAddCost = new JPanel();
                panelCenterAddCost = new JPanel();
                panelSouthAddCost = new JPanel();
                gridLayoutCenterPanelAddCost = new GridLayout(5, 2, 20 , 20);
                flowLayoutSouthPanelAddCost = new FlowLayout();

            }

            private void startAddExpense() {
                setLabelTitleAddCostAttributes();
                setLabelCategoryAddCostPanelAttributes();
                setLabelSumCostAttributes();
                setLabelCurrencyAttributes();
                setLabelDescriptionAttributes();
                setLabelDateAttributes();
                setTextFieldSumCost();
                setTextFieldCurrency();
                setTextFieldDescription();
                setDateChooserFieldAttributes();
                setButtonClearAddCostPanelAttributes();
                setButtonAddInAddCostPanelAttributes();
                locateComponentsOnAddCostPanel();
                setButtonsAddCostPanelActionListeners();
            }

            private void setLabelTitleAddCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelAddCostTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }

            private void setLabelCategoryAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(labelCategoryAddCostPanel,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setLabelSumCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelSumCost,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setLabelCurrencyAttributes() {
                ComponentUtils.setComponentsAttributes(labelCurrency,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }


            private void setLabelDescriptionAttributes() {
                ComponentUtils.setComponentsAttributes(labelDescription,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setLabelDateAttributes() {
                ComponentUtils.setComponentsAttributes(labelDate,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setTextFieldSumCost() {
                ComponentUtils.setComponentsAttributes(textFieldSumCost,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setTextFieldCurrency() {
                ComponentUtils.setComponentsAttributes(textFieldCurrency,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setTextFieldDescription() {
                ComponentUtils.setComponentsAttributes(textFieldDescription,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(100, 50));
            }

            private void setDateChooserFieldAttributes() {
                dateChooser.setDateFormatString("yyyy-MM-dd");

                ComponentUtils.setComponentsAttributes(textFieldDescription,
                        new Font("Tahoma", Font.PLAIN, 20),
                        new Dimension(100, 50));
            }


            private void setButtonClearAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(buttonClearInputsInAddCostPanel,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 50));
            }
            private void setButtonAddInAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(buttonAddNewCost,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 50));
            }

            private void locateComponentsOnAddCostPanel() {
                this.setLayout(new BorderLayout());
                panelNorthAddCost.add(labelAddCostTitle);
                this.add(panelNorthAddCost, BorderLayout.NORTH);

                panelCenterAddCost.setLayout(gridLayoutCenterPanelAddCost);
                panelCenterAddCost.add(labelCategoryAddCostPanel);
                panelCenterAddCost.add(comboBoxCategories);
                panelCenterAddCost.add(labelSumCost);
                panelCenterAddCost.add(textFieldSumCost);
                panelCenterAddCost.add(labelCurrency);
                panelCenterAddCost.add(textFieldCurrency);
                panelCenterAddCost.add(labelDescription);
                panelCenterAddCost.add(textFieldDescription);
                panelCenterAddCost.add(labelDate);
                panelCenterAddCost.add(dateChooser);
                this.add(panelCenterAddCost, BorderLayout.CENTER);

                panelSouthAddCost.setLayout(flowLayoutSouthPanelAddCost);
                panelSouthAddCost.add(buttonClearInputsInAddCostPanel);
                panelSouthAddCost.add(buttonAddNewCost);
                this.add(panelSouthAddCost, BorderLayout.SOUTH);

            }


            private void setButtonsAddCostPanelActionListeners() {
                setButtonClearActionListener();
            }

            private void setButtonClearActionListener() {
                buttonClearInputsInAddCostPanel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            clearAllTextFieldsOfAddCostPanel();
                    }
                });
            }

            private void clearAllTextFieldsOfAddCostPanel() {
                Component[] components = panelCenterAddCost.getComponents();

                for (Component component : components) {
                    if (component instanceof JTextField || component instanceof JTextArea ) {
                        ((JTextComponent) component).setText("");
                    }
                }
            }




        }


    }
}
