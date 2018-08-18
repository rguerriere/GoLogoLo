package golo.data;

import static djf.AppPropertyType.GOLO_CANVAS_PANE;
import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import golo.GoLogoLoApp;
import static golo.data.ComponentState.SELECT_COMPONENT;
import static golo.goloPropertyType.GOLO_ITEMS_TABLE_VIEW;
import golo.workspace.goloWorkspace;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author McKillaGorilla
 * @author Richie
 */
public class goloData implements AppDataComponent {
    Pane canvas;  
    GoLogoLoApp app;
    
    ObservableList<goloItemPrototype> items;
    ObservableList<Node> components;
    Node selectedComponent;
    TableViewSelectionModel itemsSelectionModel;
    
    ComponentState state;
    Effect highlight;
    public static final String HIGHLIGHT_HEX = "#5df777";
    public static final Paint HIGHLIGHT_COLOR = Paint.valueOf(HIGHLIGHT_HEX);

    
    public goloData(GoLogoLoApp initApp) {
        app = initApp;
        
        // GET ALL THE THINGS WE'LL NEED TO MANIUPLATE THE TABLE
        TableView tableView = (TableView) app.getGUIModule().getGUINode(GOLO_ITEMS_TABLE_VIEW);
        items = tableView.getItems();
        itemsSelectionModel = tableView.getSelectionModel();
        itemsSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        goloWorkspace workspace = (goloWorkspace)app.getWorkspaceComponent();
        canvas = new Pane();   
        app.getGUIModule().addGUINode(GOLO_CANVAS_PANE, canvas);
        workspace.setCanvas(canvas);
        setComponents(canvas.getChildren());
        state = SELECT_COMPONENT;
        
        DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setSpread(15);
        dropShadowEffect.setRadius(10);
	dropShadowEffect.setColor((Color) HIGHLIGHT_COLOR);
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);	
	highlight = dropShadowEffect;
        
    } 
    
    
    public void setComponents(ObservableList<Node> initComponents) {
	components = initComponents;
    }
    
    
    public Iterator<goloItemPrototype> itemsIterator() {
        return this.items.iterator();
    }

    @Override
    public void reset() {
        AppGUIModule gui = app.getGUIModule();
        
        // CLEAR OUT THE ITEMS FROM THE TABLE
        TableView tableView = (TableView)gui.getGUINode(GOLO_ITEMS_TABLE_VIEW);
        items = tableView.getItems();
        items.clear();
        components.clear();
    }

    public boolean isItemSelected() {
        ObservableList<goloItemPrototype> selectedItems = this.getSelectedItems();
        return (selectedItems != null) && (selectedItems.size() == 1);
    }
    
    public boolean areItemsSelected() {
        ObservableList<goloItemPrototype> selectedItems = this.getSelectedItems();
        return (selectedItems != null) && (selectedItems.size() > 1);        
    }

    public boolean isValidToDoItemEdit(goloItemPrototype itemToEdit, String name) {
        return isValidNewToDoItem(name);
    }

    public boolean isValidNewToDoItem(String name) {
        if (name.trim().length() == 0)
            return false;
        return true;
    }

    public void addItem(goloItemPrototype itemToAdd) {
        items.add(itemToAdd);
    }

    public int removeItem(goloItemPrototype itemToAdd) {
        int index = items.indexOf(itemToAdd);
        items.remove(itemToAdd);
        return index;
    }
    
    public void editItem(goloItemPrototype Item1, goloItemPrototype Item2) 
    {
        //Not implemented yet.
    }
    
    public goloItemPrototype getSelectedItem() {
        if (!isItemSelected()) {
            return null;
        }
        return getSelectedItems().get(0);
    }
    public ObservableList<goloItemPrototype> getSelectedItems() {
        return (ObservableList<goloItemPrototype>)this.itemsSelectionModel.getSelectedItems();
    }

    public int getItemIndex(goloItemPrototype item) {
        return items.indexOf(item);
    }

    public void addItemAt(goloItemPrototype item, int itemIndex) {
        items.add(itemIndex, item);
    }

    public void moveItem(int oldIndex, int newIndex) {
        goloItemPrototype itemToMove = items.remove(oldIndex);
        items.add(newIndex, itemToMove);
    }

    public int getNumItems() {
        return items.size();
    }

    public void selectItem(goloItemPrototype itemToSelect) {
        this.itemsSelectionModel.select(itemToSelect);
    }

    public ArrayList<goloItemPrototype> getCurrentItemsOrder() {
        ArrayList<goloItemPrototype> orderedItems = new ArrayList();
        for (goloItemPrototype item : items) {
            orderedItems.add(item);
        }
        return orderedItems;
    }

    public void clearSelected() {
        this.itemsSelectionModel.clearSelection();
        
    }

    public void sortItems(Comparator sortComparator) {
        Collections.sort(items, sortComparator);
    }

    public void rearrangeItems(ArrayList<goloItemPrototype> oldListOrder) {
        items.clear();
        oldListOrder.forEach((item) -> {
            items.add(item);
        });
    }
    
    public void setState(ComponentState initState){
        state = initState;
    }
    public ComponentState getState(){
        return state;
    }
    
    public Node getSelectedComponent(){
        return selectedComponent;
    }

     public ObservableList<Node> getShapes() {
	return components;
    }
     
    public void addComponent(Node initComponent) {
	components.add(initComponent);
    }
    public void addComponentAt(Node item, int itemIndex) {
        components.add(itemIndex, item);
    }
    
    public void removeComponent(Node initComponent) {
	components.remove(initComponent);
    }
    public void removeComponentAt(int index) {
        components.remove(index);
    }
    
    public Node getFrontComponent(int x, int y) {
	for (int i = components.size() - 1; i >= 0; i--) {
	    Node Component = (Node)components.get(i);
	    if (Component.contains(x, y)) {
		return Component;
	    }
	}
	return null;
    }
    
    public int getFrontComponentIndex(int x, int y) {
	for (int i = components.size() - 1; i >= 0; i--) {
	    Node Component = (Node)components.get(i);
	    if (Component.contains(x, y)) {
		return i;
	    }
	}
        return 0;
    }
    
    public void removeHighlight(Node selectedComponent)
    {
        selectedComponent.setEffect(null);
    }
    
    public GoLogoLoApp getapp()
    {
        return app;
    }
    
    public void addHighlight(Node selectedComponent)
    {
        selectedComponent.setEffect(highlight);
    }
    
    public boolean checkState(ComponentState State) {
	return state == State;
    }
    
    public void setSelectedComponent(Node selectComponent) {
	selectedComponent = selectComponent;
    }
    
    
    public Node selectFrontComponent(int x, int y) {
	Node component = getFrontComponent(x, y);
 	goloWorkspace workspace = (goloWorkspace)app.getWorkspaceComponent();       
	if (component == selectedComponent)
        {
            if (component != null) 
            {
                ((Drag)component).start(x, y);
            } 
	    return component;
        }
	if (selectedComponent != null) 
        {
	    removeHighlight(selectedComponent);
            selectedComponent = null;
	}
	if (component != null) 
        {
	    addHighlight(component);
	    ((Drag)component).start(x, y);
            selectedComponent = component;
            clearSelected();
            this.itemsSelectionModel.select(((Drag)selectedComponent).getPrototype());
	}
	return component;
    }
    
}