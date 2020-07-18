package inicio;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class Connector {
    static Connection conn;
    public static Connection getConnectionMySQL(String passw, String user){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/jardineriabd?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatatimeCode=false&serverTimezone=UTC",user,passw);
            System.out.println("Conexion exitosa");
            System.out.println("Conectado a MySQL");
            return conn;              
        }
        catch(Exception e){
            alerta();
            System.out.println("Conexion fallida");
            return null;
        }
    } 
    
   public static Connection getConnectionSQLServer(String passw, String user){
        String url = "jdbc:sqlserver://LAPTOP-3P1MKQVM:1433;databaseName=JardineriaBD";
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url,user,passw);
            System.out.println("Conexion exitosa");
            System.out.println("Conectado a SQL Server");
            return conn;
        }
        catch(Exception e){
            alerta();
            System.out.println("Conexion fallida");
            return null;
        }
    }
   
    public static void alerta(){
        Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
        dialogo.setTitle("Error conexion");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Favor de verificar los datos ingresados");
        dialogo.initStyle(StageStyle.UTILITY);
        dialogo.showAndWait();
    }
}
