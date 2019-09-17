package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class DeletePlaylistController {
    @FXML
    ChoiceBox<String> playlists;

    @FXML
    Button submitButton;

    HomePageController hpc;

    @FXML
    public void button(ActionEvent event) throws IOException {

        if(event.getSource() == submitButton) {
            String playlistName = playlists.getSelectionModel().getSelectedItem();
            hpc.deletePlaylist(playlistName);
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
    }

    public void setPrevController(HomePageController hpc){
        this.hpc = hpc;
    }

    public void setListOfPlaylists(ListView playlists){

        this.playlists.getItems().addAll(playlists.getItems());
    }
}
