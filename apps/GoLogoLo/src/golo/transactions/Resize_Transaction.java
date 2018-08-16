/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golo.transactions;

import golo.GoLogoLoApp;
import golo.data.Drag;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
import jtps.jTPS_Transaction;

/**
 *
 * @author Richie
 */
public class Resize_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype itemToResize;
    String transaction;
    
    public Resize_Transaction(GoLogoLoApp initapp, goloItemPrototype initItem, String type) {
        itemToResize = initItem;
        app = initapp;
        transaction = type;
    }

    @Override
    public void doTransaction() {
        Node node = itemToResize.getNode();
        if(transaction.equals("size_up")){
            if(node instanceof Ellipse){
                 ((Drag)node).size((int)((Drag)node).getWidth() * 2 , (int)((Drag)node).getWidth() * 2);
            } 
        }
        else if(transaction.equals("size_down")){
            if(node instanceof Ellipse){
             ((Drag)node).size((int)((Drag)node).getWidth() / 2 , (int)((Drag)node).getWidth() / 2);
            }
        }
    }

    @Override
    public void undoTransaction() {
        Node node = itemToResize.getNode();
        if(transaction.equals("size_down")){
            if(node instanceof Ellipse){
                 ((Drag)node).size((int)((Drag)node).getWidth() * 2 , (int)((Drag)node).getWidth() * 2);
            } 
        }
        else if(transaction.equals("size_up")){
            if(node instanceof Ellipse){
             ((Drag)node).size((int)((Drag)node).getWidth() / 2 , (int)((Drag)node).getWidth() / 2);
            }
        }
    }
}