package needs.adminpanel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import needs.importantclasses.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    final String password="Admin123456";
    @FXML
    private TextField passwordField;
    @FXML
    private Button enterButton;
    @FXML
    private void showButton(){
        if(passwordField.getText().length()>=8){
            System.out.println("1");
            enterButton.setDisable(false);
        }
        else
            enterButton.setDisable(true);
    }
    @FXML
    private void entry(){
        if(passwordField.getText().equals(password)){
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainWindow.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setTitle("Рабочее пространство");
            stage.setScene(scene);
            stage.show();
            enterButton.getScene().getWindow().hide();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }
}