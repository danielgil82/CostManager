package il.ac.hit.view;

import il.ac.hit.model.User;

import javax.swing.*;
import java.awt.*;

public class CostManagerAppView extends JFrame
{
    IView viewManager;
    /**
     * Swing components
     */
    private JButton allCostsButton;
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
        allCostsButton = new JButton();
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

    }


}
