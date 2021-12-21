package il.ac.hit.view;

import il.ac.hit.model.User;
import il.ac.hit.auxiliary.Message;
import il.ac.hit.viewmodel.IViewModel;

import javax.swing.*;

public class ViewManager implements IView
{

    private IViewModel viewModel;
    private User user;

    private CostManagerLoginView loginView;
    private CostManagerAppView appView;

    public ViewManager()
    {
        loginView = new CostManagerLoginView(this);
        loginView.setVisible(true);
      //  AppView = new CostManagerAppView(user);
    }

    @Override
    public void setIViewModel(IViewModel vm)
    {
        viewModel = vm;
    }

    @Override
    public void init()
    {
        //   loginPageFrame = new LoginPageFrame();
    }

    @Override
    public void start()
    {
    }

    @Override
    public void displayMessage(Message message)
    {
        JOptionPane.showMessageDialog(null, message.getText());
    }

    void changeFrameFromLoginViewToAppView()
    {
        loginView.setVisible(false);
        appView = new CostManagerAppView(user);
        appView.setVisible(true);

    }

    public IViewModel getViewModel()
    {
        return viewModel;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
    // creating Login Page Frame
//    public class LoginPageFrame extends JFrame implements IView, IErrorAndExceptionsHandlingStrings
//    {
//        //private IViewModel viewModel;
//        private JFrame loginFrame;
//
//
//        private JPanel panelNorthLoginFrame;
//        private JPanel panelCenterLoginFrame;
//        private JPanel panelWestLoginFrame;
//        private JPanel panelSouthLoginFrame;
//        private JLabel labelCostManagerTitle;
//        private Button loginButton;
//        private Button signUpButton;
//        private JLabel labelInvalidLoginDescription;
//        private LoginPanel loginPanel;
//        private SignUpPanel signUpPanel;
//
//        public LoginPageFrame()
//        {
//            init();
//            start();
//        }
//
//        @Override
//        public void init()
//        {
//            loginFrame = new JFrame();
//            panelNorthLoginFrame = new JPanel();
//            panelCenterLoginFrame = new JPanel();
//            panelWestLoginFrame = new JPanel();
//            panelSouthLoginFrame = new JPanel();
//            labelCostManagerTitle = new JLabel("Cost Manager");
//            loginButton = new Button("Login");
//            signUpButton = new Button("Sign Up");
//            labelInvalidLoginDescription = new JLabel();
//        }
//
//        @Override
//        public void start()
//        {
//
//            panelNorthLoginFrame.setBackground(Color.PINK);
//            panelNorthLoginFrame.add(labelCostManagerTitle);
//
//            panelWestLoginFrame.setLayout(new BoxLayout(panelWestLoginFrame, BoxLayout.PAGE_AXIS));
//            panelWestLoginFrame.add(loginButton);
//            panelWestLoginFrame.add(Box.createRigidArea(new Dimension(0, 20)));
//            panelWestLoginFrame.add(signUpButton);
//
//            panelSouthLoginFrame.add(labelInvalidLoginDescription);
//
//
//
//            loginFrame.setLayout(new BorderLayout());
//            loginFrame.add(panelNorthLoginFrame, BorderLayout.NORTH);
//            loginFrame.add(panelCenterLoginFrame, BorderLayout.CENTER);
//            loginFrame.add(panelWestLoginFrame, BorderLayout.WEST);
//            loginFrame.add(panelSouthLoginFrame, BorderLayout.SOUTH);
//            loginFrame.setSize(600, 400);
//           // setButtonSize();
//
//            loginFrame.setVisible(true);
//        }
//
//        public void setButtonSize()
//        {
//            setButtonAttributes(loginButton,
//                    //new Font("Narkisim", Font.BOLD, 30),
//                    new Dimension(40, 40));
//
//            setButtonAttributes(signUpButton,
//                   // new Font("Narkisim", Font.BOLD, 30),
//                    new Dimension(40, 40));
//        }
//
//        public void setButtonAttributes(Component component,  Dimension dimensions)
//        {
//           // component.setFont(font);
//            component.setPreferredSize(dimensions);
//        }
//
//        @Override
//        public void displayMessage(Message message)
//        {
//
//        }
//
//
//        @Override
//        public void setIViewModel(IViewModel vm)
//        {
//            viewModel = vm;
//        }
//
//        private class LoginPanel extends JPanel
//        {
//            private JLabel labelLoginTitle;
//            private JLabel labelFullName;
//            private JLabel labelPassword;
//            private JTextField textFieldFullName;
//            private JTextField textFieldPassword;
//            private JButton buttonOk;
//            private JPanel panelLoginNorth;
//            private JPanel panelLoginCenter;
//            private JPanel panelLoginSouth;
//
//            public LoginPanel()
//            {
//                LoginInit();
//                LoginStart();
//            }
//
//            private void LoginInit()
//            {
//                labelLoginTitle = new JLabel("Login");
//                labelFullName = new JLabel("Full Name:");
//                labelPassword = new JLabel("Password:");
//                textFieldFullName = new JTextField();
//                textFieldPassword = new JTextField();
//                buttonOk = new JButton("Ok");
//                panelLoginNorth = new JPanel();
//                panelLoginCenter = new JPanel();
//                panelLoginSouth = new JPanel();
//            }
//
//            private void LoginStart()
//            {
//                setComponents();
//
//                buttonOk.addActionListener(new ActionListener()
//                {
//                    @Override
//                    public void actionPerformed(ActionEvent e)
//                    {
//
//                            if (textFieldFullName.getText() != null && textFieldPassword.getText() != null)
//                            {
//                                if(validateUsersFullName(textFieldFullName.getText()))
//                                {
//                                    viewModel.getUser(textFieldFullName.getText(), textFieldPassword.getText());
//
//                                    if (loggedInUser !=  null)
//                                    {
//
//                                    }
//                                    else
//                                    {
//                                        labelInvalidLoginDescription.setText(USER_DOES_NOT_EXISTS);
//                                    }
//                                }
//                            }
//                        }
//                });
//            }
//
//            /**
//             *
//             * @param fullName users input for full name
//             * @return checks if the full name consists of letters.
//             */
//            private boolean validateUsersFullName(String fullName)
//            {
//                return fullName.matches("^[a-zA-Z]*$");
//            }
//
//            private void setComponents()
//            {
//                setComponentsAttributes(labelLoginTitle,
//                        new Font("Narkisim", Font.BOLD, 50),
//                        new Dimension(500, 150));
//
//                setComponentsAttributes(labelFullName,
//                        new Font("Narkisim", Font.BOLD, 30),
//                        new Dimension(250, 100));
//
//                setComponentsAttributes(labelPassword,
//                        new Font("Narkisim", Font.BOLD, 30),
//                        new Dimension(250, 100));
//
//                setComponentsAttributes(textFieldFullName,
//                        new Font("Narkisim", Font.BOLD, 30),
//                        new Dimension(250, 100));
//
//                setComponentsAttributes(textFieldPassword,
//                        new Font("Narkisim", Font.BOLD, 30),
//                        new Dimension(250, 100));
//
//
//
//            }
//
//            /**
//             *
//             * @param component is a type of JComponent because it gives the basis for polymorphism and
//             *                    thus, it gives you the reuse if needed in the future.
//             *                    For Example maybe in the future we'd like to set The Attributes for a JTextField.
//             * @param font        indicates the font style of the component.
//             * @param dimensions  indicates the dimensions of the component.
//             */
//            private void setComponentsAttributes(JComponent component, Font font, Dimension dimensions)
//            {
//                if (component instanceof JLabel)
//                {
//                    ((JLabel)component).setHorizontalAlignment(SwingConstants.CENTER);
//                }
//
//                component.setFont(font);
//                component.setPreferredSize(dimensions);
//            }
//
////            private void setTextFieldDimensions(JComponent component, Font font, Dimension dimensions)
////            {
////                component.setFont(font);
////                component.setPreferredSize(dimensions);
////            }
//        }
//
//        private class SignUpPanel extends JFrame
//        {
//
//        }
//
//
//    }
}
