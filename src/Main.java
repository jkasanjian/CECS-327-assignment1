import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;



public class Main {

    /**
     * Play a given audio file.
     //* @param audioFilePath Path of the audio file.
     */
    void mp3play(String file) {
        try {
            // It uses CECS327InputStream as InputStream to play the song
            InputStream is = new CECS327InputStream(file);
            Player mp3player = new Player(is);
            mp3player.play();
        }
        catch (JavaLayerException exception)
        {
            exception.printStackTrace();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ArrayList<MusicClass> master = readMusicJSON();

        // testing
        MusicClass song1 = master.get(0);
        System.out.println(song1.getSongTitle());
        System.out.println(song1.getSongArtist());
        System.out.println(song1.getSongID());

    }


    public static ArrayList<MusicClass> readMusicJSON(){
        Gson gson = new Gson();
        String fileName = "music.json";

        try{

            Type musicClassType = new TypeToken<ArrayList<MusicClass>>(){}.getType();
            ArrayList<MusicClass> musicList = gson.fromJson(new FileReader(fileName), musicClassType);
            // musicList has ArrayList of MusicClass objects
             return musicList;
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        return new ArrayList<>();
    }





}

