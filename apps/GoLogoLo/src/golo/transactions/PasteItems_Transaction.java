package golo.transactions;

import jtps.jTPS_Transaction;
import golo.GoLogoLoApp;
import golo.data.Drag;
import golo.data.DragRectangle;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class PasteItems_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype itemToPaste;
    Node ComponentsToPaste;
    int pasteIndex;
    
    public PasteItems_Transaction(  GoLogoLoApp initApp, 
                                    goloItemPrototype initItemsToPaste,
                                    Node initComponentsToPaste,
                                    int initPasteIndex) {
        app = initApp;
        itemToPaste = initItemsToPaste;
        ComponentsToPaste = initComponentsToPaste;
        pasteIndex = initPasteIndex;
    }

    @Override
    public void doTransaction() {
        goloData data = (goloData)app.getDataComponent();
        int index = pasteIndex+1;
        if(index==0){
            data.addItemAt(itemToPaste,0);
            data.addComponent(ComponentsToPaste);
            ((Drag)ComponentsToPaste).setPosandSize( ((Drag)ComponentsToPaste).getX() + 20, ((Drag)ComponentsToPaste).getY() + 20, ((Drag)ComponentsToPaste).getWidth(), ((Drag)ComponentsToPaste).getHeight());
        }
        else{
            data.addItemAt(itemToPaste, index);
            data.addComponentAt(ComponentsToPaste, data.getShapes().size() - index);
            ((Drag)ComponentsToPaste).setPosandSize( ((Drag)ComponentsToPaste).getX() + 20, ((Drag)ComponentsToPaste).getY() + 20, ((Drag)ComponentsToPaste).getWidth(), ((Drag)ComponentsToPaste).getHeight());
        }
        if(ComponentsToPaste instanceof Rectangle){
            ((DragRectangle)ComponentsToPaste).addAnchors(data);
        }
        data.clearSelected();
        if(data.getSelectedComponent()!=null)
            data.removeHighlight(data.getSelectedComponent());
        data.selectItem(itemToPaste);
        data.setSelectedComponent(ComponentsToPaste);
        data.addHighlight(ComponentsToPaste);
    }

    @Override
    public void undoTransaction() {
        goloData data = (goloData)app.getDataComponent();
        data.removeItem(itemToPaste);
        data.removeComponent(ComponentsToPaste);
        if(ComponentsToPaste instanceof Rectangle){
            ((DragRectangle)ComponentsToPaste).deleteAnchors(data);
        }
        data.removeHighlight(ComponentsToPaste);
        
    }   
}