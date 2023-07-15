package com.example.mediasafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RecuperarContrasena extends AppCompatActivity {

    private Button btnRestore;
    private EditText txtCode, edCorreo, et_pass, et_confirm_pass;
    private LinearLayout ll_pass, ll_confirm_pass, ll_email;
    private ImageButton showPasswordButton, showConfirmPasswordButton;
    private TextView tv_resendCode;
    private String code;

    private LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        et_pass = (EditText) findViewById(R.id.et_pass);
        et_confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);

        ll_pass = (LinearLayout) findViewById(R.id.ll_pass);
        ll_confirm_pass = (LinearLayout) findViewById(R.id.ll_confirm_pass);
        tv_resendCode = (TextView) findViewById(R.id.tv_resendCode);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);

        ll_pass.setVisibility(View.GONE);
        ll_confirm_pass.setVisibility(View.GONE);

        btnRestore =(Button) findViewById(R.id.btnRestore);
        txtCode = (EditText) findViewById(R.id.et_codigo);
        edCorreo = (EditText) findViewById(R.id.et_correo);

        showPasswordButton = (ImageButton) findViewById(R.id.showPasswordButton);

        showPasswordButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        });

        showConfirmPasswordButton = (ImageButton) findViewById(R.id.showConfirmPasswordButton);

        showConfirmPasswordButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    et_confirm_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    et_confirm_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnRestore.getText().equals("GUARDAR CONTRASEÑA")){

                    if(et_pass.getText().toString().equals(et_confirm_pass.getText().toString())){
                        guardarContra(view);
                    }else{
                        Toast.makeText(getApplicationContext(), "La contraseña son distintas", Toast.LENGTH_SHORT).show();
                    }


                }else if(btnRestore.getText().equals("RECUPERAR CONTRASEÑA")){
                    if(txtCode.getText().toString().equals(code)){
                        ll_pass.setVisibility(View.VISIBLE);
                        ll_confirm_pass.setVisibility(View.VISIBLE);

                        txtCode.setVisibility(View.GONE);
                        ll_email.setVisibility(View.GONE);
                        tv_resendCode.setVisibility(View.GONE);

                        btnRestore.setText("GUARDAR CONTRASEÑA");
                    }else{
                        Toast.makeText(getApplicationContext(), "Código no Valido", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(edCorreo.getText().length() == 0){
                        Toast.makeText(getApplicationContext(), "Ingrese su Correo", Toast.LENGTH_SHORT).show();
                    }else{
                        code = generarCodigoAleatorio();

                        enviarCodigo(view, code);
                    }
                }

            }
        });

        tv_resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = generarCodigoAleatorio();
                enviarCodigo(view, code);
            }
        });

    }

    public void enviarCodigo(View view, String code){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando Código...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        //Volley es una biblioteca para realizar peticiones HTTP en segundo plano
        RequestQueue queue = Volley.newRequestQueue(this);

        //SharedPreferences es una clase para alamacenar información y preferencias en la aplicación
        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        //=========================================================
        String url = "http://192.168.137.23:3000/usuario/restorePassword";
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

                            if(status == "false"){//Error to send email
                                Snackbar.make(view, "Error al Enviar el Correo Verifiquelo", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{//Email Sended
                                btnRestore.setText("RECUPERAR CONTRASEÑA");
                                txtCode.setVisibility(view.VISIBLE);
                                tv_resendCode.setVisibility(View.VISIBLE);
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
                    jsonBody.put("email", edCorreo.getText().toString());
                    jsonBody.put("code", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        //Agregamos la petición para ser procesada y ejecutada
        queue.add(request);

    }

    public static String generarCodigoAleatorio() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digito = random.nextInt(10);
            codigo.append(digito);
        }

        return codigo.toString();
    }

    public void guardarContra(View view){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String url = "http://192.168.137.23:3000/usuario/guardarContra";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                            String status = jsonObject.getString("status");//Obtener valores del JSON

                            if(status == "false"){
                                Snackbar.make(view, "Error al Cambiar la Contraseña Verifique su correo", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Intent intent = new Intent(RecuperarContrasena.this, Login.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Snackbar.make(view, "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                // Enviar un cuerpo JSON en la solicitud POST
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", edCorreo.getText().toString());
                    jsonBody.put("pass", et_pass.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        requestQueue.add(stringRequest);
    }

}