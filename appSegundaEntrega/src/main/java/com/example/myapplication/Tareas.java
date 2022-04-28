package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLData;

public class Tareas extends AppCompatActivity {

    private TextView idTV, nombreTV, descTV;
    //private Button btA, btEdit, btElim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tareas);

        idTV = (TextView) findViewById(R.id.id);
        nombreTV = (TextView) findViewById(R.id.nom);
        descTV = (TextView) findViewById(R.id.desc);

    }

    public void Registrar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String id = idTV.getText().toString();
        String nombre = nombreTV.getText().toString();
        String desc = descTV.getText().toString();

        if(!id.isEmpty() && !nombre.isEmpty() && !desc.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("codigo", id);
            registro.put("nombre", nombre);
            registro.put("descripcion", desc);
            db.insert("tarea", null, registro);
            db.close();
            idTV.setText("");
            nombreTV.setText("");
            descTV.setText("");
            Toast.makeText(this, "Registro realizado", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Revisa haber completado todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    public void Buscar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String id = idTV.getText().toString();

        if(!id.isEmpty()){
            Cursor fila = db.rawQuery
                    ("select nombre, descripcion from tarea where codigo ="+id, null);

            if(fila.moveToFirst()){
                nombreTV.setText(fila.getString(0));
                descTV.setText(fila.getString(1));
            }else{
                Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_LONG).show();
                db.close();
            }
        }else {
            Toast.makeText(this, "Revisa haber metido el identificador", Toast.LENGTH_LONG).show();
        }
    }

    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String id = idTV.getText().toString();

        if(!id.isEmpty()){
            int cantidad = db.delete("tarea", "codigo ="+id, null);
            db.close();
            idTV.setText("");
            nombreTV.setText("");
            descTV.setText("");

            if(cantidad == 1){
                Toast.makeText(this, "Tarea eliminada correctamente", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(this, "La tarea no existe", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Revisa haber introducido el identificador", Toast.LENGTH_LONG).show();
            db.close();
        }
    }

    public void Editar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String id = idTV.getText().toString();
        String nombre = nombreTV.getText().toString();
        String desc = descTV.getText().toString();

        if(!id.isEmpty() && !nombre.isEmpty() && !desc.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("codigo", id);
            registro.put("nombre", nombre);
            registro.put("descripcion", desc);
            int cantidad = db.update("tarea", registro, "codigo =" + id, null);
            db.close();
            if (cantidad == 1){
                Toast.makeText(this, "Tarea editada correctamente", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "La tarea que tratas de editar no existe", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Revisa haber completado todos los campos", Toast.LENGTH_LONG).show();
        }
    }

}