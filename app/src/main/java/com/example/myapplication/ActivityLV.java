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
    private List<String> ids = new ArrayList<>();
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
        //ids.add("0");
        //nombres.add("tarea de prueba");
        //descripciones.add("esto es solo una tarea de prueba");

        //cargarWebService();


        AdapterTarea elAdaptador = new AdapterTarea(getApplicationContext(), ids, nombres, descripciones);
        tareas.setAdapter(elAdaptador);

    }

    @Override
    public void onResume() {
        super.onResume();

        cargarWebService();
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
                                    ids.add("1");
                                    nombres.add("No hay tareas");
                                    descripciones.add("Este usuario no dispone de tareas, para asignarlas vaya a 'añadir o editar tarea'");

                                    ListView tareas = (ListView) findViewById(R.id.lv1);
                                    AdapterTarea elAdaptador = new AdapterTarea(getApplicationContext(), ids, nombres, descripciones);
                                    tareas.setAdapter(elAdaptador);
                                    progreso.hide();
                                    progreso.dismiss();
                                } else{
                                    String resultados = workInfo.getOutputData().getString("datos");
                                    String[] resultSeparados = new String[3];
                                    resultSeparados = resultados.split("],");
                                    String[] resultID = resultSeparados[0].split(":");
                                    String[] resultNombre= resultSeparados[1].split(":");
                                    String[] resultDesc = resultSeparados[2].split(":");
                                    Log.d("Resultado spliteado ID: ", resultID[1]);
                                    Log.d("Resultado spliteado Nombre: ", resultNombre[1]);
                                    Log.d("Resultado spliteado Desc: ", resultDesc[1]);

                                    StringBuilder id = new StringBuilder(resultID[1]);
                                    StringBuilder nomb = new StringBuilder(resultNombre[1]);
                                    StringBuilder de = new StringBuilder(resultDesc[1]);

                                    id.deleteCharAt(0);
                                    id.deleteCharAt(0);
                                    id.deleteCharAt(id.length()-1);
                                    resultID = id.toString().split("\",\"");

                                    nomb.deleteCharAt(0);
                                    nomb.deleteCharAt(0);
                                    nomb.deleteCharAt(nomb.length()-1);
                                    resultNombre = nomb.toString().split("\",\"");

                                    de.deleteCharAt(0);
                                    de.deleteCharAt(0);
                                    de.deleteCharAt(de.length()-1);
                                    de.deleteCharAt(de.length()-1);
                                    de.deleteCharAt(de.length()-1);
                                    resultDesc = de.toString().split("\",\"");

                                    ListView tareas = (ListView) findViewById(R.id.lv1);
                                    ids.clear();
                                    nombres.clear();
                                    descripciones.clear();
                                    for(int i=0;i<resultID.length;i++){
                                        ids.add(resultID[i]);
                                        nombres.add(resultNombre[i]);
                                        descripciones.add(resultDesc[i]);
                                    }
                                    AdapterTarea elAdaptador = new AdapterTarea(getApplicationContext(), ids, nombres, descripciones);
                                    tareas.setAdapter(elAdaptador);
                                    progreso.hide();
                                    progreso.dismiss();


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
        toTareas.putExtra("usuario", propietario);
        startActivity(toTareas);
    }



}