package golo.transactions;

import jtps.jTPS_Transaction;
import golo.data.goloData;
import golo.data.goloItemPrototype;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class AddItem_Transaction implements jTPS_Transaction {
    goloData data;
    goloItemPrototype itemToAdd;
    
    public AddItem_Transaction(goloData initData, goloItemPrototype initNewItem) {
        data = initData;
        itemToAdd = initNewItem;
    }

    @Override
    public void doTransaction() {
        
        data.addItemAt(itemToAdd,0);   
        data.addComponent(itemToAdd.getNode());
        data.selectItem(itemToAdd);
        if(data.getSelectedComponent()!=null){
        data.removeHighlight(data.getSelectedComponent());
       }
        data.setSelectedComponent(itemToAdd.getNode());
        data.addHighlight(itemToAdd.getNode());
    }

    @Override
    public void undoTransaction() {
        data.clearSelected();
        data.removeItem(itemToAdd);
        data.removeComponent(itemToAdd.getNode());
    }
}