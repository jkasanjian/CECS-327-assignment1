package controller;

import com.google.gson.JsonObject;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import model.*;
import com.google.gson.Gson;
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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import rpc.Proxy;

import java.io.IOException;
import java.util.Set;
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

    @FXML
    Button prevPageButton;

    @FXML
    Button nextPageButton;

    @FXML
    Button searchButton;

    @FXML
    Button musicButton;

    private HashMap<String, ObservableList<MusicClass>> playlists = new HashMap<String, ObservableList<MusicClass>>();

    private MediaPlayer mediaPlayer;

    private Media media;

    private ObservableList<MusicClass> master;

    private String currentPlaylist;

    private int pageNumber;

    private String sessionID;

    private boolean musicState; // True = Music Playing, False = Music Not Playing/Paused

    private Thread musicThread;

    /**
     * Initializes the table and the event listeners.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pageNumber = 1;
        prevPageButton.setVisible(false);
        musicButton.setVisible(false);
        musicState = false;
        musicThread = new Thread();
        currentPlaylist = "";
        SingletonProfile singletonProfile = SingletonProfile.GetInstance();
        sessionID = singletonProfile.getSessionID();
        System.out.println(sessionID);
        try {
            master = getSongs(sessionID, currentPlaylist, pageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        songTable = populateTable(master);
        //String songFile = "imperial.mp3";
        //media = new Media(Paths.get(songFile).toUri().toString());
        //mediaPlayer = new MediaPlayer(media);

//        gSearchTextField.setOnKeyPressed(e ->{
//            gSearchTextField.textProperty().addListener((observable, oldValue, newValue) ->{
//                filter(newValue);
//            });
//        });

        for (Playlist playlist : singletonProfile.getPlaylists()) {
            displayPlaylists.getItems().add(playlist.getName());
            playlists.put(playlist.getName(), FXCollections.observableArrayList(playlist.getMusicClassList()));
        }

    }

    /**
     * Populates the table view with a given list.
     *
     * @param songs The list of songs that should be shown in the table view.
     * @return The tableview populated with the corresponding collumns and songs.
     */
    public TableView<MusicClass> populateTable(ObservableList<MusicClass> songs) {

        try {
            songTable.getColumns().clear();
            songTable.getItems().clear();
        } catch (Exception e) {

        }
//
//        // model.Song Name Column
//        TableColumn<MusicClass,String> songIDColumn = new TableColumn<>("Song ID: ");
//        songIDColumn.setMinWidth(200);
//        songIDColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("songID"));

        // model.Song Name Column
        TableColumn<MusicClass, String> nameColumn = new TableColumn<>("Name: ");
        nameColumn.setMinWidth(400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<MusicClass, String>("songTitle"));

        // model.Song model.Artist Column
        TableColumn<MusicClass, String> artistColumn = new TableColumn<>("Artist: ");
        artistColumn.setMinWidth(200);
        artistColumn.setCellValueFactory(new PropertyValueFactory<MusicClass, String>("artistName"));

        // model.Song Duration Column
        TableColumn<MusicClass, String> durationColumn = new TableColumn<>("Duration: ");
        durationColumn.setMinWidth(200);
        durationColumn.setCellValueFactory(new PropertyValueFactory<MusicClass, String>("durationTime"));

        // model.Song Year Released Column
        TableColumn<MusicClass, Integer> yearColumn = new TableColumn<>("Year: ");
        yearColumn.setMinWidth(200);
        yearColumn.setCellValueFactory(new PropertyValueFactory<MusicClass, Integer>("songYear"));

        songTable.getColumns().addAll(nameColumn, artistColumn, durationColumn, yearColumn);
        songTable.setItems(songs);

        return songTable;
    }

    /**
     * Handles any button pressing events.
     *
     * @param event The event that happens from a corresponding button press.
     * @throws IOException
     */
    @FXML
    public void button(ActionEvent event) throws IOException, InterruptedException {
        if (event.getSource() == songButton) {
            currentPlaylist = "";
            pageNumber = 1;
            master = getSongs(sessionID, currentPlaylist, pageNumber);
            populateTable(master);
        } else if (event.getSource() == artistButton) {
        } else if (event.getSource() == addSongToPlaylist) {
            openAddSongToPlaylistWindow();
        } else if (event.getSource() == createPlaylist) {
            openCreatePlaylistWindow();
        } else if (event.getSource() == deletePlaylist) {
            openDeletePlaylistWindow();
        } else if (event.getSource() == prevPageButton) {

            if (!gSearchTextField.getText().isEmpty()) {
                pageNumber -= 1;
                filter(gSearchTextField.getText());
            } else {
                pageNumber -= 1;
                master = getSongs(sessionID, currentPlaylist, pageNumber);
                populateTable(master);
            }
        } else if (event.getSource() == nextPageButton) {

            if (!gSearchTextField.getText().isEmpty()) {
                pageNumber += 1;
                filter(gSearchTextField.getText());
            } else {
                pageNumber += 1;
                try {
                    master = getSongs(sessionID, currentPlaylist, pageNumber);
                    populateTable(master);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (event.getSource() == searchButton) {
            pageNumber = 1;
            filter(gSearchTextField.getText());
        } else if (event.getSource() == musicButton) {
            if (musicState == true) {
                musicState = false;
                musicButton.setVisible(false);
            }
        }
        if (pageNumber == 1)
            prevPageButton.setVisible(false);
        else
            prevPageButton.setVisible(true);
        if (master.size() == 0)
            nextPageButton.setVisible(false);
        else
            nextPageButton.setVisible(true);
    }

    /**
     * If song is double clicked on table view it plays a song.
     * TODO: implement with RPC
     *
     * @param click
     */
    @FXML
    public void clickSong(MouseEvent click) throws IOException, JavaLayerException {
        if (click.getClickCount() == 2) //Checking double click
        {
//            String songName = songTable.getSelectionModel().getSelectedItem().getSongTitle();
//            System.out.println(songName);
//            if(songName.equals("I Didn't Mean To")){
//                new Thread(String.valueOf(mediaPlayer)).start();
//                mediaPlayer.play();
//            }
            musicState = true;
            playSong("server/Radioactive.mp3");
            musicButton.setVisible(true);

        }
    }

    /**
     * If playlist is double clicked it populates the table with the songs from the playlist.
     *
     * @param click
     */
    @FXML
    public void clickPlaylist(MouseEvent click) {
        if (click.getClickCount() == 2) //Checking double click
        {
            pageNumber = 1;
            String selectedPlaylist = displayPlaylists.getSelectionModel().getSelectedItem();
            currentPlaylist = selectedPlaylist;
            prevPageButton.setVisible(false);
            nextPageButton.setVisible(true);
            try {
                master = getSongs(sessionID, currentPlaylist, pageNumber);
                populateTable(master);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Filters the table view with the search text field.
     *
     * @param value
     */
    public void filter(String value) {

        try {
            JsonObject ret = Proxy.GetInstance().synchExecution("search", new String[]{sessionID, currentPlaylist, value, String.valueOf(pageNumber)});
            Gson gson = new Gson();
            Playlist playlistSongs = gson.fromJson(ret.get("ret"), Playlist.class);

            ObservableList<MusicClass> songs = FXCollections.observableArrayList(playlistSongs.getMusicClassList());
            songTable.setItems(songs);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads the create playlist window.
     *
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
     *
     * @param playlistName  Name of playlist.
     * @param playlistSongs Songs.
     */
    public void addNewPlaylist(String playlistName, ObservableList<MusicClass> playlistSongs) throws IOException {
        displayPlaylists.getItems().add(playlistName);
        playlists.put(playlistName, playlistSongs);
        SingletonProfile profile = SingletonProfile.GetInstance();
        System.out.println("FOUND ID: " + profile.getSessionID());
        profile.addNewPlaylist(playlistName, playlistSongs);
        Proxy.GetInstance().synchExecution("createPlaylist",
                new String[]{profile.getSessionID(), playlistName});
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

    public void addNewSongToPlaylist(String playlistName) throws IOException {
        ObservableList<MusicClass> mc = playlists.get(playlistName);
        MusicClass selectedSong = songTable.getSelectionModel().getSelectedItem();
        mc.add(selectedSong);
        playlists.put(playlistName, mc);
        SingletonProfile profile = SingletonProfile.GetInstance();
        profile.addToPlaylist(playlistName, selectedSong);
        Proxy.GetInstance().synchExecution("addSongToPlaylist", new String[]{sessionID,
                playlistName, selectedSong.getSongID()});
    }

    public void deletePlaylist(String playlistName) throws IOException {
        for (int i = 0; i < displayPlaylists.getItems().size(); i++) {
            if (displayPlaylists.getItems().get((i)) == playlistName) {
                displayPlaylists.getItems().remove(i);
            }
        }
        playlists.remove(playlistName);
        SingletonProfile profile = SingletonProfile.GetInstance();
        profile.removePlaylist(playlistName);
        Proxy.GetInstance().asynchExecution("deletePlaylist",
                new String[]{SingletonProfile.GetInstance().getSessionID(), playlistName});
    }

    @FXML
    public void enter(MouseEvent e) {
        Button button = (Button) e.getSource();
        button.setStyle("-fx-background-color: #70C2FF");
    }

    @FXML
    public void exit(MouseEvent e) {
        Button button = (Button) e.getSource();
        button.setStyle("-fx-background-color: #668FE8");
    }

//    /**
//     * Play a given audio file.
//     * @param audioFilePath Path of the audio file.
//     */
//    public void mp3play(String file) {
//        try {
//            // It uses model.CECS327InputStream as InputStream to play the song
//            InputStream is = new model.CECS327InputStream(file);
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


    private ObservableList<MusicClass> getSongs(String sessionID, String playlistName, int pageNum) throws IOException {
        Proxy proxy = Proxy.GetInstance();
        JsonObject ret = proxy.synchExecution("getSongs", new String[]{sessionID, playlistName, String.valueOf(pageNum)});
        Gson gson = new Gson();
        Playlist playlistSongs = gson.fromJson(ret.get("ret"), Playlist.class);
        return FXCollections.observableArrayList(playlistSongs.getMusicClassList());
    }

    private void playSong(String file) throws IOException, JavaLayerException {
        System.out.println(file);
        InputStream is = new CECS327InputStream(file);
        Player player = new Player(is);
        musicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(musicState){
                        player.play();
                    }
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        });
        musicThread.setName("musicThread");
        musicThread.start();
    }
}
