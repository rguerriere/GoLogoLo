package golo.workspace.controllers;

import static djf.AppPropertyType.ADD_TEXT_CONTENT;
import static djf.AppPropertyType.ADD_TEXT_TITLE;
import golo.data.goloData;
import djf.AppTemplate;
import djf.ui.dialogs.AppDialogsFacade;
import golo.GoLogoLoApp;
import golo.data.DragRectangle;
import golo.data.DragText;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_BOLD_BUTTON;
import static golo.goloPropertyType.GOLO_FONT_FAMILY_COMBOBOX;
import golo.transactions.AddItem_Transaction;
import golo.transactions.Border_Transaction;
import golo.transactions.ColorG_Transaction;
import golo.transactions.MoveDownItem_Transaction;
import golo.transactions.MoveUpItem_Transaction;
import golo.transactions.RemoveItem_Transaction;
import golo.transactions.Text_Transaction;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * This class responds to interactions with other UI logo editing controls.
 * 
 * @author Richard McKenna
 * @author Richie
 * @version 1.0
 */
public class ComponentController 
{
    AppTemplate app;
    goloData dataManager;
    boolean bold;
    boolean italic;

    public ComponentController(AppTemplate initApp) {
	app = initApp;

    }
    
    public void processAddText(){
         dataManager = (goloData)app.getDataComponent();
         String text = AppDialogsFacade.showTextInputDialog(app.getGUIModule().getWindow(), ADD_TEXT_TITLE, ADD_TEXT_CONTENT);   
         if(!text.isEmpty()){
            DragText initText = new DragText(text);
            goloItemPrototype proto = new goloItemPrototype(text, "Text",initText);
            initText.setPrototype(proto);
            initText.setFont(Font.font("Arial", 48));
            AddItem_Transaction transaction = new AddItem_Transaction(dataManager, proto);
            app.processTransaction(transaction);
            app.getFileModule().markAsEdited(true);
         }
    }
    
    public void processAddRectangle(){
         dataManager = (goloData)app.getDataComponent();    
         DragRectangle initRectangle = new DragRectangle();
         goloItemPrototype proto = new goloItemPrototype("unnamed", "Rectangle",initRectangle);
         initRectangle.setPrototype(proto);       
         AddItem_Transaction transaction = new AddItem_Transaction(dataManager, proto);
         app.processTransaction(transaction);
         app.getFileModule().markAsEdited(true);
    }
    
    public void processRemoveComponent(){
        dataManager = (goloData)app.getDataComponent();
        RemoveItem_Transaction transaction = new RemoveItem_Transaction(dataManager, dataManager.getSelectedItem());
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processSelectComponent(){
       dataManager = (goloData)app.getDataComponent();   
       if(dataManager.getSelectedComponent()!=null){
        dataManager.removeHighlight(dataManager.getSelectedComponent());
       }
        dataManager.setSelectedComponent(dataManager.getSelectedItem().getNode());
        dataManager.addHighlight(dataManager.getSelectedComponent());
    }
    public void processMoveUpComponent(){
        MoveUpItem_Transaction transaction = new MoveUpItem_Transaction((GoLogoLoApp) app, dataManager.getSelectedItem());
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processMoveDownComponent(){
        MoveDownItem_Transaction transaction = new MoveDownItem_Transaction((GoLogoLoApp) app, dataManager.getSelectedItem());
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    
    public void processBold(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"bold");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processItalic(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"italic");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processLowercase(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"lowercase");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
        
    }
    public void processUppercase(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"uppercase");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processTextColor(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"text_color");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processFontFamily(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"font_family");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    public void processFontSize(){
        Text_Transaction transaction = new Text_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"font_size");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    
    public void processBorderThickness(){
        Border_Transaction transaction = new Border_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"thickness");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true); 
    }
    
    public void processBorderColor(){
        Border_Transaction transaction = new Border_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"color");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    
    public void processBorderRadius(){
        Border_Transaction transaction = new Border_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem(),"radius");
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    
    public void processGradient(){
        ColorG_Transaction transaction = new ColorG_Transaction((GoLogoLoApp)app, dataManager.getSelectedItem());
        app.processTransaction(transaction);
        app.getFileModule().markAsEdited(true);
    }
    
}
