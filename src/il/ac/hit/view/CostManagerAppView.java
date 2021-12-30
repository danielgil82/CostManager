package il.ac.hit.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class CostManagerAppView extends JFrame implements ICentralizeWindow
{
    private final Collection<String> listOfCategories = new ArrayList<>();
    private IView viewManager;
    /**
     * Swing components
     */
    private JButton allExpensesButton;
    private JButton operationsButton;
    private JButton reportButton;
    private JButton logOutButton;
    private JLabel labelCostManagerTitle;
    private JLabel labelFeedbackMessage;
    private JPanel panelAppContent;
    private JPanel panelNorth;
    private JPanel panelAllExpenses;
    private JPanel panelTableData;
    private JPanel panelOperations;
    private JPanel panelReport;
    private JPanel panelSouth;
    private JLayeredPane layeredPaneCenter;
    private JScrollPane scrollPaneTable;
    private JTable tableData;
    private FlowLayout panelNorthFlowLayout;
    private BorderLayout borderLayoutPanelContent;
    private ExpensesPanel expensesPanel;
//    private OperationsPanel operationsPanel;
//    private ReportPanel reportPanel;

    public CostManagerAppView(IView viewManager)
    {
        setViewManager(viewManager);
        initAppView();
        startAppView();
    }


    public Collection<String> getListOfCategories()
    {
        return listOfCategories;
    }

    public ExpensesPanel getExpensesPanel()
    {
        return expensesPanel;
    }

    public void setViewManager(IView viewManager)
    {
        this.viewManager = viewManager;
    }

    private void initAppView()
    {
        expensesPanel = new ExpensesPanel(viewManager);
        allExpensesButton = new JButton("Expenses");
        operationsButton = new JButton("Operations");
        reportButton = new JButton("Report");
        logOutButton = new JButton("LogOut");
        labelCostManagerTitle = new JLabel("Cost Manager");
        labelFeedbackMessage = new JLabel();
        panelAppContent = new JPanel();
        panelNorth = new JPanel();
        panelAllExpenses = new JPanel();
        panelTableData = new JPanel();
        panelOperations = new JPanel();
        panelReport = new JPanel();
        panelSouth = new JPanel();
        layeredPaneCenter = new JLayeredPane();
        scrollPaneTable = new JScrollPane();
        tableData = new JTable();
        panelNorthFlowLayout = new FlowLayout(0, 25, 0);
        borderLayoutPanelContent = new BorderLayout();
    }

    /**
     *
     * Setting panels , buttons and their attributes and listeners
     */
    private void startAppView()
    {
        setApplicationFrame();
        setNorthPanel();
        setNorthPanelComponentAttributes();
        setButtonsActionListeners();
        setSouthPanel();
    }

    /**
     * setting the south panel of the application , this part suppose to .
     */
    private void setSouthPanel()
    {
        panelSouth.setBackground(new Color(190, 190, 230, 155));
        panelSouth.setBounds(0, 900, 1300, 100);

        ComponentAttributes.setComponentsAttributes(labelFeedbackMessage,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(1240,50));

        panelSouth.add(labelFeedbackMessage);
        panelAppContent.add(panelSouth, BorderLayout.SOUTH);
    }

    /**
     * setting the north panel of the application , which is acting like the client navigation bar.
     */
    private void setNorthPanel()
    {
        panelNorth.setLayout(panelNorthFlowLayout);
        panelNorth.setBackground(new Color(190, 190, 230, 155));
        panelNorth.setBounds(0, 0, 1300, 200);
        panelNorth.add(allExpensesButton);
        panelNorth.add(operationsButton);
        panelNorth.add(reportButton);
        panelNorth.add(labelCostManagerTitle);
        panelNorth.add(logOutButton);
        panelAppContent.add(panelNorth, BorderLayout.NORTH);
    }

    /**
     * Buttons ActionListeners
     */
    private void setButtonsActionListeners()
    {
        allExpensesButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(panelTableData);
            layeredPaneCenter.revalidate();
        });

        operationsButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(panelOperations);
            layeredPaneCenter.revalidate();
        });

        reportButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(panelReport);
            layeredPaneCenter.revalidate();
        });

        logOutButton.addActionListener(e ->
        {
            ((ViewManager) viewManager).resetUser();
            viewManager.changeFrameFromAppViewToLoginView();
        });
    }

    /**
     * set each of the north's panel components attributes
     */
    private void setNorthPanelComponentAttributes()
    {
        Component[] components = panelNorth.getComponents();
        for (Component component : components)
        {
            if (component instanceof JButton)
            {
                ComponentAttributes.setComponentsAttributes(component,
                        new Font("Narkisim", Font.BOLD, 30),
                        new Dimension(200, 70));
            }
        }

        labelCostManagerTitle.setFont(new Font("Narkisim", Font.BOLD, 50));
        labelCostManagerTitle.setBounds(1000, 0, 200, 100);
        labelCostManagerTitle.setForeground(Color.red);
    }

    /**
     * setting the application frame size
     */
    private void setApplicationFrame()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1300, 1000);
        centralizeWindow(this);
        //panelAppContent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.add(panelAppContent);

    }
}
