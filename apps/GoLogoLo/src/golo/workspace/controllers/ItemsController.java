package golo.workspace.controllers;

import static djf.AppPropertyType.GENERAL_ERROR_CONTENT;
import static djf.AppPropertyType.GENERAL_ERROR_TITLE;
import djf.ui.dialogs.AppDialogsFacade;
import golo.GoLogoLoApp;
import golo.data.Drag;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import golo.workspace.dialogs.goloListItemDialog;
import golo.transactions.EditItem_Transaction;
import javafx.scene.text.Text;


/**
 * @author McKillaGorilla
 * @author Richie
 */
public class ItemsController {
    GoLogoLoApp app;
    goloListItemDialog itemDialog;
    
    public ItemsController(GoLogoLoApp initApp) {
        app = initApp;
        
        itemDialog = new goloListItemDialog(app);
    }
        
    public void processEditItems(goloItemPrototype initItemToEdit) 
    {

        if (initItemToEdit.getNode() instanceof Text==false) {
            // IF IT HAS A UNIQUE NAME AND COLOR
            // THEN CREATE A TRANSACTION FOR IT
            // AND ADD IT
            itemDialog.showEditDialog(initItemToEdit);
            goloItemPrototype editItem = itemDialog.getEditItem();
            if(editItem != null){
                goloData data = (goloData)app.getDataComponent();
                EditItem_Transaction transaction = new EditItem_Transaction(data,initItemToEdit,editItem.getName());
                app.processTransaction(transaction);
                app.getFileModule().markAsEdited(true);
            }
        }    
        // OTHERWISE TELL THE USER WHAT THEY
        // HAVE DONE WRONG
        else if(initItemToEdit.getNode() instanceof Text){
            goloData data = (goloData)app.getDataComponent();
            MouseController mouseController = new MouseController(app);
            mouseController.processMouseDoubleClick((int)((Drag)initItemToEdit.getNode()).getX(),(int)((Drag)initItemToEdit.getNode()).getY());
        }
        else 
        {
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), GENERAL_ERROR_TITLE, GENERAL_ERROR_CONTENT);
        }
    }
}
