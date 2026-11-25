package com.carmotors.modelo;

public class RepuestoTipo {

    private Integer id;
    private String nombre;

    public RepuestoTipo() {
    }

    public RepuestoTipo(String nombre) {
        this.nombre = nombre;
    }

    public RepuestoTipo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "RepuestoTipo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}