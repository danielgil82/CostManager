package il.ac.hit.view;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;

public class CostManagerAppView extends JFrame
{
    IView viewManager;
    /**
     * Swing components
     */
    private JButton allExpensesButton;
    private JButton operationsButton;
    private JButton reportButton;
    private JButton logOutButton;
    private JLabel costManagerTitle;
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
//    private AllExpensesPanel allExpenses;
//    private OperationsPanel operationsPanel;
//    private ReportPanel reportPanel;


    public CostManagerAppView(IView viewManager)
    {
        setViewManager(viewManager);
        initAppView();
        startAppView();
    }

    public void setViewManager(IView viewManager)
    {
        this.viewManager = viewManager;
    }


    private void initAppView()
    {
        allExpensesButton = new JButton();
        operationsButton = new JButton();
        reportButton = new JButton();
        logOutButton = new JButton();
        costManagerTitle = new JLabel("Cost Manager");
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

    private void startAppView()
    {
        setApplicationFrame();
        setNorthPanel();
    }

    /**
     * setting the north panel of the application , which is acting like the client navigation bar.
     */
    private void setNorthPanel()
    {
        panelNorth.setLayout(panelNorthFlowLayout);
        panelNorth.setBackground(new Color(190,190,230,155));
        panelNorth.setBounds(0,0,1200, 200);
        panelNorth.add(allExpensesButton);
        panelNorth.add(operationsButton);
        panelNorth.add(reportButton);
        panelNorth.add(costManagerTitle);
        panelNorth.add(logOutButton);
        setNorthPanelComponentAttributes();
        setButtonsActionListeners();
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
                    ((ViewManager)viewManager).resetUser();
                    viewManager.changeFrameFromAppViewToLoginView();
                });

    }

    /**
     *  set each of the buttons attributes
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
                        new Dimension(200,100));
            }
        }
//        ComponentAttributes.setComponentsAttributes(allExpensesButton,
//                new Font("Narkisim", Font.BOLD, 30),
//                new Dimension(200,100));
//
//        ComponentAttributes.setComponentsAttributes(operationsButton,
//                new Font("Narkisim", Font.BOLD, 30),
//                new Dimension(200,100));
//
//        ComponentAttributes.setComponentsAttributes(reportButton,
//                new Font("Narkisim", Font.BOLD, 30),
//                new Dimension(200,100));
//
//
//        ComponentAttributes.setComponentsAttributes(allExpensesButton,
//                new Font("Narkisim", Font.BOLD, 30),
//                new Dimension(200,100));
//
//
//        ComponentAttributes.setComponentsAttributes(logOutButton,
//                new Font("Narkisim", Font.BOLD, 30),
//                new Dimension(200,100));

        costManagerTitle.setFont(new Font("Narkisim", Font.BOLD, 40));
        costManagerTitle.setBounds(1000, 0, 200, 100);
        costManagerTitle.setForeground(Color.red);
    }

    /**
     * setting the application frame size
     */
    private void setApplicationFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,1300,1000);
        panelAppContent.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.add(panelAppContent);
    }
}
