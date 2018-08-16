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
	double X = startX + (width / 2);
	double Y = startY + (height / 2);
	setCenterX(X);
	setCenterY(Y);
	setRadiusX(width / 2);
	setRadiusY(height / 2);	
	
    }
        
    @Override
    public double getX() {
	return getCenterX() - getRadiusX();
    }

    @Override
    public double getY() {
	return getCenterY() - getRadiusY();
    }

    @Override
    public double getWidth() {
	return getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
	return getRadiusY() * 2;
    }
        
    @Override
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadiusX(initWidth/2);
	setRadiusY(initHeight/2);
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
