package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.myapplication.Objetos.ObjetoTarea;
import com.example.myapplication.workers.ComprobarUsuarioDB;
import com.example.myapplication.workers.ObtenerTareaDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.AdapterTarea;

public class ActivityLV extends AppCompatActivity {

    private TextView tvLV;
    private ListView lv1;
    private ProgressDialog progreso;
    private List<Integer> ids = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private List<String> descripciones = new ArrayList<>();
    private List<ObjetoTarea> listaTareas = new ArrayList<ObjetoTarea>();
    private RequestQueue request;
    private JsonObjectRequest jsonOR;
    private String propietario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);

        tvLV = (TextView) findViewById(R.id.tv1);
        if (this.propietario == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.propietario = extras.getString("usuario");
            }
        }
        tvLV.setText("Lista de tareas de :");



        setContentView(R.layout.activity_lv);
        ListView tareas = (ListView) findViewById(R.id.lv1);
        //ids.add(0);
        //nombres.add("tarea de prueba");
        //descripciones.add("esto es solo una tarea de prueba");

        cargarWebService();


        AdapterTarea elAdaptador = new AdapterTarea(getApplicationContext(), ids, nombres, descripciones);
        tareas.setAdapter(elAdaptador);

    }

    private void cargarWebService() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Obteniendo las tareas del servidor...");
        progreso.show();
        progreso.hide();



        Data datos = new Data.Builder()
                .putString("propietario", this.propietario)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ObtenerTareaDB.class).setInputData(datos).build();
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
                                } else if (workInfo.getOutputData().getString("datos").equals("no hay tareas")) {
                                    ids.add(1);
                                    nombres.add("No hay tareas");
                                    descripciones.add("Este usuario no dispone de tareas, para asignarlas vaya a 'añadir o editar tarea'");

                                    ListView tareas = (ListView) findViewById(R.id.lv1);
                                    AdapterTarea elAdaptador = new AdapterTarea(getApplicationContext(), ids, nombres, descripciones);
                                    tareas.setAdapter(elAdaptador);
                                    progreso.hide();
                                } else{

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



    }


    public void aTareas(View view){
        Intent toTareas = new Intent(this, Tareas.class);
        startActivity(toTareas);
    }



}