package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tareas extends AppCompatActivity {

    private TextView idTV, nombreTV, descTV;
    //private Button btA, btEdit, btElim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        idTV = (TextView) findViewById(R.id.id);
        nombreTV = (TextView) findViewById(R.id.nom);
        descTV = (TextView) findViewById(R.id.usu);

    }



}