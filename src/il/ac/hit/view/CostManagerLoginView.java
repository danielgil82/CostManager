package il.ac.hit.view;

import il.ac.hit.auxiliary.IErrorAndExceptionsHandlingStrings;
import il.ac.hit.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class CostManagerLoginView extends JFrame implements IErrorAndExceptionsHandlingStrings
{
    private IView viewManager;
    private JPanel panelNorthLoginFrame;
    private JPanel panelWestLoginFrame;
    private JPanel panelSouthLoginFrame;
    private JPanel loginPanelPartOfTheLayeredPanel;
    private JPanel signUPPanelPartOfTheLayeredPanel;
    private JLabel labelCostManagerTitle;
    private Button loginButton;
    private Button signUpButton;
    private JLabel labelInvalidDescription;
    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private JLayeredPane layeredPaneCenter;
    private GridLayout gridLayoutWestPanel;

    public JLabel getLabelInvalidDescription()
    {
        return labelInvalidDescription;
    }

    public CostManagerLoginView(IView viewManager)
    {
        setViewManager(viewManager);
        init();
        start();
    }

    public IView getViewManager()
    {
        return viewManager;
    }

    public void setViewManager(IView viewManager)
    {
        this.viewManager = viewManager;
    }

    public void init()
    {
        gridLayoutWestPanel = new GridLayout(2, 1, 0, 5);
        layeredPaneCenter = new JLayeredPane();
        loginPanelPartOfTheLayeredPanel = new JPanel();
        signUPPanelPartOfTheLayeredPanel = new JPanel();
        panelNorthLoginFrame = new JPanel();

        panelWestLoginFrame = new JPanel();
        panelSouthLoginFrame = new JPanel();
        labelCostManagerTitle = new JLabel("Cost Manager");
        loginButton = new Button("Login");
        signUpButton = new Button("Sign Up");
        labelInvalidDescription = new JLabel();
        loginPanel = new LoginPanel();
        signUpPanel = new SignUpPanel();
    }

    public void start()
    {
        //North Panel
        panelNorthLoginFrame.setBackground(Color.PINK);
        panelNorthLoginFrame.add(labelCostManagerTitle);
        setComponentsAttributes(labelCostManagerTitle, new Font("Narkisim", Font.BOLD, 40), new Dimension(700, 70));
        //West Panel
        panelWestLoginFrame.setLayout(gridLayoutWestPanel);
        panelWestLoginFrame.add(loginButton);
        panelWestLoginFrame.add(signUpButton);
        panelWestLoginFrame.setBackground(Color.red);
        //South Panel
        setComponentsAttributes(labelInvalidDescription, new Font("Narkisim", Font.BOLD, 20), new Dimension(700, 50));
        panelSouthLoginFrame.add(labelInvalidDescription);
        panelSouthLoginFrame.setBackground(Color.cyan);
        //Center Panel
        this.setLayout(new BorderLayout());
        setLayeredPanel();
       //  panelCenterLoginFrame.add(loginPanel);
        //panelCenterLoginFrame.add(signUpPanel);


        //Login And SignUp panels
        //loginPanel.setVisible(false);
        //signUpPanel.setVisible(false);


        //Adding all panels to "this" object which is a JFrame
        this.add(panelNorthLoginFrame, BorderLayout.NORTH);
        this.add(panelWestLoginFrame, BorderLayout.WEST);
        this.add(panelSouthLoginFrame, BorderLayout.SOUTH);
//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centreWindowAndDefineItsDimensions(this);
        setButtonSize();
        setButtonsActionListeners();
        this.setVisible(true);
    }

    private void setLayeredPanel()
    {
        int xPos = loginButton.getLocation().x + loginButton.getWidth();
        int yPos = loginButton.getLocation().y;

        //layeredPaneCenter.setBounds(xPos, yPos, this.getWidth() - 150, panelSouthLoginFrame.getHeight()  - 50);
        layeredPaneCenter.setLayout(new CardLayout(0, 0));
        setLoginPanelPartOfTheLayeredPane();
        setSignUpPanelPartOfTheLayeredPane();
        this.add(layeredPaneCenter, BorderLayout.CENTER);
    }

    private void setLoginPanelPartOfTheLayeredPane()
    {
//        int xPos = loginButton.getLocation().x + loginButton.getWidth() + 5;
//        int yPos = loginButton.getLocation().y;
//
//
//        loginPanelPartOfTheLayeredPanel.setBounds(xPos, yPos, this.getWidth() , panelSouthLoginFrame.getHeight()  - 5);
////        loginPanelPartOfTheLayeredPanel.setBackground(Color.BLUE);
        loginPanelPartOfTheLayeredPanel.add(loginPanel);
        layeredPaneCenter.add(loginPanelPartOfTheLayeredPanel);
    }

    private void setSignUpPanelPartOfTheLayeredPane()
    {
//        int xPos = loginButton.getLocation().x + loginButton.getWidth() + 5;
//        int yPos = loginButton.getLocation().y;
//
//        signUPPanelPartOfTheLayeredPanel.setBounds(xPos, yPos, this.getWidth() - 100, panelSouthLoginFrame.getHeight()  - 5);
        signUPPanelPartOfTheLayeredPanel.add(signUpPanel);
        layeredPaneCenter.add(signUPPanelPartOfTheLayeredPanel);
    }


    private void centreWindowAndDefineItsDimensions(Window frame)
    {
        frame.setSize(800, 600);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);

        frame.setLocation(x, y);
    }


    public void setButtonSize()
    {
        setButtonAttributes(loginButton, new Font("Narkisim", Font.BOLD, 20), new Dimension(80, 10));
        setButtonAttributes(signUpButton, new Font("Narkisim", Font.BOLD, 20), new Dimension(80, 10));
    }

    public void setButtonAttributes(Component component, Font font, Dimension dimensions)
    {
        component.setFont(font);
        component.setPreferredSize(dimensions);
    }

    /**
     * Buttons ActionListeners
     */
    private void setButtonsActionListeners()
    {
        //Login button
        loginButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(loginPanelPartOfTheLayeredPanel);
            layeredPaneCenter.revalidate();

//            if (signUpPanel.isVisible())
//            {
//                signUpPanel.setVisible(false);
//            }
//            loginPanel.setVisible(true);
        });

        //SignUp button
        signUpButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(signUPPanelPartOfTheLayeredPanel);
            layeredPaneCenter.revalidate();

