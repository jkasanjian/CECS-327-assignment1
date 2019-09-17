

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class CreatePlaylistController {

    @FXML
    TextField getPlaylist;

    @FXML
    Button submitButton;

    HomePageController hpc;

    @FXML
    public void button(ActionEvent event) throws IOException {

        if(event.getSource() == submitButton) {
            String playlistName = getPlaylist.getText();
            ObservableList<Song> playlistSongs = FXCollections.observableArrayList();
            hpc.addNewPlaylist(playlistName, playlistSongs);
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
    }

    public void setPrevController(HomePageController hpc){
        this.hpc = hpc;
    }
}
