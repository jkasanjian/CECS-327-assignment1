package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.ProfileAccount;
import model.SingletonProfile;
import model.SingletonProfiles;
import javafx.scene.input.MouseEvent;
import rpc.Proxy;


/**
 * Controller handling the sign in view. Provides functionality and authorization for signing in and
 * provides functionality for going to the sign up page if no account.
 */
public class SignInController extends Controller {

    @FXML
    TextField userField;

    @FXML
    TextField passwordField;

    /**
     * Authorizes the user name and password from the inputs when the sign in button is clicked.
     * @param e The event from the clicking of the sign in button.
     */
    @FXML
    public void authorize( ActionEvent e ) throws Exception {
        String username;
        String password;

        SingletonProfiles singletonProfiles = SingletonProfiles.GetInstance();

        username = this.userField.getText();
        password = this.passwordField.getText();

        if( username.isEmpty() || password.isEmpty() ){
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "You must input both a user name and password to sign in.");
            alert.show();
            return;
        }

        Proxy proxy = Proxy.GetInstance();
        JsonObject ret = proxy.synchExecution("loginAccount", new String[]{"abc", "abc"});
        Gson gson = new Gson();
        ProfileAccount acc = gson.fromJson( ret.get("ret"), ProfileAccount.class );
        System.out.println( acc.getUsername() );
        SingletonProfile profile = SingletonProfile.GetInstance();
        profile.setUsername(acc.getUsername());
        profile.setPassword(acc.getPassword());
        profile.setSessionID(acc.getSessionID());
        profile.setPlaylists(acc.getPlaylists());

        LoadFXML(e, "Home Page", "/view/HomePage.fxml");
    }

    /**
     * Goes to the sign up view.
     * @param e
     */
    @FXML
    public void signupview( ActionEvent e) {
        System.out.println("sign up view");
        LoadFXML(e, "Sign up", "/view/Sign_Up.fxml");
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