//            if (loginPanel.isVisible())
//            {
//                loginPanel.setVisible(false);
//            }
//            signUpPanel.setVisible(true);
        });
    }

    /**
     * @param fullName users input for full name
     * @return checks if the full name consists of letters and spaces.
     */
    private boolean validateUsersFullName(String fullName)
    {
        char[] chars = fullName.toCharArray();

        for (char c : chars)
        {
            if (!Character.isLetter(c) && c != ' ')
            {
                return false;
            }
        }

        return true;
    }

    /**
     * @param component  is a type of JComponent because it gives the basis for polymorphism and
     *                   thus, it gives you the reuse if needed in the future.
     *                   For Example maybe in the future we'd like to set The Attributes for a JTextField.
     * @param font       indicates the font style of the component.
     * @param dimensions indicates the dimensions of the component.
     */
    private void setComponentsAttributes(JComponent component, Font font, Dimension dimensions)
    {
        if (component instanceof JLabel)
        {
            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
        }

        component.setFont(font);
        component.setPreferredSize(dimensions);
    }


    /**
     *
     *
     * LoginPanel class which displays the login part
     */
    private class LoginPanel extends JPanel
    {

        private JLabel labelLoginTitle;
        private JLabel labelFullNameLoginPanel;
        private JLabel labelPasswordLoginPanel;
        private JTextField textFieldFullNameLoginPanel;
        private JTextField textFieldPasswordLoginPanel;
        private JButton buttonOkLoginPanel;
        private JPanel panelLoginNorthLoginPanel;
        private JPanel panelLoginCenterLoginPanel;
        private JPanel panelLoginSouthLoginPanel;

        public LoginPanel()
        {
            LoginPanelInit();
            LoginPanelStart();
        }

        private void LoginPanelInit()
        {
            labelLoginTitle = new JLabel("Login");
            labelFullNameLoginPanel = new JLabel("Full Name:");
            labelPasswordLoginPanel = new JLabel("Password:");
            textFieldFullNameLoginPanel = new JTextField();
            textFieldPasswordLoginPanel = new JTextField();
            buttonOkLoginPanel = new JButton("Ok");
            panelLoginNorthLoginPanel = new JPanel();
            panelLoginCenterLoginPanel = new JPanel();
            panelLoginSouthLoginPanel = new JPanel();
        }


        private void LoginPanelStart()
        {
            this.setPreferredSize(new Dimension(500, 300));
            setLoginPanelComponents();
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(30);
            this.setLayout(borderLayout);
            //LoginPanelNorth
            panelLoginNorthLoginPanel.add(labelLoginTitle);
            this.add(panelLoginNorthLoginPanel, BorderLayout.NORTH);
            //LoginPanelCenter
            GridLayout gridLayout = new GridLayout(2, 2, 20, 10);
            gridLayout.setVgap(25);
            panelLoginCenterLoginPanel.setLayout(gridLayout);
            panelLoginCenterLoginPanel.add((labelFullNameLoginPanel));
            panelLoginCenterLoginPanel.add(textFieldFullNameLoginPanel);
            panelLoginCenterLoginPanel.add((labelPasswordLoginPanel));
            panelLoginCenterLoginPanel.add(textFieldPasswordLoginPanel);
            this.add(panelLoginCenterLoginPanel, BorderLayout.CENTER);
            //LoginPanelSouth
            setButtonAttributes(buttonOkLoginPanel, new Font("Narkisim", Font.BOLD, 20), new Dimension(70, 30));
            panelLoginSouthLoginPanel.add(buttonOkLoginPanel);
            this.add(panelLoginSouthLoginPanel, BorderLayout.SOUTH);


            /**
             *
             *calling the getUser method of the view model for
             */
            buttonOkLoginPanel.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (textFieldFullNameLoginPanel.getText() != null && textFieldPasswordLoginPanel.getText() != null)
                    {
                        if (validateUsersFullName(textFieldFullNameLoginPanel.getText()))
                        {
                            ((ViewManager) viewManager).getViewModel().getUser(textFieldFullNameLoginPanel.getText(), textFieldPasswordLoginPanel.getText());
//                            if (((ViewManager) viewManager).getUser() != null)
//                            {
//                                ((ViewManager) viewManager).changeFrameFromLoginViewToAppView();
//                            }
//                            else
//                            {
//                                labelInvalidDescription.setText(USER_DOES_NOT_EXISTS);
//                            }
                        }
                        else
                        {
                            labelInvalidDescription.setText(INVALID_FULL_NAME);
                        }
                    }
                    else
                    {
                        labelInvalidDescription.setText(EMPTY_FIELDS);
                    }
                }
            });
        }

        private void setLoginPanelComponents()
        {
            List<JComponent> componentsList = new ArrayList<>();

            Font font = new Font("Narkisim", Font.BOLD, 20);
            Dimension dimension = new Dimension(new Dimension(0, 0));

            setComponentsAttributes(labelLoginTitle, new Font("Narkisim", Font.BOLD, 40), new Dimension(150, 70));

            componentsList.add(labelFullNameLoginPanel);
            componentsList.add(labelPasswordLoginPanel);
            componentsList.add(textFieldFullNameLoginPanel);
            componentsList.add(textFieldPasswordLoginPanel);

            for (JComponent component : componentsList)
            {
                setComponentsAttributes(component, font, dimension);
            }
//            setComponentsAttributes(labelFullNameLoginPanel,
//                    new Font("Narkisim", Font.BOLD, 30),
//                    new Dimension(250, 100));
//            setComponentsAttributes(labelPasswordLoginPanel,
//                    new Font("Narkisim", Font.BOLD, 30),
//                    new Dimension(250, 100));
//            setComponentsAttributes(textFieldFullNameLoginPanel,
//                    new Font("Narkisim", Font.BOLD, 30),
//                    new Dimension(250, 100));
//            setComponentsAttributes(textFieldPasswordLoginPanel,
//                    new Font("Narkisim", Font.BOLD, 30),
//                    new Dimension(250, 100));
        }
