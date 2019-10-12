package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import rpc.Proxy;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.function.Predicate;


/**
 * Controller class corresponding to the home page fxml.
 */
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

    @FXML Button prevPageButton;

    @FXML Button nextPageButton;

    HashMap <String, ObservableList<MusicClass>> playlists = new HashMap<String, ObservableList<MusicClass>>();

    private MediaPlayer mediaPlayer;

    private Media media;

    private ObservableList<MusicClass> master;

    private String currentPlaylist;

    private int pageNumber;

    private String sessionID;

    /**
     * Initializes the table and the event listeners.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        pageNumber = 1;
        currentPlaylist = "";
        Proxy proxy = Proxy.GetInstance();
        SingletonProfile singletonProfile = SingletonProfile.GetInstance();
        sessionID = singletonProfile.getSessionID();
        System.out.println(sessionID);
        try {
            JsonObject ret = proxy.synchExecution("getSongs", new String[]{"1", currentPlaylist, Integer.toString(pageNumber)});
            Gson gson = new Gson();
            Playlist playlistSongs = gson.fromJson( ret.get("ret"), Playlist.class );
            ObservableList<MusicClass> musicOList = FXCollections.observableArrayList(playlistSongs.getMusicClassList());
            master = musicOList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        songTable = populateTable(master);
        //String songFile = "imperial.mp3";
        //media = new Media(Paths.get(songFile).toUri().toString());
        //mediaPlayer = new MediaPlayer(media);

        gSearchTextField.setOnKeyPressed(e ->{
            gSearchTextField.textProperty().addListener((observable, oldValue, newValue) ->{
                filter(newValue);
            });
        });

        for (Playlist playlist : singletonProfile.getPlaylists()) {
            displayPlaylists.getItems().add(playlist.getName());
            playlists.put(playlist.getName(), FXCollections.observableArrayList(playlist.getMusicClassList()));
        }

    }

    /**
     * Populates the table view with a given list.
     * @param songs The list of songs that should be shown in the table view.
     * @return The tableview populated with the corresponding collumns and songs.
     */
    public TableView<MusicClass> populateTable(ObservableList<MusicClass> songs){

        songTable.getColumns().clear();
        songTable.getItems().clear();
//
//        // model.Song Name Column
//        TableColumn<MusicClass,String> songIDColumn = new TableColumn<>("Song ID: ");
//        songIDColumn.setMinWidth(200);
//        songIDColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("songID"));

        // model.Song Name Column
        TableColumn<MusicClass,String> nameColumn = new TableColumn<>("Name: ");
        nameColumn.setMinWidth(400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("songTitle"));

        // model.Song model.Artist Column
        TableColumn<MusicClass,String> artistColumn = new TableColumn<>("Artist: ");
        artistColumn.setMinWidth(200);
        artistColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("artistName"));

        // model.Song Duration Column
        TableColumn<MusicClass,String> durationColumn = new TableColumn<>("Duration: ");
        durationColumn.setMinWidth(200);
        durationColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("durationTime"));

        // model.Song Year Released Column
        TableColumn<MusicClass,Integer> yearColumn = new TableColumn<>("Year: ");
        yearColumn.setMinWidth(200);
        yearColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,Integer>("songYear"));

        songTable.getColumns().addAll(nameColumn, artistColumn, durationColumn, yearColumn);
        songTable.setItems(songs);

        return songTable;
    }

    /**
     * Handles any button pressing events.
     * @param event The event that happens from a corresponding button press.
     * @throws IOException
     */
    @FXML
    public void button(ActionEvent event) throws IOException{

        if(event.getSource() == songButton) {
            populateTable(master);
            currentPlaylist = "";
            pageNumber = 1;
        } else if(event.getSource() == artistButton){
        } else if (event.getSource() == addSongToPlaylist){
            openAddSongToPlaylistWindow();
        } else if(event.getSource() == createPlaylist){
            openCreatePlaylistWindow();
        } else if(event.getSource() == deletePlaylist){
            openDeletePlaylistWindow();
        } else if(event.getSource() == prevPageButton){
            pageNumber -= 1;
            Proxy proxy = Proxy.GetInstance();
            try {
                JsonObject ret = proxy.synchExecution("getSongs", new String[]{sessionID, currentPlaylist, Integer.toString(pageNumber)});
                Gson gson = new Gson();
                Playlist playlistSongs = gson.fromJson( ret.get("ret"), Playlist.class );
                ObservableList<MusicClass> musicOList = FXCollections.observableArrayList(playlistSongs.getMusicClassList());
                master = musicOList;
                populateTable(master);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(pageNumber == 1){
                prevPageButton.setVisible(false);
                nextPageButton.setVisible(true);
            }else{
                prevPageButton.setVisible(true);
                nextPageButton.setVisible(true);
            }
        } else if(event.getSource() == nextPageButton){
            pageNumber += 1;
            Proxy proxy = Proxy.GetInstance();
            try {
                JsonObject ret = proxy.synchExecution("getSongs", new String[]{sessionID, currentPlaylist, Integer.toString(pageNumber)});
                Gson gson = new Gson();
                Playlist playlistSongs = gson.fromJson( ret.get("ret"), Playlist.class );
                ObservableList<MusicClass> musicOList = FXCollections.observableArrayList(playlistSongs.getMusicClassList());
                master = musicOList;
                populateTable(master);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(master.size() < 20){
                prevPageButton.setVisible(true);
                nextPageButton.setVisible(false);
            }else{
                prevPageButton.setVisible(true);
                nextPageButton.setVisible(true);
            }
        }
    }

    /**
     * If song is double clicked on table view it plays a song.
     * @param click
     */
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

    /**
     * If playlist is double clicked it populates the table with the songs from the playlist.
     * @param click
     */
    @FXML
    public void clickPlaylist(MouseEvent click)
    {
        if (click.getClickCount() == 2) //Checking double click
        {
            String selectedPlaylist = displayPlaylists.getSelectionModel().getSelectedItem();
            currentPlaylist = selectedPlaylist;

            Proxy proxy = Proxy.GetInstance();
            try {
                JsonObject ret = proxy.synchExecution("getSongs", new String[]{sessionID, currentPlaylist, Integer.toString(pageNumber)});
                Gson gson = new Gson();
                Playlist playlistSongs = gson.fromJson( ret.get("ret"), Playlist.class );
                ObservableList<MusicClass> selectedPlaylistSongs = FXCollections.observableArrayList(playlistSongs.getMusicClassList());
                populateTable(selectedPlaylistSongs);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Filters the table view with the search text field.
     * @param value
     */
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

    /**
     * Loads the create playlist window.
     * @throws IOException
     */
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

    /**
     * Adds a new playlist to the account.
     * @param playlistName Name of playlist.
     * @param playlistSongs Songs.
     */
    public void addNewPlaylist(String playlistName, ObservableList<MusicClass> playlistSongs){
        displayPlaylists.getItems().add(playlistName);
        playlists.put(playlistName, playlistSongs);
        SingletonProfile profile = SingletonProfile.GetInstance();
        profile.addNewPlaylist(playlistName, playlistSongs);
        SingletonProfiles.GetInstance().sync(profile);
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
       SingletonProfile profile = SingletonProfile.GetInstance();
       profile.addToPlaylist(playlistName, selectedSong);
       SingletonProfiles.GetInstance().sync(profile);
    }

    public void deletePlaylist(String playlistName){
        for(int i = 0; i < displayPlaylists.getItems().size(); i++){
            if(displayPlaylists.getItems().get((i)) == playlistName){
                displayPlaylists.getItems().remove(i);
            }
        }
        playlists.remove(playlistName);
        SingletonProfile profile = SingletonProfile.GetInstance();
        profile.removePlaylist(playlistName);
        SingletonProfiles.GetInstance().sync(profile);
    }

    /**
     * Reads JSON file to retrieve music.
     * @return
     */
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

    @FXML
    public void enter( MouseEvent e ){
        Button button = (Button)e.getSource();
        button.setStyle("-fx-background-color: #70C2FF");
    }

    @FXML
    public void exit( MouseEvent e ){
        Button button = (Button)e.getSource();
        button.setStyle("-fx-background-color: #668FE8");
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
