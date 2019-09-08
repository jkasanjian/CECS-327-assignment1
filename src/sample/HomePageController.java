package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    VBox songList;

    @FXML
    TableView<Song> songTable;

    @FXML
    Button songButton;

    @FXML
    Button artistButton;

    @FXML
    Button createPlaylist;

    @FXML
    Button deletePlaylist;

    @FXML
    ListView<String> displayPlaylists;

    HashMap <String, ObservableList<Song>> playlists = new HashMap<String, ObservableList<Song>>();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        songTable = populateTable(getAllSongs());
    }

    public ObservableList<Song> getAllSongs(){
        ObservableList<Song> songs = FXCollections.observableArrayList();
        songs.add(new Song("Radioactive", "Imagine Dragons", "3:10", "2012"));
        songs.add(new Song("Rescue Me", "One Republic", "2:39", "2019"));
        return songs;
    }

    public TableView<Song> populateTable(ObservableList<Song> songs){
        // Song Name Column
        TableColumn<Song,String> nameColumn = new TableColumn<>("Name: ");
        nameColumn.setMinWidth(400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Song Artist Column
        TableColumn<Song,String> artistColumn = new TableColumn<>("Artist: ");
        artistColumn.setMinWidth(200);
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        // Song Duration Column
        TableColumn<Song,String> durationColumn = new TableColumn<>("Duration: ");
        durationColumn.setMinWidth(200);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Song Year Released Column
        TableColumn<Song,String> yearColumn = new TableColumn<>("Year: ");
        yearColumn.setMinWidth(200);
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearReleased"));

        songTable.setItems(songs);
        songTable.getColumns().addAll(nameColumn, artistColumn, durationColumn, yearColumn);

        return songTable;
    }

    @FXML
    public void button(ActionEvent event) throws IOException{

        if(event.getSource() == songButton) {
            populateTable(getAllSongs());
        } else if(event.getSource() == artistButton){
            songTable.getItems().clear();
            songTable.getColumns().clear();
        } else if(event.getSource() == createPlaylist){
            openCreatePlaylistWindow();
        } else if(event.getSource() == deletePlaylist){
            openDeletePlaylistWindow();
        }
    }

    @FXML
    public void clickSong(MouseEvent event)
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            System.out.println(songTable.getSelectionModel().getSelectedItem().getName());
        }
    }

    public void openCreatePlaylistWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreatePlaylist.fxml"));
        Parent root = loader.load();
        CreatePlaylistController cpc = loader.getController();
        cpc.setPrevController(this);
        Stage newWindow = new Stage();
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void addNewPlaylist(String playlistName, ObservableList<Song> playlistSongs){
        displayPlaylists.getItems().add(playlistName);
        playlists.put(playlistName, playlistSongs);
    }

    public void openDeletePlaylistWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeletePlaylist.fxml"));
        Parent root = loader.load();
        DeletePlaylistController dpc = loader.getController();
        dpc.setPrevController(this);
        dpc.setListOfPlaylists(displayPlaylists);
        Stage newWindow = new Stage();
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }




}