//            private void setTextFieldDimensions(JComponent component, Font font, Dimension dimensions)
//            {
//                component.setFont(font);
//                component.setPreferredSize(dimensions);
//            }
    }

    private class SignUpPanel extends JPanel
    {
        private JLabel labelSignUpTitle;
        private JLabel labelFullNameSignUp;
        private JLabel labelPasswordSignUp;
        private JLabel labelConfirmPasswordSignUp;
        private JTextField textFieldFullNameSignUp;
        private JTextField textFieldPasswordSignUp;
        private JTextField textFieldConfirmPasswordSignUp;
        private JButton buttonSubmitSignUp;
        private JPanel panelNorthSignUp;
        private JPanel panelCenterSignUp;
        private JPanel panelSouthSignUp;

        public SignUpPanel()
        {
            SignUpInit();
            SignUpStart();
        }

        private void SignUpInit()
        {
            labelSignUpTitle = new JLabel("Sign Up");
            labelFullNameSignUp = new JLabel("Full Name:");
            labelPasswordSignUp = new JLabel("Password:");
            labelConfirmPasswordSignUp = new JLabel("Confirm Password:");
            textFieldFullNameSignUp = new JTextField();
            textFieldPasswordSignUp = new JTextField();
            textFieldConfirmPasswordSignUp = new JTextField();
            buttonSubmitSignUp = new JButton("Submit");
            panelNorthSignUp = new JPanel();
            panelCenterSignUp = new JPanel();
            panelSouthSignUp = new JPanel();
        }

        private void SignUpStart()
        {
            this.setPreferredSize(new Dimension(500, 400));

            setSignsUpPanelComponents();

            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(20);
            this.setLayout(borderLayout);
            panelNorthSignUp.add(labelSignUpTitle);
            this.add(panelNorthSignUp, BorderLayout.NORTH);
            GridLayout gridLayout = new GridLayout(3, 2, 20, 0);
            gridLayout.setVgap(20);
            panelCenterSignUp.setLayout(gridLayout);
            panelCenterSignUp.add((labelFullNameSignUp));
            panelCenterSignUp.add(textFieldFullNameSignUp);
            panelCenterSignUp.add((labelPasswordSignUp));
            panelCenterSignUp.add(textFieldPasswordSignUp);
            panelCenterSignUp.add((labelConfirmPasswordSignUp));
            panelCenterSignUp.add(textFieldConfirmPasswordSignUp);
            this.add(panelCenterSignUp, BorderLayout.CENTER);
            setButtonAttributes(buttonSubmitSignUp, new Font("Narkisim", Font.BOLD, 20), new Dimension(110, 30));
            panelSouthSignUp.add(buttonSubmitSignUp);
            this.add(panelSouthSignUp, BorderLayout.SOUTH);

            buttonSubmitSignUp.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String fullName = textFieldFullNameSignUp.getText();
                    String password = textFieldPasswordSignUp.getText();
                    String confirmPassword = textFieldConfirmPasswordSignUp.getText();

                    if (fullName != null && password != null && confirmPassword != null)
                    {
                        if (validateUsersFullName(fullName))
                        {
                            // Using BiPredicate Functional Interface inorder to check if the passwords match each other
                            if (confirmPasswords(password, confirmPassword, (firstPassword, secondPassword) -> firstPassword.equals(secondPassword)))
                            {
                                //Here going to add a new user to the users table.
                                ((ViewManager) viewManager).getViewModel().addNewUser(new User(fullName, password));
                            }
                            else
                            {
                                labelInvalidDescription.setText(PASSWORDS_DO_NOT_MATCH);
                            }
                        }
                        else
                        {
                            labelInvalidDescription.setText(INVALID_FULL_NAME);
                        }
                    }
                    else
                    {
                        labelInvalidDescription.setText(EMPTY_FIELDS);
                    }
                }
            });
        }

        private boolean confirmPasswords(String firstPassword, String secondPassword, BiPredicate<String, String> passwordsMatchTest)
        {
            return passwordsMatchTest.test(firstPassword, secondPassword);
        }

        private void setSignsUpPanelComponents()
        {
            List<JComponent> componentsList = new ArrayList<>();

            Font font = new Font("Narkisim", Font.BOLD, 20);
            Dimension dimension = new Dimension(new Dimension(250, 50));

            setComponentsAttributes(labelSignUpTitle, new Font("Narkisim", Font.BOLD, 35), new Dimension(200, 70));

            componentsList.add(labelFullNameSignUp);
            componentsList.add(labelPasswordSignUp);
            componentsList.add(labelConfirmPasswordSignUp);
            componentsList.add(textFieldFullNameSignUp);
            componentsList.add(textFieldPasswordSignUp);
            componentsList.add(textFieldConfirmPasswordSignUp);
            for (JComponent component : componentsList)
            {
                setComponentsAttributes(component, font, dimension);
            }
        }
    }
}

