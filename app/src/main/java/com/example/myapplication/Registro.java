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

public class Registro extends AppCompatActivity {

    private TextView tvUs, tvCon, tvCon2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);

        tvUs = (TextView) findViewById(R.id.usu);
        tvCon = (TextView) findViewById(R.id.contra);
        tvCon2 = (TextView) findViewById(R.id.contra2);

    }

    public void reg(View view){

    }



}