package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
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

import org.json.JSONObject;

;

public class Login extends AppCompatActivity {

    private EditText usuario, contra;
    private ListView lv1;
    private CheckBox cb;

    private RequestQueue request;
    private JsonObjectRequest jsonOR;
    private ProgressDialog progreso ;


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

    public void log(View view) {

        cargarWebService(view);


    }

    private void cargarWebService(View view) {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Comprobando tus credenciales...");
        progreso.show();

        String us = usuario.getText().toString();
        String con = contra.getText().toString();

        if (cb.isChecked() == true) {
            gPref(view);
            Toast.makeText(this, "CB pulsada", Toast.LENGTH_LONG).show();
        }

        if (!us.equals("") && !con.equals("")) {
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
                                        progreso.dismiss();
                                    } else if (workInfo.getOutputData().getString("datos").equals("0")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Este usuario no existe, para crearlo ve a registro", Toast.LENGTH_SHORT);
                                        toast.show();
                                        progreso.hide();
                                        progreso.dismiss();
                                    } else if (workInfo.getOutputData().getString("datos").equals("1")){
                                        procesoLogin(us, con);
                                    }else{
                                        Log.d("WorkerInfo: ", workInfo.getOutputData().getString("datos"));
                                        progreso.hide();
                                        progreso.dismiss();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                    progreso.dismiss();
                                }
                            }
                        }
                    });
            WorkManager.getInstance(this).enqueue(otwr);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Introduce usuario y contraseña", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void procesoLogin(String us, String con) {
        Data datos = new Data.Builder()
                .putString("nombre", us)
                .putString("contra", con)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(LoginDB.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                if (workInfo.getOutputData().getString("datos").equals("error")) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesión, vuelve a intentarlo más tarde", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                    progreso.dismiss();
                                } else if (workInfo.getOutputData().getString("datos").equals("loggeado")) { //Si los datos son correctos hacer login
                                    Toast toast = Toast.makeText(getApplicationContext(), "¡Hola, " + us + "!", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                    progreso.dismiss();

                                    //Abrir la actividad del ListView y cerrar esta
                                    Intent intent = new Intent(Login.this, ActivityLV.class);
                                    intent.putExtra("usuario", us);
                                    startActivity(intent);
                                    finish();
                                } else if (workInfo.getOutputData().getString("datos").equals("incorrecto")) { //Si los datos son incorrectos no hacer nada
                                    Toast toast = Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                    progreso.dismiss();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progreso.hide();
                                    progreso.dismiss();
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                toast.show();
                                progreso.hide();
                                progreso.dismiss();
                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }


    public void reg(View view) {
        Intent toReg = new Intent(Login.this, Registro.class);
        startActivity(toReg);
        finish();
    }

    public void gPref(View view) {

        SharedPreferences prefGuardar = getSharedPreferences("log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefGuardar.edit();
        editor.putString("usuario", usuario.getText().toString());
        editor.putString("contraseña", contra.getText().toString());
        editor.commit();
        Toast.makeText(this, "Contraseña y usuario guardados correctamente", Toast.LENGTH_LONG).show();
    }
}
