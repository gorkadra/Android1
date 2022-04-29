package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.workers.ComprobarUsuarioDB;
import com.example.myapplication.workers.LoginDB;
import com.example.myapplication.workers.RegistroDB;

import org.json.JSONObject;

import java.util.ArrayList;

public class Registro extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    private EditText tvUs, tvCon, tvCon2;
    private ProgressDialog progreso;

    private RequestQueue request;
    private JsonObjectRequest jsonOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tvUs = (EditText) findViewById(R.id.usu);
        tvCon = (EditText) findViewById(R.id.contra);
        tvCon2 = (EditText) findViewById(R.id.contra2);

        request = Volley.newRequestQueue(this);

    }

    public void registrar(View view){
        progreso = new ProgressDialog(this);
        progreso.setMessage("Realizando el registro...");
        progreso.show();
        String us = tvUs.getText().toString();
        String con = tvCon.getText().toString();

        if(tvCon2.getText().toString().compareTo(tvCon.getText().toString())==0) {
            Log.d("Usuario que mete en datos ", us);
            Data datos = new Data.Builder()
                    .putString("nombre", us)
                    .build();

            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ComprobarUsuarioDB.class).setInputData(datos).build();
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                    .observe(this, new Observer<WorkInfo>(){
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                    if (workInfo.getOutputData().getString("datos").equals("error")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                        toast.show();
                                        progreso.hide();
                                    } else if (workInfo.getOutputData().getString("datos").equals("0")) {
                                        procesoRegistro(us, con);
                                    } else if (workInfo.getOutputData().getString("datos").equals("1")){
                                        tvCon.setText("");
                                        tvCon2.setText("");
                                        tvUs.setText("");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Este usuario ya existe", Toast.LENGTH_SHORT);
                                        toast.show();
                                        progreso.hide();
                                    }else{
                                        Log.d("WorkerInfo: ", workInfo.getOutputData().getString("datos"));
                                        progreso.hide();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                }
                            }
                        }
                    });
            WorkManager.getInstance(this).enqueue(otwr);
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }

    private void procesoRegistro(String us, String con) {
        Data datos = new Data.Builder()
                .putString("nombre", us)
                .putString("contra", con)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(RegistroDB.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                if (workInfo.getOutputData().getString("datos").equals("error")) {
                                    Toast toast= Toast.makeText(getApplicationContext(),"No se ha podido crear un nuevo usuario, vuelve a intentarlo más tarde",Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                } else if (workInfo.getOutputData().getString("datos").equals("creado")) {
                                    tvCon.setText("");
                                    tvCon2.setText("");
                                    tvUs.setText("");
                                    Toast toast= Toast.makeText(getApplicationContext(),"Usuario " + us + " creado correctamente",Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                } else if (workInfo.getOutputData().getString("datos").equals("existe")) {
                                    tvCon.setText("");
                                    tvCon2.setText("");
                                    tvUs.setText("");
                                    Toast toast= Toast.makeText(getApplicationContext(),"El nombre de usuario " + us + " ya esá cogido",Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                } else {
                                    Toast toast= Toast.makeText(getApplicationContext(),"Algo ha ido mal :(",Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                }
                            } else {
                                Toast toast= Toast.makeText(getApplicationContext(),"Algo ha ido mal :(",Toast.LENGTH_SHORT);
                                toast.show();
                                progreso.hide();
                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this, "No se pudo registrar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.d("ERROR: ", error.toString());
        tvCon.setText("");
        tvUs.setText("");
        tvCon2.setText("");
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(this, "Registro realizado de manera exitosa", Toast.LENGTH_SHORT).show();
        tvCon.setText("");
        tvUs.setText("");
        tvCon2.setText("");
    }

}