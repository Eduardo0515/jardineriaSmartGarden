/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Registro;

/**
 *
 * @author Hugo Ruiz
 */
public class Riego {
    private String fecha;
    private int idRegistro;
    private int idProducto;
    private String nombre;
    private String tipo;
    private String condicion;

    public Riego(String fecha, int idRegistro, int idProducto, String nombre, String tipo, String condicion) {
        this.fecha = fecha;
        this.idRegistro = idRegistro;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.condicion = condicion;
    }

    public Riego(int idProducto, String fecha, int idRegistro) {
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.idRegistro = idRegistro;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
    
}
