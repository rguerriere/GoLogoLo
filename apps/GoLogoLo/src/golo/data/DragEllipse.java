package golo.data;

import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
/** 
 * @author Richie
 */
public class DragEllipse extends Ellipse implements Drag {
    double startX;
    double startY;
    goloItemPrototype attachedPrototype;
    
    public DragEllipse() {
	setRadiusX(0.0);
	setRadiusY(0.0);
        setCenterX(0.0);
	setCenterY(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
    }
    
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startX;
	double diffY = y - startY;
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startX = x;
	startY = y;
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - startX;
	double height = y - startY;
	double X = startX + (width);
	double Y = startY + (height);
	setCenterX(X);
	setCenterY(Y);
	setRadiusX(width);
	setRadiusY(height);	
	
    }
        
    @Override
    public double getX() {
	return getCenterX();
    }

    @Override
    public double getY() {
	return getCenterY();
    }

    @Override
    public double getWidth() {
	return getRadiusX();
    }

    @Override
    public double getHeight() {
	return getRadiusY();
    }
        
    @Override
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX);
	setCenterY(initY);
	setRadiusX(initWidth);
	setRadiusY(initHeight);
    }
    
    @Override
    public String getShapeType() {
	return ELLIPSE;
    }

    @Override
    public Node clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public goloItemPrototype getPrototype() {
        return attachedPrototype;
    }
    
    @Override
    public void setPrototype(goloItemPrototype initproto){
        attachedPrototype = initproto;   
    }

}
