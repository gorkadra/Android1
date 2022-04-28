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


    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_lv);
        lv1 = (ListView) findViewById(R.id.lv1);

    }

    public void aTareas(View view){
        Intent toTareas = new Intent(this, Tareas.class);
        startActivity(toTareas);
    }



}