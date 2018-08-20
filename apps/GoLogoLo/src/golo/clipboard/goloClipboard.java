package golo.clipboard;

import djf.components.AppClipboardComponent;
import golo.GoLogoLoApp;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import golo.transactions.CutItems_Transaction;
import golo.transactions.PasteItems_Transaction;
import javafx.scene.Node;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class goloClipboard implements AppClipboardComponent {
    GoLogoLoApp app;
    
    goloItemPrototype clipboardCutItem;
    goloItemPrototype clipboardCopiedItem;
    
    Node clipboardCopiedComponent;
    
    public goloClipboard(GoLogoLoApp initApp) {
        app = initApp;
        clipboardCutItem = null;
        clipboardCopiedItem = null;
    }
    
    @Override
    public void cut() {
        goloData data = (goloData)app.getDataComponent();
        if (data.isItemSelected() || data.areItemsSelected()) {
            clipboardCutItem = data.getSelectedItem(); 
            clipboardCopiedItem = null;
            CutItems_Transaction transaction = new CutItems_Transaction((GoLogoLoApp)app, clipboardCutItem);
            app.processTransaction(transaction);
            app.getFileModule().markAsEdited(true);
        }
    }

    @Override
    public void copy() {
        goloData data = (goloData)app.getDataComponent();
        if (data.isItemSelected() || data.areItemsSelected()) {
            goloItemPrototype tempItem = data.getSelectedItem();
            Node tempComponent = tempItem.getNode();
            copyToCopiedClipboard(tempItem,tempComponent);
        }

    }
    
    private void copyToCutClipboard(goloItemPrototype itemToCopy) {
        if(itemToCopy!=null){
            clipboardCutItem = (goloItemPrototype)itemToCopy.clone();
            clipboardCopiedItem = null;        
            app.getFoolproofModule().updateAll(); 
        }
    }
    
    private void copyToCopiedClipboard(goloItemPrototype itemToCopy, Node ComponentToCopy) {
        goloData data = (goloData)app.getDataComponent();
        clipboardCutItem = null;
        clipboardCopiedItem = (goloItemPrototype)itemToCopy.clone();
        clipboardCopiedComponent = clipboardCopiedItem.getNode();
        app.getFoolproofModule().updateAll();
    }

     @Override
    public void paste() {
        goloData data = (goloData)app.getDataComponent();
        if (data.isItemSelected()) {
            int selectedIndex = data.getItemIndex(data.getSelectedItem());
            if (clipboardCutItem != null){
                // NOW WE HAVE TO RE-COPY THE CUT ITEMS TO MAKE
                // SURE IF WE PASTE THEM AGAIN THEY ARE BRAND NEW OBJECTS
                copyToCutClipboard(clipboardCopiedItem);
                clipboardCutItem = (goloItemPrototype)clipboardCutItem.clone();
                PasteItems_Transaction transaction = new PasteItems_Transaction((GoLogoLoApp)app, clipboardCutItem, clipboardCutItem.getNode() , selectedIndex);
                app.processTransaction(transaction);
                app.getFileModule().markAsEdited(true);
            }
            else if (clipboardCopiedItem != null){
                PasteItems_Transaction transaction = new PasteItems_Transaction((GoLogoLoApp)app, clipboardCopiedItem,clipboardCopiedComponent, selectedIndex);
                app.processTransaction(transaction);

                // NOW WE HAVE TO RE-COPY THE COPIED ITEMS TO MAKE
                // SURE IF WE PASTE THEM AGAIN THEY ARE BRAND NEW OBJECTS
                copyToCopiedClipboard(clipboardCopiedItem,clipboardCopiedComponent);
                app.getFileModule().markAsEdited(true);
            }
        }
        else{
            if (clipboardCutItem != null){
                // NOW WE HAVE TO RE-COPY THE CUT ITEMS TO MAKE
                // SURE IF WE PASTE THEM AGAIN THEY ARE BRAND NEW OBJECTS
                copyToCutClipboard(clipboardCopiedItem);
                clipboardCutItem = (goloItemPrototype)clipboardCutItem.clone();
                PasteItems_Transaction transaction = new PasteItems_Transaction((GoLogoLoApp)app, clipboardCutItem, clipboardCutItem.getNode() , -1);
                app.processTransaction(transaction);
                app.getFileModule().markAsEdited(true);
            }
            else if (clipboardCopiedItem != null){
                PasteItems_Transaction transaction = new PasteItems_Transaction((GoLogoLoApp)app, clipboardCopiedItem,clipboardCopiedComponent, -1);
                app.processTransaction(transaction);

                // NOW WE HAVE TO RE-COPY THE COPIED ITEMS TO MAKE
                // SURE IF WE PASTE THEM AGAIN THEY ARE BRAND NEW OBJECTS
                copyToCopiedClipboard(clipboardCopiedItem,clipboardCopiedComponent);
                app.getFileModule().markAsEdited(true);
            }
        }
    }


    @Override
    public boolean hasSomethingToCut() {
        return ((goloData)app.getDataComponent()).isItemSelected()
                || ((goloData)app.getDataComponent()).areItemsSelected();
    }

    @Override
    public boolean hasSomethingToCopy() {
        return ((goloData)app.getDataComponent()).isItemSelected()
                || ((goloData)app.getDataComponent()).areItemsSelected();
    }

    public void setclipboardCutItem(goloItemPrototype proto){
       clipboardCutItem = proto;      
    }
    
     public void setclipboardCopiedItem(goloItemPrototype proto){
       clipboardCopiedItem = proto;      
    }
    
    @Override
    public boolean hasSomethingToPaste() {
        if (clipboardCutItem != null)
            return true;
        else if (clipboardCopiedItem != null)
            return true;
        else
            return false;
    }
}