package rpc;

import java.io.IOException;

import com.google.gson.Gson;
import dfs.DFS;
import model.MusicDatabase;
import model.ProfileDatabase;

public class Main {
   
      /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
      /**
    void mp3play(Long file, ProxyInterface proxy) {
        try {
            // It uses CECS327InputStream as InputStream to play the song 
             InputStream is = new CECS327InputStream(file, proxy);
             Player mp3player = new Player(is);
             mp3player.play();
	}
	catch (JavaLayerException ex) {
	    ex.printStackTrace();
	}
	catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }
       */
     
     /*
     *  The function test the classes rpc.Dispatcher, rpc.SongDispatcher
     *  and CECS327InputStream. Proxy is incomplete.
    */
    public static void main(String[] args) throws Exception {
        DFS dfs = new DFS(9002);
        dfs.join("127.0.0.1", 9000);
        MusicDatabase.GetInstance().setDfs(dfs);
        ProfileDatabase.GetInstance().setDfs(dfs);
        Dispatcher dispatcher = new Dispatcher();
        SongDispatcher songDispatcher       = new SongDispatcher();
        AccountDispatcher accountDispatcher = new AccountDispatcher();
        PlaylistDispatcher playlistDispatcher = new PlaylistDispatcher();

        dispatcher.registerObject(songDispatcher, "SongServices");
        dispatcher.registerObject(accountDispatcher, "LoginServices");
        dispatcher.registerObject(playlistDispatcher, "PlaylistServices");
        CommunicationModule comm = new CommunicationModule( 2345 , dispatcher );
        comm.listen();
        //ProxyInterface proxy = new Proxy(dispatcher);

        //rpc.Main player = new rpc.Main();
        //player.mp3play(490183L, proxy);
        System.out.println("End of the song");

    }
 
}

