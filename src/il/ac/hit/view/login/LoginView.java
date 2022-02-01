package il.ac.hit.view.login;

import il.ac.hit.model.User;
import il.ac.hit.view.ComponentUtils;
import javafx.scene.layout.BorderStrokeStyle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/** This class represents the loginView frame. */
public class LoginView extends JFrame {
    /** Swing components. */
    private JPanel panelNorthLoginFrame;
    private JPanel panelWestLoginFrame;
    private JPanel panelSouthLoginFrame;
    private JPanel panelContainer;
    private JPanel loginPanelPartOfTheLayeredPane;
    private JPanel signUPPanelPartOfTheLayeredPane;
    private JLabel labelCostManagerTitle;
    private JLayeredPane layeredPaneCenter;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel labelInvalidDescription;
    private FlowLayout flowLayoutContainerPanel;
    private GridLayout gridLayoutWestPanel;

    /**
     * Objects that represent the login an signUp panel
     */
    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;


    /**
     * Interface that has the methods that are going to be invoked on action from different buttons.
     * it simulates the observer pattern but not actually,
     * it's more like a mimic to it, because as you know in the Observer Design Pattern there are listeners and notifier
     * in our case our listener is the ViewManager and the notifiers are LoginPanel and SignUpPanel each of these classes
     * has its own button that "acts like" a notifier because when needed each of these buttons invoke a specific
     * method in this interface LoginView. So, it mimics the observer design pattern but the thing is that the listeners
     * don't register to the notifier.
     */
    private LoginUtils loginUtils;

    /**
     * Boolean that represent if the user credentials are valid or not.
     */
    private boolean areUserCredentialsValidInSignUpPanel = true;
    private boolean areUserCredentialsValidInLoginPanel = true;

    /**
     * Ctor that receives the loginUtils parameter.
     *
     * @param loginUtils an interface that has the methods that are going to be invoked by okButton and submitButton
     */
    public LoginView(LoginUtils loginUtils) {
        setLoginUtils(loginUtils);
        initLoginView();
        startLoginView();
    }

    /** Setter for loginUtils. */
    public void setLoginUtils(LoginUtils loginUtils) {
        this.loginUtils = loginUtils;
    }

    /**
     * This method sets the flag for LoginPanel.
     *
     * @param areUserCredentialsValidInLoginPanel - this boolean represents if the credentials of the
     *                                              user are valid in LoginPanel
     */
    public void setAreUserCredentialsValidInLoginPanel(boolean areUserCredentialsValidInLoginPanel) {
        this.areUserCredentialsValidInLoginPanel = areUserCredentialsValidInLoginPanel;
    }

    /**
     * This method sets the flag for signUpPanel.
     *
     * @param areUserCredentialsValidInSignUpPanel - this boolean represents if the credentials of
     *                                             the user are valid in SignUpPanel
     */
    public void setAreUserCredentialsValidInSignUpPanel(boolean areUserCredentialsValidInSignUpPanel) {
        this.areUserCredentialsValidInSignUpPanel = areUserCredentialsValidInSignUpPanel;
    }

    /**
     * This method returns the labelInvalidDescription.
     *
     * @return a label that shows the description.
     */
    public JLabel getLabelInvalidDescription() {
        return labelInvalidDescription;
    }

    /**
     * Initializing the data members of the "this" class.
     */
    public void initLoginView() {
        //initialize the swing components.
        gridLayoutWestPanel = new GridLayout(2, 1, 5, 30);
        layeredPaneCenter = new JLayeredPane();
        panelContainer = new JPanel();
        flowLayoutContainerPanel = new FlowLayout(4,30,130);
        panelContainer.setLayout(flowLayoutContainerPanel);
        loginPanelPartOfTheLayeredPane = new JPanel();
        signUPPanelPartOfTheLayeredPane = new JPanel();
        panelNorthLoginFrame = new JPanel();
        panelWestLoginFrame = new JPanel();
        panelSouthLoginFrame = new JPanel();
        labelCostManagerTitle = new JLabel("Cost Manager");
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");
        labelInvalidDescription = new JLabel();

        //initialize the objects that represent the login an signUp panel
        loginPanel = new LoginPanel();
        signUpPanel = new SignUpPanel();
    }

