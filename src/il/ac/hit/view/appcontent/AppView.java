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
    private JLabel labelExpenseManagerTitle;
    private JLabel labelFeedbackMessage;
    private JPanel panelAppContent;
    private JPanel panelNorthAppView;
    private JPanel panelAllExpenses;
    private JPanel panelOperations;
    private JPanel panelReport;
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

    //colors we will use in this class
    private final Color colorForButtons = new Color(220,220,220);
    private final Color colorForTitles = new Color(247, 221, 194);

    /**
     * Ctor that receives an appUtils object which is implemented by ViewManager.
     * @param appUtils - an auxiliary class that have methods that be executed from this class.
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
        labelExpenseManagerTitle = new JLabel("Cost Manager");
        labelFeedbackMessage = new JLabel();
        borderLayoutPanelContent = new BorderLayout();
        panelAppContent = new JPanel(borderLayoutPanelContent);
        panelNorthAppView = new JPanel();
        panelAllExpenses = new JPanel();
        panelOperations = new JPanel();
        panelReport = new JPanel();
        panelSouthAppView = new JPanel();
        panelChartReport = new JPanel();
        layeredPaneCenterAppView = new JLayeredPane();
        panelNorthFlowLayout = new FlowLayout(0, 60, 0);
        report = new Report();
    }


    /**
     * This method settings the component's attributes.
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

    /**
     * This method responsible for initialize the pie chart who will represent the costs.
     * @param expenses - Hashtable that contains from the category and the amount of money
     *                   that was spent in this category.
     */
    public void initPieChart(Hashtable<String,Float> expenses) {
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

        plot.setLabelFont(new Font("Narkisim", Font.BOLD, 15));
        gen = new StandardPieSectionLabelGenerator("{0} : {1}",
                new DecimalFormat("0"),
                new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);
        ChartPanel chartPanel = new ChartPanel(chart);

        panelChartReport.add(chartPanel);
        panelChartReport.updateUI();
    }

    /**
     * This method init the report table in Report panel.
     * @param expenses - list of expenses.
     */
    public void initTableReportPanel(List<Expense> expenses){
        DefaultTableModel defaultTableModel = setTableCosts(expenses);
        this.report.tableDataReport.setModel(defaultTableModel);
    }

    /**
     * This method removes the selected category from the combobox.
     * @param category - to be removed.
     */
    public void removeChosenCategoryFromComboBox(String category) {
        comboBoxCategoriesAddCostPanel.removeItem(comboBoxCategoriesAddCostPanel.getSelectedItem());
    }

    /**
     * This method adds a new category to the combobox.
     * @param category - the chosen category.
     */
    public void addCategoryToComboBox(String category) {
        comboBoxCategoriesAddCostPanel.addItem(category);
        comboBoxCategoriesRemoveCategoryPanel.addItem(category);
        comboBoxCategoriesAllExpensesPanel.addItem(category);
    }

    /**
     * Updating the category combo boxes by deleting the given category.
     * @param categoryToRemove - the category that's going to be deleted.
     */
    public void updateCategoriesComboBoxes(String categoryToRemove) {
        comboBoxCategoriesAllExpensesPanel.removeItem(categoryToRemove);
        comboBoxCategoriesRemoveCategoryPanel.removeItem(categoryToRemove);
        comboBoxCategoriesAddCostPanel.removeItem(categoryToRemove);
    }

    /**
     * This method is responsible for updating the combobox by removing
     * a cost id that returned from the costIDToRemove list.
     * @param costIDToRemove - list that have the costsID that must be removed from the comboBoxCostID.
     */
    public void updateCostIDComboBox(List<Integer> costIDToRemove) {
        for (Integer costID : costIDToRemove) {
            comboBoxCostID.removeItem(costID);
        }
    }

    /**
     * Setter for categoryInput.
     * @param categoryInputValid indicates if the category name is valid or not.
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
     * This method recieves the categories that belong to the loggedIn user,
     * and then calls to a method that sets the categories to the combobox.
     * @param listOfCategories - that are going to be added to the categoriesListBySpecificUser.
     */
    public void setTheCategoriesToCategoriesComboBox(List<String> listOfCategories) {
        //insert categories to the combo box
//        listOfCategories.add("all");
        insertCategoriesToComboBoxes(listOfCategories);
    }

    /**
     * This method recieves the costs' id's that belong to the loggedIn user costs,
     * and then calls to a method that sets the costs' id to the combobox.
     * @param listOfCostsID - that are going to be added to the categoriesListBySpecificUser.
     */
    public void setTheCostsIDToCostIDComboBox(List<Integer> listOfCostsID) {
        //insert costs to the costs' combo box
        insertCostsToCostsIDComboBox(listOfCostsID);
    }

    /**
     * This method responsible for insert all cost's id to the IDComboBox.
     * @param listOfCostsID - list of all cost's id that belong to a specific user.
     */
    private void insertCostsToCostsIDComboBox(List<Integer> listOfCostsID) {
        clearComboBoxesItems(comboBoxCostID);

        for (Integer costId : listOfCostsID) {
            comboBoxCostID.addItem(costId);
        }
    }

    /**
     * This method builds the expenses' table of the user that logged in.
     * But first, it calls setTable to create the columns names for the table.
     * @param expenses - list of the expenses according to the chosen category.
     */
    public void setTableInAllExpensesPanel(List<Expense> expenses) {
        // link the model and the table
        this.expensesByCategory.tableData.setModel(setTableCosts(expenses));
    }

    /**
     * This method responsible for setting a table of costs.
     * @return table of costs.
     */
    public DefaultTableModel setTableCosts(List<Expense> expenses){
        // create the columns names for the table
        DefaultTableModel tableDataModel = new DefaultTableModel(new String[]{
                "cost_id", "category", "sum_cost", "currency", "description", "date", "user_id"},
                0);

        // add each cost to the table model
        for (Expense expense : expenses) {
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

        return tableDataModel;
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
        layeredPaneCenterAppView.setBounds(0, 400, 1300, 700);
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

    /**
     * This method responsible for setting the panel Operation on the layered pane.
     */
    private void setPanelOperationsPartOfTheLayeredPane() {
        panelOperations.setBackground(new Color(230, 230, 230));
        panelOperations.add(categoryAndExpenseOperations);
        layeredPaneCenterAppView.add(panelOperations);
    }

    /**
     * This method responsible for setting the panel Report on the layered pane.
     */
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

    /*** Setting the combo boxes that contain only Strings. */
    private void setComboBoxAttributes(JComboBox<String> comboBox) {
        ComponentUtils.setComponentsAttributes(
                comboBox,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(180, 35));
    }

    /**
     * Clear the given combo box.
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
        panelSouthAppView.setBackground(new Color(105,105,105));
        panelSouthAppView.setBounds(0, 900, 1400, 100);

        ComponentUtils.setComponentsAttributes(labelFeedbackMessage,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(1350, 50));
        labelFeedbackMessage.setForeground(Color.WHITE);

        panelSouthAppView.add(labelFeedbackMessage);
        panelAppContent.add(panelSouthAppView, BorderLayout.SOUTH);
    }

    /**
     * This method settings the north panel of the application, which is acting like the client navigation bar.
     */
    private void setNorthPanel() {
        panelNorthAppView.setLayout(panelNorthFlowLayout);
        panelNorthAppView.setBackground(new Color(120,0,0));
        panelNorthAppView.setBounds(0, 0, 1400, 200);
        panelNorthAppView.add(buttonExpenses);
        panelNorthAppView.add(buttonOperations);
        panelNorthAppView.add(labelExpenseManagerTitle);
        panelNorthAppView.add(buttonReport);
        panelNorthAppView.add(buttonLogout);
        panelAppContent.add(panelNorthAppView, BorderLayout.NORTH);
        labelExpenseManagerTitle.setForeground(Color.WHITE);
        setComponentBackAndForeGround(buttonLogout, Color.BLACK , colorForButtons);
        setComponentBackAndForeGround(buttonExpenses, Color.BLACK , colorForButtons);
        setComponentBackAndForeGround(buttonOperations, Color.BLACK , colorForButtons);
        setComponentBackAndForeGround(buttonReport, Color.BLACK , colorForButtons);
    }

    /**
     * This method represents the buttons ActionListeners.
     */
    private void setButtonsActionListeners() {

        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonExpenses, panelAllExpenses);
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonOperations, panelOperations);
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(layeredPaneCenterAppView, buttonReport, panelReport);

        //different action listener for the buttonLogout.
        buttonLogout.addActionListener(e -> {
            System.exit(0);
        });
    }

    /**
     * This method responsible for setting the background and the foreground of the given component.
     * @param component - the given component to set its background and the foreground.
     * @param foreGroundColor - color for the foreGround.
     * @param backGroundColor - color for the backGround.
     */
    private void setComponentBackAndForeGround(Component component, Color foreGroundColor, Color backGroundColor){
        component.setForeground(foreGroundColor);
        component.setBackground(backGroundColor);
    }


    /**
     * This method sets each of the north's panel components attributes by first
     * extracting all the buttons from the northPanel and then their attributes,
     * and separately set the labelCostManagerTitle attributes.
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

        /*** Ctor */
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
         * CategorySelectorPanel class allows the user to choose the category in order to
         * display the expenses according to the chosen category.
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
                panelNorthTitleCategorySelector.setBackground(new Color(247, 221, 194));
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
         * Swing components.
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

        /**
         * Ctor.
         */
        public CategoryAndExpenseOperations() {
            initOperationsPanel();
            startOperationsPanel();
        }

        /**
         * This class responsible for initialize the OperationsPanel components.
         */
        private void initOperationsPanel() {
            panelLeftCategories = new JPanel();
            panelRightCost = new JPanel();
            operationsGrid = new GridLayout(1, 2, 10, 0);
            leftGridLayout = new GridLayout(2, 1, 0, 10);
            rightGridLayout = new GridLayout(2, 1, 0, 10);
            addCategory = new AddCategoryPanel();
            removeCategory = new RemoveCategoryPanel();
            addCostPanel = new AddCostPanel();
            removeCostPanel = new RemoveCostPanel();
        }

        /**
         * This class responsible for set the frame and the panels of this class.
         */
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

        /**
         * Add category panel class represents adding new category operation as a panel.
         */
        private class AddCategoryPanel extends JPanel {

            /** Swing components. */
            private JLabel labelTitleAddCategory;
            private JLabel labelAddCategoryName;
            private JTextField textFieldAddCategory;
            private JButton buttonAddNewCategory;
            private JPanel panelNorthAddCategory;
            private JPanel panelCenterAddCategory;
            private JPanel panelSouthAddCategory;
            private FlowLayout flowLayoutAddCategory;

            /**
             * Ctor.
             */
            private AddCategoryPanel() {
                initAddCategory();
                startAddCategory();
            }

            /**
             * This class responsible for initialize the all components of this class.
             */
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

            /**
             * This method responsible for setting the components of this class.
             */
            private void startAddCategory() {
                setLabelTitleAddCategoryAttributes();
                setLabelAddCategoryAttributes();
                setTextFieldAddCategoryAttributes();
                setBtnAddCategoryAttributes();
                locateComponentsOnAddCategoryPane();
                setButtonAddActionListener();
            }

            /**
             * This method responsible for setting the title "Add Category" attributes.
             */
            private void setLabelTitleAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelTitleAddCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }

            /**
             * This method responsible for setting the "Category name" label attributes.
             */
            private void setLabelAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelAddCategoryName,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(250, 50));
            }

            /**
             * This method responsible for setting the category textfield attributes.
             */
            private void setTextFieldAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(textFieldAddCategory,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 40));
            }

            /**
             * This method responsible for setting the add category button attributes.
             */
            private void setBtnAddCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(buttonAddNewCategory,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(100, 40));
            }

            /**
             * This method responsible for locating the components on AddCategoryPane.
             */
            private void locateComponentsOnAddCategoryPane() {
                this.setLayout(new BorderLayout());
                this.setPreferredSize(new Dimension(450, 400));
                panelNorthAddCategory.add(labelTitleAddCategory);
                panelNorthAddCategory.setBackground(colorForTitles);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                this.add(panelNorthAddCategory, BorderLayout.NORTH);
                panelCenterAddCategory.setLayout(flowLayoutAddCategory);
                panelCenterAddCategory.add(labelAddCategoryName);
                panelCenterAddCategory.add(textFieldAddCategory);
                this.add(panelCenterAddCategory, BorderLayout.CENTER);
                panelSouthAddCategory.add(buttonAddNewCategory);
                this.add(panelSouthAddCategory, BorderLayout.SOUTH);
            }

            /**
             * This method responsible for setting the action listener of the buttons.
             */
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

        /**
         * Class that responsible for removing a specific category.
         */
        private class RemoveCategoryPanel extends JPanel {

            /** Swing components. */
            private JLabel labelRemoveCategoryTitle;
            private JLabel labelRemoveCategory;
            private JButton buttonRemoveCategory;
            private JPanel panelNorthRemoveCategory;
            private JPanel panelCenterRemoveCategory;
            private JPanel panelSouthRemoveCategory;
            private FlowLayout flowLayoutRemoveCategory;

            /**
             * Ctor.
             */
            private RemoveCategoryPanel() {
                initRemoveCategory();
                startRemoveCategory();
            }

            /**
             * This method responsible for initialize all the components of this class.
             */
            private void initRemoveCategory() {
                labelRemoveCategoryTitle = new JLabel("Remove Category");
                labelRemoveCategory = new JLabel("Select category:");
                buttonRemoveCategory = new JButton("Remove");
                panelNorthRemoveCategory = new JPanel();
                panelCenterRemoveCategory = new JPanel();
                panelSouthRemoveCategory = new JPanel();
                flowLayoutRemoveCategory = new FlowLayout(0, 25, 25);
            }

            /**
             * This method responsible for setting the components of this clas.
             */
            private void startRemoveCategory() {
                setLabelTitleRemoveCategoryAttributes();
                setLabelRemoveCategoryAttributes();
                setButtonRemoveAttributes();
                locateComponentsOnRemoveCategoryPane();
                setButtonRemoveCategoryActionListener();
            }

            /**
             * This method responsible for setting the title "Remove Category" attributes.
             */
            private void setLabelTitleRemoveCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCategoryTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }

            /**
             * This method responsible for setting the "Select category" label attributes
             * on Remove Category panel.
             */
            private void setLabelRemoveCategoryAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCategory,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(250, 50));
            }

            /**
             * This method responsible for setting the button "Remove" attributes
             * on Remove Category panel.
             */
            private void setButtonRemoveAttributes() {
                ComponentUtils.setComponentsAttributes(buttonRemoveCategory,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(140, 40));
            }

            /***
             * This method responsible for locating all the components on RemoveCategoryPane.
             */
            private void locateComponentsOnRemoveCategoryPane() {
                this.setLayout(new BorderLayout());
                panelNorthRemoveCategory.add(labelRemoveCategoryTitle);
                panelNorthRemoveCategory.setBackground(colorForTitles);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                this.add(panelNorthRemoveCategory, BorderLayout.NORTH);
                panelCenterRemoveCategory.setLayout(flowLayoutRemoveCategory);
                panelCenterRemoveCategory.add(labelRemoveCategory);
                panelCenterRemoveCategory.add(comboBoxCategoriesRemoveCategoryPanel);
                this.add(panelCenterRemoveCategory, BorderLayout.CENTER);
                panelSouthRemoveCategory.add(buttonRemoveCategory);
                this.add(panelSouthRemoveCategory, BorderLayout.SOUTH);
            }

            /**
             * This method responsible for setting the action listener for Remove button on RemoveCategory panel.
             */
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

            /** Swing components. */
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
            private JButton buttonClearInputsInAddCostPanel;
            private JButton buttonAddNewCost;
            private JPanel panelNorthAddCost;
            private JPanel panelCenterAddCost;
            private JPanel panelSouthAddCost;
            private GridLayout gridLayoutCenterPanelAddCost;
            private FlowLayout flowLayoutSouthPanelAddCost;

            /**
             * Ctor.
             */
            private AddCostPanel() {
                initAddCost();
                startAddCost();
            }

            /**
             * This class responsible for initialize all the components of this class.
             */
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

            /**
             * This class responsible for setting the components of this class.
             */
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

            /**
             * This method responsible for setting title "Add Cost" attributes in AddCost panel.
             */
            private void setLabelTitleAddCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelAddCostTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(500, 50));
            }

            /**
             * This method responsible for setting Category label attributes in AddCost panel.
             */
            private void setLabelCategoryAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(labelCategoryInAddCostPanel,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting sumCost label attributes in AddCost panel.
             */
            private void setLabelSumCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelSumCost,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting Currency label attributes in AddCost panel.
             */
            private void setLabelCurrencyAttributes() {
                ComponentUtils.setComponentsAttributes(labelCurrency,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting Description label attributes in AddCost panel.
             */
            private void setLabelDescriptionAttributes() {
                ComponentUtils.setComponentsAttributes(labelDescription,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting Date label attributes in AddCost panel.
             */
            private void setLabelDateAttributes() {
                ComponentUtils.setComponentsAttributes(labelDate,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting sumCost textField attributes in AddCost panel.
             */
            private void setTextFieldSumCost() {
                ComponentUtils.setComponentsAttributes(textFieldSumCost,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting Currency textField attributes in AddCost panel.
             */
            private void setTextFieldCurrency() {
                ComponentUtils.setComponentsAttributes(textFieldCurrency,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(50, 10));
            }
            /**
             * This method responsible for setting description textField attributes in AddCost panel.
             */

            private void setTextFieldDescription() {
                ComponentUtils.setComponentsAttributes(textAreaDescription,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(45, 10));
            }

            /**
             * This method responsible for setting DateChooserField attributes in AddCost panel.
             */
            private void setDateChooserFieldAttributes() {
                dateChooser.setDateFormatString("yyyy-MM-dd");

                ComponentUtils.setComponentsAttributes(dateChooser,
                        new Font("Tahoma", Font.PLAIN, 20),
                        new Dimension(50, 10));
            }

            /**
             * This method responsible for setting Clear button attributes in AddCost panel.
             */
            private void setButtonClearAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(buttonClearInputsInAddCostPanel,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(120, 40));
            }

            /**
             * This method responsible for setting Add button attributes in AddCost panel.
             */
            private void setButtonAddInAddCostPanelAttributes() {
                ComponentUtils.setComponentsAttributes(buttonAddNewCost,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(100, 40));
            }

            /**
             * This method responsible for locating all components of this class.
             */
            private void locateComponentsOnAddCostPanel() {
                this.setLayout(new BorderLayout());
                this.setPreferredSize(new Dimension(450, 400));
                panelNorthAddCost.setBackground(colorForTitles);
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

            /**
             * This method responsible for setting the action listener of Clear and Add buttons.
             */
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

            /**
             * Setting the buttonAdd action listener.
             */
            private void setButtonAddActionListener() {
                buttonAddNewCost.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addNewCost();
                        clearAllTextFieldsOfAddCostPanel();
                    }
                });
            }

            /**
             * This method responsible for adding the new cost.
             */
            private void addNewCost() {
                String categorySelected = comboBoxCategoriesAddCostPanel.getSelectedItem().toString();
                String sumCost = textFieldSumCost.getText();
                String currency = textFieldCurrency.getText();
                String description = textAreaDescription.getText();
                Date date = dateChooser.getDate();

                appUtils.validateAndAddNewCost(categorySelected, sumCost, currency, description, date);
            }
        }

        /** This class responsible for removing a specific costID. */
        private class RemoveCostPanel extends JPanel {
            /** Swing components. */
            private JLabel labelRemoveCostTitle;
            private JLabel labelCostID;
            private JButton buttonRemoveCost;
            private JPanel panelNorthRemoveCost;
            private JPanel panelCenterRemoveCost;
            private JPanel panelSouthRemoveCost;
            private FlowLayout flowLayoutCenterRemoveCost;

            /**
             * Ctor.
             */
            private RemoveCostPanel() {
                initRemoveCost();
                startRemoveCost();
            }

            /**
             * This method responsible for initialize all components of RemoveCost panel.
             */
            private void initRemoveCost() {
                labelRemoveCostTitle = new JLabel("Remove Cost");
                labelCostID = new JLabel("Cost ID:");
                buttonRemoveCost = new JButton("Remove");
                panelNorthRemoveCost = new JPanel();
                panelCenterRemoveCost = new JPanel();
                panelSouthRemoveCost = new JPanel();
                flowLayoutCenterRemoveCost = new FlowLayout(0, 120, 25);
            }

            /**
             * This method responsible for setting all components of RemoveCost panel.
             */
            private void startRemoveCost() {
                setLabelTitleRemoveCostAttributes();
                setLabelCostIDAttributes();
                setComboboxCostIDAttributes();
                setButtonRemoveCostAttributes();
                locateComponentsOnRemoveCostPanel();
                setButtonRemoveCostActionListener();
            }

            /**
             * This method responsible for setting the title "Remove Cost" attributes of RemoveCost panel.
             */
            private void setLabelTitleRemoveCostAttributes() {
                ComponentUtils.setComponentsAttributes(labelRemoveCostTitle,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 50));
            }

            /**
             * This method responsible for setting the CostID label attributes of RemoveCost panel.
             */
            private void setLabelCostIDAttributes() {
                ComponentUtils.setComponentsAttributes(labelCostID,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(120, 30));
            }

            /**
             * This method responsible for setting the combobox of costsID attributes of RemoveCost panel.
             */
            private void setComboboxCostIDAttributes() {
                ComponentUtils.setComponentsAttributes(
                        comboBoxCostID,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(180, 35));

                appUtils.getCostsID();
            }

            /**
             * This method responsible for setting the Remove button attributes of RemoveCost panel.
             */
            private void setButtonRemoveCostAttributes() {
                ComponentUtils.setComponentsAttributes(buttonRemoveCost,
                        new Font("Narkisim", Font.BOLD, 20),
                        new Dimension(140, 40));
            }

            /**
             * This method responsible for locating the components of RemoveCost panel.
             */
            private void locateComponentsOnRemoveCostPanel() {
                this.setLayout(new BorderLayout());
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                panelNorthRemoveCost.setBackground(colorForTitles);
                panelNorthRemoveCost.add(labelRemoveCostTitle);
                this.add(panelNorthRemoveCost, BorderLayout.NORTH);

                panelCenterRemoveCost.setLayout(flowLayoutCenterRemoveCost);
                panelCenterRemoveCost.add(labelCostID);
                panelCenterRemoveCost.add(comboBoxCostID);
                this.add(panelCenterRemoveCost, BorderLayout.CENTER);

                panelSouthRemoveCost.add(buttonRemoveCost);
                this.add(panelSouthRemoveCost, BorderLayout.SOUTH);
            }

            /**
             * This method responsible for setting the action listener of button remove in RemoveCost panel.
             */
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

    /** This class responsible for displaying the costs by two chosen dates. */
    private class Report extends JPanel {
        /** Swing components. */
        private GridLayout gridLayoutLeftReportPanel;
        private GridLayout gridLayoutCenterDatesPanel;
        private FlowLayout flowLayoutReportPanel;
        private JPanel panelSelectDatesReport;
        private JPanel panelNorthDatesReport;
        private JPanel panelCenterDatesReport;
        private JPanel panelSouthDatesReport;
        private JPanel panelLeftReport;
        private JPanel panelRightReport;
        private JLabel labelTitleSelectDates;
        private JLabel labelStartDate;
        private JLabel labelEndDate;
        private JDateChooser dateChooserStart;
        private JDateChooser dateChooserEnd;
        private JButton buttonDisplayChartAndTable;
        private JPanel panelTableDataReport;
        private JScrollPane scrollPaneTableReport;
        private JTable tableDataReport;

        /**
         * Ctor.
         */
        private Report() {
            initReport();
            startReport();
        }

        /**
         * This class responsible for initialize all components of this class.
         */
        private void initReport() {
            gridLayoutLeftReportPanel = new GridLayout(2, 1, 0, 20);
            gridLayoutCenterDatesPanel = new GridLayout(2, 2, 20, 15);
            flowLayoutReportPanel = new FlowLayout(5, 20, 10);
            panelSelectDatesReport = new JPanel();
            panelNorthDatesReport = new JPanel();
            panelCenterDatesReport = new JPanel();
            panelSouthDatesReport = new JPanel();
            panelLeftReport = new JPanel();
            panelRightReport = new JPanel();
            labelTitleSelectDates = new JLabel("Select Dates");
            labelStartDate = new JLabel("Start Date:");
            labelEndDate = new JLabel("End Date");
            dateChooserStart = new JDateChooser();
            dateChooserEnd = new JDateChooser();
            buttonDisplayChartAndTable = new JButton("Display");
            panelTableDataReport = new JPanel();
            scrollPaneTableReport = new JScrollPane();
            tableDataReport = new JTable();
        }

        /**
         * This class responsible for setting all components of this class.
         */
        private void startReport() {
            setPanelDatesReport();
            buttonDisplayPieChartSetOnClickListener();
            setExpensesTableReport();
            locateComponentsOnReportPanel();
        }

        /**
         * This method responsible for setting the PanelDatesReport.
         */
        private void setPanelDatesReport() {
            setTitleSelectDatesAttributes();
            setLabelStartAndEndDateAttributes();
            setDateChooserStartAndEndDateAttributes();
            setButtonDisplayChartAttributes();
        }

        /**
         * This method responsible for setting the title "Select dates" attributes in DatesReport panel.
         */
        private void setTitleSelectDatesAttributes() {
            ComponentUtils.setComponentsAttributes(labelTitleSelectDates,
                    new Font("Narkisim", Font.BOLD, 30),
                    new Dimension(200, 30));
        }

        /**
         * This method responsible for setting the startDate and endDate labels attributes in DatesReport panel.
         */
        private void setLabelStartAndEndDateAttributes() {
            ComponentUtils.setComponentsAttributes(labelStartDate,
                    new Font("Narkisim", Font.BOLD, 25),
                    new Dimension(100, 15));

            ComponentUtils.setComponentsAttributes(labelEndDate,
                    new Font("Narkisim", Font.BOLD, 25),
                    new Dimension(100, 15));
        }

        /**
         * This method responsible for setting the DateChooserStart and DateChooserEnd attributes in DatesReport panel.
         */
        private void setDateChooserStartAndEndDateAttributes() {
            dateChooserStart.setDateFormatString("yyyy-MM-dd");
            dateChooserEnd.setDateFormatString("yyyy-MM-dd");

            ComponentUtils.setComponentsAttributes(dateChooserStart,
                    new Font("Tahoma", Font.PLAIN, 20),
                    new Dimension(70, 10));

            ComponentUtils.setComponentsAttributes(dateChooserEnd,
                    new Font("Tahoma", Font.PLAIN, 20),
                    new Dimension(70, 10));
        }

        /**
         * This method responsible for setting the button display in DatesReport panel.
         */
        private void setButtonDisplayChartAttributes() {
            ComponentUtils.setComponentsAttributes(buttonDisplayChartAndTable,
                    new Font("Narkisim", Font.BOLD, 20),
                    new Dimension(120, 30));
        }

        /**
         * This method builds the expenses table in Report panel.
         */
        private void setExpensesTableReport() {
            panelTableDataReport.setLayout(new BorderLayout());
            panelTableDataReport.add(scrollPaneTableReport);
            tableDataReport.setFont(new Font("Narkisim", Font.PLAIN, 20));
            tableDataReport.setRowHeight(30);
            tableDataReport.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scrollPaneTableReport.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPaneTableReport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneTableReport.setViewportView(tableDataReport);
            panelTableDataReport.setPreferredSize(new Dimension(300, 270));
        }

        /**
         * This method responsible for locating all components in Report panel.
         */
        private void locateComponentsOnReportPanel() {
            this.setLayout(flowLayoutReportPanel);
            panelLeftReport.setLayout(gridLayoutLeftReportPanel);
            BorderLayout borderLayoutNorthReportPanel = new BorderLayout();
            panelSelectDatesReport.setLayout(borderLayoutNorthReportPanel);
            panelSelectDatesReport.setPreferredSize(new Dimension(600, 200));
            panelSelectDatesReport.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            panelNorthDatesReport.setBackground(colorForTitles);
            panelNorthDatesReport.add(labelTitleSelectDates);
            panelSelectDatesReport.add(panelNorthDatesReport, BorderLayout.NORTH);
            panelCenterDatesReport.setLayout(gridLayoutCenterDatesPanel);
            panelCenterDatesReport.add(labelStartDate);
            panelCenterDatesReport.add(dateChooserStart);
            panelCenterDatesReport.add(labelEndDate);
            panelCenterDatesReport.add(dateChooserEnd);
            panelSelectDatesReport.add(panelCenterDatesReport, BorderLayout.CENTER);
            panelSouthDatesReport.add(buttonDisplayChartAndTable);
            panelSelectDatesReport.add(panelSouthDatesReport, BorderLayout.SOUTH);
            panelLeftReport.add(panelSelectDatesReport);
            panelLeftReport.add(panelTableDataReport);
            this.add(panelLeftReport);
            panelChartReport.setPreferredSize(new Dimension(600, 500));
            this.add(panelChartReport);
        }

        /**
         * This method responsible for setting the action listener for button display.
         */
        private void buttonDisplayPieChartSetOnClickListener() {
            buttonDisplayChartAndTable.addActionListener(new ActionListener() {
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