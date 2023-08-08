package com.example.mediasafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;


import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mediasafe.medico.HomeMedico;
import com.google.android.material.snackbar.Snackbar;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText pass;
    private EditText et_email;
    private ImageButton showPasswordButton, btn_huella;
    private TextView txtRegDerechoHabiente, txtRegMedico, txtRecuperarContraseña;

    private Button btnIngresar;

    private EditText editTextServerIp, editTextIP;
    private AlertDialog ipDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip = localStorage.getString("ip", null);


        editTextIP = new EditText(this);

        if(ip != null){
            editTextIP.setText(ip);
        }

        btn_huella = (ImageButton) findViewById(R.id.btn_huella);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingresar IP")
                .setView(editTextIP)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String ip = editTextIP.getText().toString();

                        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                        SharedPreferences.Editor editor = localStorage.edit();
                        editor.putString("ip", ip);
                        editor.apply();

                        Boolean Huella = localStorage.getBoolean("Huella", false);
                        String huella_token = localStorage.getString("huella_token", "");


                        if(Huella == true && !huella_token.equals("")){
                            checkBiometricSupport();
                            showBiometricPrompt();
                            btn_huella.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .setCancelable(false);

        ipDialog = builder.create();

        pass = (EditText) findViewById(R.id.et_pass);
        et_email = (EditText) findViewById(R.id.et_email);

        showPasswordButton = (ImageButton) findViewById(R.id.showPasswordButton);
        txtRegDerechoHabiente = (TextView) findViewById(R.id.txtRegDerechoHabiente);
        txtRegMedico = (TextView) findViewById(R.id.txtRegMedico);
        txtRecuperarContraseña= (TextView) findViewById(R.id.txtRecuperarContraseña);
        btnIngresar = (Button) findViewById(R.id.btn_Ingresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autenticarse(view);
            }
        });


        btn_huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBiometricSupport();
                showBiometricPrompt();
            }
        });

        btn_huella.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                checkBiometricSupport();
                showBiometricPrompt();

                return true;
            }
        });

        showPasswordButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pass.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        });


        txtRegDerechoHabiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegistroDerechoHabiente.class);
                startActivity(intent);
            }
        });

        txtRegMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegistroMedico.class);
                startActivity(intent);
            }
        });

        txtRecuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RecuperarContrasena.class);
                startActivity(intent);
            }
        });


        //With the token I can verify if the user is already logged or not .O.
        String token = localStorage.getString("token", null);
        String rol = localStorage.getString("rol", null);

        if(token != null && rol != null){

            if(rol.equals("medico")){
                Intent intent = new Intent(getApplicationContext(), HomeMedico.class);
                startActivity(intent);
                finish();
            }else if(rol.equals("paciente")){
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        ipDialog.show();
    }


    private void checkBiometricSupport() {
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                //Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show();

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // El dispositivo no tiene un sensor de huellas digitales.
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // El sensor de huellas digitales no está disponible actualmente.
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // No se han registrado huellas digitales en el dispositivo.
                break;
        }


    }

    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación con huella digital")
                .setSubtitle("Coloca tu huella en el lector")
                .setNegativeButtonText("Cancelar")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);

                String huella_token = localStorage.getString("huella_token", null);

                String rol = localStorage.getString("rol", null);
                Toast.makeText(getApplicationContext(), "Rol: " + huella_token, Toast.LENGTH_SHORT).show();

                if(huella_token != null && rol != null){

                    if(rol.equals("medico")){
                        Intent intent = new Intent(getApplicationContext(), HomeMedico.class);
                        startActivity(intent);
                        finish();
                    }else if(rol.equals("paciente")){
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

            @Override
            public void onAuthenticationFailed() {
                // La autenticación falló. Puedes mostrar un mensaje de error aquí.
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                // Ocurrió un error en la autenticación. Puedes manejarlo aquí.
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }

    //Requests to the Server
    public void autenticarse(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        //Volley es una biblioteca para realizar peticiones HTTP en segundo plano
        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        //=========================================================
        String url = "http://"+ip_servidor+":3000/usuarios/auth";
        //=========================================================

        //El siguiente código es el cuerpo de la petición en este caso POST
        //Hay métodos para responder en caso de exito y en caso de error
        //Hay métodos para agregar encabezados a la petición
        //Hay métodos para mandar parametros en JSON
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                            String status = jsonObject.getString("status");//Obtener valores del JSON

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(view, "Correo o Contraseña Incorrectos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{//Usuario Autenticado

                                //Con SharedPreferences almacenó el token que mandó con JWT desde el servidor
                                //este token se usara para autorizar cada petición que se desea ejecutar por el usuario.
                                SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                                SharedPreferences.Editor editor = localStorage.edit();
                                editor.putString("rol", jsonObject.getString("rol"));
                                editor.putString("token", jsonObject.getString("auth"));
                                editor.putString("idPerfil", jsonObject.getString("idPerfil"));
                                editor.commit();

                                String rol = jsonObject.getString("rol");

                                if(rol.equals("medico")){
                                    //Abrir la pantalla Inicio
                                    Intent intent = new Intent(Login.this, HomeMedico.class);
                                    startActivity(intent);
                                }else if(rol.equals("paciente")){
                                    //Abrir la pantalla Inicio
                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                }


                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {//En caso de error con el servidor
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Snackbar.make(view, "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        error.printStackTrace();
                    }
                }
        ){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Agregar un header estándar
                return headers;
            }

            //Mandar parametros a la petición en formato JSON
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Enviar un cuerpo JSON en la solicitud POST
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("correo", et_email.getText().toString());
                    jsonBody.put("pass", pass.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        //Agregamos la petición para ser procesada y ejecutada
        queue.add(request);
    }
}