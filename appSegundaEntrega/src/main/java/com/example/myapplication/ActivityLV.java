package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityLV extends AppCompatActivity {

    private TextView tvLV;
    private ListView lv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);

        tvLV = (TextView) findViewById(R.id.tv1);
        lv1 = (ListView) findViewById(R.id.lv1);

        ArrayAdapter <Objetos> adapter = new ArrayAdapter<Objetos>(this, R.layout.list_item_gorka, llenarTareas());
        lv1.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_lv);
        lv1 = (ListView) findViewById(R.id.lv1);
        ArrayAdapter <Objetos> adapterResume = new ArrayAdapter<Objetos>(this, R.layout.list_item_gorka, llenarTareas());
        lv1.setAdapter(adapterResume);
    }

    public void reg(View view){
        Intent toTareas = new Intent(this, Tareas.class);
        startActivity(toTareas);
    }

    public ArrayList<Objetos> llenarTareas(){
        ArrayList<Objetos> lista = new ArrayList<>();
        Objetos tarea = new Objetos
                (0, "Tarea de muestra", "Así deberían verse las tareas");
        lista.add(tarea);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery
                    ("select * from tarea", null);

        if(fila.moveToFirst()){
            tarea.update((int)fila.getInt(0), fila.getString(1), fila.getString(2));
            lista.add(tarea);
        }else{
            tarea.update(0, "No hay tareas", "Aún no ha sido introducida ninguna tarea en la base de daros");
            lista.add(tarea);
        }


        while (fila.moveToNext()){
            tarea.update((int)fila.getInt(0), fila.getString(1), fila.getString(2));
            lista.add(tarea);
        }

        return lista;
    }

}