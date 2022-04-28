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
