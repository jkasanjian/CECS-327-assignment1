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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    HashMap <String, ObservableList<MusicClass>> playlists = new HashMap<String, ObservableList<MusicClass>>();

    private MediaPlayer mediaPlayer;

    private Media media;

    private ObservableList<MusicClass> master;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        master = readMusicJSON();
        songTable = populateTable(master);
        String songFile = "CECS-327-assignment1/imperial.mp3";
        media = new Media(Paths.get(songFile).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public TableView<MusicClass> populateTable(ObservableList<MusicClass> songs){

//        songTable.getColumns().clear();
//        songTable.getItems().clear();

        // Song Name Column
        TableColumn<MusicClass,String> nameColumn = new TableColumn<>("Name: ");
        nameColumn.setMinWidth(400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("songTitle"));

        // Song Artist Column
        TableColumn<MusicClass,String> artistColumn = new TableColumn<>("Artist: ");
        artistColumn.setMinWidth(400);
        artistColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,String>("artistName"));

        // Song Duration Column
        TableColumn<MusicClass,Double> durationColumn = new TableColumn<>("Duration: ");
        durationColumn.setMinWidth(200);
        durationColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,Double>("durationTime"));

        // Song Year Released Column
        TableColumn<MusicClass,Integer> yearColumn = new TableColumn<>("Year: ");
        yearColumn.setMinWidth(200);
        yearColumn.setCellValueFactory(new PropertyValueFactory<MusicClass,Integer>("songYear"));

        songTable.getColumns().addAll(nameColumn, artistColumn, durationColumn, yearColumn);
        songTable.setItems(songs);

        return songTable;
    }

    @FXML
    public void button(ActionEvent event) throws IOException{

        if(event.getSource() == songButton) {
            populateTable(master);
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
            ObservableList<MusicClass> selectedPlaylistSongs = playlists.get(selectedPlaylist);
            populateTable(selectedPlaylistSongs);
        }
    }

    public void openCreatePlaylistWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreatePlaylist.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeletePlaylist.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeletePlaylist.fxml"));
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
        String fileName = "CECS-327-assignment1/music.json";

        try {
            Type musicClassType = new TypeToken<ObservableList<MusicClass>>() {
            }.getType();
            ArrayList<MusicClass> musicList = gson.fromJson(new FileReader(fileName), musicClassType);
            ObservableList<MusicClass> musicOList = FXCollections.observableArrayList(musicList);
            // musicList has ArrayList of MusicClass objects
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
