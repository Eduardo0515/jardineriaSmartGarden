package Productos;

import Menu.ControllerMenu;
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

public class productoController implements Initializable {

    Connection conns;
    @FXML
    TextField nombretxt, idtxt, textU, textE, tipo;
    @FXML
    ComboBox condicion,comboTipo;
    @FXML
    DatePicker fechatxt;
    @FXML
    Button reg;

    @FXML
    private TableView<ProductoJ> tabla;
    private int id = 0;

    @FXML private TableColumn<ProductoJ, Integer> idCol;
    @FXML private TableColumn<ProductoJ, String> tipoCol;
    @FXML private TableColumn<ProductoJ, String> nombreCol;
    @FXML private TableColumn<ProductoJ, String> fechaCol;
    @FXML private TableColumn<ProductoJ, String> condicionCol;

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

    public void setConnection(Connection conn) {
        this.conns = conn;
    }
    
    ObservableList<ProductoJ> producList = FXCollections.observableArrayList();
    
    public void getProductoList(ActionEvent event) {
        producList.clear();
        String query = "select tp.Tipo, p.nombre, p.IdProducto, p.condicion,p.fechaIngreso from productos p inner join tiposproductos tp on p.tipo = tp.IdTipo ";
        Statement st;
        ResultSet rs;
        try {
            st = conns.createStatement();
            rs = st.executeQuery(query);

            ProductoJ producto1;
            System.out.print(rs);
            while (rs.next()) {
                producto1 = new ProductoJ(rs.getString("Tipo"), rs.getInt("IdProducto"), rs.getString("nombre"), rs.getString("fechaIngreso"), rs.getString("condicion"));
                producList.add(producto1);
            }
            tabla.setItems(producList);
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        idCol.setMinWidth(80);
        tipoCol.setMinWidth(80);
        nombreCol.setMinWidth(100);
        fechaCol.setMinWidth(90);
        condicionCol.setMinWidth(80);
        showProductos();
        //llenarCombo();

        tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // TODO Auto-generated method stub
                if (tabla.getSelectionModel().getSelectedItem() != null) {
                    ProductoJ producto = tabla.getSelectionModel().getSelectedItem();
                    textU.setText(String.valueOf(producto.getId()));
                    textE.setText(String.valueOf(producto.getId()));
                }
            }
        });
    }

    public void llenarCombo() {
        condicion.getItems().clear();
        condicion.getItems().add("Buen estado");
        condicion.getItems().add("Falta de agua");
        condicion.getItems().add("Exceso de agua");
        condicion.getItems().add("Con plaga");
    }

    public void executeQuery(String query) {
        Statement st;
        try {
            st = conns.createStatement();
            st.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void showProductos() {
        tipoCol.setCellValueFactory(new PropertyValueFactory<ProductoJ, String>("tipo"));
        idCol.setCellValueFactory(new PropertyValueFactory<ProductoJ, Integer>("id"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<ProductoJ, String>("nombre"));
        fechaCol.setCellValueFactory(new PropertyValueFactory<ProductoJ, String>("fecha"));
        condicionCol.setCellValueFactory(new PropertyValueFactory<ProductoJ, String>("condicion"));
        llenarCombo();
        listCombo();
    }

    public void llenarTipo(ActionEvent event) {
        comboTipo.getItems().clear();
        // TODO Auto-generated method stub
        String query = "SELECT * FROM tiposproductos ";
        Statement st;
        ResultSet rs;
        try {
            st = conns.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {               
                comboTipo.getItems().add(rs.getInt("IdTipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void agregar(ActionEvent event) {
        try {
            int tipo = Integer.parseInt(comboTipo.getSelectionModel().getSelectedItem().toString());
            String query = "insert into productos values(" + tipo+ "," + cod() + ",'" + nombretxt.getText() + "','" + fechatxt.getValue() + "','" + condicion.getSelectionModel().getSelectedItem().toString() + "')";
            executeQuery(query);
            getProductoList(event);
        } catch (Exception e) {
            alerta();
        }
    }

    public int cod() {
        id = (int) (100000000 * Math.random());
        return id;
    }

    @FXML
    public void editar(ActionEvent event) {
        try {
            int tipo = Integer.parseInt(comboTipo.getSelectionModel().getSelectedItem().toString());
            String query = "UPDATE productos SET tipo=" + tipo + ",nombre='" + nombretxt.getText() + "',condicion='" + condicion.getSelectionModel().getSelectedItem().toString() + "' WHERE IdProducto=" + Integer.parseInt(textU.getText()) + "";
            executeQuery(query);
            getProductoList(event);
        } catch (Exception e) {
           alerta();
        }
    }

    @FXML
    public void eliminar(ActionEvent event) {
        try {
            String query = "DELETE FROM productos WHERE IdProducto=" + Integer.parseInt(textE.getText()) + "";
            executeQuery(query);
            getProductoList(event);
        } catch (Exception e) {
            alerta();
        }
    }
    
    @FXML
    public void listCombo(){
        //comboTipo.setOnAction(e -> System.out.println("Action Nueva Selección: " + comboTipo.getValue()));
        comboTipo.valueProperty().addListener((ov, p1, p2) -> {
            Statement st;
                ResultSet rs;
                String query2 = "SELECT * FROM tiposproductos where IdTipo = "+Integer.parseInt(comboTipo.getValue().toString());
                try{
                    st = conns.createStatement();
                    rs = st.executeQuery(query2);
                    rs.next();
                        tipo.setText(rs.getString("Tipo")); 
                }catch(Exception e){
                    System.out.println("Error");
                }
        });
    }

    public void alerta() {
        Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
        dialogo.setTitle("ERROR");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Error en ingreso de datos");
        dialogo.initStyle(StageStyle.UTILITY);
        dialogo.showAndWait();
    }
}
