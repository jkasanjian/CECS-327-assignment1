import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;



public class Main {

    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
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


    /*
    public static void main(String[] args) {
        Integer i;
        Main player = new Main();
        player.mp3play("imperial.mp3");
    }
    */

}

