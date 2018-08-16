package golo.transactions;

import golo.GoLogoLoApp;
import golo.data.DragText;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_BORDER_COLORPICKER;
import static golo.goloPropertyType.GOLO_BORDER_RADIUS_SLIDER;
import static golo.goloPropertyType.GOLO_BORDER_THICKNESS_SLIDER;
import static golo.goloPropertyType.GOLO_TEXT_COLOR_COLORPICKER;
import java.awt.Stroke;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author Richie
 */
public class Border_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype borderItem;
    String type;
    Paint borderColor;
    int borderWidth;
    int Arc;
    Paint AfterborderColor;
    int AfterborderWidth;
    int AfterArc;
    
    public Border_Transaction(GoLogoLoApp initapp, goloItemPrototype initBorderItem, String transactionType) {
        app = initapp;
        borderItem = initBorderItem;
        type = transactionType;
        borderColor = ((Shape)borderItem.getNode()).getStroke();
        borderWidth = (int)((Shape)borderItem.getNode()).getStrokeWidth();
        if(borderItem.getNode() instanceof Rectangle){
            Arc = (int)((Rectangle)borderItem.getNode()).getArcHeight();
        }
        
    }

    @Override
    public void doTransaction() {
        Shape component = (Shape)borderItem.getNode();
        if(AfterborderColor==null && AfterborderWidth==0 && AfterArc==0){
            if(type.equals("thickness")){
                (component).setStrokeWidth(((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_THICKNESS_SLIDER)).getValue());
            }
            else if(type.equals("color")){
                (component).setStroke(((ColorPicker)app.getGUIModule().getGUINode(GOLO_BORDER_COLORPICKER)).getValue());
            }
            else if(type.equals("radius")){
                if(component instanceof Rectangle){
                    ((Rectangle)component).setArcWidth((int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue()*5);
                    ((Rectangle)component).setArcHeight((int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue()*5);
                }
            }
            AfterborderColor = ((ColorPicker)app.getGUIModule().getGUINode(GOLO_BORDER_COLORPICKER)).getValue();
            AfterborderWidth = (int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_THICKNESS_SLIDER)).getValue();
            AfterArc = (int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue()*5;
        }
        else{
            (component).setStrokeWidth(AfterborderWidth);
            (component).setStroke(AfterborderColor);
            if(component instanceof Rectangle){
                    ((Rectangle)component).setArcWidth(AfterArc);
                    ((Rectangle)component).setArcHeight(AfterArc);
            }
        }
    }

    @Override
    public void undoTransaction() {
        ((Shape)borderItem.getNode()).setStroke(borderColor);
        ((Shape)borderItem.getNode()).setStrokeWidth(borderWidth);
        if(borderItem.getNode() instanceof Rectangle){
            ((Rectangle)borderItem.getNode()).setArcHeight(Arc);
            ((Rectangle)borderItem.getNode()).setArcWidth(Arc);
        }
    }
}