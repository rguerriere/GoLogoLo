package golo.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.DISABLED;
import static djf.modules.AppGUIModule.ENABLED;
import static djf.modules.AppGUIModule.FOCUS_TRAVERSABLE;
import static djf.modules.AppGUIModule.HAS_KEY_HANDLER;
import djf.ui.AppNodesBuilder;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import golo.GoLogoLoApp;
import static golo.goloPropertyType.GOLO_FOOLPROOF_SETTINGS;
import static golo.workspace.style.goloStyle.CLASS_GOLO_BUTTON;
import static golo.workspace.style.goloStyle.CLASS_GOLO_COLUMN;
import static golo.workspace.style.goloStyle.CLASS_GOLO_TABLE;
import golo.workspace.controllers.ItemsController;
import golo.workspace.controllers.ItemsTableController;
import static golo.workspace.style.goloStyle.CLASS_GOLO_BOX;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import static golo.goloPropertyType.*;
import static golo.workspace.style.goloStyle.CLASS_GOLO_EDITTOOLBAR;
import static golo.workspace.style.goloStyle.CLASS_GOLO_LIST;
import static golo.workspace.style.goloStyle.CLASS_GOLO_LIST_BUTTONS;
import golo.workspace.foolproof.goloFoolproofDesign;
import golo.transactions.SortItems_Transaction;
import golo.workspace.controllers.MouseController;
import golo.workspace.controllers.ComponentController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.text.Font;
import javafx.util.Callback;
/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class goloWorkspace extends AppWorkspaceComponent {

    
    Pane canvas;
    MouseController mouseController;
    VBox goloPane;
    VBox editToolbar;
    
    public goloWorkspace(GoLogoLoApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();
        
        // 
        initFoolproofDesign();
    }
        
    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ComponentBuilder = app.getGUIModule().getNodesBuilder();
        
	// THIS HOLDS ALL THE CONTROLS IN THE WORKSPACE
	goloPane               = ComponentBuilder.buildVBox(GOLO_PANE,               null,           null,   CLASS_GOLO_BOX, HAS_KEY_HANDLER,             FOCUS_TRAVERSABLE,      ENABLED);
        VBox itemsPane              = ComponentBuilder.buildVBox(GOLO_ITEMS_PANE,                 goloPane,       null,   CLASS_GOLO_LIST, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        HBox itemButtonsPane        = ComponentBuilder.buildHBox(GOLO_ITEM_BUTTONS_PANE,          goloPane,          null,   CLASS_GOLO_LIST_BUTTONS, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        
        // THIS HAS THE ITEMS PANE COMPONENTS
        Button editItemButton        = ComponentBuilder.buildIconButton(GOLO_EDIT_ITEM_BUTTON,      itemButtonsPane,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        Button moveUpButton     = ComponentBuilder.buildIconButton(GOLO_MOVEUP_ITEM_BUTTON,   itemButtonsPane,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        Button moveDownButton     = ComponentBuilder.buildIconButton(GOLO_MOVEDOWN_ITEM_BUTTON,   itemButtonsPane,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        

        // AND NOW THE TABLE
        TableView<goloItemPrototype> itemsTable  = ComponentBuilder.buildTableView(GOLO_ITEMS_TABLE_VIEW,       itemsPane,          null,   CLASS_GOLO_TABLE, HAS_KEY_HANDLER,    FOCUS_TRAVERSABLE,  true);
        TableColumn orderColumn     = ComponentBuilder.buildTableColumn(  GOLO_ORDER_COLUMN,   itemsTable,         CLASS_GOLO_COLUMN);
        TableColumn nameColumn     = ComponentBuilder.buildTableColumn(  GOLO_NAME_COLUMN,   itemsTable,         CLASS_GOLO_COLUMN);
        TableColumn typeColumn     = ComponentBuilder.buildTableColumn(  GOLO_TYPE_COLUMN,   itemsTable,         CLASS_GOLO_COLUMN);

        // USED FOR AUTOMATIC ORDERING OF ORDER IN TABLE VIEW
        orderColumn.setCellFactory( new Callback<TableColumn, TableCell>()
        {
        @Override
        public TableCell call( TableColumn p)
        {
            return new TableCell()
            {
                @Override
                public void updateItem( Object item, boolean empty )
                {
                    super.updateItem( item, empty );
                    setText( empty ? null : getIndex() + 1 + "" );
                }
            };
        }
        });      
        itemsTable.prefHeightProperty().bind(itemsPane.heightProperty());
        VBox.setVgrow(itemsPane, Priority.ALWAYS);
        
        itemsTable.setPlaceholder(new Label(""));

        // SPECIFY THE TYPES FOR THE COLUMNS
        orderColumn.setCellValueFactory(new PropertyValueFactory<String, String>("order"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        
        orderColumn.setSortable(DISABLED);
        nameColumn.setSortable(DISABLED);
        typeColumn.setSortable(DISABLED); 
        editToolbar = ComponentBuilder.buildVBox(GOLO_PANE, null, null, CLASS_GOLO_BOX, HAS_KEY_HANDLER, FOCUS_TRAVERSABLE, ENABLED);
        editToolbar.setSpacing(5);
        
        // ROW1
        HBox row1 = ComponentBuilder.buildHBox(GOLO_ITEM_BUTTONS_PANE,         editToolbar,          null,   CLASS_GOLO_EDITTOOLBAR, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        Button addTextButton        = ComponentBuilder.buildIconButton(GOLO_TEXT_BUTTON,      row1,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  ENABLED);
        Button addImageButton        = ComponentBuilder.buildIconButton(GOLO_IMAGE_BUTTON,      row1,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  ENABLED);
        Button addRectangleButton        = ComponentBuilder.buildIconButton(GOLO_RECTANGLE_BUTTON,      row1,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  ENABLED);
        Button addCircleButton        = ComponentBuilder.buildIconButton(GOLO_CIRCLE_BUTTON,      row1,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  ENABLED);
        Button addTriangleButton        = ComponentBuilder.buildIconButton(GOLO_TRIANGLE_BUTTON,      row1,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  ENABLED);
        Button removeComponentButton        = ComponentBuilder.buildIconButton(GOLO_REMOVE_COMPONENT_BUTTON,      row1,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
         
        // ROW2
        VBox row2 = ComponentBuilder.buildVBox(GOLO_ITEM_BUTTONS_PANE,         editToolbar,          null,   CLASS_GOLO_EDITTOOLBAR, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        row2.setSpacing(5);
        HBox row2Box1 = new HBox();
        HBox row2Box2 = new HBox();
        row2.getChildren().addAll(row2Box1,row2Box2);
        ComboBox<String> FontFamily = ComponentBuilder.buildComboBox(GOLO_FONT_FAMILY_COMBOBOX,GOLO_EMPTY,GOLO_EMPTY,      row2Box1,    null,   null, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        FontFamily.getItems().addAll(FXCollections.observableList(Font.getFamilies()));
        FontFamily.getSelectionModel().select(2);
	ComboBox<Integer> FontSize = ComponentBuilder.buildComboBox(GOLO_FONT_SIZE_COMBOBOX,GOLO_EMPTY,GOLO_EMPTY,      row2Box1,    null,   null, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        FontSize.getItems().addAll(8,12,16,20,24,28,32,36,40,44,48,52,56,60,64,68,72,76,80);
        FontSize.getSelectionModel().select(10);
        
        Button BoldButton             = ComponentBuilder.buildIconButton(GOLO_BOLD_BUTTON,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        Button ItalicsButton          = ComponentBuilder.buildIconButton(GOLO_ITALICS_BUTTON,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        Button LowercaseButton        = ComponentBuilder.buildIconButton(GOLO_LOWERCASE_BUTTON,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        Button UppercaseButton        = ComponentBuilder.buildIconButton(GOLO_UPPERCASE_BUTTON,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        ColorPicker TextColor         = ComponentBuilder.buildColorPicker(GOLO_TEXT_COLOR_COLORPICKER,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        // ROW3
        VBox row3 = ComponentBuilder.buildVBox(GOLO_ITEM_BUTTONS_PANE,         editToolbar,          null,   CLASS_GOLO_EDITTOOLBAR, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        row3.setSpacing(5);
        Label borderThicknessLabel = new Label("Border Thickness: ");
        borderThicknessLabel.setTextFill(Color.WHITE);
        row3.getChildren().add(borderThicknessLabel);
        Slider borderThicknessSlider = ComponentBuilder.buildSlider(GOLO_BORDER_THICKNESS_SLIDER,      row3,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);

        Label borderColorLabel = new Label("Border Color: ");
        borderColorLabel.setTextFill(Color.WHITE);
        row3.getChildren().add(borderColorLabel);
        
        ColorPicker borderColor = ComponentBuilder.buildColorPicker(GOLO_BORDER_COLORPICKER,      row3,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);

        Label borderRadiusLabel = new Label("Border Radius: ");
        borderRadiusLabel.setTextFill(Color.WHITE);
        row3.getChildren().add(borderRadiusLabel);
        Slider borderRadiusSlider    = ComponentBuilder.buildSlider(GOLO_BORDER_RADIUS_SLIDER,      row3,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
       
        // ROW4
        VBox row4 = ComponentBuilder.buildVBox(GOLO_ITEM_BUTTONS_PANE,         editToolbar,          null,   CLASS_GOLO_EDITTOOLBAR, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        
        Label colorGradientLabel     = new Label("Color Gradient");
        colorGradientLabel.setStyle("-fx-font: 24 arial;");
        colorGradientLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(colorGradientLabel);

        
        Label focusAngleLabel        = new Label("Focus Angle: ");
        focusAngleLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(focusAngleLabel);
        Slider focusAngleSlider      = ComponentBuilder.buildSlider(GOLO_FOCUS_ANGLE_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label focusDistanceLabel     = new Label("Focus Distance: ");
        focusDistanceLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(focusDistanceLabel);
        Slider focusDistanceSlider   = ComponentBuilder.buildSlider(GOLO_FOCUS_DISTANCE_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label centerXLabel           = new Label("Center X: ");
        centerXLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(centerXLabel);
        Slider centerXSlider         = ComponentBuilder.buildSlider(GOLO_CENTER_X_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label centerYLabel           = new Label("Center Y: ");
        centerYLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(centerYLabel);
        Slider centerYSlider         = ComponentBuilder.buildSlider(GOLO_CENTER_Y_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label radiusSliderLabel      = new Label("Radius: ");
        radiusSliderLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(radiusSliderLabel);
        Slider radiusSlider          = ComponentBuilder.buildSlider(GOLO_RADIUS_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label cycleMethodlabel       = new Label("Cycle Method: ");
        cycleMethodlabel.setTextFill(Color.WHITE);
        row4.getChildren().add(cycleMethodlabel);
        ComboBox<CycleMethod> cycleMethod = ComponentBuilder.buildComboBox(GOLO_CYCLE_METHOD_COMBOBOX,GOLO_EMPTY,GOLO_EMPTY,      row4,    null,   null, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        cycleMethod.getItems().addAll(CycleMethod.NO_CYCLE,CycleMethod.REFLECT,CycleMethod.REPEAT);
        cycleMethod.getSelectionModel().select(0);
        
        Label stopZeroColorLabel     = new Label("Stop 0 Color: ");
        stopZeroColorLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(stopZeroColorLabel);
        ColorPicker stopZeroColor    = ComponentBuilder.buildColorPicker(GOLO_STOP_ZERO_COLORPICKER,      row4,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label stopOneColorLabel      = new Label("Stop 1 Color: ");
        stopOneColorLabel.setTextFill(Color.WHITE);
        row4.getChildren().add(stopOneColorLabel);
        ColorPicker stopOneColor     = ComponentBuilder.buildColorPicker(GOLO_STOP_ONE_COLORPICKER,      row4,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        
        
        
            
	// AND PUT EVERYTHING IN THE WORKSPACE
        
	workspace = new BorderPane();


        // AND NOW SETUP ALL THE EVENT HANDLING CONTROLLERS
        ItemsController itemsController = new ItemsController((GoLogoLoApp)app);
        editItemButton.setOnAction(e->{
            itemsController.processEditItems(itemsTable.getSelectionModel().getSelectedItem());
        });
        
        ComponentController componentController = new ComponentController((GoLogoLoApp)app);
        moveUpButton.setOnAction(e->{
            componentController.processMoveUpComponent();  
        });
        moveDownButton.setOnAction(e->{
            componentController.processMoveDownComponent();
        });
        
        //TEXT CONTROLS
        BoldButton.setOnAction(e->{
            componentController.processBold();
        });
        ItalicsButton.setOnAction(e->{
            componentController.processItalic();
            app.getFoolproofModule().updateAll();
        });
        LowercaseButton.setOnAction(e->{
            componentController.processLowercase();
            app.getFoolproofModule().updateAll();
        });
        UppercaseButton.setOnAction(e->{
            componentController.processUppercase();
            app.getFoolproofModule().updateAll();
        });
        
        TextColor.setOnAction(e->{
            componentController.processTextColor();
            app.getFoolproofModule().updateAll();
        });
        
        FontFamily.setOnAction(e->{
            componentController.processFontFamily();
            app.getFoolproofModule().updateAll();
        });
        
        FontSize.setOnAction(e->{
            componentController.processFontSize();
            app.getFoolproofModule().updateAll();
        });
        
        borderThicknessSlider.setOnMouseReleased(e->{
                componentController.processBorderThickness();
                app.getFoolproofModule().updateAll();
        });
        
        borderColor.setOnAction(e->{
            componentController.processBorderColor();
            app.getFoolproofModule().updateAll();
        });
        
        borderRadiusSlider.setOnMouseReleased(e->{
                componentController.processBorderRadius();
                app.getFoolproofModule().updateAll();
        });
        
        
        
        focusAngleSlider.setOnMouseReleased(e->{
                componentController.processGradient();
                app.getFoolproofModule().updateAll();
        });
        
        focusDistanceSlider.setOnMouseReleased(e->{
                componentController.processGradient();
                app.getFoolproofModule().updateAll();
        });
        
        centerXSlider.setOnMouseReleased(e->{
                componentController.processGradient();
                app.getFoolproofModule().updateAll();
        });
        
        centerYSlider.setOnMouseReleased(e->{
                componentController.processGradient();
                app.getFoolproofModule().updateAll();
        });
        
        radiusSlider.setOnMouseReleased(e->{
                componentController.processGradient();
                app.getFoolproofModule().updateAll();
        });
        
        cycleMethod.setOnAction(e->{
            componentController.processGradient();
            app.getFoolproofModule().updateAll();
        });
        
        stopZeroColor.setOnAction(e->{
            componentController.processGradient();
            app.getFoolproofModule().updateAll();
        });
        
        stopOneColor.setOnAction(e->{
            componentController.processGradient();
            app.getFoolproofModule().updateAll();
        });

        addTextButton.setOnAction(e->{
            componentController.processAddText();
            app.getFoolproofModule().updateAll();
        });
        
        addRectangleButton.setOnAction(e->{
            componentController.processAddRectangle();
            app.getFoolproofModule().updateAll();
        });
        
        addImageButton.setOnAction(e->{
            componentController.processAddImage();
            app.getFoolproofModule().updateAll();
        });
        
        removeComponentButton.setOnAction(e->{
            componentController.processRemoveComponent();
            app.getFoolproofModule().updateAll();
        });
        
        itemsTable.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2)
                 itemsController.processEditItems(itemsTable.getSelectionModel().getSelectedItem());
        });
        
        ItemsTableController iTC = new ItemsTableController(app);
        itemsTable.widthProperty().addListener(e->{
            iTC.processChangeTableSize();
        });
        
        itemsTable.setOnSort(new EventHandler<SortEvent<TableView<goloItemPrototype>>>(){
            @Override
            public void handle(SortEvent<TableView<goloItemPrototype>> event) {
                goloData data = (goloData)app.getDataComponent();
                ArrayList<goloItemPrototype> oldListOrder = data.getCurrentItemsOrder();
                TableView view = event.getSource();
                ObservableList sortOrder = view.getSortOrder();
                if ((sortOrder != null) && (sortOrder.size() == 1)) {
                    TableColumn sortColumn = event.getSource().getSortOrder().get(0);
                    String columnText = sortColumn.getText();
                    SortType sortType = sortColumn.getSortType();
                    System.out.println("Sort by " + columnText);
                    event.consume();
                    SortItems_Transaction transaction = new SortItems_Transaction(data, oldListOrder, columnText, sortType);
                    app.processTransaction(transaction);
                    app.getFoolproofModule().updateAll();
                }
            }            
        });
        itemsTable.setOnMouseClicked(e -> {
            componentController.processSelectComponent();
            app.getFoolproofModule().updateAll();
        });
    }
    
    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(GOLO_FOOLPROOF_SETTINGS, 
                new goloFoolproofDesign((GoLogoLoApp)app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
       // System.out.println("WORKSPACE REPONSE TO " + ke.getCharacter());
    }
    
    public void resetWorkspace(goloData newData){};
    
    public void setCanvas(Pane initCanvas){
        canvas = initCanvas;
        ((BorderPane)workspace).setCenter(canvas); 
        ((BorderPane)workspace).setLeft(goloPane);
        ((BorderPane)workspace).setRight(editToolbar);
        mouseController = new MouseController(app);
        canvas.setOnMousePressed(e->{
	    mouseController.processMousePress((int)e.getX(), (int)e.getY());           
	});
	canvas.setOnMouseReleased(e->{
	    mouseController.processMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    mouseController.processMouseDragged((int)e.getX(), (int)e.getY());
	});
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent click) {
            if (click.getClickCount() == 2) {
              mouseController.processMouseDoubleClick((int)click.getX(), (int)click.getY());
            }
        }
    });
        
    }
}