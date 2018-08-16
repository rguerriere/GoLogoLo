package golo.workspace.dialogs;

import static djf.AppPropertyType.MISSING_DATA_CONTENT;
import static djf.AppPropertyType.MISSING_DATA_TITLE;
import djf.modules.AppLanguageModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import golo.GoLogoLoApp;
import golo.data.goloData;
import static golo.goloPropertyType.GOLO_ITEM_DIALOG_CANCEL_BUTTON;
import static golo.goloPropertyType.GOLO_ITEM_DIALOG_EDIT_HEADER_TEXT;
import static golo.goloPropertyType.GOLO_ITEM_DIALOG_HEADER;
import static golo.goloPropertyType.GOLO_ITEM_DIALOG_OK_BUTTON;
import static golo.workspace.style.goloStyle.CLASS_GOLO_DIALOG_BUTTON;
import static golo.workspace.style.goloStyle.CLASS_GOLO_DIALOG_GRID;
import static golo.workspace.style.goloStyle.CLASS_GOLO_DIALOG_HEADER;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.GOLO_ITEM_DIALOG_NAME_PROMPT;
import static golo.workspace.style.goloStyle.CLASS_GOLO_DIALOG_PANE;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class goloListItemDialog extends Stage {
    
    GoLogoLoApp app;
    GridPane gridPane;
    
    Label headerLabel = new Label();    
    Label NameLabel = new Label();
    TextField NameTextField = new TextField();
    HBox okCancelPane = new HBox();
    Button okButton = new Button();
    Button cancelButton = new Button();

    goloItemPrototype itemToEdit;
    goloItemPrototype editItem;
    boolean editing;

    EventHandler cancelHandler;
    EventHandler addItemOkHandler;
    EventHandler editItemOkHandler;
    
    public goloListItemDialog(GoLogoLoApp initApp) {
        // KEEP THIS FOR WHEN THE WORK IS ENTERED
        app = initApp;

        // EVERYTHING GOES IN HERE
        gridPane = new GridPane();
        gridPane.getStyleClass().add(CLASS_GOLO_DIALOG_GRID);
        initDialog();

        // NOW PUT THE GRID IN THE SCENE AND THE SCENE IN THE DIALOG
        Scene scene = new Scene(gridPane);
        this.setScene(scene);
        
        // SETUP THE STYLESHEET
        app.getGUIModule().initStylesheet(this);
        scene.getStylesheets().add(CLASS_GOLO_DIALOG_GRID);                             
        
        // MAKE IT MODAL
        this.initOwner(app.getGUIModule().getWindow());
        this.initModality(Modality.APPLICATION_MODAL);
    }
       
    protected void initGridNode(Node node, Object nodeId, String styleClass, int col, int row, int colSpan, int rowSpan, boolean isLanguageDependent) {
        // GET THE LANGUAGE SETTINGS
        AppLanguageModule languageSettings = app.getLanguageModule();
        
        // TAKE CARE OF THE TEXT
        if (isLanguageDependent) {
            languageSettings.addLabeledControlProperty(nodeId + "_TEXT", ((Labeled)node).textProperty());
            ((Labeled)node).setTooltip(new Tooltip(""));
            languageSettings.addLabeledControlProperty(nodeId + "_TOOLTIP", ((Labeled)node).tooltipProperty().get().textProperty());
        }
        
        // PUT IT IN THE UI
        if (col >= 0)
            gridPane.add(node, col, row, colSpan, rowSpan);

        // SETUP IT'S STYLE SHEET
        node.getStyleClass().add(styleClass);
    }

    private void initDialog() {
        // THE NODES ABOVE GO DIRECTLY INSIDE THE GRID
        initGridNode(headerLabel,           GOLO_ITEM_DIALOG_HEADER,                CLASS_GOLO_DIALOG_HEADER,       0, 0, 3, 1, true);
        initGridNode(NameLabel,             GOLO_ITEM_DIALOG_NAME_PROMPT,           CLASS_GOLO_DIALOG_PANE,         0, 1, 3, 1, true);
        initGridNode(NameTextField,          null,                                  CLASS_GOLO_DIALOG_PANE,         3, 1, 4, 1, false);
        initGridNode(okCancelPane,           null,                                  CLASS_GOLO_DIALOG_PANE,         0, 3, 5, 1, false);

        okButton = new Button();
        cancelButton = new Button();
        app.getGUIModule().addGUINode(GOLO_ITEM_DIALOG_OK_BUTTON, okButton);
        app.getGUIModule().addGUINode(GOLO_ITEM_DIALOG_CANCEL_BUTTON, cancelButton);
        okButton.getStyleClass().add(CLASS_GOLO_DIALOG_BUTTON);
        cancelButton.getStyleClass().add(CLASS_GOLO_DIALOG_BUTTON);
        okCancelPane.getChildren().add(okButton);
        okCancelPane.getChildren().add(cancelButton);
        okCancelPane.setAlignment(Pos.CENTER);

        AppLanguageModule languageSettings = app.getLanguageModule();
        languageSettings.addLabeledControlProperty(GOLO_ITEM_DIALOG_OK_BUTTON + "_TEXT",    okButton.textProperty());
        languageSettings.addLabeledControlProperty(GOLO_ITEM_DIALOG_CANCEL_BUTTON + "_TEXT",    cancelButton.textProperty());
       
        // AND SETUP THE EVENT HANDLERS
        NameTextField.setOnAction(e->{
            processCompleteWork();
        });
        okButton.setOnAction(e->{
            processCompleteWork();
        });
        cancelButton.setOnAction(e->{
            editItem = null;
            this.hide();
        });   
    }
    

    
    private void processCompleteWork() {
        // GET THE SETTINGS
        String name = NameTextField.getText();
        
        // IF WE ARE EDITING
        goloData data = (goloData)app.getDataComponent();
        if (editing) {
            if (data.isValidToDoItemEdit(itemToEdit, name)) {
                editItem = new goloItemPrototype(name,"");
            }
            else {
                // OPEN MESSAGE DIALOG EXPLAINING WHAT WENT WRONG
                // @todo
                AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), MISSING_DATA_TITLE, MISSING_DATA_CONTENT);
                editItem = null;
            }
        }
        
        // CLOSE THE DIALOG
        this.hide();
    
    }

    public void showEditDialog(goloItemPrototype initItemToEdit) {
        // WE'LL NEED THIS FOR VALIDATION
        itemToEdit = initItemToEdit;
        
        // USE THE TEXT IN THE HEADER FOR EDIT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String headerText = props.getProperty(GOLO_ITEM_DIALOG_EDIT_HEADER_TEXT);
        headerLabel.setText(headerText);
        setTitle(headerText);
        
        // WE'LL ONLY PROCEED IF THERE IS A LINE TO EDIT
        editing = true;
        editItem = null;
                       
        // AND OPEN THE DIALOG
        showAndWait();
    }
    
    public goloItemPrototype getEditItem() {
        return editItem;
    }
}