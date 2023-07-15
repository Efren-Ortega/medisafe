package com.example.mediasafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistroDerechoHabiente extends AppCompatActivity {
    private EditText pass;
    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_nss_cedula;
    private EditText et_correo;
    private Button btn_register;
    private ImageButton showPasswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_derecho_habiente);

        pass = (EditText) findViewById(R.id.pass);
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_apellidos = (EditText) findViewById(R.id.et_apellidos);
        et_nss_cedula = (EditText) findViewById(R.id.et_nss_cedula);
        et_correo = (EditText) findViewById(R.id.et_correo);

        showPasswordButton = (ImageButton) findViewById(R.id.showPasswordButton);
        btn_register = (Button) findViewById(R.id.btnRegister);

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

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pass.getText().length() == 0 || et_nombre.getText().length() == 0 || et_apellidos.getText().length() == 0
                        || et_nss_cedula.getText().length() == 0 || et_correo.getText().length() == 0){
                    if(et_nombre.getText().length() == 0){
                        Snackbar.make(view, "Ingrese su Nombre", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(et_apellidos.getText().length() == 0){
                        Snackbar.make(view, "Ingrese sus Apellidos", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(et_nss_cedula.getText().length() == 0){
                        Snackbar.make(view, "Ingrese su Seguro Social", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(et_correo.getText().length() == 0){
                        Snackbar.make(view, "Ingrese su Correo", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(pass.getText().length() == 0){
                        Snackbar.make(view, "Ingrese su Contraseña", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else{
                    registrarse(view);
                }
            }
        });
    }

    public void registrarse(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        String url = "http://192.168.137.23:3000/usuario/registrarse";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(view, "El correo ingresado ya se encuentra registrado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{

                                SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                                SharedPreferences.Editor editor = localStorage.edit();
                                editor.putString("token", jsonObject.getString("auth"));
                                editor.putString("idPerfil", jsonObject.getString("idPerfil"));
                                editor.commit();

                                //Abrir la pantalla Inicio Sesion
                                Intent intent = new Intent(RegistroDerechoHabiente.this, Home.class);
                                startActivity(intent);
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
                        System.out.println(error);
                        Snackbar.make(view, "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
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
                    jsonBody.put("rol", "paciente");
                    jsonBody.put("name", et_nombre.getText().toString());
                    jsonBody.put("last", et_apellidos.getText().toString().split(" ")[0].trim());
                    jsonBody.put("middle", et_apellidos.getText().toString().split(" ")[1].trim());
                    jsonBody.put("email", et_correo.getText().toString());
                    jsonBody.put("nss", et_nss_cedula.getText().toString());
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