    /**
     * Adding components to the panels
     */
    public void startLoginView() {
        //North Panel
        panelNorthLoginFrame.setBackground(new Color(128,0,0));
        panelNorthLoginFrame.add(labelCostManagerTitle);
        ComponentUtils.setComponentsAttributes(labelCostManagerTitle, new Font("Narkisim", Font.BOLD, 40), new Dimension(700, 70));
        labelCostManagerTitle.setForeground(Color.WHITE);
        //West Panel
        panelWestLoginFrame.setLayout(gridLayoutWestPanel);
        panelWestLoginFrame.add(loginButton);
        panelWestLoginFrame.add(signUpButton);
        panelContainer.add(panelWestLoginFrame);
        //South Panel
        ComponentUtils.setComponentsAttributes(labelInvalidDescription, new Font("Narkisim", Font.BOLD, 20), new Dimension(700, 50));
        labelInvalidDescription.setForeground(Color.WHITE);
        panelSouthLoginFrame.add(labelInvalidDescription);
        panelSouthLoginFrame.setBackground(new Color(105,105,105));
        //Center Panel
        this.setLayout(new BorderLayout());

        //setting the layered pane with panels
        setLayeredPane();

        //Adding all panels to "this" object which is a JFrame
        this.add(panelNorthLoginFrame, BorderLayout.NORTH);
        this.add(panelContainer, BorderLayout.WEST);
        this.add(panelSouthLoginFrame, BorderLayout.SOUTH);

        // setting "this" attributes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        ComponentUtils.centralizeWindow(this);

        //setting the attributes
        setButtonAttributes();

        //setting the action listeners
        setButtonsActionListeners();

        this.setVisible(true);
    }

    /**
     * Setting the layered pane.
     */
    private void setLayeredPane() {
        layeredPaneCenter.setLayout(new CardLayout(0, 0));
        setLoginPanelPartOfTheLayeredPane();
        setSignUpPanelPartOfTheLayeredPane();
        this.add(layeredPaneCenter, BorderLayout.CENTER);
    }

    /**
     * Adding the loginPanel object into the layeredPane.
     */
    private void setLoginPanelPartOfTheLayeredPane() {
        loginPanelPartOfTheLayeredPane.add(loginPanel);
        layeredPaneCenter.add(loginPanelPartOfTheLayeredPane);
    }

    /**
     * Adding the signUpPanel object into the layeredPane.
     */
    private void setSignUpPanelPartOfTheLayeredPane() {
        signUPPanelPartOfTheLayeredPane.add(signUpPanel);
        layeredPaneCenter.add(signUPPanelPartOfTheLayeredPane);
    }

    /**
     * Adding attributes to each of the buttons in the west panel of the login frame.
     */
    public void setButtonAttributes() {
        ComponentUtils.setComponentsAttributes(signUpButton,
                new Font("Narkisim", Font.BOLD, 20),
                new Dimension(110, 30 ));
        signUpButton.setBorder(new StrokeBorder(new BasicStroke(3), new Color(128,0,0)));


        ComponentUtils.setComponentsAttributes(loginButton,
                new Font("Narkisim", Font.BOLD, 20),
                new Dimension(110, 30));
        loginButton.setBorder(new StrokeBorder(new BasicStroke(3),  new Color(128,0,0)));
    }

    /**
     * This method adds buttons actionListeners.
     */
    private void setButtonsActionListeners() {
        //loginButton
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(
                layeredPaneCenter, loginButton, loginPanelPartOfTheLayeredPane);

        //signUpButton
        ComponentUtils.setActionListenersToChangePanelsOnLayeredPane(
                layeredPaneCenter, signUpButton, signUPPanelPartOfTheLayeredPane);
    }

