/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golo.transactions;

import golo.GoLogoLoApp;
import golo.data.DragRectangle;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_ITEMS_TABLE_VIEW;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;
import jtps.jTPS_Transaction;

/**
 *
 * @author Richie
 */
public class MoveUpItem_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype ItemMoved;
    
    public MoveUpItem_Transaction(  GoLogoLoApp initApp, 
                                    goloItemPrototype initItemMoved) {
        app = initApp;
        ItemMoved = initItemMoved;
    }

    @Override
    public void doTransaction() {
        goloData data = (goloData)app.getDataComponent();
        TableView tableView = (TableView) app.getGUIModule().getGUINode(GOLO_ITEMS_TABLE_VIEW);
        int index = data.getItemIndex(ItemMoved);
            if(index>0)
            {
                tableView.getItems().add(index-1, tableView.getItems().remove(index));
                tableView.getSelectionModel().clearAndSelect(index-1);
            }      
        app.getFoolproofModule().updateAll();
        if (ItemMoved.getNode() != null) {
	    index = data.getShapes().indexOf(ItemMoved.getNode());
            data.removeComponent(ItemMoved.getNode());
            if(ItemMoved.getNode() instanceof Rectangle){
                ((DragRectangle)ItemMoved.getNode()).deleteAnchors(data);
            }
            if(index == data.getShapes().size())
                data.addComponent(ItemMoved.getNode());
            else
                data.addComponentAt(ItemMoved.getNode(), index+1);
            
            if(ItemMoved.getNode() instanceof Rectangle){
                ((DragRectangle)ItemMoved.getNode()).addAnchors(data);
            }
	}
    }

    @Override
    public void undoTransaction() {
        goloData data = (goloData)app.getDataComponent();
        TableView tableView = (TableView) app.getGUIModule().getGUINode(GOLO_ITEMS_TABLE_VIEW);
        int index = data.getItemIndex(ItemMoved);
        if(index<tableView.getItems().size()-1)
            {
                // swap items
                tableView.getItems().add(index+1, tableView.getItems().remove(index));
                // select item at new position
                tableView.getSelectionModel().clearAndSelect(index+1);
            }
        app.getFoolproofModule().updateAll();
        if (ItemMoved.getNode() != null) {
            index = data.getShapes().indexOf(ItemMoved.getNode());
            data.removeComponent(ItemMoved.getNode());
            if(ItemMoved.getNode() instanceof Rectangle){
                ((DragRectangle)ItemMoved.getNode()).deleteAnchors(data);
            }
            data.addComponentAt(ItemMoved.getNode(), index-1);
            if(ItemMoved.getNode() instanceof Rectangle){
                ((DragRectangle)ItemMoved.getNode()).addAnchors(data);
            }
        }
    }   
}
