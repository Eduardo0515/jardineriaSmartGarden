/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Registro;

import Menu.ControllerMenu;
import inicio.Connector;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Hugo Ruiz
 */
public class ControllerRegistroRiego implements Initializable {
    Connection conns;
    @FXML private Button reg;
    @FXML private TableView<Riego> tabla;
    @FXML private TableColumn<Riego, Integer> productoCol;
    @FXML private TableColumn<Riego, String> fechaCol;
    @FXML private TableColumn<Riego, String> nombreCol;
    @FXML private TableColumn<Riego, String> tipoCol;
    @FXML private TableColumn<Riego, String> condicionCol;
    @FXML private TableColumn<Riego, Integer> idRegistroCol;
    @FXML private ComboBox productoCombo;
    @FXML private DatePicker pick1;
    @FXML private DatePicker pick2;
    @FXML private TextField idActualizar;
    @FXML private TextField idEliminar;
    @FXML private TextField nomM,tipoM,condM;
    
    public void setConnection(Connection conn){
        this.conns = conn;
    }
    
    public void listCombo(){
        productoCombo.setOnAction(e -> System.out.println("Action Nueva Selección: " + productoCombo.getValue()));
        productoCombo.valueProperty().addListener((ov, p1, p2) -> {
            Statement st;
                ResultSet rs;
                String query2 = "SELECT * FROM productos where IdProducto = "+Integer.parseInt(productoCombo.getValue().toString());
                try{
                    st = conns.createStatement();
                    rs = st.executeQuery(query2);
                    rs.next();
                        nomM.setText(rs.getString("nombre")); 
                        tipoM.setText(rs.getString("tipo"));
                        condM.setText(rs.getString("condicion")); 
                }catch(Exception e){
                    System.out.println("Error");
                }
        });
    }
    
    @FXML
    private void agregar(ActionEvent event) {
        try{
            String producto = productoCombo.getSelectionModel().getSelectedItem().toString();    
            int idProducto = Integer.parseInt(producto);
            //Connection connection = Connector.getConnection();
            String query = "insert into registroriego values ("+cod()+","+idProducto+",'"+pick1.getValue().toString()+"')";
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
    private void actualizar(ActionEvent event) {
        try{
            int idRegistro = Integer.parseInt(idActualizar.getText());
            //Connection connection = Connector.getConnection();
            String query= "UPDATE registroriego set FechaRiego='"+pick2.getValue().toString()+"' WHERE IdRegistro="+idRegistro;
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
            int idRegistro = Integer.parseInt(idEliminar.getText());
            //Connection connection = Connector.getConnection();
            String query = "DELETE FROM registroriego WHERE IdRegistro="+idRegistro;
            Statement st;
            try{
                st = conns.createStatement();
                st.executeUpdate(query);
                idEliminar.clear();
            }catch(Exception e){
                System.out.println("--");
            }
            getTipoProductList(event);
        }catch(Exception e){
            alerta();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showProductos();
        listener();       
        listCombo();
    }
    
    ObservableList<Riego> producList = FXCollections.observableArrayList();
    public void getTipoProductList(ActionEvent event) {
        producList.clear();
        //Connection connection = Connector.getConnection();
        String query = "select * from productos p, tiposproductos tp, registroriego r\n" +
"	where p.tipo = tp.IdTipo\n" +
"	and r.IdProducto = p.IdProducto ";
        Statement st,st2;
        ResultSet rs,rs2;
        try {
            st = conns.createStatement();
            rs = st.executeQuery(query);
            Riego riego;
            System.out.print(rs);
            while (rs.next()) {               
                riego = new Riego(rs.getString("FechaRiego"), rs.getInt("IdRegistro"),rs.getInt("IdProducto"), rs.getString("nombre"),rs.getString(7),rs.getString("condicion"));    
                producList.add(riego);
                tabla.setItems(producList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }
    
    public void listaProductos(ActionEvent event){
        Statement st;
        ResultSet rs;
        String query = "SELECT * FROM productos ";
        try{          
            productoCombo.getItems().clear();          
            st = conns.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                productoCombo.getItems().add(0,rs.getInt("IdProducto"));
            }
        }catch(Exception e){
            System.out.println("Error");
        }     
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
        tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // TODO Auto-generated method stub
                if(tabla.getSelectionModel().getSelectedItem()!=null) {                  
                   idActualizar.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdRegistro()));
                   idEliminar.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdRegistro()));
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

    public void showProductos() {
        //ObservableList<Riego> list = getTipoProductList();
        productoCol.setCellValueFactory(new PropertyValueFactory<Riego, Integer>("idProducto"));
        fechaCol.setCellValueFactory(new PropertyValueFactory<Riego, String>("fecha"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<Riego, String>("nombre"));
        tipoCol.setCellValueFactory(new PropertyValueFactory<Riego, String>("tipo"));
        condicionCol.setCellValueFactory(new PropertyValueFactory<Riego, String>("condicion"));
        idRegistroCol.setCellValueFactory(new PropertyValueFactory<Riego, Integer>("idRegistro"));
        //tabla.setItems(list);
    }
    
}