    /**
     * LoginPanel class represents the login part of the GUI,
     * and he is kind of a JPanel.
     */
    private class LoginPanel extends JPanel {
        /** Swing components. */
        private JLabel labelLoginTitle;
        private JLabel labelFullNameLoginPanel;
        private JLabel labelPasswordLoginPanel;
        private JTextField textFieldFullNameLoginPanel;
        private JTextField textFieldPasswordLoginPanel;
        private JButton buttonOkLoginPanel;
        private JPanel panelLoginNorth;
        private JPanel panelLoginCenter;
        private JPanel panelLoginSouth;

        /**
         * LoginPanel Constructor.
         */
        private LoginPanel() {
            LoginPanelInit();
            LoginPanelStart();
        }

        /**
         * This method initialize login's panel components.
         */
        private void LoginPanelInit() {
            //initializing the swing components
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

        /**
         * This method sets the gui components.
         */
        private void LoginPanelStart() {
            //setting different attributes to the loginPanelStart.
            this.setPreferredSize(new Dimension(500, 300));
            setLoginPanelComponentsAttributes();
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
            panelLoginCenter.add(labelFullNameLoginPanel);
            panelLoginCenter.add(textFieldFullNameLoginPanel);
            panelLoginCenter.add(labelPasswordLoginPanel);
            panelLoginCenter.add(textFieldPasswordLoginPanel);
            this.add(panelLoginCenter, BorderLayout.CENTER);


            //Here is a use of a static method of the ComponentUtils class
            ComponentUtils.setComponentsAttributes(buttonOkLoginPanel, new Font("Narkisim", Font.BOLD, 20), new Dimension(70, 30));

            //LoginPanelSouth
            panelLoginSouth.add(buttonOkLoginPanel);
            this.add(panelLoginSouth, BorderLayout.SOUTH);

            //this method setting the buttonOk action listener
            setButtonOkActionListener();
        }

        /**
         * Action listener for the buttonOk.
         */
        private void setButtonOkActionListener() {
            /*
             * first we take out the inputs out of the textFields,
             * then we clear the textFields,
             * and then send the inputs for validation in the viewModel part,
             * lastly, if and only if the credentials are valid we change the frames to the app itself.
             */
            buttonOkLoginPanel.addActionListener(e -> {
                String userFullName = textFieldFullNameLoginPanel.getText();
                String userPassword = textFieldPasswordLoginPanel.getText();

                if (!userFullName.equals("") || !userPassword.equals("")) {
                    ComponentUtils.clearComponents(panelLoginCenter.getComponents());
                }

                loginUtils.validateUserCredentials(userFullName, userPassword);

                if (areUserCredentialsValidInLoginPanel) {
                    loginUtils.validateUserExistence(userFullName, userPassword);
                }
            });
        }

        /**
         * This method sets the attributes of the login panel.
         */
        private void setLoginPanelComponentsAttributes() {
           //adding all the components to a list and traverse through it and init the attributes of each of the objects.
            List<JComponent> componentsList = new ArrayList<>();

            Font font = new Font("Narkisim", Font.BOLD, 20);

            Dimension dimension = new Dimension(new Dimension(0, 0));

            //Here is a use of a static method of the ComponentUtils class
            ComponentUtils.setComponentsAttributes(labelLoginTitle,
                    new Font("Narkisim", Font.BOLD, 40), new Dimension(150, 70));

            componentsList.add(labelFullNameLoginPanel);
            componentsList.add(labelPasswordLoginPanel);
            componentsList.add(textFieldFullNameLoginPanel);
            componentsList.add(textFieldPasswordLoginPanel);

            //Here is a use of a static method of the ComponentUtils class
            for (JComponent component : componentsList) {
                ComponentUtils.setComponentsAttributes(component, font, dimension);
            }
        }
    }


    /**
     * SignUpPanel class represents the SignUpPanel part of the GUI,
     * and he is kind of a JPanel.
     */
    private class SignUpPanel extends JPanel {
        /** Swing components. */
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

