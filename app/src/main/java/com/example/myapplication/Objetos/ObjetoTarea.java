package com.example.myapplication.Objetos;

public class ObjetoTarea {
    private String nombre;
    private int id;
    private String desc;

    public ObjetoTarea(int iD, String nm, String dsc){
        this.id = iD;
        this.nombre = nm;
        this.desc = dsc;
    }
    public ObjetoTarea(){

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
