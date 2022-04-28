package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText usuario, contra;
    private ListView lv1;
    private CheckBox cb;

    private String nombres [] = {};
    private String edades [] = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.usu);
        contra = (EditText) findViewById(R.id.contra);
        cb = (CheckBox) findViewById(R.id.recordar);


        SharedPreferences pref = getSharedPreferences("log", Context.MODE_PRIVATE);
        usuario.setText(pref.getString("usuario", ""));
        contra.setText(pref.getString("contraseña", ""));
    }

    public void log(View view){
        Intent toLV = new Intent(this, ActivityLV.class);
        String us = usuario.getText().toString();
        String con = contra.getText().toString();
        if(us.compareTo("admin") == 0){
            if (con.compareTo("admin") == 0) {
                if (cb.isChecked() == true){
                    gPref(view);
                    Toast.makeText(this, "CB pulsada", Toast.LENGTH_LONG).show();

                }
                startActivity(toLV);
            } else{

                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                contra.setText("");
            }
        } else {
            Toast.makeText(this, "Este usuario no existe", Toast.LENGTH_SHORT).show();
            contra.setText("");
            usuario.setText("");
        }
    }

    public void reg(View view){
        Intent toReg = new Intent(this, Registro.class);
        startActivity(toReg);
    }

    public void gPref(View view){

        SharedPreferences prefGuardar = getSharedPreferences("log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefGuardar.edit();
        editor.putString("usuario", usuario.getText().toString());
        editor.putString("contraseña", contra.getText().toString());
        editor.commit();
        Toast.makeText(this, "Contraseñ y usuario guardados correctamente", Toast.LENGTH_LONG).show();
    }

}