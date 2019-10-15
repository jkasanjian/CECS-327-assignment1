import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rpc.CommunicationModule;
import rpc.Proxy;

import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws java.io.IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Sign_In.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Sign In / Sign Up");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SocketException, UnknownHostException, FileNotFoundException {
        CommunicationModule communicationModule = CommunicationModule.GetInstance();
        communicationModule.init(2345);
        Proxy proxy = Proxy.GetInstance();
        proxy.init(communicationModule);
        launch(args);
    }


    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
    void mp3play(String file) {
        try {
            // It uses model.CECS327InputStream as InputStream to play the song
            InputStream is = new model.CECS327InputStream(file);
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
    */
}