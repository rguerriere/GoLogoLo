package golo.data;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Richie
 */
public class DragText extends Text  implements Drag{
    double startX;
    double startY;
    
    boolean bold;
    boolean italic;
    
    String TextIn;
    
    goloItemPrototype attachedPrototype;
    
    public DragText() 
    {
	setX(300);
	setY(300.0);	
        
	setOpacity(1.0);
        
	startX = 0.0;
	startY = 0.0;
        
        bold=false;
        italic=false;      
    }
    
    public DragText(String text) {
	setX(300.0);
	setY(300.0);	
        
	setOpacity(1.0);
        
	startX = 0.0;
	startY = 0.0;
        
        TextIn=text;
        setText(text);
    }
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startX ;
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
        return TEXT;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    public Node clone(){
        DragText copy=new DragText();
        copy.setText(this.getText());
        copy.xProperty().set(this.xProperty().get());
	copy.yProperty().set(this.yProperty().get());
        copy.setFont(this.getFont());
        copy.setFill(this.getFill());
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
    
    public boolean isBold()
    {
        return bold;
    }
    
    public boolean isItalic()
    {
        return italic;
    }
    
    public void toggleBold(){
        this.setFont(Font.font(this.getFont().getFamily(), bold ? FontWeight.NORMAL : FontWeight.BOLD , !italic ? FontPosture.REGULAR : FontPosture.ITALIC, this.getFont().getSize()));
        bold = !bold;   
    }
    
    public void toggleItalic(){
        this.setFont(Font.font(this.getFont().getFamily(), !bold ? FontWeight.NORMAL : FontWeight.BOLD , italic ? FontPosture.REGULAR : FontPosture.ITALIC, this.getFont().getSize()));
        italic = !italic;   
    }
    public void setFamily(String family){
        this.setFont(Font.font(family, !bold ? FontWeight.NORMAL : FontWeight.BOLD , !italic ? FontPosture.REGULAR : FontPosture.ITALIC, this.getFont().getSize())); 
    }
    public void setSize(Integer value){
        this.setFont(Font.font(this.getFont().getFamily(), !bold ? FontWeight.NORMAL : FontWeight.BOLD , !italic ? FontPosture.REGULAR : FontPosture.ITALIC, value)); 
    }

}
