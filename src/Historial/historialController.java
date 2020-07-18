package Historial;

import Menu.ControllerMenu;
import inicio.Connector;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class historialController implements Initializable {
    @FXML private TableView<Historial> tabla;
    @FXML private TableColumn<Historial, Integer> idProductoCol;
    @FXML private TableColumn<Historial, String> fechaCol;
    @FXML private TableColumn<Historial, String> nombreCol;
    @FXML private TableColumn<Historial, String> tipoCol;
    @FXML private TableColumn<Historial, Integer> idFotografiaCol;
    @FXML ImageView slideshowImageView;
    @FXML ImageView visualizarImagen;
    @FXML Button reg;
    @FXML Button fotografia;
    @FXML TextField ruta;
    @FXML TextField idA;
    @FXML TextField idE,nomM,tipoM,condM;
    @FXML DatePicker fecha;
    @FXML private ComboBox productoCombo;
    @FXML private ComboBox actualizacion;
    Historial historialListener;
    int slideshowCount = 0;
    Connection conns;
    
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
        tm.cancel();
    }
    
    @FXML
    private void agregar(ActionEvent event) {
        try{
            FileInputStream fis = null;          
            String sql = "INSERT INTO historial (IdProducto, Fotografia, FechaFotografia,IdFotografia)\n" +
                         "VALUES (?,?,?,?);";
            PreparedStatement ps = null;
            int id = Integer.parseInt(productoCombo.getSelectionModel().getSelectedItem().toString());
            try{                       
                ps = conns.prepareStatement(sql);
                ps.setInt(1,id);
                ps.setInt(4,cod());
                ps.setString(3, fecha.getValue().toString());           
                try{
                    File file = new File(ruta.getText());
                    fis = new FileInputStream(file);   
                    ps.setBinaryStream(2, fis,(int)file.length());
                }catch(Exception e1){
                     ps.setBinaryStream(2, null);
                     System.out.println("Erro F");
                }           
                ps.executeUpdate();        
                visualizarImagen.setImage(null);
            }catch(SQLException ex){
                System.out.println("A exMessage");
                alerta();
            }catch(Exception ex){
                System.out.println("B exMessage");
                alerta();
            }finally{
                try{
                    ps.close();                  
                }catch(Exception ex){}
            }

            tm.cancel();

            getTipoProductList(event);       
            showImages(event); 
        }catch(Exception e){
            alerta();
        }
    }
    
    @FXML 
    private void actualizar(ActionEvent event){
        try{
            if(actualizacion.getSelectionModel().getSelectedItem().toString().equals("Fotografia")){
                upDate();
                visualizarImagen.setImage(null);
            }else if(actualizacion.getSelectionModel().getSelectedItem().toString().equals("Fecha")){
                int IdFotografia = Integer.parseInt(idA.getText());
                String queryUpd = "UPDATE historial SET FechaFotografia='"+fecha.getValue().toString()+"' WHERE IdFotografia="+IdFotografia;
                executeQuery(queryUpd);
            }
            getTipoProductList(event);
            showImages(event);
        }catch(Exception e){
            alerta();
        }
    }
    
    @FXML
    private void eliminar(ActionEvent event){
        try{
            int IdFotografia = Integer.parseInt(idE.getText());
            String query = "DELETE FROM historial WHERE IdFotografia="+IdFotografia;
            Statement st;
            try{
                st = conns.createStatement();                
                st.executeUpdate(query);
                visualizarImagen.setImage(null);
                idE.clear();
            }catch(Exception e){
                alerta();
                System.out.println("--");
            }
            tm.cancel();
            getTipoProductList(event);       
            showImages(event);
        }catch(Exception e){
            alerta();
        }
    }
    
    ArrayList<Image> array = new ArrayList<Image>();
    public void getImageList() {    
        array.clear();
        String query = "SELECT * FROM historial ";
        Statement st;
        ResultSet rs;
         System.out.println("rsdes");
        try {
            st = conns.createStatement();
            rs = st.executeQuery(query);
            System.out.print(rs);
            while (rs.next()) {               
                byte byteImage[] = null;
                try{                   
                    Blob blob = rs.getBlob("Fotografia");
                    byteImage = blob.getBytes(1, (int) blob.length());
                    Image img = new Image(new ByteArrayInputStream(byteImage));
                    array.add(img);
                   
                }catch(Exception es){  
                    System.out.println("No image in DB");
                }              
            }
        } catch (Exception e) {
            System.out.println("Error array");
        }       
    }
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showProductos();
        seleccionarFotografia();
        mostrarFotografias();
        llenarCombo();
        listCombo();
    }
    
    public int count = 0;
    Timer tm;
    public void showImages(ActionEvent event) {
        count =0;  
        // then in your method
            tm = new Timer(); 
            getImageList();
            long delay = 2000; //update once per 2 seconds.
            tm.schedule(new TimerTask() {
                @Override
                public void run() {
                    slideshowImageView.setImage(array.get(count++));
                    if (count >= array.size()) {
                        count = 0;
                    }
                }
            }, 0, delay);  
    }
    
    ObservableList<Historial> producList = FXCollections.observableArrayList();
    public  void  getTipoProductList(ActionEvent event) {     
        producList.clear();
        String query = "select * from productos p, tiposproductos tp, historial h\n" +
                       "where p.tipo = tp.IdTipo\n" +
                       "and h.IdProducto = p.IdProducto";
        Statement st;
        ResultSet rs;

        try {
            st = conns.createStatement();          
            rs = st.executeQuery(query);             
            Historial historial;           
            while (rs.next()) {                  
                historial = new Historial(rs.getInt("IdProducto"), rs.getString("nombre"), rs.getString(7),rs.getString("FechaFotografia"),rs.getInt("IdFotografia"));                            
                producList.add(historial);                 
            }
            tabla.setItems(producList);
             
        } catch (Exception e) {
            System.out.println("Error en getProductList");
        }     
    }
    
    public void agrearIdProductos(ActionEvent event){
        Statement st;
        ResultSet rs;
        String query2 = "SELECT * FROM productos  ";       
        try{                
            st = conns.createStatement();           
            rs = st.executeQuery(query2);           
            while(rs.next()){
                productoCombo.getItems().add(0,rs.getInt("IdProducto"));
            }
        }catch(Exception e){
            System.out.println("Error");
        }    
    }
    
    public void seleccionarFotografia(){
        fotografia.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar Imagen");
            // Agregar filtros para facilitar la busqueda
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            // Obtener la imagen seleccionada
            File imgFile = fileChooser.showOpenDialog(null);
            // Mostar la imagen
            if (imgFile != null) {
                ruta.setText(imgFile.getAbsolutePath());
                System.out.println("Absolute path "+imgFile.getAbsolutePath());
                System.out.println(imgFile.getAbsolutePath());
                //Image image = new Image("file:" + imgFile.getAbsolutePath());
            }
        });
    }
    
    public void mostrarFotografias(){
        tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // TODO Auto-generated method stub
                if(tabla.getSelectionModel().getSelectedItem()!=null) {
                    historialListener = tabla.getSelectionModel().getSelectedItem();
                    String query = "Select Fotografia from historial where IdFotografia = "+historialListener.getIdFotografia();
                    Statement st;
                    ResultSet rs;
                    try {
                        st = conns.createStatement();
                        rs = st.executeQuery(query);                                               
                        rs.next();              
                            byte byteImage[] = null;
                            Blob blob = rs.getBlob("Fotografia");
                            byteImage = blob.getBytes(1, (int) blob.length());
                            Image img = new Image(new ByteArrayInputStream(byteImage)); 
                            visualizarImagen.setImage(img);  
                            idE.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdFotografia()));
                            idA.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdFotografia()));
                    } catch (Exception e) {    
                        idE.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdFotografia()));
                        idA.setText(String.valueOf(tabla.getSelectionModel().getSelectedItem().getIdFotografia()));
                        Image image = new Image("file:" + "C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\AppJardineria\\src\\Historial\\sinImagen.jpg");
                         visualizarImagen.setImage(image);
                        System.out.println("No image");
                    }
                }
            }		
        });
    }
    
    public void upDate() { 
        PreparedStatement ps=null;
         try{                 
            String update = "UPDATE historial set Fotografia = ? where IdFotografia = ?";
            int IdFotografia = Integer.parseInt(idA.getText());
            ps= conns.prepareStatement(update); 
            ps.setInt(2, IdFotografia);
            try{
                File file = new File(ruta.getText());
                InputStream imgs = new FileInputStream(ruta.getText());    
                ps.setBlob(1, imgs);          
            }catch(Exception e1){
                ps.setBinaryStream(1, null);
            }
            ps.executeUpdate();
        }catch(SQLException ex){
            alerta();
            System.out.println("A ex.getMessage()");
        }catch(Exception ex){
            alerta();
            System.out.println("B ex.getMessage()");
        }finally{
            try{
                ps.close();                  
            }catch(Exception ex){}
        }
    }
    
    public void executeQuery(String query) {
        Statement st;
        try {
            st = conns.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            alerta();
        }
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
                    condM.setText(rs.getString("condicion"));
                }catch(Exception e){
                    System.out.println("Error");
                }
        });
    }
    
    public int cod(){
        int dig3= (int)(100000000 * Math.random());
        return dig3;
    }
    
    public void llenarCombo(){
        actualizacion.getItems().add("Fotografia");
        actualizacion.getItems().add("Fecha");
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
        //ObservableList<Historial> list = getTipoProductList(events);
        idProductoCol.setCellValueFactory(new PropertyValueFactory<Historial, Integer>("idProducto"));
        fechaCol.setCellValueFactory(new PropertyValueFactory<Historial, String>("fecha"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<Historial, String>("nombre"));
        tipoCol.setCellValueFactory(new PropertyValueFactory<Historial, String>("tipo"));
        idFotografiaCol.setCellValueFactory(new PropertyValueFactory<Historial, Integer>("idFotografia"));
        //tabla.setItems(list);
    }

}