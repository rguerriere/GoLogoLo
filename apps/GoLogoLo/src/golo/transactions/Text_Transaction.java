package golo.transactions;

import golo.GoLogoLoApp;
import golo.data.DragText;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_BOLD_BUTTON;
import static golo.goloPropertyType.GOLO_FONT_FAMILY_COMBOBOX;
import static golo.goloPropertyType.GOLO_FONT_SIZE_COMBOBOX;
import static golo.goloPropertyType.GOLO_TEXT_COLOR_COLORPICKER;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;

/**
 *
 * @author Richie
 */
public class Text_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype textItem;
    String transaction_type;
    Font font;
    Font Afterfont;
    
    public Text_Transaction(GoLogoLoApp initapp, goloItemPrototype initTextItem, String transactionType) {
        app = initapp;
        textItem = initTextItem;
        transaction_type = transactionType;
        font = ((DragText)textItem.getNode()).getFont();
        Afterfont=null;
    }

    @Override
    public void doTransaction() {
        DragText component = (DragText)textItem.getNode();
        if(Afterfont==null){
            if(transaction_type.equals("bold"))
               component.toggleBold();
            else if(transaction_type.equals("italic"))
               component.toggleItalic();
            else if(transaction_type.equals("lowercase"))
               component.setText(component.getText().toLowerCase());
            else if(transaction_type.equals("uppercase"))
               component.setText(component.getText().toUpperCase());
            else if(transaction_type.equals("text_color"))
               component.setFill(((ColorPicker)app.getGUIModule().getGUINode(GOLO_TEXT_COLOR_COLORPICKER)).getValue());
            else if(transaction_type.equals("font_family"))
                component.setFamily((String) ((ComboBox)app.getGUIModule().getGUINode(GOLO_FONT_FAMILY_COMBOBOX)).getSelectionModel().getSelectedItem());
            else if(transaction_type.equals("font_size"))
               component.setSize((Integer) ((ComboBox)app.getGUIModule().getGUINode(GOLO_FONT_SIZE_COMBOBOX)).getSelectionModel().getSelectedItem());
            Afterfont = component.getFont();
        }
        else{
            component.setFont(Afterfont);  
        }
            
    }

    @Override
    public void undoTransaction() {
        ((DragText)textItem.getNode()).setFont(font);
    }
}