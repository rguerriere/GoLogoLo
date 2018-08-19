package golo.data;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
/**
 *
 * @author Richie
 */
public class DragTriangle extends Polygon implements Drag{

    double startX;
    double startY;
    double DraggedX;
    double DraggedY;
    goloItemPrototype attachedPrototype;
    
    public DragTriangle() {
        getPoints().setAll(
            800d, 800d,
            200d, 800d,
            500d, 500d
        );
	setOpacity(1.0);
        setStroke(Color.BLACK);
	startX = 0.0;
	startY = 0.0;
        setStroke(Color.BLACK);
        setStrokeWidth(3);
        setFill(new RadialGradient(0,0,0,0,0,true,CycleMethod.NO_CYCLE,new Stop(0,Color.WHITE)));
    }
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double dragX = x - startX;
	double dragY = y - startY;
        for(int i=0;i<getPoints().size();i+=2){
            getPoints().set(i, dragX + getPoints().get(i));
            getPoints().set(i+1, dragY + getPoints().get(i+1));
        }
        DraggedX+= dragX;
        DraggedY+= dragY;
        startX = x;
	startY = y;
    }
    
    @Override
    public void size(int x, int y) {
        
    }
    
    @Override
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
	setLayoutX(initX);
	setLayoutY(initY);
    }
    
    @Override
    public String getShapeType() {
	return TRIANGLE;
    }
    
   @Override
   public Node clone(){
        DragTriangle copy=new DragTriangle();
        copy.getPoints().addAll(getPoints());
        copy.setFill(getFill());
        copy.setStroke(this.getStroke());
        copy.setStrokeWidth(this.getStrokeWidth());
        return (Node)copy;
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
        return DraggedX;
    }

    @Override
    public double getY() {
        return DraggedY;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }
    
}
