package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.myapplication.workers.InsertarTareaDB;
import com.example.myapplication.workers.LoginDB;

public class Tareas extends AppCompatActivity {

    private TextView nombreTV, descTV, idTV;
    private ProgressDialog progreso ;
    private String propietario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        if (this.propietario == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.propietario = extras.getString("usuario");
            }
        }
        nombreTV = (TextView) findViewById(R.id.nom);
        descTV = (TextView) findViewById(R.id.usu);
        idTV = (TextView) findViewById(R.id.id);

    }



    public void sumar(View view) {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Sumando tarea a tu lista...");
        progreso.show();
        String nombre = nombreTV.getText().toString();
        String desc = descTV.getText().toString();
        nombre = nombre.replace(" ","%20");
        desc = desc.replace(" ","%20");

        Data datos = new Data.Builder()
                .putString("nombre", nombre)
                .putString("descripcion", desc)
                .putString("propietario", this.propietario)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(InsertarTareaDB.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                if (workInfo.getOutputData().getString("datos").equals("error")) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No se ha podido insertar", Toast.LENGTH_SHORT);
                                    toast.show();
                                    nombreTV.setText("");
                                    descTV.setText("");
                                    idTV.setText("");
                                    progreso.hide();
                                    progreso.dismiss();
                                } else if (workInfo.getOutputData().getString("datos").equals("noInsertada")) { //Si los datos son correctos hacer login
                                    Toast toast = Toast.makeText(getApplicationContext(), "Su tarea no ha podido ser insertada, intentelo más tarde", Toast.LENGTH_SHORT);
                                    toast.show();
                                    nombreTV.setText("");
                                    descTV.setText("");
                                    idTV.setText("");
                                    progreso.hide();
                                    progreso.dismiss();


                                } else if (workInfo.getOutputData().getString("datos").equals("insertada")) { //Si los datos son incorrectos no hacer nada
                                    Toast toast = Toast.makeText(getApplicationContext(), "su tarea ha sido insertada con éxito", Toast.LENGTH_SHORT);
                                    toast.show();
                                    nombreTV.setText("");
                                    descTV.setText("");
                                    idTV.setText("");
                                    progreso.hide();
                                    progreso.dismiss();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                    toast.show();
                                    nombreTV.setText("");
                                    descTV.setText("");
                                    idTV.setText("");
                                    progreso.hide();
                                    progreso.dismiss();
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "No esta en comprobacion de datos", Toast.LENGTH_SHORT);
                                toast.show();
                                nombreTV.setText("");
                                descTV.setText("");
                                idTV.setText("");
                                progreso.hide();
                                progreso.dismiss();
                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

}