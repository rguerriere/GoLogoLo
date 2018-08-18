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
	setRadiusX(90.0);
	setRadiusY(90.0);
        setCenterX(600.0);
	setCenterY(600.0);
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
    public void size(int width, int height) {
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
    public void setX(double X){
        setCenterX(X);
    }
    
    public void setY(double Y){
        setCenterY(Y);
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
        DragEllipse cloney = new DragEllipse();
        cloney.setCenterX(getCenterX());
        cloney.setCenterY(getCenterY());
        cloney.setRadiusX(getRadiusX());
        cloney.setRadiusY(getRadiusY());
        cloney.setFill(getFill());
        return cloney;
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
