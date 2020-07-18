/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Productos;

/**
 *
 * @author Hugo Ruiz
 */
public class ProductoJ {

    private int id;
    private String tipo;
    private String nombre;
    private String fecha;
    private String condicion;

    ProductoJ(String tipo, int id, String nombre, String fecha, String condicion) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.condicion = condicion;
    }
    
    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

}
