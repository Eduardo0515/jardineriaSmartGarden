package Menu;

import Historial.historialController;
import Productos.productoController;
import Registro.ControllerRegistroRiego;
import Reporte.ReporteController;
import java.io.IOException;
import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tipoProductos.ControllerTipoProducto;

public class ControllerMenu {
    Connection conn;
    @FXML
    Button cerrar, tip, prod, riego, histo,reporte;

    @FXML
    public void cerrarSesion(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/inicio/Sample.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();

        Stage stage2 = (Stage) cerrar.getScene().getWindow();
        // do what you have to do
        stage2.close();
    }

    @FXML
    public void irTipo(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/tipoProductos/tipo.fxml"));
        loader.load();
        ControllerTipoProducto controllerTipo = loader.getController();
        controllerTipo.setConnection(conn);
        controllerTipo.getTipoProductList(event);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        Stage stage2 = (Stage) tip.getScene().getWindow();
        stage2.close();
    }

    @FXML
    public void irProductos(ActionEvent event) throws IOException {        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Productos/producto.fxml"));
        loader.load();
        productoController controllerProducto = loader.getController();
        controllerProducto.setConnection(conn);
        controllerProducto.getProductoList(event);
        controllerProducto.llenarTipo(event);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        Stage stage2 = (Stage) prod.getScene().getWindow();
        stage2.close();
    }

    @FXML
    public void irRiego(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Registro/registro.fxml"));
        loader.load();
        ControllerRegistroRiego controllerRegistro = loader.getController();
        controllerRegistro.setConnection(conn);
        controllerRegistro.getTipoProductList(event);
        controllerRegistro.listaProductos(event);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        Stage stage2 = (Stage) riego.getScene().getWindow();
        stage2.close();
    }

    @FXML
    public void irHisto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Historial/historial.fxml"));
        loader.load();
        
        historialController controllerHistorial = loader.getController();
        controllerHistorial.setConnection(conn);
        controllerHistorial.getTipoProductList(event);
        controllerHistorial.showImages(event);
        controllerHistorial.agrearIdProductos(event);
                
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        Stage stage2 = (Stage) histo.getScene().getWindow();
        stage2.close();
    }

    @FXML
    public void irReportes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Reporte/reporte.fxml"));
        loader.load();
        ReporteController controllerReporte = loader.getController();
        controllerReporte.setConnection(conn);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
        Stage stage2 = (Stage) reporte.getScene().getWindow();
        stage2.close();
        
    }
    
    public void setConnection(Connection conn){
        this.conn = conn;
    }
    
    public Connection getConnection(){
        return conn;
    }
}
