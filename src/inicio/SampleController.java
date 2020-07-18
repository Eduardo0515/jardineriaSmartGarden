package inicio;

import Menu.ControllerMenu;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SampleController implements Initializable{    
    @FXML RadioButton mysql,sql;    
    @FXML
    private Button datosPersonales, ingresar;
    @FXML private TextField user,password;
    ToggleGroup groupButon;
    Group root;   
    Connection conn;

    @FXML
    public void datos(ActionEvent event) throws IOException {
        String path = "C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\AppJardineria\\src\\inicio\\datosVideo2.mp4";
        //Instantiating Media class  
        Media media = new Media(new File(path).toURI().toString());
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        //Instantiating MediaView class   
        MediaView mediaView = new MediaView(mediaPlayer);
        //by setting this property to true, the Video will be played   
        mediaPlayer.setAutoPlay(true);
        //setting group and scene        
        root = new Group();
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root, 850, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void irMenu(ActionEvent event) throws IOException {     
        RadioButton  radButon = (RadioButton)groupButon.getSelectedToggle();
        if(radButon.getText().equals("MySQL")){
              conn = Connector.getConnectionMySQL(password.getText(),user.getText());
        }
        else if(radButon.getText().equals("SQL Server")){
            conn = Connector.getConnectionSQLServer(password.getText(),user.getText());
        }
        if(conn!=null){
             FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Menu/menu.fxml"));
            loader.load();
            ControllerMenu controllerMenu = loader.getController();
            controllerMenu.setConnection(conn);
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
            Stage stage2 = (Stage) ingresar.getScene().getWindow();
            stage2.close();     
        }                  
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        groupButon = new ToggleGroup();
        mysql.setToggleGroup(groupButon);
        mysql.setSelected(true);
        sql.setToggleGroup(groupButon);
    }     
}
