
package Historial;

/**
 *
 * @author Hugo Ruiz
 */
public class Historial {
    private int idProducto;
    private String nombre;
    private String tipo;
    private String fecha;
    private int idFotografia;

    public Historial(int idProducto, String fecha, int idFotografia) {
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.idFotografia = idFotografia;
    }

    public Historial(int idProducto, String nombre, String tipo, String fecha, int idFotografia) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.idFotografia = idFotografia;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdFotografia() {
        return idFotografia;
    }

    public void setIdFotografia(int idFotografia) {
        this.idFotografia = idFotografia;
    }
    
}
