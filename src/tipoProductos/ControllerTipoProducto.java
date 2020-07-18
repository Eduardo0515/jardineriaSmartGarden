
package tipoProductos;

import Menu.ControllerMenu;
import inicio.Connector;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import inicio.SampleController;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
/**
 *
 * @author Hugo Ruiz
 */
public class ControllerTipoProducto implements Initializable {
    Connection conns;
    @FXML private Button reg;
    @FXML 
    private TableView<TipoProducto> tabla;
    @FXML private TableColumn<TipoProducto,Integer>identificador;
    @FXML private TableColumn<TipoProducto,String> tipoCol;
    @FXML private TextField tipoC;
    @FXML private TextField tipoA;
    @FXML private TextField idA;
    @FXML private TextField idE;
    
    public void setConnection(Connection conn){
        this.conns = conn;
    }
    
    @FXML
    private void agregar(ActionEvent event) {   
        try{
            String producto = tipoC.getText();       
            String query = "insert into tiposproductos values ("+cod()+",'"+producto+"')";
            Statement st;
            try{
                st = conns.createStatement();
                st.executeUpdate(query);
            }catch(Exception e){
                System.out.println("Ag--");
            }
            getTipoProductList(event);       
        }catch(Exception e){
            alerta();
        }         
    }
                                                                                                                                                            
    @FXML
    private void actualizar(ActionEvent event) {
        try{
            int idTipo = Integer.parseInt(idA.getText());
            //Connection connection = Connector.getConnection();
            String query= "UPDATE tiposproductos set Tipo='"+tipoA.getText()+"' WHERE IdTipo="+idTipo;
            Statement st;
            try{
                st = conns.createStatement();
                st.executeUpdate(query);
            }catch(Exception e){
                System.out.println("--");
            }
            getTipoProductList(event);
        }catch(Exception e){
            alerta();
        }
    }
    
    @FXML
    private void eliminar(ActionEvent event){
        try{
            int idTipo = Integer.parseInt(idE.getText());
            //Connection connection = Connector.getConnection();
            String query = "DELETE FROM tiposproductos WHERE IdTipo="+idTipo;        
            Statement st;
            try{
                st = conns.createStatement();
                st.executeUpdate(query);
                idE.clear();
            }catch(Exception e){
                System.out.println("--");
            }
            getTipoProductList(event);
        }catch(Exception e){
            alerta();
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle r) {
        //showProductos();   
        listener();
    }  
    ObservableList<TipoProducto> producList = FXCollections.observableArrayList();
    public void getTipoProductList(ActionEvent event){ 
         producList.clear();
        //Connection connection = Connector.getConnection();
        String query = "SELECT * FROM tiposproductos ";
        Statement st;
        ResultSet rs;
        try{
             System.out.println("4");
            st = conns.createStatement();
              System.out.println("5");
            rs = st.executeQuery(query);
              System.out.println("6");
            TipoProducto tipoProducto;
            System.out.print(rs);
            while(rs.next()){
                tipoProducto = new TipoProducto(rs.getString("Tipo"), rs.getInt("IdTipo"));
                System.out.println("Id: "+tipoProducto.getIdtipo());
                producList.add(tipoProducto);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        tabla.setItems(producList);
        //return producList;
    }
     
    @FXML
    public void regresar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Menu/menu.fxml"));
        loader.load();
        ControllerMenu controllerMenu = loader.getController();
        controllerMenu.setConnection(conns);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        Stage stage2 = (Stage) reg.getScene().getWindow();
        stage2.close();
    }
    
    public void listener(){
        //Connection connection = Connector.getConnection();
        tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // TODO Auto-generated method stub
                if(tabla.getSelectionModel().getSelectedItem()!=null) {
                    tipoA.setText(tabla.getSelectionModel().getSelectedItem().getTipo()); 
                    idA.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdtipo()));
                    idE.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdtipo()));
                }
            }		
        }); 
    }
    
    public int cod(){
        int dig3= (int)(100000000 * Math.random());
        return dig3;
    }
    
    public static void alerta(){
        Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
        dialogo.setTitle("Error");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Verificar \n(a)El llenado de los campos solicitados y/o \n(b)Los datos ingresados");
        dialogo.initStyle(StageStyle.UTILITY);
        dialogo.showAndWait();
    }
    
    public void showProductos(ActionEvent event) {
        //ObservableList<TipoProducto> list = getTipoProductList(event);
      //  identificador.setCellValueFactory(new PropertyValueFactory<TipoProducto,Integer>("iport"));
      //  tipoCol.setCellValueFactory(new PropertyValueFactory<TipoProducto,String>("tipo"));     
        
    }     
}
