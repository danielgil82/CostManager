package il.ac.hit.view;

import javax.swing.*;
import java.awt.*;


/**
 *  ComponentUtils is a package friendly class, that has a static method that sets attributes to a given component
 */
public final class ComponentUtils
{
    /**
     * private ctor to avoid of objects creation of this class
     */
    private ComponentUtils() {}

    /**
     * this method sets the components attributes
     * @param component  is a type of JComponent because it gives the basis for polymorphism and
     *                   thus, it gives you the reuse if needed in the future.
     *                   For Example maybe in the future we'd like to set The Attributes for a JTextField.
     * @param font       indicates the font style of the component.
     * @param dimensions indicates the dimensions of the component.
     */
    public static void setComponentsAttributes(Component component, Font font, Dimension dimensions) {
        if (component instanceof JLabel)
        {
            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
        }

        component.setFont(font);
        component.setPreferredSize(dimensions);
    }

    /**
     * this method centers a window that it receives.
     * @param frame the given frame that are going to be centered
     */
    public static void centralizeWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}