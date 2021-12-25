package il.ac.hit.view;

import javax.swing.*;
import java.awt.*;


/**
 *  ComponentAttributes is a package friendly class
 *  that has a static method that sets attributes to a given component
 *
 */

class ComponentAttributes
{
    public static void setComponentsAttributes(Component component, Font font, Dimension dimensions)
    {
        if (component instanceof JLabel)
        {
            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
        }

        component.setFont(font);
        component.setPreferredSize(dimensions);
    }
}
