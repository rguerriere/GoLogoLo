package golo.transactions;

import jtps.jTPS_Transaction;
import static djf.AppPropertyType.APP_CLIPBOARD_FOOLPROOF_SETTINGS;
import golo.GoLogoLoApp;
import golo.data.goloData;
import golo.data.goloItemPrototype;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class CutItems_Transaction implements jTPS_Transaction {
    GoLogoLoApp app;
    goloItemPrototype itemToCut;
    int cutItemLocation;
    int cutComponentLocation;
    
    public CutItems_Transaction(GoLogoLoApp initApp, goloItemPrototype initItemToCut) {
        app = initApp;
        itemToCut = initItemToCut;
    }

    @Override
    public void doTransaction() {
        goloData data = (goloData)app.getDataComponent();
        cutItemLocation = data.removeItem(itemToCut);
        cutComponentLocation = data.getShapes().indexOf(itemToCut.getNode());
        data.removeComponent(itemToCut.getNode());
        data.clearSelected();
        data.removeHighlight(itemToCut.getNode());
        data.setSelectedComponent(null);
        app.getFoolproofModule().updateControls(APP_CLIPBOARD_FOOLPROOF_SETTINGS);
    }

    @Override
    public void undoTransaction() {
        goloData data = (goloData)app.getDataComponent();
        data.addItemAt(itemToCut, cutItemLocation);
        data.addComponentAt(itemToCut.getNode(), cutComponentLocation);
        data.selectItem(itemToCut);
        data.addHighlight(itemToCut.getNode());
        data.setSelectedComponent(itemToCut.getNode());
        app.getFoolproofModule().updateControls(APP_CLIPBOARD_FOOLPROOF_SETTINGS);
    }   
}