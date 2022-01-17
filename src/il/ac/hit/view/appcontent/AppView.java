package il.ac.hit.view.appcontent;

import com.toedter.calendar.JDateChooser;
import il.ac.hit.auxiliary.HandlingMessage;
import il.ac.hit.model.Expense;
import il.ac.hit.view.ComponentUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Class that represents AppView itself.
 */
public class AppView extends JFrame {

    /**
     * List of all categories that belongs to a specific user.
     */
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
    private JPanel panelChartReport;
    private JLayeredPane layeredPaneCenterAppView;
    private JComboBox<String> comboBoxCategoriesAddCostPanel;
    private JComboBox<String> comboBoxCategoriesAllExpensesPanel;
    private JComboBox<String> comboBoxCategoriesRemoveCategoryPanel;
    private JComboBox<Integer> comboBoxCostID;
    private FlowLayout panelNorthFlowLayout;
    private BorderLayout borderLayoutPanelContent;
    private DefaultPieDataset pieDataset = new DefaultPieDataset();
    private JFreeChart chart;
    private ExpensesByCategory expensesByCategory;
    private CategoryAndExpenseOperations categoryAndExpenseOperations;
    private Report report;
    private PieSectionLabelGenerator gen;
//    private CurrenciesPanel currenciesPanel;


    /**
     * Ctor that receives an appUtils object which is implemented by ViewManager.
     *
     * @param appUtils - ...
     */
    public AppView(AppUtils appUtils) {
        setAppUtils(appUtils);
        initAppView();
        startAppView();
    }

    /*** This method initialize the swing components. */
    private void initAppView() {
        comboBoxCategoriesAddCostPanel = new JComboBox<>();
        comboBoxCategoriesAllExpensesPanel = new JComboBox<>();
        comboBoxCategoriesRemoveCategoryPanel = new JComboBox<>();
        comboBoxCostID = new JComboBox<>();
        expensesByCategory = new ExpensesByCategory();
        categoryAndExpenseOperations = new CategoryAndExpenseOperations();
        categoryAndExpenseOperations.setBounds(0, 0, 1300, 700);

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
        panelChartReport = new JPanel();
        layeredPaneCenterAppView = new JLayeredPane();
        panelNorthFlowLayout = new FlowLayout(0, 25, 0);
        report = new Report();
    }


    /**
     * This method settings the panels, buttons and their attributes.
     */
    private void startAppView() {
        setApplicationFrame();
        setNorthPanel();
        setNorthPanelComponentAttributes();
        setButtonsActionListeners();
        setComboBoxAttributes(comboBoxCategoriesAddCostPanel);
        setComboBoxAttributes(comboBoxCategoriesAllExpensesPanel);
        setComboBoxAttributes(comboBoxCategoriesRemoveCategoryPanel);
        setLayeredPane();

        //get all the categories that belong to the loggedIn user.
        appUtils.getCategoriesThatBelongToSpecificUser();

        setSouthPanel();
    }

    public void initPieChart(Hashtable<String,Float> expenses) {
       // report.setPanelChartReport(expenses);
        panelChartReport.removeAll();
        if (pieDataset != null) {
            pieDataset = null;

        }
        pieDataset = new DefaultPieDataset();
        if (chart != null) {
            chart = null;

        }
        chart = ChartFactory.createPieChart3D("Report By Category", pieDataset, true, true, false);

        Set<String> setOfKeys = expenses.keySet();

        // Iterating through the Hashtable
        // object using for-Each loop
        for (String key : setOfKeys) {
            pieDataset.setValue(key, expenses.get(key));
        }

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);

        plot.setLabelFont(new Font("Narkisim", Font.BOLD, 16));
        gen = new StandardPieSectionLabelGenerator("{0} : {1}",
                new DecimalFormat("0"),
                new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);
        ChartPanel chartPanel = new ChartPanel(chart);


