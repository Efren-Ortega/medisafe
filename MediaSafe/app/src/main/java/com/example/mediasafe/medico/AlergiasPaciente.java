package com.example.mediasafe.medico;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mediasafe.MyBottomSheetDialogFragment;
import com.example.mediasafe.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlergiasPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlergiasPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout alergias_container;

    private ScrollView scrollView;
    private ImageButton btn_add;

    private EditText et_alergia;

    private ImageView btn_icon_back;


    private String nss;
    public AlergiasPaciente() {
        // Required empty public constructor
    }


    public static AlergiasPaciente newInstance(String param1, String param2) {
        AlergiasPaciente fragment = new AlergiasPaciente();
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
        View view = inflater.inflate(R.layout.fragment_alergias_paciente, container, false);

        Bundle args = getArguments();
        if (args != null) {
            nss = args.getString("nss");

        }

        alergias_container = (LinearLayout) view.findViewById(R.id.alergias_container);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        et_alergia  = (EditText) view.findViewById(R.id.et_alergia);

        btn_add = (ImageButton) view.findViewById(R.id.btn_add);

        obtenerAlergias("");

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_alergia.getText().length() == 0){
                    Snackbar.make(getView(), "Ingrese la Alergia >:", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    crearAlergia();
                }
            }
        });

        btn_icon_back = (ImageView) view.findViewById(R.id.btn_icon_back);
        btn_icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para cerrar el Fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void agregar(String alergia, String idAlergia, String scroll){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int id = R.layout.alergia_componente;
        LinearLayout relativeLayout = (LinearLayout) inflater.inflate(id, null, false);

        EditText editText = (EditText) relativeLayout.findViewById(R.id.tv_alergia);
        ImageButton edit = (ImageButton) relativeLayout.findViewById(R.id.btn_edit);
        ImageButton del = (ImageButton) relativeLayout.findViewById(R.id.btn_delete);

        editText.setText(alergia);
        del.setTag(idAlergia);
        edit.setTag(idAlergia);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String) view.getTag();

                MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment();
                Bundle args = new Bundle();
                args.putString("id", id); // Reemplaza buttonId con el valor real del ID del botón
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.setOnSaveClickListener(onSaveClickListener, id);
                bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        // Aquí se ejecuta la acción de eliminar si el usuario hace clic en el botón "Eliminar"
                        // Puedes poner aquí la lógica para eliminar el elemento
                        eliminarAlergia(view.getTag().toString());
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aquí se ejecuta la acción de cancelar si el usuario hace clic en el botón "Cancelar"
                        // Cerrar el AlertDialog
                        dialog.dismiss();
                    }
                });

                // Crear y mostrar el AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(params);
        alergias_container.addView(relativeLayout);

        if(!scroll.equals("no-scroll")){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }

    }

    public void obtenerAlergias(String scroll){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip = localStorage.getString("ip", "");

        String url = "http://"+ip+":3000/alergias/"+nss;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "true") {
                                JSONArray res = jsonObject.getJSONArray("res");

                                alergias_container.removeAllViews();

                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject objeto = res.getJSONObject(i);

                                    agregar(objeto.getString("alergia"), objeto.getString("idpaciente_alergias"), scroll);

                                }


                            }else{
                                Snackbar.make(getView(), "Error al Obtener las Alergias", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getView(), "Se produjo un error", Snackbar.LENGTH_LONG)
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

    public void actualizarAlergia(View view, String alergia, String id){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip = localStorage.getString("ip", null);

        String url = "http://"+ip+":3000/modificar-alergia";

        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(view, "Error al Actualizar", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Snackbar.make(view, "Actualizado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                obtenerAlergias("no-scroll");
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

                        Snackbar.make(view, "Se produjo un Error intentelo más Tarde", Snackbar.LENGTH_LONG)
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
                    jsonBody.put("id", id);
                    jsonBody.put("alergia", alergia);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }

    public void crearAlergia(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        String url = "http://"+ip_servidor+":3000/nueva-alergia";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(getView(), "Error Intentelo más Tarde", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{

                                String msg = jsonObject.getString("msg");

                                if(msg.equals("Esa Alergia ya esta Registrada")){

                                    Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

                                    return;
                                }

                                Snackbar.make(getView(), "Hecho", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                obtenerAlergias("");
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
                    jsonBody.put("nss", nss);
                    jsonBody.put("alergia", et_alergia.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }


    public void eliminarAlergia(String id){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip_servidor = localStorage.getString("ip", "");//Obtengo la IP del servidor

        String url = "http://"+ip_servidor+":3000/eliminar-alergia/"+id;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //En caso de éxito
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){//Usuario No Autorizado
                                Snackbar.make(getView(), "Error al Eliminar", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{

                                Snackbar.make(getView(), "Alergia Eliminada", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                obtenerAlergias("no-scroll");
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

    private MyBottomSheetDialogFragment.OnSaveClickListener onSaveClickListener = new MyBottomSheetDialogFragment.OnSaveClickListener() {
        @Override
        public void onSaveClicked(String inputText, String Id) {
            if(inputText.trim().length() == 0){
                Snackbar.make(getView(), "Ingrese la Alergia >:", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                actualizarAlergia(getView(), inputText, Id);
            }
        }
    };

}