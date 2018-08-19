package golo.transactions;

import golo.data.DragRectangle;
import jtps.jTPS_Transaction;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class RemoveItem_Transaction implements jTPS_Transaction {
    goloData data;
    goloItemPrototype itemToRemove;
    int posItem, posComponent;
    
    public RemoveItem_Transaction(goloData initData, goloItemPrototype initItem) {
        itemToRemove = initItem;
        data = initData;
        posItem = data.getItemIndex(initItem);

    }

    @Override
    public void doTransaction() {
        posComponent = data.getShapes().indexOf(itemToRemove.getNode());
        data.removeItem(itemToRemove);
        data.removeComponent(itemToRemove.getNode());
        if(itemToRemove.getNode() instanceof Rectangle){
            ((DragRectangle)itemToRemove.getNode()).deleteAnchors(data);
        }
    }

    @Override
    public void undoTransaction() {
        data.addItemAt(itemToRemove, posItem);
        data.addComponentAt(itemToRemove.getNode(), posComponent);
        if(itemToRemove.getNode() instanceof Rectangle){
            ((DragRectangle)itemToRemove.getNode()).addAnchors(data);
        }
        data.selectItem(itemToRemove);
    }
}