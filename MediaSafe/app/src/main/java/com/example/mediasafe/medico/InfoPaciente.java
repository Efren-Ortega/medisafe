package com.example.mediasafe.medico;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mediasafe.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText et_nombre, et_fechNac, et_direccion, et_telefono, et_sexo, et_correo;
    private ImageView iv_profile;
    private TextView tv_nss, tv_edad, tv_nombre;
    private Button btn_guardar;

    private String nss;
    public InfoPaciente() {
        // Required empty public constructor
    }

    public static InfoPaciente newInstance(String param1, String param2) {
        InfoPaciente fragment = new InfoPaciente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            // Si ya existe el fragmento, no es necesario recrearlo
            return getView();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_paciente, container, false);

        Bundle args = getArguments();
        if (args != null) {
            nss = args.getString("nss");

        }

        et_nombre = view.findViewById(R.id.et_nombre);
        et_fechNac = view.findViewById(R.id.et_fechNac);
        et_direccion = view.findViewById(R.id.et_direccion);
        et_telefono = view.findViewById(R.id.et_telefono);
        et_sexo = view.findViewById(R.id.et_sexo);
        et_correo = view.findViewById(R.id.et_correo);
        tv_edad = view.findViewById(R.id.tv_edad);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_nombre = view.findViewById(R.id.tv_nombre);

        btn_guardar = view.findViewById(R.id.btn_guardar);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarInformacion();
            }
        });

        tv_nss = view.findViewById(R.id.tv_nss);
        tv_nss.setText(nss);

        obtenerInfo();

        return view;
    }

    public void actualizarInformacion(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip = localStorage.getString("ip", null);

        String url = "http://"+ip+":3000/medico/actualizarInformacion/"+nss;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(getView(), "Error al Actualizar la Información", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Snackbar.make(getView(), "Información Actualizada", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Snackbar.make(getView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        ){
            //Establecer encabezados
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("nombre", et_nombre.getText().toString());
                    jsonBody.put("fechaNac", et_fechNac.getText().toString());
                    jsonBody.put("direccion", et_direccion.getText().toString());
                    jsonBody.put("telefono", et_telefono.getText().toString());
                    jsonBody.put("sexo", et_sexo.getText().toString());
                    jsonBody.put("correo", et_correo.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }

    public void obtenerInfo(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");

        String url = "http://"+ip_servidor+":3000/medico/obtenerExpediente/"+nss;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "true") {

                                JSONArray res = jsonObject.getJSONArray("res");

                                JSONObject objeto = res.getJSONObject(0);

                                String nombre = objeto.getString("name");
                                String apellidoPaterno = objeto.getString("apellido_p");
                                String apellidoMeterno = objeto.getString("apellido_m");

                                tv_nombre.setText(nombre + " " + apellidoPaterno + " " + apellidoMeterno);
                                tv_edad.setText(objeto.getString("edad") + " años");

                                et_nombre.setText(nombre);

                                if(objeto.getString("fechaNac") == "null"){
                                    et_fechNac.setText("");
                                }else{
                                    et_fechNac.setText(objeto.getString("fechaNac"));
                                }

                                if(objeto.getString("direccion") == "null"){
                                    et_direccion.setText("");
                                }else{
                                    et_direccion.setText(objeto.getString("direccion"));
                                }

                                if(objeto.getString("telefono") == "null"){
                                    et_telefono.setText("");
                                }else{
                                    et_telefono.setText(objeto.getString("telefono"));
                                }


                                String url_foto = objeto.getString("photo");

                                String imageUrl =  "http://"+ip_servidor+":3000/imagen/"+url_foto;
                                Glide.with(getActivity())
                                        .load(imageUrl)
                                        .override(200, 200)
                                        .into(iv_profile);

                            }else{
                                Snackbar.make(getView(), "El Expediente No Existe", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getView(), "Se produjo para obtener el expediente", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                System.out.println("Error " + error);
            }
        });
        queue.add(stringRequest);
    }

}