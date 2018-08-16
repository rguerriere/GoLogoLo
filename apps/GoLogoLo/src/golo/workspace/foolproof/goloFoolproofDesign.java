package golo.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import golo.GoLogoLoApp;
import golo.data.DragText;
import static golo.goloPropertyType.GOLO_EDIT_ITEM_BUTTON;
import static golo.goloPropertyType.GOLO_MOVEDOWN_ITEM_BUTTON;
import static golo.goloPropertyType.GOLO_MOVEUP_ITEM_BUTTON;
import golo.data.goloData;
import static golo.goloPropertyType.GOLO_BOLD_BUTTON;
import static golo.goloPropertyType.GOLO_BORDER_COLORPICKER;
import static golo.goloPropertyType.GOLO_BORDER_RADIUS_SLIDER;
import static golo.goloPropertyType.GOLO_BORDER_THICKNESS_SLIDER;
import static golo.goloPropertyType.GOLO_CENTER_X_SLIDER;
import static golo.goloPropertyType.GOLO_CENTER_Y_SLIDER;
import static golo.goloPropertyType.GOLO_CYCLE_METHOD_COMBOBOX;
import static golo.goloPropertyType.GOLO_FOCUS_ANGLE_SLIDER;
import static golo.goloPropertyType.GOLO_FOCUS_DISTANCE_SLIDER;
import static golo.goloPropertyType.GOLO_FONT_FAMILY_COMBOBOX;
import static golo.goloPropertyType.GOLO_FONT_SIZE_COMBOBOX;
import static golo.goloPropertyType.GOLO_ITALICS_BUTTON;
import static golo.goloPropertyType.GOLO_ITEMS_TABLE_VIEW;
import static golo.goloPropertyType.GOLO_LOWERCASE_BUTTON;
import static golo.goloPropertyType.GOLO_RADIUS_SLIDER;
import static golo.goloPropertyType.GOLO_REMOVE_COMPONENT_BUTTON;
import static golo.goloPropertyType.GOLO_STOP_ONE_COLORPICKER;
import static golo.goloPropertyType.GOLO_STOP_ZERO_COLORPICKER;
import static golo.goloPropertyType.GOLO_TEXT_COLOR_COLORPICKER;
import static golo.goloPropertyType.GOLO_UPPERCASE_BUTTON;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class goloFoolproofDesign implements FoolproofDesign {
    GoLogoLoApp app;
    
    public goloFoolproofDesign(GoLogoLoApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        AppGUIModule gui = app.getGUIModule();
       
        // CHECK AND SEE IF A TABLE ITEM IS SELECTED
        goloData data = (goloData)app.getDataComponent();
        boolean itemIsSelected = data.isItemSelected();
        boolean itemsAreSelected = data.areItemsSelected();
        Node selectedComponent = data.getSelectedComponent();
        gui.getGUINode(GOLO_EDIT_ITEM_BUTTON).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_MOVEUP_ITEM_BUTTON).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_MOVEDOWN_ITEM_BUTTON).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_BORDER_THICKNESS_SLIDER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_BORDER_COLORPICKER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_BORDER_RADIUS_SLIDER).setDisable(!itemIsSelected); 
        
        gui.getGUINode(GOLO_FOCUS_ANGLE_SLIDER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_FOCUS_DISTANCE_SLIDER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_CENTER_X_SLIDER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_CENTER_Y_SLIDER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_RADIUS_SLIDER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_CYCLE_METHOD_COMBOBOX).setDisable(!itemIsSelected); 
        gui.getGUINode(GOLO_STOP_ZERO_COLORPICKER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_STOP_ONE_COLORPICKER).setDisable(!itemIsSelected);
        gui.getGUINode(GOLO_REMOVE_COMPONENT_BUTTON).setDisable(!itemIsSelected);
        
        if(itemIsSelected == true || itemsAreSelected == true){
            gui.getGUINode(GOLO_MOVEUP_ITEM_BUTTON).setDisable(data.getItemIndex(data.getSelectedItem())==0);
            gui.getGUINode(GOLO_MOVEDOWN_ITEM_BUTTON).setDisable(data.getItemIndex(data.getSelectedItem())==data.getNumItems()-1);
            TableView tableView = (TableView) app.getGUIModule().getGUINode(GOLO_ITEMS_TABLE_VIEW);
            gui.getGUINode(GOLO_MOVEDOWN_ITEM_BUTTON).setDisable(tableView.getSelectionModel().getSelectedIndex()==tableView.getItems().size()-1);
            gui.getGUINode(GOLO_BOLD_BUTTON).setDisable(!(selectedComponent instanceof Text));
            gui.getGUINode(GOLO_ITALICS_BUTTON).setDisable(!(selectedComponent instanceof Text));        
            gui.getGUINode(GOLO_LOWERCASE_BUTTON).setDisable(!(selectedComponent instanceof Text));
            gui.getGUINode(GOLO_UPPERCASE_BUTTON).setDisable(!(selectedComponent instanceof Text));
            gui.getGUINode(GOLO_TEXT_COLOR_COLORPICKER).setDisable(!(selectedComponent instanceof Text));
            gui.getGUINode(GOLO_FONT_FAMILY_COMBOBOX).setDisable(!(selectedComponent instanceof Text));
            gui.getGUINode(GOLO_FONT_SIZE_COMBOBOX).setDisable(!(selectedComponent instanceof Text));
            
            gui.getGUINode(GOLO_FOCUS_ANGLE_SLIDER).setDisable((selectedComponent instanceof Text));
            gui.getGUINode(GOLO_FOCUS_DISTANCE_SLIDER).setDisable((selectedComponent instanceof Text));
            gui.getGUINode(GOLO_CENTER_X_SLIDER).setDisable((selectedComponent instanceof Text));
            gui.getGUINode(GOLO_CENTER_Y_SLIDER).setDisable((selectedComponent instanceof Text));
            gui.getGUINode(GOLO_RADIUS_SLIDER).setDisable((selectedComponent instanceof Text));
            gui.getGUINode(GOLO_CYCLE_METHOD_COMBOBOX).setDisable((selectedComponent instanceof Text)); 
            gui.getGUINode(GOLO_STOP_ZERO_COLORPICKER).setDisable((selectedComponent instanceof Text));
            gui.getGUINode(GOLO_STOP_ONE_COLORPICKER).setDisable((selectedComponent instanceof Text)); 
            gui.getGUINode(GOLO_BORDER_RADIUS_SLIDER).setDisable((selectedComponent instanceof Text));
            
            if(selectedComponent instanceof ImageView==false){
                ((Slider)gui.getGUINode(GOLO_BORDER_THICKNESS_SLIDER)).setValue(((Shape)selectedComponent).getStrokeWidth());
                ((ColorPicker)gui.getGUINode(GOLO_BORDER_COLORPICKER)).setValue((Color)((Shape)selectedComponent).getStroke());
            }
            if(selectedComponent instanceof Rectangle)
                ((Slider)gui.getGUINode(GOLO_BORDER_RADIUS_SLIDER)).setValue(((Rectangle)selectedComponent).getArcWidth()/5);
            
            if(selectedComponent instanceof Text){
                ((ColorPicker)gui.getGUINode(GOLO_TEXT_COLOR_COLORPICKER)).setValue((Color)((Text)selectedComponent).getFill());
                EventHandler<ActionEvent> handler = ((ComboBox)gui.getGUINode(GOLO_FONT_FAMILY_COMBOBOX)).getOnAction();
                ((ComboBox)gui.getGUINode(GOLO_FONT_FAMILY_COMBOBOX)).setOnAction(null);
                ((ComboBox)gui.getGUINode(GOLO_FONT_FAMILY_COMBOBOX)).getSelectionModel().select(((Text)selectedComponent).getFont().getFamily());
                ((ComboBox)gui.getGUINode(GOLO_FONT_FAMILY_COMBOBOX)).setOnAction(handler);
                EventHandler<ActionEvent> handler1 = ((ComboBox)gui.getGUINode(GOLO_FONT_SIZE_COMBOBOX)).getOnAction();
                ((ComboBox)gui.getGUINode(GOLO_FONT_SIZE_COMBOBOX)).setOnAction(null);
                ((ComboBox)gui.getGUINode(GOLO_FONT_SIZE_COMBOBOX)).getSelectionModel().select((int)((Text)selectedComponent).getFont().getSize());
                ((ComboBox)gui.getGUINode(GOLO_FONT_SIZE_COMBOBOX)).setOnAction(handler1);
            }
   
        }

    }
}