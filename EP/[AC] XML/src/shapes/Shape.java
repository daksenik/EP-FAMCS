package shapes;

import java.awt.*;

/**
 * Created by daksenik on 01.05.2016.
 */
public interface Shape {
    void draw(Graphics g);
    double measure();
    String toString();
    String getParameterString();
    void setParameterString(String s);
}
