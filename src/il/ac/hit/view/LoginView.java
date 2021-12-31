package il.ac.hit.view;

import il.ac.hit.auxiliary.HandlingMessage;
import il.ac.hit.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class LoginView extends JFrame {
    private View viewManager;
    private JPanel panelNorthLoginFrame;
    private JPanel panelWestLoginFrame;
    private JPanel panelSouthLoginFrame;
    private JPanel loginPanelPartOfTheLayeredPane;
    private JPanel signUPPanelPartOfTheLayeredPane;
    private JLabel labelCostManagerTitle;
    private JLayeredPane layeredPaneCenter;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel labelInvalidDescription;
    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private GridLayout gridLayoutWestPanel;

    public JLabel getLabelInvalidDescription() {

        return labelInvalidDescription;
    }

    public LoginView(View viewManager, BiConsumer<String, String> signInHandler) {
        setViewManager(viewManager);
        initLoginView();
        startLoginView();
        loginPanel.registerUserAndPasswordInvokeListener(signInHandler);

    }

    public void setViewManager(View viewManager) {
        this.viewManager = viewManager;
    }

    public void initLoginView() {
        gridLayoutWestPanel = new GridLayout(2, 1, 0, 5);
        layeredPaneCenter = new JLayeredPane();
        loginPanelPartOfTheLayeredPane = new JPanel();
        signUPPanelPartOfTheLayeredPane = new JPanel();
        panelNorthLoginFrame = new JPanel();

        panelWestLoginFrame = new JPanel();
        panelSouthLoginFrame = new JPanel();
        labelCostManagerTitle = new JLabel("Cost Manager");
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");
        labelInvalidDescription = new JLabel();
        loginPanel = new LoginPanel();
        signUpPanel = new SignUpPanel();
    }

    public void startLoginView() {
        //North Panel
        panelNorthLoginFrame.setBackground(Color.PINK);
        panelNorthLoginFrame.add(labelCostManagerTitle);
        ComponentUtils.setComponentsAttributes(labelCostManagerTitle, new Font("Narkisim", Font.BOLD, 40), new Dimension(700, 70));
        //West Panel
        panelWestLoginFrame.setLayout(gridLayoutWestPanel);
        panelWestLoginFrame.add(loginButton);
        panelWestLoginFrame.add(signUpButton);
        panelWestLoginFrame.setBackground(Color.red);
        //South Panel
        ComponentUtils.setComponentsAttributes(labelInvalidDescription, new Font("Narkisim", Font.BOLD, 20), new Dimension(700, 50));
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
        this.setSize(800, 600);
        ComponentUtils.centralizeWindow(this);
        //centreWindowAndDefineItsDimensions(this);
        setButtonAttributes();
        setButtonsActionListeners();
        this.setVisible(true);
    }

    private void setLayeredPanel() {
        int xPos = loginButton.getLocation().x + loginButton.getWidth();
        int yPos = loginButton.getLocation().y;

        //layeredPaneCenter.setBounds(xPos, yPos, this.getWidth() - 150, panelSouthLoginFrame.getHeight()  - 50);
        layeredPaneCenter.setLayout(new CardLayout(0, 0));
        setLoginPanelPartOfTheLayeredPane();
        setSignUpPanelPartOfTheLayeredPane();
        this.add(layeredPaneCenter, BorderLayout.CENTER);
    }

    /**
     * adding the loginPanel object into the layeredPane.
     */
    private void setLoginPanelPartOfTheLayeredPane() {
        loginPanelPartOfTheLayeredPane.add(loginPanel);
        layeredPaneCenter.add(loginPanelPartOfTheLayeredPane);
    }

    /**
     * adding the signUpPanel object into the layeredPane.
     */
    private void setSignUpPanelPartOfTheLayeredPane() {
        signUPPanelPartOfTheLayeredPane.add(signUpPanel);
        layeredPaneCenter.add(signUPPanelPartOfTheLayeredPane);
    }


//    private void centreWindowAndDefineItsDimensions(Window frame)
//    {
//        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
//        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
//        frame.setLocation(x, y);
//    }

    /**
     * adding attributes to each of the buttons in the west panel of the login frame.
     */
    public void setButtonAttributes() {
//        Component[] components = panelWestLoginFrame.getComponents();
//        for (Component component : components)
//        {
//            if (component instanceof JButton)
//            {
//                ComponentAttributes.setComponentsAttributes(component,
//                        new Font("Narkisim", Font.BOLD, 30),
//                        new Dimension(80, 10));
//
//            }
//        }
        ComponentUtils.setComponentsAttributes(signUpButton, new Font("Narkisim", Font.BOLD, 20), new Dimension(80, 10));
        ComponentUtils.setComponentsAttributes(loginButton, new Font("Narkisim", Font.BOLD, 20), new Dimension(80, 10));
    }

    /**
     * adding buttons actionListeners
     */
    private void setButtonsActionListeners() {
        //Login button
        loginButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(loginPanelPartOfTheLayeredPane);
            layeredPaneCenter.revalidate();
        });

        //SignUp button
        signUpButton.addActionListener(e ->
        {
            layeredPaneCenter.removeAll();
            layeredPaneCenter.add(signUPPanelPartOfTheLayeredPane);
            layeredPaneCenter.revalidate();
        });
    }

    /**
     * @param fullName users input for full name
     * @return checks if the full name consists of letters and spaces.
     */
    private boolean validateUsersFullName(String fullName) {
        char[] chars = fullName.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }

        return true;
    }

    /**
     * LoginPanel class which displays the login part
     */
    private class LoginPanel extends JPanel {
        private JLabel labelLoginTitle;
        private JLabel labelFullNameLoginPanel;
        private JLabel labelPasswordLoginPanel;
        private JTextField textFieldFullNameLoginPanel;
        private JTextField textFieldPasswordLoginPanel;
        private JButton buttonOkLoginPanel;
        private JPanel panelLoginNorth;
        private JPanel panelLoginCenter;
        private JPanel panelLoginSouth;
        private List<BiConsumer<String, String>> userAndPasswordInvokedListeners = new LinkedList<>();

        private LoginPanel() {
            LoginPanelInit();
            LoginPanelStart();
        }

        public void registerUserAndPasswordInvokeListener(BiConsumer<String, String> listener) {
            userAndPasswordInvokedListeners.add(listener);
        }

        private void notifyAllListeners(String username, String password) {
            userAndPasswordInvokedListeners.forEach(listener -> listener.accept(username, password));
        }

        private void LoginPanelInit() {
            labelLoginTitle = new JLabel("Login");
            labelFullNameLoginPanel = new JLabel("Full Name:");
            labelPasswordLoginPanel = new JLabel("Password:");
            textFieldFullNameLoginPanel = new JTextField();
            textFieldPasswordLoginPanel = new JTextField();
            buttonOkLoginPanel = new JButton("Ok");
            panelLoginNorth = new JPanel();
            panelLoginCenter = new JPanel();
            panelLoginSouth = new JPanel();
        }


        private void LoginPanelStart() {
            this.setPreferredSize(new Dimension(500, 300));
            setLoginPanelComponents();
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(30);
            this.setLayout(borderLayout);
            //LoginPanelNorth
            panelLoginNorth.add(labelLoginTitle);
            this.add(panelLoginNorth, BorderLayout.NORTH);
            //LoginPanelCenter
            GridLayout gridLayout = new GridLayout(2, 2, 20, 10);
            gridLayout.setVgap(25);
            panelLoginCenter.setLayout(gridLayout);
            panelLoginCenter.add((labelFullNameLoginPanel));
            panelLoginCenter.add(textFieldFullNameLoginPanel);
            panelLoginCenter.add((labelPasswordLoginPanel));
            panelLoginCenter.add(textFieldPasswordLoginPanel);
            this.add(panelLoginCenter, BorderLayout.CENTER);
            //LoginPanelSouth
            ComponentUtils.setComponentsAttributes(buttonOkLoginPanel, new Font("Narkisim", Font.BOLD, 20), new Dimension(70, 30));
            panelLoginSouth.add(buttonOkLoginPanel);
            this.add(panelLoginSouth, BorderLayout.SOUTH);


            /**
             *
             *calling the getUser method of the view model for
             */
            buttonOkLoginPanel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (textFieldFullNameLoginPanel.getText() != null && textFieldPasswordLoginPanel.getText() != null) {

                        if (validateUsersFullName(textFieldFullNameLoginPanel.getText())) {

                            //((ViewManager) viewManager).getViewModel().validateUserExistence(textFieldFullNameLoginPanel.getText(), textFieldPasswordLoginPanel.getText());
                            notifyAllListeners(textFieldFullNameLoginPanel.getText(), textFieldPasswordLoginPanel.getText());
                        } else {

                            labelInvalidDescription.setText(HandlingMessage.INVALID_FULL_NAME.toString());
                        }
                    } else {
                        labelInvalidDescription.setText(HandlingMessage.EMPTY_FIELDS.toString());
                    }
                }
            });
        }

        private void setLoginPanelComponents() {
            List<JComponent> componentsList = new ArrayList<>();

            Font font = new Font("Narkisim", Font.BOLD, 20);
            Dimension dimension = new Dimension(new Dimension(0, 0));

            ComponentUtils.setComponentsAttributes(labelLoginTitle, new Font("Narkisim", Font.BOLD, 40), new Dimension(150, 70));

            componentsList.add(labelFullNameLoginPanel);
            componentsList.add(labelPasswordLoginPanel);
            componentsList.add(textFieldFullNameLoginPanel);
            componentsList.add(textFieldPasswordLoginPanel);

            for (JComponent component : componentsList) {
                ComponentUtils.setComponentsAttributes(component, font, dimension);
            }
    }

    private class SignUpPanel extends JPanel {
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

        private SignUpPanel() {
            SignUpInit();
            SignUpStart();
        }

        private void SignUpInit() {
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

        private void SignUpStart() {
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
            ComponentUtils.setComponentsAttributes(buttonSubmitSignUp, new Font("Narkisim", Font.BOLD, 20), new Dimension(110, 30));
            panelSouthSignUp.add(buttonSubmitSignUp);
            this.add(panelSouthSignUp, BorderLayout.SOUTH);

            buttonSubmitSignUp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String fullName = textFieldFullNameSignUp.getText();
                    String password = textFieldPasswordSignUp.getText();
                    String confirmPassword = textFieldConfirmPasswordSignUp.getText();

                    if (!fullName.equals("") && !password.equals("") && !confirmPassword.equals("")) {
                        if (validateUsersFullName(fullName)) {
                            // Using BiPredicate Functional Interface inorder to check if the passwords match each other
                            if (confirmPasswords(password, confirmPassword, (firstPassword, secondPassword) -> firstPassword.equals(secondPassword))) {
                                //Here going to add a new user to the users table.
                                ((ViewManager) viewManager).getViewModel().addNewUser(new User(fullName, password));
                            } else {
                                labelInvalidDescription.setText(HandlingMessage.PASSWORDS_DO_NOT_MATCH.toString());
                            }
                        } else {
                            labelInvalidDescription.setText(HandlingMessage.INVALID_FULL_NAME.toString());
                        }
                    } else {
                        labelInvalidDescription.setText(HandlingMessage.EMPTY_FIELDS.toString());
                    }
                }
            });
        }

        private boolean confirmPasswords(String firstPassword, String secondPassword, BiPredicate<String, String> passwordsMatchTest) {
            return passwordsMatchTest.test(firstPassword, secondPassword);
        }

        private void setSignsUpPanelComponents() {
            List<JComponent> componentsList = new ArrayList<>();

            Font font = new Font("Narkisim", Font.BOLD, 20);
            Dimension dimension = new Dimension(new Dimension(250, 50));

            ComponentUtils.setComponentsAttributes(labelSignUpTitle, new Font("Narkisim", Font.BOLD, 35), new Dimension(200, 70));

            componentsList.add(labelFullNameSignUp);
            componentsList.add(labelPasswordSignUp);
            componentsList.add(labelConfirmPasswordSignUp);
            componentsList.add(textFieldFullNameSignUp);
            componentsList.add(textFieldPasswordSignUp);
            componentsList.add(textFieldConfirmPasswordSignUp);
            for (JComponent component : componentsList) {
                ComponentUtils.setComponentsAttributes(component, font, dimension);
            }
        }
    }
}
}