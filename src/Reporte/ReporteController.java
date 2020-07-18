package Reporte;

import Menu.ControllerMenu;
import Registro.ControllerRegistroRiego;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ReporteController {
    Connection conns;
    @FXML
    private Button button;
    @FXML
    private LineChart<String, Integer> chartBarras;
    @FXML
    private CategoryAxis xAxis;
    private ObservableList<String> leyenda;
    @FXML Button reg;
    
    public void setConnection(Connection conn){
        this.conns = conn;
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
    
    @FXML
    private void btnGenera(ActionEvent event) throws Exception {
        //Button reporteBoton = (Button )event.getSource();
        chartBarras.getData().clear();
        String value = ((Button) event.getSource()).getText();
        Statement st = conns.createStatement();       
        String sql = "";
        String nombreReporte = "";
        switch(value){
            case "Productos por condicion actual":
                sql = "select condicion,count(*) Cantidad from productos " 
                      +" group by condicion ";
                nombreReporte = "Productos por condición actual";
            break;
            case "Fotografias por tipo de producto":
                sql = " select p.tipo,count(*) from productos p inner join historial h " 
                     +" on p.IdProducto = h.IdProducto "
                     +"  group by tipo";
                nombreReporte = "Fotografias por tipo de producto";
                break;
            case "Productos por tipo de producto":
                sql = "select tp.Tipo,count(*) from tiposproductos tp inner join productos p on tp.IdTipo = p.tipo"
        		+" group by tp.Tipo";
                nombreReporte = "Productos por tipo de producto";

                break;
            case "Riegos por dia":
                sql ="select FechaRiego,count(*) Cantidad from registroriego \n" +
                    "group by FechaRiego";
                nombreReporte = "Riegos por dia";             

                break;
              
        }  
        
        ResultSet rs = st.executeQuery(sql);         
        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<>();
        // crea la variable para almacenar las leyendas
        ObservableList<String> leyenda =  FXCollections.observableArrayList();
        
        while (rs.next()){    
            leyenda.add(""+rs.getString(1)); // se agrega como leyenda el campo que que quiera, debe coincidir con la serie
            dataSeries1.getData().add(new XYChart.Data<>(""+rs.getString(1), rs.getInt(2))); // posicion 1 es la leyenda 
        }
        dataSeries1.setName(nombreReporte);
        xAxis.setCategories(leyenda);  // se asigna la leyenda a la grafica
        chartBarras.getData().addAll(dataSeries1); // se agrega la serie de datos   
    }
}
