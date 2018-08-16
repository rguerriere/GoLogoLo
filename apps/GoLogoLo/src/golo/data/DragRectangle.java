package golo.data;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 *
 * @author Richie
 */
public class DragRectangle extends Rectangle implements Drag{

    double startX;
    double startY;
    goloItemPrototype attachedPrototype;
    
    public DragRectangle() {
	setX(500.0);
	setY(500.0);
	setWidth(600.0);
	setHeight(300.0);
	setOpacity(1.0);
        setStroke(Color.BLACK);
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
	double newX = getX() + diffX;
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
	widthProperty().set(width);
	double height = y - getY();
	heightProperty().set(height);	
    }
    
    @Override
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
	widthProperty().set(initWidth);
	heightProperty().set(initHeight);
    }
    
    @Override
    public String getShapeType() {
	return RECTANGLE;
    }
    
   @Override
   public Node clone(){
        DragRectangle copy=new DragRectangle();
        copy.xProperty().set(this.xProperty().get());
	copy.yProperty().set(this.yProperty().get());
	copy.widthProperty().set(this.widthProperty().get());
	copy.heightProperty().set(this.heightProperty().get());	
        //add GRADIENT
        copy.setStroke(this.getStroke());
        copy.setStrokeWidth(this.getStrokeWidth());
        return copy;
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
