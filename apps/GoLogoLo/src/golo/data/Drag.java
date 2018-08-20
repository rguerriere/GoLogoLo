package golo.data;

import javafx.scene.Node;

/**
 *
 * @author Richie
 */
public interface Drag {
    public static final String RECTANGLE = "RECTANGLE";
    public static final String ELLIPSE = "ELLIPSE";
    public static final String TRIANGLE = "TRIANGLE";
    public static final String IMAGE = "IMAGE";
    public static final String TEXT = "TEXT";
    
    public void start(int x, int y);
    public void drag(int x, int y);
    public void size(int x, int y);
    
    public double getX();
    public double getY();
    public double getWidth();
    public double getHeight();
    
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight);
    
    public String getShapeType();
    
    public Node clone();
    
    public goloItemPrototype getPrototype();
    
    public void setPrototype(goloItemPrototype initProto);
}
