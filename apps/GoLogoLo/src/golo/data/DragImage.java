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
    
    public DragImage(String initPath) {
	setX(500.0);
	setY(500.0);
        file_path=initPath;
        setImage(new Image(initPath));
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
	double newX = getX() + diffX;
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
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
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }
    
    
   public String getpath()
   {
       return file_path;
   }

    @Override
    public Node clone() {
        DragImage image = new DragImage(this.file_path);
        image.setX(this.xProperty().getValue());
        image.setY(this.yProperty().getValue());
        return image;
    }
    
    
    
}
