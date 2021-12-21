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
    private JFrame loginFrame;
    private JPanel panelNorthLoginFrame;
    private JPanel panelCenterLoginFrame;
    private JPanel panelWestLoginFrame;
    private JPanel panelSouthLoginFrame;
    private JLabel labelCostManagerTitle;
    private Button loginButton;
    private Button signUpButton;
    private JLabel labelInvalidDescription;
    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;

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
        loginFrame = new JFrame();
        panelNorthLoginFrame = new JPanel();
        panelCenterLoginFrame = new JPanel();
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
        panelNorthLoginFrame.setBackground(Color.PINK);
        panelNorthLoginFrame.add(labelCostManagerTitle);
        panelWestLoginFrame.setLayout(new BoxLayout(panelWestLoginFrame, BoxLayout.PAGE_AXIS));
        panelWestLoginFrame.add(loginButton);
        panelWestLoginFrame.add(Box.createRigidArea(new Dimension(0, 20)));
        panelWestLoginFrame.add(signUpButton);
        panelSouthLoginFrame.add(labelInvalidDescription);
        loginPanel.setVisible(false);
        signUpPanel.setVisible(false);
        panelCenterLoginFrame.add(loginPanel);
        panelCenterLoginFrame.add(signUpPanel);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.add(panelNorthLoginFrame, BorderLayout.NORTH);
        loginFrame.add(panelCenterLoginFrame, BorderLayout.CENTER);
        loginFrame.add(panelWestLoginFrame, BorderLayout.WEST);
        loginFrame.add(panelSouthLoginFrame, BorderLayout.SOUTH);
        loginFrame.setSize(600, 400);
        setButtonSize();
        loginFrame.setVisible(true);
        setButtonsActionListeners();
    }

    public void setButtonSize()
    {
        setButtonAttributes(loginButton,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(40, 40));
        setButtonAttributes(signUpButton,
                new Font("Narkisim", Font.BOLD, 30),
                new Dimension(40, 40));
    }

    public void setButtonAttributes(Component component, Font font, Dimension dimensions)
    {
        component.setFont(font);
        component.setPreferredSize(dimensions);
    }

    private void setButtonsActionListeners()
    {
        loginButton.addActionListener(e ->
        {
            if (signUpPanel.isVisible())
            {
                signUpPanel.setVisible(false);
            }
            loginPanel.setVisible(true);
        });
        signUpButton.addActionListener(e ->
        {
            if (loginPanel.isVisible())
            {
                loginPanel.setVisible(false);
            }
            signUpPanel.setVisible(true);
        });
    }

    /**
     * @param fullName users input for full name
     * @return checks if the full name consists of letters.
     */
    private boolean validateUsersFullName(String fullName)
    {
        return fullName.matches("^[a-zA-Z]*$");
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
            LoginInit();
            LoginStart();
        }

        private void LoginInit()
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

        /**
         * calling the getUser method of the view model for
         */
        private void LoginStart()
        {
            setPanelViewsComponents();
            this.setPreferredSize(new Dimension(400, 300));
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(20);
            this.setLayout(borderLayout);
            panelLoginNorthLoginPanel.add(labelLoginTitle);
            this.add(panelLoginNorthLoginPanel, BorderLayout.NORTH);
            GridLayout gridLayout = new GridLayout(2, 2, 20, 50);
            gridLayout.setVgap(20);
            panelLoginCenterLoginPanel.setLayout(gridLayout);
            panelLoginCenterLoginPanel.add((labelFullNameLoginPanel));
            panelLoginCenterLoginPanel.add(textFieldFullNameLoginPanel);
            panelLoginCenterLoginPanel.add((labelPasswordLoginPanel));
            panelLoginCenterLoginPanel.add(textFieldPasswordLoginPanel);
            this.add(panelLoginCenterLoginPanel, BorderLayout.CENTER);
            panelLoginSouthLoginPanel.add(buttonOkLoginPanel);
            this.add(panelLoginSouthLoginPanel, BorderLayout.SOUTH);
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
                            if (((ViewManager) viewManager).getUser() != null)
                            {
                                ((ViewManager) viewManager).changeFrameFromLoginViewToAppView();
                            }
                            else
                            {
                                labelInvalidDescription.setText(USER_DOES_NOT_EXISTS);
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
//        /**
//         * @param fullName users input for full name
//         * @return checks if the full name consists of letters.
//         */
//        private boolean validateUsersFullName(String fullName)
//        {
//            return fullName.matches("^[a-zA-Z]*$");
//        }

        private void setPanelViewsComponents()
        {
            List<JComponent> componentsList = new ArrayList<>();
            Font font = new Font("Narkisim", Font.BOLD, 30);
            Dimension dimension = new Dimension(new Dimension(250, 100));
            setComponentsAttributes(labelLoginTitle,
                    new Font("Narkisim", Font.BOLD, 50),
                    new Dimension(500, 150));
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

    private class SignUpPanel extends JFrame
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
            buttonSubmitSignUp = new JButton("Ok");
            panelNorthSignUp = new JPanel();
            panelCenterSignUp = new JPanel();
            panelSouthSignUp = new JPanel();
        }

        private void SignUpStart()
        {
            setSignsUpPanelComponents();
            this.setPreferredSize(new Dimension(450, 350));
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(20);
            this.setLayout(borderLayout);
            panelNorthSignUp.add(labelSignUpTitle);
            this.add(panelNorthSignUp, BorderLayout.NORTH);
            GridLayout gridLayout = new GridLayout(3, 2, 20, 50);
            gridLayout.setVgap(20);
            panelCenterSignUp.setLayout(gridLayout);
            panelCenterSignUp.add((labelFullNameSignUp));
            panelCenterSignUp.add(textFieldFullNameSignUp);
            panelCenterSignUp.add((labelPasswordSignUp));
            panelCenterSignUp.add(textFieldPasswordSignUp);
            panelCenterSignUp.add((labelConfirmPasswordSignUp));
            panelCenterSignUp.add(textFieldConfirmPasswordSignUp);
            this.add(panelCenterSignUp, BorderLayout.CENTER);
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
                                ((ViewManager) viewManager).getViewModel().addNewUser(new User(fullName, password));
                                if (((ViewManager) viewManager).getUser() != null)
                                {
                                    ((ViewManager) viewManager).changeFrameFromLoginViewToAppView();
                                }
                                else
                                {
                                    labelInvalidDescription.setText(USER_DOES_NOT_EXISTS);
                                }
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
            Font font = new Font("Narkisim", Font.BOLD, 30);
            Dimension dimension = new Dimension(new Dimension(250, 100));
            setComponentsAttributes(labelSignUpTitle,
                    new Font("Narkisim", Font.BOLD, 50),
                    new Dimension(500, 150));
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
