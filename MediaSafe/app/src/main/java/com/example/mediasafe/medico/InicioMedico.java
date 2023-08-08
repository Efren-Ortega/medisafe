package com.example.mediasafe.medico;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link InicioMedico#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioMedico extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String imageUrl;
    private LinearLayout btn_buscar, btn_crear, formCrear, formBuscar, btn_continuar, resultados, expediente, btn_formCrear;
    private Button btn_aceptar, btn_cancelar;
    private ImageView iv_profile;
    private TextView tv_nombre, tv_edad, tv_nss, txt_warn_nss;
    private EditText et_nss, et_nombre, et_segundoNombre, et_apellidoPaterno, et_apellidoMaterno, et_nss_form, et_edad;
    private ImageButton btn_search, btnEliminar;
    public InicioMedico() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Expedientes.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioMedico newInstance(String param1, String param2) {
        InicioMedico fragment = new InicioMedico();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_incio_medico, container, false);

        btn_buscar = view.findViewById(R.id.btn_buscar);
        btn_crear = view.findViewById(R.id.btn_crear);
        formCrear = view.findViewById(R.id.formCrear);
        formBuscar = view.findViewById(R.id.formBuscar);
        btn_aceptar = view.findViewById(R.id.btn_aceptar);
        btn_cancelar = view.findViewById(R.id.btn_cancelar);
        btn_continuar = view.findViewById(R.id.btn_continuar);
        resultados = view.findViewById(R.id.resultados);
        et_nss = view.findViewById(R.id.et_nss_search);
        et_edad = view.findViewById(R.id.et_edad);

        et_nombre = view.findViewById(R.id.et_nombre);
        et_segundoNombre = view.findViewById(R.id.et_segundoNombre);
        et_apellidoPaterno = view.findViewById(R.id.et_apellidoPaterno);
        et_apellidoMaterno = view.findViewById(R.id.et_apellidoMaterno);
        et_nss_form = view.findViewById(R.id.et_nss_form);
        btn_formCrear = view.findViewById(R.id.btn_formCrear);

        expediente = view.findViewById(R.id.expediente);

        iv_profile = view.findViewById(R.id.iv_profile);
        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_edad = view.findViewById(R.id.tv_edad);
        tv_nss = view.findViewById(R.id.tv_nss);

        btn_search = view.findViewById(R.id.btn_search);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        txt_warn_nss = view.findViewById(R.id.txt_warn_nss);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCrear.setVisibility(View.GONE);
                formBuscar.setVisibility(View.VISIBLE);
                btn_buscar.setBackgroundResource(R.drawable.bg_card);
                btn_crear.setBackgroundResource(R.drawable.bg_card_no_active);
                btn_formCrear.setVisibility(View.VISIBLE);
                btn_continuar.setVisibility(View.GONE);
            }
        });

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultados.setVisibility(View.GONE);
                expediente.setVisibility(View.GONE);
                formCrear.setVisibility(View.VISIBLE);
                formBuscar.setVisibility(View.GONE);
                btn_buscar.setBackgroundResource(R.drawable.bg_card_no_active);
                btn_crear.setBackgroundResource(R.drawable.bg_card);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCrear.setVisibility(View.GONE);
                formBuscar.setVisibility(View.GONE);
                btn_continuar.setVisibility(View.GONE);
            }
        });

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_nombre.length() == 0){
                    Snackbar.make(getView(), "Ingrese el Nombre", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(et_apellidoPaterno.length() == 0){
                    Snackbar.make(getView(), "Ingrese el Apellido Paterno", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(et_nss_form.length() == 0){
                    Snackbar.make(getView(), "Ingrese el No. de Seguro Social", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(et_edad.length() == 0){
                    Snackbar.make(getView(), "Ingrese la Edad", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    crearExpediente();
                }

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_nss.getText().length() != 0){

                    obtenerInfo();
                    txt_warn_nss.setVisibility(View.GONE);
                }else{
                    txt_warn_nss.setVisibility(View.VISIBLE);
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmación")
                        .setMessage("¿Estás seguro de que desea eliminar el Expediente?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarExpediente();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        expediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirExpedientePaciente();
            }
        });


        return view;
    }


    private void abrirExpedientePaciente() {
        ExpedientePaciente fragmentoDestino = new ExpedientePaciente();
        Bundle args = new Bundle();
        args.putString("nombre", tv_nombre.getText().toString());
        args.putString("edad", tv_edad.getText().toString());
        args.putString("nss", tv_nss.getText().toString());
        args.putString("photo", imageUrl);


        fragmentoDestino.setArguments(args);

        loadFragment(fragmentoDestino);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void obtenerInfo(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");

        String url = "http://"+ip_servidor+":3000/medico/obtenerExpediente/"+et_nss.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "true") {

                                expediente.setVisibility(View.VISIBLE);
                                resultados.setVisibility(View.VISIBLE);

                                JSONArray res = jsonObject.getJSONArray("res");

                                JSONObject objeto = res.getJSONObject(0);

                                String nombre = objeto.getString("name");
                                String apellidoPaterno = objeto.getString("apellido_p");
                                String apellidoMeterno = objeto.getString("apellido_m");

                                tv_nombre.setText(nombre + " " + apellidoPaterno + " " + apellidoMeterno);
                                tv_edad.setText(objeto.getString("edad") + " años");
                                tv_nss.setText(objeto.getString("NSS"));

                                String url_foto = objeto.getString("photo");

                                imageUrl =  "http://"+ip_servidor+":3000/imagen/"+url_foto;
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

    public void crearExpediente(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        String url = "http://"+ip_servidor+":3000/medico/crearExpediente";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(getView(), "NSS ya Existe", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{

                                Snackbar.make(getView(), "Expediente Creado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                // Crear un constructor de AlertDialog.Builder
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                // Inflar el layout del contenido del AlertDialog
                                View dialogView = getLayoutInflater().inflate(R.layout.layout_confirmation_dialog, null);
                                builder.setView(dialogView);

                                // Personalizar el AlertDialog
                                builder.setCancelable(true); // Permite cerrar el AlertDialog haciendo clic fuera de él
                                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        btn_formCrear.setVisibility(View.GONE);
                                        btn_continuar.setVisibility(View.VISIBLE);

                                        dialog.dismiss();
                                    }
                                });

                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Aquí se ejecuta la acción de cancelar si el usuario hace clic en el botón "Cancelar"
                                        Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                                        // Cerrar el AlertDialog
                                        dialog.dismiss();
                                    }
                                });

                                // Crear y mostrar el AlertDialog
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


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
                        Snackbar.make(getView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
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

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("name", et_nombre.getText().toString());
                    jsonBody.put("edad", et_edad.getText().toString());
                    jsonBody.put("middleName", et_segundoNombre.getText().toString());
                    jsonBody.put("last1", et_apellidoPaterno.getText().toString());
                    jsonBody.put("last2", et_apellidoMaterno.getText().toString());
                    jsonBody.put("nss", et_nss_form.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }

    public void eliminarExpediente(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        String url = "http://"+ip_servidor+":3000/medico/eliminarExpediente/"+et_nss.getText().toString();

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(getView(), "Ya no es Posible Eliminar este Expediente", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{

                                Snackbar.make(getView(), "Expediente Eliminado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                resultados.setVisibility(View.GONE);
                                expediente.setVisibility(View.GONE);
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
                        Snackbar.make(getView(), "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        );

        queue.add(request);
    }

}