        panelChartReport.add(chartPanel);
        panelChartReport.updateUI();
       // report.getPanelChart().add(chartPanel);
       // panelChartReport.add(chartPanel);

    }

    /**
     * This method removes the selected category from the combobox.
     *
     * @param category - to be removed.
     */
    public void removeChosenCategoryFromComboBox(String category) {
        comboBoxCategoriesAddCostPanel.removeItem(comboBoxCategoriesAddCostPanel.getSelectedItem());
    }

    /**
     * This method adds a new category to the combobox.
     *
     * @param category - the chosen category.
     */
    public void addCategoryToComboBox(String category) {
        comboBoxCategoriesAddCostPanel.addItem(category);
        comboBoxCategoriesRemoveCategoryPanel.addItem(category);
        comboBoxCategoriesAllExpensesPanel.addItem(category);
    }

    /**
     * Updating the category combo boxes by deleting the given category.
     *
     * @param categoryToRemove - the category that's going to be deleted.
     */
    public void updateCategoriesComboBoxes(String categoryToRemove) {
        comboBoxCategoriesAllExpensesPanel.removeItem(categoryToRemove);
        comboBoxCategoriesRemoveCategoryPanel.removeItem(categoryToRemove);
        comboBoxCategoriesAddCostPanel.removeItem(categoryToRemove);
    }

    /**
     * This method is responsible for updating the combobox by removing a cost id that returned
     * from the costIDToRemove list.
     *
     * @param costIDToRemove - list that have the costsID that must be removed from the comboBoxCostID.
     */
    public void updateCostIDComboBox(List<Integer> costIDToRemove) {
        for (Integer costID : costIDToRemove) {
            comboBoxCostID.removeItem(costID);
        }
    }

    /**
     * setter for categoryInput
     *
     * @param categoryInputValid indicates if the category name is valid or not
     */
    public void setCategoryInputValid(boolean categoryInputValid) {
        isCategoryInputValid = categoryInputValid;
    }

    /**
     * This method returns the labelFeedbackMessage.
     *
     * @return a label that shows the description
     */
    public JLabel getLabelFeedbackMessage() {
        return labelFeedbackMessage;
    }

    /**
     * This method recieves the categories that belong to the loggedIn user , and then
     * calls to a method that sets the categories to the combobox.
     *
     * @param listOfCategories - that are going to be added to the categoriesListBySpecificUser.
     */
    public void setTheCategoriesToCategoriesComboBox(List<String> listOfCategories) {
        //insert categories to the combo box
//        listOfCategories.add("all");
        insertCategoriesToComboBoxes(listOfCategories);
    }

    /**
     * This method recieves the costs' id's that belong to the loggedIn user costs , and then
     * calls to a method that sets the costs' id to the combobox.
     *
     * @param listOfCostsID - that are going to be added to the categoriesListBySpecificUser.
     */
    public void setTheCostsIDToCostIDComboBox(List<Integer> listOfCostsID) {
        //insert costs to the costs' combo box
        insertCostsToCostsIDComboBox(listOfCostsID);
    }

    /**
     * @param listOfCostsID
     */
    private void insertCostsToCostsIDComboBox(List<Integer> listOfCostsID) {
        clearComboBoxesItems(comboBoxCostID);

        for (Integer costId : listOfCostsID) {
            comboBoxCostID.addItem(costId);
        }
    }

    /**
     * This method builds the expenses' table of the user that logged in.
     *
     * @param expensesByCategory - list of the expenses according to the chosen category.
     */
    public void setTableInAllExpensesPanel(List<Expense> expensesByCategory) {
        // create the titles for the tables
        DefaultTableModel tableDataModel = new DefaultTableModel(new String[]{
                "cost_id", "category", "sum_cost", "currency", "description", "date", "user_id"},
                0);

        // add each cost to the table model
        for (Expense expense : expensesByCategory) {
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
     *
     * @param appUtils - viewManager that mediate between the view and the  model.
     */
    public void setAppUtils(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    /**
     * Setting the layered pane.
     */
    private void setLayeredPane() {
        layeredPaneCenterAppView.setBounds(0, 200, 1300, 700);
        layeredPaneCenterAppView.setLayout(new CardLayout(0, 0));
        setPanelAllExpensesPartOfTheLayeredPane();
        setPanelOperationsPartOfTheLayeredPane();
        setPanelReportPartOfTheLayeredPane();
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

    private void setPanelReportPartOfTheLayeredPane(){
        panelReport.setBackground(new Color(230, 230, 230));
        panelReport.add(report);
        layeredPaneCenterAppView.add(panelReport);
    }

    /**
     * First we clear all the items in the combo box, and then we add each of the categories to the combo box.
     */
    private void insertCategoriesToComboBoxes(List<String> listOfCategories) {
        clearComboBoxesItems(comboBoxCategoriesAddCostPanel);
        clearComboBoxesItems(comboBoxCategoriesAllExpensesPanel);
        clearComboBoxesItems(comboBoxCategoriesRemoveCategoryPanel);

        comboBoxCategoriesAllExpensesPanel.addItem("all");

        for (String category : listOfCategories) {
            comboBoxCategoriesAddCostPanel.addItem(category);
            comboBoxCategoriesAllExpensesPanel.addItem(category);
            comboBoxCategoriesRemoveCategoryPanel.addItem(category);
        }
    }

    /*** Setting the combo box. */
    private void setComboBoxAttributes(JComboBox<String> comboBox) {
        ComponentUtils.setComponentsAttributes(
                comboBox,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(180, 35));
    }

    /**
     * Clear the given combo box  .
     */
    private void clearComboBoxesItems(JComboBox comboBox) {
        if (comboBox.getItemCount() > 0) {
            comboBox.removeAllItems();
        }
    }

    /**
     * This method settings the south panel of the application and defined the relevant text.
     */
    private void setSouthPanel() {
        panelSouthAppView.setBackground(new Color(190, 190, 230, 255));
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
            panelChartReport.removeAll();
            appUtils.resetUser();
            appUtils.changeFrameFromAppViewToLoginView();
        });
    }

    /**
     * This method sets each of the north's panel components attributes
     * by first extracting all the buttons from the northPanel
     * and then their attributes, and separately set the labelCostManagerTitle attributes.
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
        /**
         * Data member which represents all the expenses by a specific category.
         */
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

        /**
         * This method inits the all the data members in "this".
         */
        private void initExpensesPanel() {
            panelCategorySelector = new CategorySelectorPanel();
            panelTableData = new JPanel();
            scrollPaneTable = new JScrollPane();
            tableData = new JTable();
            gridLayoutExpensesPanel = new GridLayout(2, 1, 0, 150);
        }

        /**
         * This method adding different components to "this",
         * and calling to a method that builds the expenses table.
         */
        private void startExpensesPanel() {
            this.setLayout(gridLayoutExpensesPanel);
            this.add(panelCategorySelector);
            setExpensesTableBySpecificCategory();
            this.add(panelTableData);
        }

        /**
         * This method builds the expenses table.
         */
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

            /**
             * This method inits the all the data members in "this".
             */
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
                panelCenterLabelAndComboBox.add(comboBoxCategoriesAllExpensesPanel);
                this.add(panelCenterLabelAndComboBox, BorderLayout.CENTER);
                panelSouthOfTheCategorySelectorPanel.add(buttonDisplayExpenses);
                this.add(panelSouthOfTheCategorySelectorPanel, BorderLayout.SOUTH);
            }

            /**
             * This method adds action listener to the display button.
             */
            private void setButtonDisplayListener() {
                buttonDisplayExpenses.addActionListener(e ->
                        appUtils.getExpensesByCategory(comboBoxCategoriesAllExpensesPanel.getSelectedItem().toString()));
            }
        }
    }

    private class CategoryAndExpenseOperations extends JPanel {
        /**
         * Swing components
         */
        private JPanel panelLeftCategories;
        private JPanel panelRightCost;
        private GridLayout leftGridLayout;
        private GridLayout rightGridLayout;
        //        private FlowLayout flowLayoutOperationsPanel;
        private GridLayout operationsGrid;
        private AddCategoryPanel addCategory;
        private RemoveCategoryPanel removeCategory;
        private AddCostPanel addCostPanel;
        private RemoveCostPanel removeCostPanel;


        public CategoryAndExpenseOperations() {
            initOperationsPanel();
            startOperationsPanel();
        }

        private void initOperationsPanel() {
            panelLeftCategories = new JPanel();
            panelRightCost = new JPanel();
            operationsGrid = new GridLayout(1, 2, 10, 0);
            leftGridLayout = new GridLayout(2, 1, 0, 10);
            rightGridLayout = new GridLayout(2, 1, 0, 10);
//            flowLayoutOperationsPanel = new FlowLayout(0, 35, 10);
            addCategory = new AddCategoryPanel();
            removeCategory = new RemoveCategoryPanel();
            addCostPanel = new AddCostPanel();
            removeCostPanel = new RemoveCostPanel();
        }

        private void startOperationsPanel() {
//            this.setLayout(flowLayoutOperationsPanel);
            this.setLayout(operationsGrid);
            panelLeftCategories.setLayout(leftGridLayout);
            panelRightCost.setLayout(rightGridLayout);
            panelLeftCategories.add(addCategory);
            panelLeftCategories.add(removeCategory);
            this.add(panelLeftCategories);

            panelRightCost.add(addCostPanel);
            panelRightCost.add(removeCostPanel);
            this.add(panelRightCost);
        }

        /*** add category panel class represents adding new category operation as a panel.*/
        private class AddCategoryPanel extends JPanel {

            /**
             * Swing components
             */
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

            private void setLabelAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelAddCategoryName,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(250, 50));
            }

            private void setTextFieldAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(textFieldAddCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 40));
            }

            private void setBtnAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(buttonAddNewCategory,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(100, 40));
            }

            private void locateComponentsOnAddCategoryPane() {
                this.setLayout(new BorderLayout());
                this.setPreferredSize(new Dimension(450, 400));
                panelNorthAddCategory.add(labelTitleAddCategory);
                //panelNorthAddCategory.setBackground(new Color(207, 220, 218, 255));
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                this.add(panelNorthAddCategory, BorderLayout.NORTH);
                panelCenterAddCategory.setLayout(flowLayoutAddCategory);
                panelCenterAddCategory.add(labelAddCategoryName);
                panelCenterAddCategory.add(textFieldAddCategory);
                this.add(panelCenterAddCategory, BorderLayout.CENTER);
                panelSouthAddCategory.add(buttonAddNewCategory);
                this.add(panelSouthAddCategory, BorderLayout.SOUTH);
            }

            private void setButtonAddActionListener() {
                buttonAddNewCategory.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        appUtils.validateAndSetNewCategory(textFieldAddCategory.getText());
                        textFieldAddCategory.setText("");
                    }
                });
            }
        }

        private class RemoveCategoryPanel extends JPanel {

            /**
             * swing components
             */
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

            private void initRemoveCategory() {
                labelRemoveCategoryTitle = new JLabel("Remove Category");
                labelRemoveCategory = new JLabel("Select category:");
                buttonRemoveCategory = new JButton("Remove");
                panelNorthRemoveCategory = new JPanel();
                panelCenterRemoveCategory = new JPanel();
                panelSouthRemoveCategory = new JPanel();
                flowLayoutRemoveCategory = new FlowLayout(0, 25, 25);
            }

            private void startRemoveCategory() {
                setLabelTitleRemoveCategoryAttributes();
                setLabelRemoveCategoryAttributes();
                setButtonRemoveAttributes();
                locateComponentsOnRemoveCategoryPane();
                setButtonRemoveCategoryActionListener();
            }

            private void setLabelTitleRemoveCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCategoryTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }

            private void setLabelRemoveCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCategory,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(250, 50));
            }

            private void setButtonRemoveAttributes() {
                ComponentUtils.setComponentsAttributes(buttonRemoveCategory,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(140, 40));
            }

            private void locateComponentsOnRemoveCategoryPane() {
                this.setLayout(new BorderLayout());
                panelNorthRemoveCategory.add(labelRemoveCategoryTitle);
                // panelNorthRemoveCategory.setBackground(new Color(207, 220, 218, 255));
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                this.add(panelNorthRemoveCategory, BorderLayout.NORTH);
                panelCenterRemoveCategory.setLayout(flowLayoutRemoveCategory);
                panelCenterRemoveCategory.add(labelRemoveCategory);
                panelCenterRemoveCategory.add(comboBoxCategoriesRemoveCategoryPanel);
                this.add(panelCenterRemoveCategory, BorderLayout.CENTER);
                panelSouthRemoveCategory.add(buttonRemoveCategory);
                this.add(panelSouthRemoveCategory, BorderLayout.SOUTH);
            }

            private void setButtonRemoveCategoryActionListener() {
                buttonRemoveCategory.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String categoryToRemove = comboBoxCategoriesRemoveCategoryPanel.getSelectedItem().toString();
                        appUtils.removeCategory(categoryToRemove);
                        appUtils.removeCostsThatReferToChosenCategory(categoryToRemove);
                    }
                });
            }
        }

        /**
         * Class that represents the Add Expense Panel Operation.
         */
        private class AddCostPanel extends JPanel {

            /**
             * Swing components
             */
            private JLabel labelAddCostTitle;
            private JLabel labelCategoryInAddCostPanel;
            private JLabel labelSumCost;
            private JLabel labelCurrency;
            private JLabel labelDescription;
            private JLabel labelDate;
            private JTextField textFieldSumCost;
            private JTextField textFieldCurrency;
            private JTextArea textAreaDescription;
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
                initAddCost();
                startAddCost();
            }

            private void initAddCost() {
                labelAddCostTitle = new JLabel("Add Cost");
                labelCategoryInAddCostPanel = new JLabel("Category:");
                labelSumCost = new JLabel("Sum cost:");
                labelCurrency = new JLabel("Currency:");
                labelDescription = new JLabel("Description:");
                labelDate = new JLabel("Date:");
                textFieldSumCost = new JTextField(10);
                textFieldCurrency = new JTextField(10);
                textAreaDescription = new JTextArea(5, 1);
                dateChooser = new JDateChooser();
                buttonClearInputsInAddCostPanel = new JButton("Clear");
                buttonAddNewCost = new JButton("Add");
                panelNorthAddCost = new JPanel();
                panelCenterAddCost = new JPanel();
                panelSouthAddCost = new JPanel();
                gridLayoutCenterPanelAddCost = new GridLayout(5, 2, 5, 15);
                flowLayoutSouthPanelAddCost = new FlowLayout(0, 135, 25);
            }

            private void startAddCost() {
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
                ComponentUtils.setComponentsAttributes(labelCategoryInAddCostPanel,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            private void setLabelSumCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelSumCost,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            private void setLabelCurrencyAttributes() {
                ComponentUtils.setComponentsAttributes(labelCurrency,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }


            private void setLabelDescriptionAttributes() {
                ComponentUtils.setComponentsAttributes(labelDescription,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            private void setLabelDateAttributes() {
                ComponentUtils.setComponentsAttributes(labelDate,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            private void setTextFieldSumCost() {
                ComponentUtils.setComponentsAttributes(textFieldSumCost,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(50, 10));
            }

            private void setTextFieldCurrency() {
                ComponentUtils.setComponentsAttributes(textFieldCurrency,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(50, 10));
            }

            private void setTextFieldDescription() {
                ComponentUtils.setComponentsAttributes(textAreaDescription,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(45, 10));
            }

            private void setDateChooserFieldAttributes() {
                dateChooser.setDateFormatString("yyyy-MM-dd");

                ComponentUtils.setComponentsAttributes(dateChooser,
                        new Font("Tahoma", Font.PLAIN, 20),
                        new Dimension(50, 10));
            }

            private void setButtonClearAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(buttonClearInputsInAddCostPanel,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(120, 40));
            }

            private void setButtonAddInAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(buttonAddNewCost,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(100, 40));
            }

            private void locateComponentsOnAddCostPanel() {
                this.setLayout(new BorderLayout());
                this.setPreferredSize(new Dimension(450, 400));

                panelNorthAddCost.add(labelAddCostTitle);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                this.add(panelNorthAddCost, BorderLayout.NORTH);
                panelCenterAddCost.setLayout(gridLayoutCenterPanelAddCost);
                panelCenterAddCost.add(labelCategoryInAddCostPanel);
                panelCenterAddCost.add(comboBoxCategoriesAddCostPanel);
                panelCenterAddCost.add(labelSumCost);
                panelCenterAddCost.add(textFieldSumCost);
                panelCenterAddCost.add(labelCurrency);
                panelCenterAddCost.add(textFieldCurrency);
                panelCenterAddCost.add(labelDescription);
                panelCenterAddCost.add(textAreaDescription);
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
                setButtonAddActionListener();
            }

            /**
             * Setting the buttonClear action listener.
             */
            private void setButtonClearActionListener() {
                buttonClearInputsInAddCostPanel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        clearAllTextFieldsOfAddCostPanel();
                    }
                });
            }

            /**
             * Clearing all the field of the components except the date chooser.
             */
            private void clearAllTextFieldsOfAddCostPanel() {
                Component[] components = panelCenterAddCost.getComponents();

                for (Component component : components) {

                    if (component instanceof JTextComponent) {
                        ((JTextComponent) component).setText("");
                    }
                }

                dateChooser.setDate(null);
                comboBoxCategoriesAddCostPanel.setSelectedIndex(0);
            }

            private void setButtonAddActionListener() {
                buttonAddNewCost.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addNewCost();
                        clearAllTextFieldsOfAddCostPanel();
                    }
                });
            }

            private void addNewCost() {
                String categorySelected = comboBoxCategoriesAddCostPanel.getSelectedItem().toString();
                String sumCost = textFieldSumCost.getText();
                String currency = textFieldCurrency.getText();
                String description = textAreaDescription.getText();
                Date date = dateChooser.getDate();

                appUtils.validateAndAddNewCost(categorySelected, sumCost, currency, description, date);
            }
        }

        private class RemoveCostPanel extends JPanel {
            /**
             * Swing components
             */
            private JLabel labelRemoveCostTitle;
            private JLabel labelCostID;

            private JButton buttonRemoveCost;
            private JPanel panelNorthRemoveCost;
            private JPanel panelCenterRemoveCost;
            private JPanel panelSouthRemoveCost;
            private FlowLayout flowLayoutCenterRemoveCost;

            private RemoveCostPanel() {
                initRemoveCost();
                startRemoveCost();
            }

            private void initRemoveCost() {
                labelRemoveCostTitle = new JLabel("Remove Cost");
                labelCostID = new JLabel("Cost ID:");
                buttonRemoveCost = new JButton("Remove");
                panelNorthRemoveCost = new JPanel();
                panelCenterRemoveCost = new JPanel();
                panelSouthRemoveCost = new JPanel();
                flowLayoutCenterRemoveCost = new FlowLayout(0, 120, 25);
            }

            private void startRemoveCost() {
                setLabelTitleRemoveCostAttributes();
                setLabelCostIDAttributes();
                setComboboxCostIDAttributes();
                setButtonRemoveCostAttributes();
                locateComponentsOnRemoveCostPanel();
                setButtonRemoveCostActionListener();
            }

            private void setLabelTitleRemoveCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCostTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 30));
            }

            private void setLabelCostIDAttributes() {
                ComponentUtils.setComponentsAttributes(labelCostID,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(120, 30));
            }

            private void setComboboxCostIDAttributes() {
                ComponentUtils.setComponentsAttributes(
                        comboBoxCostID,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(180, 35));

                appUtils.getCostsID();
            }

            private void setButtonRemoveCostAttributes() {
                ComponentUtils.setComponentsAttributes(buttonRemoveCost,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(140, 40));
            }

            private void locateComponentsOnRemoveCostPanel() {
                this.setLayout(new BorderLayout());
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                panelNorthRemoveCost.add(labelRemoveCostTitle);
                this.add(panelNorthRemoveCost, BorderLayout.NORTH);

                panelCenterRemoveCost.setLayout(flowLayoutCenterRemoveCost);
                panelCenterRemoveCost.add(labelCostID);
                panelCenterRemoveCost.add(comboBoxCostID);
                this.add(panelCenterRemoveCost, BorderLayout.CENTER);

                panelSouthRemoveCost.add(buttonRemoveCost);
                this.add(panelSouthRemoveCost, BorderLayout.SOUTH);
            }

            private void setButtonRemoveCostActionListener() {
                buttonRemoveCost.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (comboBoxCostID.getSelectedItem() != null) {
                            appUtils.removeCost(Integer.parseInt(comboBoxCostID.getSelectedItem().toString()));
                        } else {
                            labelFeedbackMessage.setText(HandlingMessage.CANT_REMOVE_NO_COST.toString());
                        }
                    }
                });
            }
        }
    }

    private class Report extends JPanel {
        /**
         * Swing components
         */
        private GridLayout gridLayoutReportPanel;
        private GridLayout gridLayoutCenterDatesPanel;
        private JPanel panelSelectDatesReport;
        private JPanel panelNorthDatesReport;
        private JPanel panelCenterDatesReport;
        private JPanel panelSouthDatesReport;
        private JLabel labelTitleSelectDates;
        private JLabel labelStartDate;
        private JLabel labelEndDate;
        private JDateChooser dateChooserStart;
        private JDateChooser dateChooserEnd;
        private JButton buttonDisplayPieChart;

        private Report() {
            initReport();
            startReport();
        }

        private void initReport() {
            gridLayoutReportPanel = new GridLayout(2, 1, 0, 40);
            gridLayoutCenterDatesPanel = new GridLayout(2, 2, 20, 20);
            panelSelectDatesReport = new JPanel();
            panelNorthDatesReport = new JPanel();
            panelCenterDatesReport = new JPanel();
            panelSouthDatesReport = new JPanel();
            labelTitleSelectDates = new JLabel("Select Dates");
            labelStartDate = new JLabel("Start Date:");
            labelEndDate = new JLabel("End Date");
            dateChooserStart = new JDateChooser();
            dateChooserEnd = new JDateChooser();
            buttonDisplayPieChart = new JButton("Display");
        }

        private void startReport() {
            setPanelDatesReport();
            buttonDisplayPieChartSetOnClickListener();
            locateComponentsOnReportPanel();
        }


        private void setPanelDatesReport() {
            setTitleSelectDatesAttributes();
            setLabelStartAndEndDateAttributes();
            setDateChooserStartAndEndDateAttributes();
            setButtonDisplayChartAttributes();
        }

        private void setTitleSelectDatesAttributes() {
            ComponentUtils.setComponentsAttributes(labelTitleSelectDates,
                    new Font("Narkisim", Font.BOLD, 30),
                    new Dimension(200, 30));
        }


        private void setLabelStartAndEndDateAttributes() {
            ComponentUtils.setComponentsAttributes(labelStartDate,
                    new Font("Narkisim", Font.BOLD, 25),
                    new Dimension(100, 15));

            ComponentUtils.setComponentsAttributes(labelEndDate,
                    new Font("Narkisim", Font.BOLD, 25),
                    new Dimension(100, 15));
        }

        private void setDateChooserStartAndEndDateAttributes() {
            dateChooserStart.setDateFormatString("yyyy-MM-dd");
            dateChooserEnd.setDateFormatString("yyyy-MM-dd");

            ComponentUtils.setComponentsAttributes(dateChooserStart,
                    new Font("Tahoma", Font.PLAIN, 20),
                    new Dimension(100, 10));

            ComponentUtils.setComponentsAttributes(dateChooserEnd,
                    new Font("Tahoma", Font.PLAIN, 20),
                    new Dimension(100, 10));
        }

        private void setButtonDisplayChartAttributes() {
            ComponentUtils.setComponentsAttributes(buttonDisplayPieChart,
                    new Font("Narkisim", Font.BOLD, 20),
                    new Dimension(120, 30));
        }

        private void locateComponentsOnReportPanel() {
            this.setLayout(gridLayoutReportPanel);
            BorderLayout borderLayoutNorthReportPanel = new BorderLayout();
            panelSelectDatesReport.setLayout(borderLayoutNorthReportPanel);
            panelSelectDatesReport.setPreferredSize(new Dimension(550, 300));
            panelSelectDatesReport.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            panelNorthDatesReport.add(labelTitleSelectDates);
            panelSelectDatesReport.add(panelNorthDatesReport, BorderLayout.NORTH);
            panelCenterDatesReport.setLayout(gridLayoutCenterDatesPanel);
            panelCenterDatesReport.add(labelStartDate);
            panelCenterDatesReport.add(dateChooserStart);
            panelCenterDatesReport.add(labelEndDate);
            panelCenterDatesReport.add(dateChooserEnd);
            panelSelectDatesReport.add(panelCenterDatesReport, BorderLayout.CENTER);
            panelSouthDatesReport.add(buttonDisplayPieChart);
            panelSelectDatesReport.add(panelSouthDatesReport, BorderLayout.SOUTH);
            this.add(panelSelectDatesReport);
            panelChartReport.setPreferredSize(new Dimension(650, 400));
            this.add(panelChartReport);
        }

        private void buttonDisplayPieChartSetOnClickListener() {
            buttonDisplayPieChart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Date firstDate = dateChooserStart.getDate();
                    Date secondDate = dateChooserEnd.getDate();

                    appUtils.getCostsBetweenChosenDates(firstDate, secondDate);
                }
            });
        }
    }
}
