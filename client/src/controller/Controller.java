package controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This class is an interface that offers LoadFXML method
 * This class is to be extended by every ViewController class
 */
public abstract class Controller {

    /**
     *
     * @param event ActionEvent associated with the action
     * @param title Title of the new view to load
     * @param fxml Path of the fxml view to load
     */
    protected void LoadFXML(Event event, String title, String fxml) {
        Stage window = null;
        if(event.getSource() instanceof Node) {
            window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else if(event.getSource() instanceof MenuItem) {
            window = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        window.setTitle(title);
        try {
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        window.show();
    }
}