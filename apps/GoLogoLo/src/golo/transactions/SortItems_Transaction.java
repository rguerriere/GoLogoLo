package golo.transactions;

import java.util.ArrayList;
import java.util.Comparator;
import javafx.scene.control.TableColumn.SortType;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_NAME_COLUMN;
import static golo.goloPropertyType.GOLO_ORDER_COLUMN;

/**
 * DISABLED UNTIL STAGE 3
 *
 * @author McKillaGorilla
 * @author Richie
 */


public class SortItems_Transaction implements jTPS_Transaction {

    goloData data;
    ArrayList<goloItemPrototype> oldListOrder;
    ArrayList<goloItemPrototype> newListOrder;
    String sortingCriteria;
    SortType sortType;
    Comparator sortComparator;

    public SortItems_Transaction(   goloData initData, 
                                    ArrayList<goloItemPrototype> initOldListOrder, 
                                    String initSortingCriteria,
                                    SortType initSortType) {
        data = initData;
        oldListOrder = initOldListOrder;
        sortingCriteria = initSortingCriteria;
        sortType = initSortType;
        sortComparator = new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                goloItemPrototype tD1 = (goloItemPrototype)o1;
                goloItemPrototype tD2 = (goloItemPrototype)o2;
                Comparable c1, c2;
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                if (sortingCriteria.equals(props.getProperty(GOLO_ORDER_COLUMN + "_TEXT"))) {
                    c1 = data.getItemIndex(tD1);
                    c2 = data.getItemIndex(tD2);
                }
                else if (sortingCriteria.equals(props.getProperty(GOLO_NAME_COLUMN + "_TEXT"))) {
                    c1 = tD1.getName();
                    c2 = tD2.getName();
                }
                else{
                    c1 = tD1.getType();
                    c2 = tD2.getType();
                }
                if (sortType == SortType.ASCENDING) {
                    return c1.compareTo(c2);
                }
                else {
                    return c2.compareTo(c1);
                }
            }
        };
    }

    @Override
    public void doTransaction() {
        data.sortItems(sortComparator);
        newListOrder = data.getCurrentItemsOrder();
    }

    @Override
    public void undoTransaction() {
        data.rearrangeItems(oldListOrder);
    }   

}