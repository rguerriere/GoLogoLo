package golo.transactions;

import golo.GoLogoLoApp;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_CENTER_X_SLIDER;
import static golo.goloPropertyType.GOLO_CENTER_Y_SLIDER;
import static golo.goloPropertyType.GOLO_CYCLE_METHOD_COMBOBOX;
import static golo.goloPropertyType.GOLO_FOCUS_ANGLE_SLIDER;
import static golo.goloPropertyType.GOLO_FOCUS_DISTANCE_SLIDER;
import static golo.goloPropertyType.GOLO_RADIUS_SLIDER;
import static golo.goloPropertyType.GOLO_STOP_ONE_COLORPICKER;
import static golo.goloPropertyType.GOLO_STOP_ZERO_COLORPICKER;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author Richie
 */
public class ColorG_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype borderItem;
    Paint fill;
    Paint Afterfill;
    
    public ColorG_Transaction(GoLogoLoApp initapp, goloItemPrototype initBorderItem) {
        app = initapp;
        borderItem = initBorderItem;
        fill = ((Shape)initBorderItem.getNode()).getFill();
    }

    @Override
    public void doTransaction() {
        Shape component = (Shape)borderItem.getNode();
        if(Afterfill == null){
            RadialGradient gradient = new RadialGradient(((Slider)app.getGUIModule().getGUINode(GOLO_FOCUS_ANGLE_SLIDER)).getValue()*2,
                ((Slider)app.getGUIModule().getGUINode(GOLO_FOCUS_DISTANCE_SLIDER)).getValue()*.1,
                ((Slider)app.getGUIModule().getGUINode(GOLO_CENTER_X_SLIDER)).getValue()*.1,
                ((Slider)app.getGUIModule().getGUINode(GOLO_CENTER_Y_SLIDER)).getValue()*.1,
                ((Slider)app.getGUIModule().getGUINode(GOLO_RADIUS_SLIDER)).getValue()*.1,
                true,
                (CycleMethod)((ComboBox)app.getGUIModule().getGUINode(GOLO_CYCLE_METHOD_COMBOBOX)).getValue(),
                new Stop(0, ((ColorPicker)app.getGUIModule().getGUINode(GOLO_STOP_ZERO_COLORPICKER)).getValue()),
                new Stop(1, ((ColorPicker)app.getGUIModule().getGUINode(GOLO_STOP_ONE_COLORPICKER)).getValue()));
                component.setFill(gradient);
                Afterfill = gradient;
        }
        else
            component.setFill(Afterfill);
    }

    @Override
    public void undoTransaction() {
        ((Shape)borderItem.getNode()).setFill(fill);
    }
}