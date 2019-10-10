package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.ProfileAccount;
import model.SingletonProfiles;
import rpc.CommunicationModule;
import rpc.Proxy;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Controller handling the sign up view. Provides functionality for signing up and
 * provides functionality for going to the sign in page after.
 * */
public class SignUpController extends Controller {

    @FXML
    TextField userField;

    @FXML
    PasswordField passwordField;

    /**
     * Button handler for signing up. Creates an account if information is good.
     * @param e
     */
    @FXML
    public void handleSubmitButtonAction( ActionEvent e ) throws Exception {
        String username;
        String password;

        SingletonProfiles singletonProfiles = SingletonProfiles.GetInstance();

        username = this.userField.getText();
        password = this.passwordField.getText();

//        if( username.isEmpty() || password.isEmpty() ){
//            Alert alert = new Alert( Alert.AlertType.ERROR );
//            alert.setContentText( "You must input both a user name and password to sign up.");
//            alert.show();
//            return;
//        }
//
//        if( singletonProfiles.contains(username) ) {
//            Alert alert = new Alert( Alert.AlertType.ERROR );
//            alert.setContentText( "Username already exists" );
//            alert.show();
//            return;
//        }
        Proxy proxy = Proxy.GetInstance();
        Gson gson = new Gson();
        JsonObject ret = proxy.synchExecution("registerAccount", new String[]{username, password});
        System.out.println( ret.get("ret"));
        ProfileAccount acc = gson.fromJson( ret.get("ret"), ProfileAccount.class );

        if(acc.getUsername().length() == 0) {
            Alert alert = new Alert( Alert.AlertType.ERROR);
            alert.setContentText("Invalid username or password.");
            alert.show();
            return;
        }

        LoadFXML(e, "Sign in", "/view/Sign_In.fxml");

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
}
