package com.example.myapplication.Objetos;

public class ObjetoUsuario {

    private String nombre;
    private String contra;

    public ObjetoUsuario(String nombre, String contra) {
        this.nombre = nombre;
        this.contra = contra;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContra() {
        return contra;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }
}
