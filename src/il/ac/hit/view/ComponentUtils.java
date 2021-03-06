package il.ac.hit.view;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;


/**
 * ComponentUtils is a final class because it has a private ctor , so there is no chance to extend from
 * this class.
 *  This class has different methods that are used in different classes more than couple of times.
 */
public final class ComponentUtils
{
    /** Private ctor to avoid of objects creation of this class. */
    private ComponentUtils() {}

    /**
     * This method sets the components attributes.
     *
     * @param component - is a type of JComponent because it gives the basis for polymorphism and
     *                   thus, it gives you the reuse if needed in the future.
     *                   For Example maybe in the future we'd like to set The Attributes for a JTextField.
     * @param font - indicates the font style of the component.
     * @param dimensions - indicates the dimensions of the component.
     */
    public static void setComponentsAttributes(Component component, Font font, Dimension dimensions) {
        /*
         * if the component is an instance of JLabel we cast it to it and set the text to be centralized
         * because only JLabel has an attribute like this.
         *
         * then we set the fond and the PreferredSize of the text.
         */
        if (component instanceof JLabel)
        {
            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
        }

        component.setFont(font);
        component.setPreferredSize(dimensions);
    }

    /**
     * This method centers a window that it receives.
     * @param frame - the given frame that are going to be centered.
     */
    public static void centralizeWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     * This method sets the action listener for different buttons, that are going to change
     * panels from the current panel to the destination panel on the layered pane that are given.
     *
     * @param layeredPane - the pane that we will display another panel on it.
     * @param button - the button that getting action listener.
     * @param destinationPanel - the panel to display now one the given layered pane.
     */
    public static void setActionListenersToChangePanelsOnLayeredPane(JLayeredPane layeredPane,JButton button, JPanel destinationPanel) {
        button.addActionListener(e -> {
            layeredPane.removeAll();
            layeredPane.add(destinationPanel);
            layeredPane.revalidate();
        });
    }

    public static void clearComponents(Component[] components) {
        /*
         * clearing all the components in the components Array.
         */
        for (Component component : components) {

            if (component instanceof JTextComponent) {
                ((JTextComponent) component).setText("");
            }
        }
    }
}