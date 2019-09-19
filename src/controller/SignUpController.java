package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ProfileAccount;
import model.SingletonProfiles;

public class SignUpController extends Controller {

    @FXML
    TextField userField;

    @FXML
    PasswordField passwordField;

    @FXML
    public void handleSubmitButtonAction( ActionEvent e ){
        String username;
        String password;

        SingletonProfiles singletonProfiles = SingletonProfiles.GetInstance();

        username = this.userField.getText();
        password = this.passwordField.getText();

        if( username.isEmpty() || password.isEmpty() ){
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "You must input both a user name and password to sign up.");
            alert.show();
            return;
        }

        if( singletonProfiles.contains(username) ) {
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "Username already exists" );
            alert.show();
            return;
        }

        ProfileAccount profileAccount = new ProfileAccount(username, password);
        singletonProfiles.addProfile(profileAccount);

        LoadFXML(e, "Sign in", "/view/Sign_In.fxml");
    }
}
