package controller;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.IOException;
import java.util.function.Predicate;


public class HomePageController implements Initializable {

    @FXML
    VBox songList;

    @FXML
    TableView<MusicClass> songTable;

    @FXML
    Button songButton;

    @FXML
    Button artistButton;

    @FXML
    Button createPlaylist;

    @FXML
    Button deletePlaylist;

    @FXML
    Button addSongToPlaylist;

    @FXML
    ListView<String> displayPlaylists;

    @FXML
    TextField gSearchTextField;

    HashMap <String, ObservableList<MusicClass>> playlists = new HashMap<String, ObservableList<MusicClass>>();

    private MediaPlayer mediaPlayer;

    private Media media;

    private ObservableList<MusicClass> master;
    private String currentPlaylist;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        master = readMusicJSON();
        songTable = populateTable(master);
        currentPlaylist = "master";
        String songFile = "imperial.mp3";
        media = new Media(Paths.get(songFile).toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        gSearchTextField.setOnKeyPressed(e ->{
            gSearchTextField.textProperty().addListener((observable, oldValue, newValue) ->{
                filter(newValue);
            });
        });

    }

    public TableView<MusicClass> populateTable(ObservableList<MusicClass> songs){

//        songTable.getColumns().clear();
//        songTable.getItems().clear();

        // model.Song Name Column
        TableColumn<MusicClass,String> songIDColumn = new TableColumn<>("model.Song ID: ");
        songIDColumn.setMinWidth(200);
        songIDColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("songID"));

        // model.Song Name Column
        TableColumn<MusicClass,String> nameColumn = new TableColumn<>("Name: ");
        nameColumn.setMinWidth(400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("songTitle"));

        // model.Song model.Artist Column
        TableColumn<MusicClass,String> artistColumn = new TableColumn<>("model.Artist: ");
        artistColumn.setMinWidth(200);
        artistColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("artistName"));

        // model.Song Duration Column
        TableColumn<MusicClass,Double> durationColumn = new TableColumn<>("Duration: ");
        durationColumn.setMinWidth(200);
        durationColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,Double>("durationTime"));

        // model.Song Year Released Column
        TableColumn<MusicClass,Integer> yearColumn = new TableColumn<>("Year: ");
        yearColumn.setMinWidth(200);
        yearColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,Integer>("songYear"));

        songTable.getColumns().addAll(songIDColumn,nameColumn, artistColumn, durationColumn, yearColumn);
        songTable.setItems(songs);

        return songTable;
    }

    @FXML
    public void button(ActionEvent event) throws IOException{

        if(event.getSource() == songButton) {
            populateTable(master);
            currentPlaylist = "master";
        } else if(event.getSource() == artistButton){
        } else if (event.getSource() == addSongToPlaylist){
            openAddSongToPlaylistWindow();
        } else if(event.getSource() == createPlaylist){
            openCreatePlaylistWindow();
        } else if(event.getSource() == deletePlaylist){
            openDeletePlaylistWindow();
        }
    }

    @FXML
    public void clickSong(MouseEvent click)
    {
        if (click.getClickCount() == 2) //Checking double click
        {
            String songName = songTable.getSelectionModel().getSelectedItem().getSongTitle();
            System.out.println(songName);
            if(songName.equals("I Didn't Mean To")){
                new Thread(String.valueOf(mediaPlayer)).start();
                mediaPlayer.play();
            }
        }
    }

    @FXML
    public void clickPlaylist(MouseEvent click)
    {
        if (click.getClickCount() == 2) //Checking double click
        {
            String selectedPlaylist = displayPlaylists.getSelectionModel().getSelectedItem();
            currentPlaylist = selectedPlaylist;
            ObservableList<MusicClass> selectedPlaylistSongs = playlists.get(selectedPlaylist);
            populateTable(selectedPlaylistSongs);
        }
    }

    public void filter(String value){
        String lowerCase = value.toLowerCase();
        FilteredList<MusicClass> filteredData;
        if(currentPlaylist == "master")
            filteredData = new FilteredList<>(master);
        else
            filteredData = new FilteredList<>(playlists.get(currentPlaylist));

        filteredData.setPredicate((Predicate<? super MusicClass>) musicClass ->{
            if (value.isEmpty() || value == null) return true;
            if (musicClass.getSongID().contains(value)) return true;
            if (musicClass.getSongTitle().toLowerCase().contains(lowerCase)) return true;
            if (musicClass.getArtistName().toLowerCase().contains(lowerCase)) return true;

            return false;
        });

        songTable.setItems(filteredData);
    }

    public void openCreatePlaylistWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreatePlaylist.fxml"));
        Parent root = loader.load();
        CreatePlaylistController cpc = loader.getController();
        cpc.setPrevController(this);
        cpc.setPlaylist(playlists);
        Stage newWindow = new Stage();
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void addNewPlaylist(String playlistName, ObservableList<MusicClass> playlistSongs){
        displayPlaylists.getItems().add(playlistName);
        playlists.put(playlistName, playlistSongs);
    }

    public void openDeletePlaylistWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeletePlaylist.fxml"));
        Parent root = loader.load();
        DeletePlaylistController dpc = loader.getController();
        dpc.setPrevController(this);
        dpc.setListOfPlaylists(displayPlaylists);
        dpc.setAction("delete");
        Stage newWindow = new Stage();
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void openAddSongToPlaylistWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeletePlaylist.fxml"));
        Parent root = loader.load();
        DeletePlaylistController dpc = loader.getController();
        dpc.setPrevController(this);
        dpc.setListOfPlaylists(displayPlaylists);
        dpc.setAction("add");
        Stage newWindow = new Stage();
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void addNewSongToPlaylist(String playlistName){
       ObservableList<MusicClass> mc = playlists.get(playlistName);
       MusicClass selectedSong = songTable.getSelectionModel().getSelectedItem();
       mc.add(selectedSong);
       playlists.put(playlistName,mc);
    }

    public void deletePlaylist(String playlistName){
        for(int i = 0; i < displayPlaylists.getItems().size(); i++){
            if(displayPlaylists.getItems().get((i)) == playlistName){
                displayPlaylists.getItems().remove(i);
            }
        }
        playlists.remove(playlistName);
    }

    public static ObservableList<MusicClass> readMusicJSON() {
        Gson gson = new Gson();
        String fileName = "music.json";

        try {
            Type musicClassType = new TypeToken<ObservableList<MusicClass>>() {
            }.getType();
            ArrayList<MusicClass> musicList = gson.fromJson(new FileReader(fileName), musicClassType);
            ObservableList<MusicClass> musicOList = FXCollections.observableArrayList(musicList);
            // musicList has ArrayList of model.MusicClass objects
            return musicOList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return null;
    }

//    /**
//     * Play a given audio file.
//     * @param audioFilePath Path of the audio file.
//     */
//    public void mp3play(String file) {
//        try {
//            // It uses CECS327InputStream as InputStream to play the song
//            InputStream is = new CECS327InputStream(file);
//            Player mp3player = new Player(is);
//            mp3player.play();
//        }
//        catch (JavaLayerException exception)
//        {
//            exception.printStackTrace();
//        }
//        catch (IOException exception)
//        {
//            exception.printStackTrace();
//        }
//    }




}
