package com.example.myapplication;

public class Objetos {
    private String nombre;
    private int id;
    private String desc;

    public Objetos(int iD, String nm, String dsc){
        this.id = iD;
        this.nombre = nm;
        this.desc = dsc;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void update(int iD, String nm, String dsc){
        this.id = iD;
        this.nombre = nm;
        this.desc = dsc;
    }

    public int getID(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getDesc(){
        return desc;
    }


}
