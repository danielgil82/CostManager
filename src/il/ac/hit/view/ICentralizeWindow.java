package il.ac.hit.view;

import java.awt.*;

/**
 * this interface should centralize a window, it has a default method.
 */
public interface ICentralizeWindow
{
    default void centralizeWindow(Window frame)
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
