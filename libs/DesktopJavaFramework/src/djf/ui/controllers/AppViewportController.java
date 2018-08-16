/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui.controllers;

import djf.AppTemplate;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

/**
 *
 * @author Richie
 */
public class AppViewportController {
    private AppTemplate app;
    
    public AppViewportController(AppTemplate initApp) {
        app = initApp;
    }
    
    public void processResetViewPort() 
    {
        Pane canvas = (Pane) ((BorderPane)app.getWorkspaceComponent().getWorkspace()).getCenter();
        canvas.setScaleX(1);
        canvas.setScaleY(1);
        canvas.getParent().layout();
    }    
    public void processZoomInViewPort() 
    {
        double zoomFactor = 2;
        Pane canvas =(Pane) ((BorderPane)app.getWorkspaceComponent().getWorkspace()).getCenter();
        canvas.setScaleX(canvas.getScaleX() * zoomFactor);
        canvas.setScaleY(canvas.getScaleY() * zoomFactor);
        canvas.getParent().layout();
    }
    public void processZoomOutViewPort() {
        double zoomFactor = 2;
        Pane canvas =(Pane) ((BorderPane)app.getWorkspaceComponent().getWorkspace()).getCenter();
        canvas.setScaleX(canvas.getScaleX() / zoomFactor);
        canvas.setScaleY(canvas.getScaleY() / zoomFactor);
        canvas.getParent().layout();
    }
    public void processResizeViewPort() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Resize Viewport");
        dialog.setHeaderText("Change Dialog Dimensions");

        dialog.getDialogPane().getButtonTypes().addAll( ButtonType.OK, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField Height = new TextField();
        Height.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    Height.setText(oldValue);
                }
            }
        });
        TextField Width = new TextField();
        Width.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    Width.setText(oldValue);
                }
            }
        });

        grid.add(new Label("Height:"), 0, 0);
        grid.add(Height, 1, 0);
        grid.add(new Label("Width:"), 0, 1);
        grid.add(Width, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent() && !Height.getText().isEmpty() && !Width.getText().isEmpty()) {
            Pane canvas =(Pane) ((BorderPane)app.getWorkspaceComponent().getWorkspace()).getCenter();
            canvas.setLayoutX(Double.parseDouble(Height.getText()));
            canvas.setLayoutY(Double.parseDouble(Width.getText()));
        }
        
    }
    public void processSnapViewPort() {
    }
}
