package golo.transactions;

import golo.data.Drag;
import golo.data.goloData;
import javafx.scene.Node;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;
/**
 *
 * @author Richie
 */
public class Drag_Transaction implements jTPS_Transaction {
    goloData data;
    Node original;
    Node After;
    Node AfterCloned;

    public Drag_Transaction(goloData initData, Node initOriginal) {
        data = initData;
        original = (Node)((Drag)initOriginal).clone();
    }

    @Override
    public void doTransaction() {
        if(After instanceof Text==false) 
            ((Drag)After).setPosandSize(((Drag)AfterCloned).getX(),((Drag)AfterCloned).getY(), ((Drag)AfterCloned).getWidth(), ((Drag)AfterCloned).getHeight());
        else
            ((Drag)After).setPosandSize(((Drag)AfterCloned).getX(),((Drag)AfterCloned).getY(), 0, 0);
    }

    @Override
    public void undoTransaction() {
         if(After instanceof Text==false) 
        ((Drag)After).setPosandSize(((Drag)original).getX(), ((Drag)original).getY(), ((Drag)original).getWidth(), ((Drag)original).getHeight());
        else
            ((Drag)After).setPosandSize(((Drag)original).getX(), ((Drag)original).getY(), 0, 0);
    }
    public void setAfter(Node after) {
        After = after;
        AfterCloned = ((Drag)after).clone();
    }
}