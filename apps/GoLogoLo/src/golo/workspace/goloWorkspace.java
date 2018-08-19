package golo.workspace;

import static djf.AppPropertyType.GOLO_CANVAS_PANE;
import static djf.AppPropertyType.SNAP_CHECKBOX;
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
import golo.data.Drag;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
        Button TextSizeUpButton        = ComponentBuilder.buildIconButton(GOLO_TEXT_SIZE_UP_BUTTON,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        Button TextSizeDownButton        = ComponentBuilder.buildIconButton(GOLO_TEXT_SIZE_DOWN_BUTTON,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        ColorPicker TextColor         = ComponentBuilder.buildColorPicker(GOLO_TEXT_COLOR_COLORPICKER,      row2Box2,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        // ROW3
        VBox row3 = ComponentBuilder.buildVBox(GOLO_ITEM_BUTTONS_PANE,         editToolbar,          null,   CLASS_GOLO_EDITTOOLBAR, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        row3.setSpacing(5);
        Label borderThicknessLabel = ComponentBuilder.buildLabel(GOLO_BORDER_THICKNESS_LABEL,         row3,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        borderThicknessLabel.setTextFill(Color.WHITE);
        
        Slider borderThicknessSlider = ComponentBuilder.buildSlider(GOLO_BORDER_THICKNESS_SLIDER,      row3,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);

        Label borderColorLabel = ComponentBuilder.buildLabel(GOLO_BORDER_LABEL,         row3,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        borderColorLabel.setTextFill(Color.WHITE);
        
        ColorPicker borderColor = ComponentBuilder.buildColorPicker(GOLO_BORDER_COLORPICKER,      row3,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);

        Label borderRadiusLabel = ComponentBuilder.buildLabel(GOLO_BORDER_RADIUS_LABEL,         row3,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        borderRadiusLabel.setTextFill(Color.WHITE);
        Slider borderRadiusSlider    = ComponentBuilder.buildSlider(GOLO_BORDER_RADIUS_SLIDER,      row3,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
       
        // ROW4
        VBox row4 = ComponentBuilder.buildVBox(GOLO_ITEM_BUTTONS_PANE,         editToolbar,          null,   CLASS_GOLO_EDITTOOLBAR, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        
        Label colorGradientLabel     = ComponentBuilder.buildLabel(GOLO_COLOR_GRADIENT_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        colorGradientLabel.setStyle("-fx-font: 24 arial;");
        colorGradientLabel.setTextFill(Color.WHITE);
        
        Label focusAngleLabel        = ComponentBuilder.buildLabel(GOLO_FOCUS_ANGLE_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        focusAngleLabel.setTextFill(Color.WHITE);
        Slider focusAngleSlider      = ComponentBuilder.buildSlider(GOLO_FOCUS_ANGLE_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label focusDistanceLabel     = ComponentBuilder.buildLabel(GOLO_FOCUS_DISTANCE_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        focusDistanceLabel.setTextFill(Color.WHITE);
        Slider focusDistanceSlider   = ComponentBuilder.buildSlider(GOLO_FOCUS_DISTANCE_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label centerXLabel           = ComponentBuilder.buildLabel(GOLO_CENTER_X_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        centerXLabel.setTextFill(Color.WHITE);
        Slider centerXSlider         = ComponentBuilder.buildSlider(GOLO_CENTER_X_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label centerYLabel           = ComponentBuilder.buildLabel(GOLO_CENTER_Y_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        centerYLabel.setTextFill(Color.WHITE);
        Slider centerYSlider         = ComponentBuilder.buildSlider(GOLO_CENTER_Y_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label radiusSliderLabel      = ComponentBuilder.buildLabel(GOLO_RADIUS_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        radiusSliderLabel.setTextFill(Color.WHITE);
        Slider radiusSlider          = ComponentBuilder.buildSlider(GOLO_RADIUS_SLIDER,      row4,    null,   CLASS_GOLO_BUTTON,0,10, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label cycleMethodlabel       = ComponentBuilder.buildLabel(GOLO_CYCLE_METHOD_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        cycleMethodlabel.setTextFill(Color.WHITE);
        ComboBox<CycleMethod> cycleMethod = ComponentBuilder.buildComboBox(GOLO_CYCLE_METHOD_COMBOBOX,GOLO_EMPTY,GOLO_EMPTY,      row4,    null,   null, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        cycleMethod.getItems().addAll(CycleMethod.NO_CYCLE,CycleMethod.REFLECT,CycleMethod.REPEAT);
        cycleMethod.getSelectionModel().select(0);
        
        Label stopZeroColorLabel     = ComponentBuilder.buildLabel(GOLO_STOP_ZERO_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        stopZeroColorLabel.setTextFill(Color.WHITE);
        ColorPicker stopZeroColor    = ComponentBuilder.buildColorPicker(GOLO_STOP_ZERO_COLORPICKER,      row4,    null,   CLASS_GOLO_BUTTON, HAS_KEY_HANDLER,   FOCUS_TRAVERSABLE,  DISABLED);
        
        Label stopOneColorLabel      = ComponentBuilder.buildLabel(GOLO_STOP_ONE_LABEL,         row4,          null,   null, HAS_KEY_HANDLER,     FOCUS_TRAVERSABLE,  ENABLED);
        stopOneColorLabel.setTextFill(Color.WHITE);
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
            app.getFoolproofModule().updateAll();
        });
        ItalicsButton.setOnAction(e->{
            componentController.processItalic();
            app.getFoolproofModule().updateAll();
        });
        TextSizeUpButton.setOnAction(e->{
            componentController.processTextSizeUp();
            app.getFoolproofModule().updateAll();
        });
        TextSizeDownButton.setOnAction(e->{
            componentController.processTextSizeDown();
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
        addCircleButton.setOnAction(e->{
            componentController.processAddCircle();
            app.getFoolproofModule().updateAll();
        });
        addTriangleButton.setOnAction(e->{
            componentController.processAddTriangle();
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
        canvas.setStyle("-fx-background-color : white; -fx-border-color : grey;");
        StackPane CanvasHolder = new StackPane();
        CanvasHolder.getChildren().add(canvas);
        final Rectangle outputClip = new Rectangle();
        canvas.setClip(outputClip);
        canvas.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });    
        CanvasHolder.setAlignment(Pos.CENTER);
        ((BorderPane)workspace).setCenter(CanvasHolder); 
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
        
        canvas.setOnScroll((ScrollEvent event) -> 
        {
            mouseController.processMouseScroll((int)event.getDeltaY());
        });
    }

    @Override
    public void processSnap() {
        Pane canvas = (Pane) app.getGUIModule().getGUINode(GOLO_CANVAS_PANE);
        if(((CheckBox)app.getGUIModule().getGUINode(SNAP_CHECKBOX)).isSelected()){
            canvas.setStyle("-fx-background-color: #FFF," +
                                    "linear-gradient(from 0.5px 0px to 10.5px 0px, repeat, black 5%, transparent 5%),\n" +
                                    "linear-gradient(from 0px 0.5px to 0px 10.5px, repeat, black 5%, transparent 5%);");
            ObservableList<Node> components = canvas.getChildren();
            for(int i=0;i<components.size();i++){
                ((Drag)components.get(i)).setPosandSize((Math.round(((Drag)components.get(i)).getX() - 20)),
                        (Math.round(((Drag)components.get(i)).getY() - 20)) , ((Drag)components.get(i)).getWidth() , ((Drag)components.get(i)).getHeight());
            }
        }
        else
            canvas.setStyle("-fx-background-color : white; -fx-border-color : grey;");
        
        
        
        
    }
}