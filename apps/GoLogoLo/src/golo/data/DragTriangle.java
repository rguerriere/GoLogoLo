package golo.data;

import static golo.data.Drag.RECTANGLE;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
/**
 *
 * @author Richie
 */
public class DragTriangle extends Polygon implements Drag{

    double startX;
    double startY;
    goloItemPrototype attachedPrototype;
    
    public DragTriangle() {
        getPoints().setAll(
            100d, 100d,
            150d, 50d,
            250d, 150d
        );
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
	//double newX = getX() + diffX;
	//double newY = getY() + diffY;
	//xProperty().set(newX);
	//yProperty().set(newY);
	startX = x;
	startY = y;
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
	//widthProperty().set(width);
	double height = y - getY();
	//heightProperty().set(height);	
    }
    
    @Override
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
	//xProperty().set(initX);
	//yProperty().set(initY);
	//widthProperty().set(initWidth);
	//heightProperty().set(initHeight);
    }
    
    @Override
    public String getShapeType() {
	return RECTANGLE;
    }
    
   @Override
   public Node clone(){
        DragRectangle copy=new DragRectangle();
       // copy.xProperty().set(this.xProperty().get());
	//copy.yProperty().set(this.yProperty().get());
	//copy.widthProperty().set(this.widthProperty().get());
	//copy.heightProperty().set(this.heightProperty().get());	
        copy.setFill(getFill());
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

    @Override
    public double getX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setX(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setY(double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
