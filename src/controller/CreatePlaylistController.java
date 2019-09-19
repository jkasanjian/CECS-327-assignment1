package controller;

import controller.HomePageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.MusicClass;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CreatePlaylistController implements Initializable {

    @FXML
    TextField getPlaylist;

    @FXML
    Button submitButton;

    @FXML
    Label errMsg;

    HomePageController hpc;

    HashMap<String, ObservableList<MusicClass>> playlists = new HashMap<String, ObservableList<MusicClass>>();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        errMsg.setVisible(false);
        errMsg.setAlignment(Pos.CENTER);
    }
    @FXML
    public void button(ActionEvent event) throws IOException {
        String playlistName = getPlaylist.getText();
        if(event.getSource() == submitButton) {
            boolean contains = false;
            for ( String key : playlists.keySet() ) {
                if (playlistName.equals(key)){
                    contains = true;
                }
            }
            if(contains){
                errMsg.setVisible(true);
                errMsg.setText("Playlist Name Already Exists");
            }else{
                ObservableList<MusicClass> playlistSongs = FXCollections.observableArrayList();
                hpc.addNewPlaylist(playlistName, playlistSongs);
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }
        }
    }

    public void setPrevController(HomePageController hpc){
        this.hpc = hpc;
    }

    public void setPlaylist(HashMap<String, ObservableList<MusicClass>> playlists){
        this.playlists = playlists;
    }
}
