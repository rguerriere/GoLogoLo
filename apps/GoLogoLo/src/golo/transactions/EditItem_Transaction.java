package golo.transactions;

import golo.data.Drag;
import jtps.jTPS_Transaction;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import javafx.scene.text.Text;
/**
 * @author Richie
 */
public class EditItem_Transaction implements jTPS_Transaction {
    
    goloData data;
    goloItemPrototype itemToEdit;
    String BeforeName;
    String EditName;
    
    public EditItem_Transaction(goloData initData,goloItemPrototype inititem,String name) {
        data = initData;
        itemToEdit = inititem;
        BeforeName = itemToEdit.getName();
        EditName = name;
    }

    @Override
    public void doTransaction() {
        itemToEdit.setName(EditName);
        if(itemToEdit.getNode() instanceof Text){
            ((Text)((Drag)itemToEdit.getNode())).setText(EditName);
        }
    }

    @Override
    public void undoTransaction() {
       itemToEdit.setName(BeforeName);
       if(itemToEdit.getNode() instanceof Text){
            ((Text)((Drag)itemToEdit.getNode())).setText(BeforeName);
        }
    }
}