        /**
         * Ctor that call the init and start methods that build and set the attributes
         * of the SignUpPanel.
         */
        private SignUpPanel() {
            SignUpInit();
            SignUpStart();
        }

        /**
         * This method initializes the gui components.
         */
        private void SignUpInit() {
            //initializing the swing components
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

        /**
         * This method sets the gui components attributes.
         */
        private void SignUpStart() {
            // setting different attributes to the swing components
            this.setPreferredSize(new Dimension(500, 400));
            setSignsUpPanelComponents();
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(20);
            this.setLayout(borderLayout);

            //panel North
            panelNorthSignUp.add(labelSignUpTitle);
            this.add(panelNorthSignUp, BorderLayout.NORTH);

            GridLayout gridLayout = new GridLayout(3, 2, 20, 10);

            //panel Center
            panelCenterSignUp.setLayout(gridLayout);
            panelCenterSignUp.add((labelFullNameSignUp));
            panelCenterSignUp.add(textFieldFullNameSignUp);
            panelCenterSignUp.add((labelPasswordSignUp));
            panelCenterSignUp.add(textFieldPasswordSignUp);
            panelCenterSignUp.add((labelConfirmPasswordSignUp));
            panelCenterSignUp.add(textFieldConfirmPasswordSignUp);
            this.add(panelCenterSignUp, BorderLayout.CENTER);

            //setting the attributes of button submitSinUp.
            ComponentUtils.setComponentsAttributes(buttonSubmitSignUp,
                    new Font("Narkisim", Font.BOLD, 20), new Dimension(110, 30));

            //panel South
            panelSouthSignUp.add(buttonSubmitSignUp);
            this.add(panelSouthSignUp, BorderLayout.SOUTH);

            //Setting the action listener to the button
            setButtonSubmitActionListener();
        }

        /**
         * Using methods from the loginUtils interface, to validate users credentials
         * and if and only if the credentials are valid, we're going to add this new
         * user to the database.
         */
        private void setButtonSubmitActionListener() {
            /*
             * first we take out the inputs out of the textFields,
             * and then send the inputs for validation in the viewModel part,
             * then we clear the textFields,
             * lastly, if and only if the credentials are valid we add the user to the db.
             */
            buttonSubmitSignUp.addActionListener(e -> {
                String fullName = textFieldFullNameSignUp.getText();
                String password = textFieldPasswordSignUp.getText();
                String confirmPassword = textFieldConfirmPasswordSignUp.getText();

                loginUtils.validateUsersFullNameAndPasswords(fullName, password, confirmPassword);

                if (!fullName.equals("") || !password.equals("") || !confirmPassword.equals("")) {
                    ComponentUtils.clearComponents(panelCenterSignUp.getComponents());
                }

                if (areUserCredentialsValidInSignUpPanel) {
                    loginUtils.addNewUser(fullName, password);
                }
            });
        }

        /**
         * Setting the components of the SignUpPanel GUI part.
         */
        private void setSignsUpPanelComponents() {
            //adding all the components to a list and traverse through it and init the attributes of each of the objects.
            List<JComponent> componentsList = new ArrayList<>();

            Font font = new Font("Narkisim", Font.BOLD, 20);
            Dimension dimension = new Dimension(new Dimension(250, 50));

            //Here is a use of a static method of the ComponentUtils class
            ComponentUtils.setComponentsAttributes(labelSignUpTitle,
                    new Font("Narkisim", Font.BOLD, 35), new Dimension(200, 70));

            componentsList.add(labelFullNameSignUp);
            componentsList.add(labelPasswordSignUp);
            componentsList.add(labelConfirmPasswordSignUp);
            componentsList.add(textFieldFullNameSignUp);
            componentsList.add(textFieldPasswordSignUp);
            componentsList.add(textFieldConfirmPasswordSignUp);

            //Here is a use of a static method of the ComponentUtils class
            for (JComponent component : componentsList) {
                ComponentUtils.setComponentsAttributes(component, font, dimension);
            }
        }
    }
}