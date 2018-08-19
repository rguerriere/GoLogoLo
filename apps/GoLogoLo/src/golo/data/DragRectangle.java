package golo.data;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
/**
 *
 * @author Richie
 */
public class DragRectangle extends Rectangle implements Drag{

    double startX;
    double startY;
    goloItemPrototype attachedPrototype;
    ArrayList<Anchor> anchors;
    
    public DragRectangle() {
	setX(500.0);
	setY(500.0);
	setWidth(600.0);
	setHeight(300.0);
	setOpacity(1.0);
        setStroke(Color.BLACK);
	startX = 0.0;
	startY = 0.0;
        Anchor TopLeft = new Anchor(AnchorPosition.TL);
        Anchor TopRight = new Anchor(AnchorPosition.TR);
        Anchor BottomRight = new Anchor(AnchorPosition.BR);
        Anchor BottomLeft = new Anchor(AnchorPosition.BL);
        TopLeft.centerXProperty().bind(xProperty());
        TopLeft.centerYProperty().bind(yProperty());      
        TopRight.centerXProperty().bind(xProperty().add(widthProperty()));
        TopRight.centerYProperty().bind(yProperty());  
        BottomRight.centerXProperty().bind(xProperty().add(widthProperty()));
        BottomRight.centerYProperty().bind(yProperty().add(heightProperty()));
        BottomLeft.centerXProperty().bind(xProperty());
        BottomLeft.centerYProperty().bind(yProperty().add(heightProperty())); 
        TopLeft.setAttachedNode(this);
        TopRight.setAttachedNode(this);
        BottomRight.setAttachedNode(this);
        BottomLeft.setAttachedNode(this);
        anchors = new ArrayList<>();
        anchors.addAll(Arrays.asList(TopLeft,TopRight,BottomRight,BottomLeft));
        setStroke(Color.BLACK);
        setStrokeWidth(3);
        setFill(new RadialGradient(0,0,0,0,0,true,CycleMethod.NO_CYCLE,new Stop(0,Color.WHITE),new Stop(1,Color.WHITE)));
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
        copy.setX(getX());
	copy.setY(getY());
	copy.widthProperty().set(this.widthProperty().get());
	copy.heightProperty().set(this.heightProperty().get());	
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
    
    public void deleteAnchors(goloData data){
        data.getShapes().removeAll(anchors);
    }  
    
    public ArrayList<Anchor> getAnchors(){
        return anchors;
    } 
    
    public void addAnchors(goloData data){
        data.getShapes().addAll(data.getComponentIndex(this) + 1, anchors);
    }  
    
}
