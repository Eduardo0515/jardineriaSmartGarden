
package tipoProductos;

/**
 *
 * @author Hugo Ruiz
 */
public class TipoProducto {
    private int idtipo;
    private String tipo;
    
    public TipoProducto(){}

    public TipoProducto(String tipo, int idtipo) {
        this.idtipo = idtipo;
        this.tipo = tipo;
    }

    public int getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
