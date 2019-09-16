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



        Gson gson = new Gson();
        String fileName = "music.json";

        try{

            Type musicClassType = new TypeToken<ArrayList<MusicClass>>(){}.getType();
            List<MusicClass> musicList = gson.fromJson(new FileReader(fileName), musicClassType);
            // musicList has list of MusicClass objects

            // testing: getting name of third song
            MusicClass first = musicList.get(2);
            System.out.println(first.getSong().getTitle());
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }










    }

}

