package golo.transactions;

import golo.GoLogoLoApp;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_BORDER_COLORPICKER;
import static golo.goloPropertyType.GOLO_BORDER_RADIUS_SLIDER;
import static golo.goloPropertyType.GOLO_BORDER_THICKNESS_SLIDER;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
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
                else if(component instanceof Polygon){
                    ((Polygon)component).setStrokeMiterLimit(10 - ((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue());
                    if(((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue() > 5){
                        ((Polygon)component).setStrokeLineCap(StrokeLineCap.ROUND);
                        ((Polygon)component).setStrokeLineJoin(StrokeLineJoin.ROUND);
                    }
                    else{
                        ((Polygon)component).setStrokeLineCap(StrokeLineCap.SQUARE);
                        ((Polygon)component).setStrokeLineJoin(StrokeLineJoin.MITER);
                    }
                }
            }
            AfterborderColor = ((ColorPicker)app.getGUIModule().getGUINode(GOLO_BORDER_COLORPICKER)).getValue();
            AfterborderWidth = (int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_THICKNESS_SLIDER)).getValue();
            if(component instanceof Rectangle){
                     AfterArc = (int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue()*5;
                }
                else if(component instanceof Polygon){
                     AfterArc = (int)((Slider)app.getGUIModule().getGUINode(GOLO_BORDER_RADIUS_SLIDER)).getValue();
                }
        }
        else{
            (component).setStrokeWidth(AfterborderWidth);
            (component).setStroke(AfterborderColor);
            if(component instanceof Rectangle){
                    ((Rectangle)component).setArcWidth(AfterArc);
                    ((Rectangle)component).setArcHeight(AfterArc);
            }
            else if(component instanceof Polygon){
                if(AfterArc > 5){
                        ((Polygon)component).setStrokeLineCap(StrokeLineCap.ROUND);
                        ((Polygon)component).setStrokeLineJoin(StrokeLineJoin.ROUND);
                }
                else{
                    ((Polygon)component).setStrokeLineCap(StrokeLineCap.SQUARE);
                    ((Polygon)component).setStrokeLineJoin(StrokeLineJoin.MITER);
                }
                ((Polygon)component).setStrokeMiterLimit(10 - AfterArc);
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
        if(borderItem.getNode() instanceof Polygon){
            ((Polygon)borderItem.getNode()).setStrokeMiterLimit(10 - Arc);
            if(AfterArc > 5){
                ((Polygon)borderItem.getNode()).setStrokeLineCap(StrokeLineCap.ROUND);
                ((Polygon)borderItem.getNode()).setStrokeLineJoin(StrokeLineJoin.ROUND);
            }
            else{
                ((Polygon)borderItem.getNode()).setStrokeLineCap(StrokeLineCap.SQUARE);
                ((Polygon)borderItem.getNode()).setStrokeLineJoin(StrokeLineJoin.MITER);
            }
        }
    }
}