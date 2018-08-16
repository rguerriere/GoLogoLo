package golo.data;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Richie
 */
public class DragImage extends ImageView implements Drag{
    double startX;
    double startY;
    String file_path;
    goloItemPrototype attachedPrototype;
    
    public DragImage(String path) {
	setX(0.0);
	setY(0.0);	
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
        file_path=path;
        setImage(new Image(path));
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

    }
    
    @Override
    public void setPosandSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
    }
    @Override
    public String getShapeType() {
        return IMAGE;
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   public String getpath()
   {
       return file_path;
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
