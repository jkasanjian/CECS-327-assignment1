package rpc;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Scanner;

import com.google.gson.Gson;
import dfs.DFS;
import dfs.RemoteInputFileStream;
import model.MusicDatabase;
import model.ProfileDatabase;

public class Main implements Serializable {
   
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

        //RemoteInputFileStream remoteInputFileStream = new RemoteInputFileStream("profiles.json");
        //remoteInputFileStream.connect();
        //dfs.create("ProfilesJson");
        //dfs.append("ProfilesJson", remoteInputFileStream);
//        while(true) {
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Enter Command: ");
//            String token = scanner.nextLine();
//            if(token.equals("print"))
//                dfs.print();
//            else if(token.equals("ls"))
//                dfs.lists();
//            else {
//                System.out.println("Invalid Command");
//                break;
//            }
//        }
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

