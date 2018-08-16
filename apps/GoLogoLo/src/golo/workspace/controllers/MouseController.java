package golo.workspace.controllers;

import static djf.AppPropertyType.EDIT_TEXT_CONTENT;
import static djf.AppPropertyType.EDIT_TEXT_TITLE;
import djf.AppTemplate;
import static golo.data.ComponentState.DRAG_COMPONENT;
import static golo.data.ComponentState.DRAG_NONE;
import static golo.data.ComponentState.SELECT_COMPONENT;
import golo.data.Drag;
import golo.data.goloData;
import golo.transactions.Drag_Transaction;
import golo.transactions.EditItem_Transaction;
import golo.workspace.goloWorkspace;
import java.util.Optional;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import properties_manager.PropertiesManager;

/**
 *
 * @author Richie
 */
public class MouseController {
    AppTemplate app;
    Drag_Transaction transaction;
    
    public MouseController (AppTemplate initApp) {
        app = initApp;
    }

    public void processMousePress(int x, int y) {
        goloData dataManager = (goloData) app.getDataComponent();
        if (dataManager.checkState(SELECT_COMPONENT)) {
            // SELECT THE TOP SHAPE       
            Node shape = dataManager.selectFrontComponent(x, y);
            Scene scene = app.getGUIModule().getPrimaryScene();
            // AND START DRAGGING IT
            
            
            if (shape != null) {
                transaction = new Drag_Transaction(dataManager, shape);
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(DRAG_COMPONENT);
                
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAG_NONE);
                dataManager.clearSelected();
            }
        }
        app.getFileModule().markAsEdited(true);
    }

    public void processMouseDragged(int x, int y) {
        goloData dataManager = (goloData) app.getDataComponent();
        if (dataManager.checkState(DRAG_COMPONENT)) 
        {
            Drag DragComponent = (Drag) dataManager.getSelectedComponent();
            DragComponent.drag(x, y);             
        }
    }

    public void processMouseRelease(int x, int y) {
        goloData dataManager = (goloData) app.getDataComponent();
        goloWorkspace workspace = (goloWorkspace) app.getWorkspaceComponent();
        if (dataManager.checkState(DRAG_COMPONENT)) {   
            dataManager.setState(SELECT_COMPONENT);
            Scene scene = app.getGUIModule().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            transaction.setAfter(dataManager.getSelectedComponent());
            app.processTransaction(transaction);
            app.getFileModule().markAsEdited(true);
        } else if (dataManager.checkState(DRAG_NONE)) {
            dataManager.setState(SELECT_COMPONENT);
            dataManager.clearSelected();
        }
    }
    
    public void processMouseDoubleClick(int x, int y){
         goloData dataManager = (goloData) app.getDataComponent();
         if(dataManager.getSelectedComponent() instanceof Text)
         {
             if (dataManager.checkState(SELECT_COMPONENT)) {
                Text shape = (Text)dataManager.selectFrontComponent(x, y);
                TextInputDialog dialog = new TextInputDialog(shape.getText());
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                dialog.setHeaderText(props.getProperty(EDIT_TEXT_TITLE));
                dialog.setContentText(props.getProperty(EDIT_TEXT_CONTENT));
                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                   shape.setText(result.get());
                   EditItem_Transaction transaction = new EditItem_Transaction(dataManager,((Drag)shape).getPrototype(),result.get());
                   app.processTransaction(transaction);
                   
                }
                
             }
         }
        
    }

}
