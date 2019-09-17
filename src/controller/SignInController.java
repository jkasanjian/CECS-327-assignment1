package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Profile;
import model.Profiles;


public class SignInController extends Controller {
    @FXML
    Label gUserNameLabel;
    @FXML
    TextField gUserNameTextField;
    @FXML
    Label gPasswordLabel;
    @FXML
    TextField gPasswordTextField;
    @FXML
    Button gSignInButton;
    @FXML
    Button gSignUpButton;

    @FXML
    public void initialize(){
    }

    public SignInController(){ };

    @FXML
    public void authorize( MouseEvent e ){
        System.out.println("HELLO");
        String username;
        String password;

        Profiles profiles = Profiles.GetInstance();

        username = this.gUserNameTextField.getText();
        password = this.gPasswordTextField.getText();

        if( username.isEmpty() || password.isEmpty() ){
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "You must input both a user name and password to sign in.");
            alert.show();
            return;
        }

        if( !profiles.verify(username, password) ) {
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setContentText( "username or password is incorrect" );
            alert.show();
            return;
        }

        // TODO:: Load profile scene view and set static USER
        // LoadFXML(e, "MyProfile", "/view/ProfileAccount.fxml")

        Profile profile = new Profile(username, password);

        System.out.println("Current User: " + profile.getUsername());
    }

    @FXML
    public void signupview( MouseEvent e) {
        System.out.println("sign up view");
        LoadFXML(e, "Sign up", "/view/Sign_Up.fxml");
    }

}
