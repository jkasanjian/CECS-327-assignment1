package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ProfileAccount;
import model.Profiles;

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
    public void handleSubmitButtonAction( ActionEvent e ){
        String username;
        String password;

        Profiles profiles = Profiles.GetInstance();

        username = this.userField.getText();
        password = this.passwordField.getText();

        if( username.isEmpty() || password.isEmpty() ){
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "You must input both a user name and password to sign up.");
            alert.show();
            return;
        }

        if( profiles.contains(username) ) {
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "Username already exists" );
            alert.show();
            return;
        }

        ProfileAccount profileAccount = new ProfileAccount(username, password);
        profiles.addProfile(profileAccount);

        LoadFXML(e, "Sign in", "/view/Sign_In.fxml");
    }
}
