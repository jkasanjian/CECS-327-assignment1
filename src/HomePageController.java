
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
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;

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

    private MediaPlayer mediaPlayer;

    private Media media;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        songTable = populateTable(getAllSongs());
        String songFile = "Radioactive.mp3";
        media = new Media(Paths.get(songFile).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public ObservableList<Song> getAllSongs(){
        ObservableList<Song> songs = FXCollections.observableArrayList();
        songs.add(new Song("Radioactive", "Imagine Dragons", "3:10", "2012"));
        songs.add(new Song("Rescue Me", "One Republic", "2:39", "2019"));
        return songs;
    }

    public TableView<Song> populateTable(ObservableList<Song> songs){

        songTable.getItems().clear();
        songTable.getColumns().clear();

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

        songTable.getColumns().addAll(nameColumn, artistColumn, durationColumn, yearColumn);
        songTable.setItems(songs);

        return songTable;
    }

    @FXML
    public void button(ActionEvent event) throws IOException{

        if(event.getSource() == songButton) {
            populateTable(getAllSongs());
        } else if(event.getSource() == artistButton){
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
            String songName = songTable.getSelectionModel().getSelectedItem().getName();
            System.out.println(songName);
            if(songName.equals("Radioactive")){
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
            ObservableList<Song> selectedPlaylistSongs = playlists.get(selectedPlaylist);
            populateTable(selectedPlaylistSongs);
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

    public void deletePlaylist(String playlistName){
        for(int i = 0; i < displayPlaylists.getItems().size(); i++){
            if(displayPlaylists.getItems().get((i)) == playlistName){
                displayPlaylists.getItems().remove(i);
            }
        }
        playlists.remove(playlistName);
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
