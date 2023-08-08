package com.example.mediasafe;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mediasafe.medico.HomeMedico;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton edti_foto;
    private String base64Image_perfil;
    private ImageView iv_profile, btn_icon_back;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 2;
    private String base64Image;
    private String nameUploaded;


    private EditText name, correo, apellidos, contra;
    private Button btn_guardar, btn_activar_huella;

    private ToggleButton toggleButton;

    public Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);


        edti_foto = (ImageButton) view.findViewById(R.id.edti_foto);
        iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
        name = (EditText) view.findViewById(R.id.et_name);
        correo = (EditText) view.findViewById(R.id.et_email);
        apellidos = (EditText) view.findViewById(R.id.et_last);
        contra = (EditText) view.findViewById(R.id.pass);
        btn_guardar = (Button) view.findViewById(R.id.btn_guardar);
        btn_activar_huella = (Button) view.findViewById(R.id.btn_activar_huella);

        toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        btn_icon_back = (ImageView) view.findViewById(R.id.btn_icon_back);
        String huella_token = localStorage.getString("huella_token", "");

        if(!huella_token.equals("")){
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);
        }


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Aquí puedes realizar acciones según el estado del botón (Activado/Desactivado)
                SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
                SharedPreferences.Editor editor = localStorage.edit();

                if (isChecked) {
                    Snackbar.make(view, "Huella Activada", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    String token = localStorage.getString("token", null);
                    editor.putBoolean("Huella", true);
                    editor.putString("huella_token", token);
                    editor.apply();
                } else {
                    Snackbar.make(view, "Huella Desactivada", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    editor.remove("Huella");
                    editor.remove("huella_token");
                    editor.apply();
                }
            }
        });

        btn_activar_huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
                SharedPreferences.Editor editor = localStorage.edit();
                String token = localStorage.getString("token", null);
                Boolean Huella = localStorage.getBoolean("Huella", false);

                editor.putBoolean("Huella", !Huella);
                editor.putString("huella_token", token);
                editor.apply();
            }
        });

        edti_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear el diálogo de selección de imagen
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Seleccionar imagen");
                builder.setItems(new CharSequence[]{"Cámara", "Galería"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Abrir la cámara
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                break;
                            case 1:
                                // Abrir la galería de imágenes
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                                break;
                        }
                    }
                });

                // Mostrar el diálogo de selección de imagen
                builder.show();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().length() == 0 || apellidos.getText().length() == 0 || correo.getText().length() == 0 || contra.getText().length() == 0){
                    if(name.getText().length() == 0){
                        Snackbar.make(view, "Ingrese Nombre", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(apellidos.getText().length() == 0){
                        Snackbar.make(view, "Ingrese apellidos", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(correo.getText().length() == 0){
                        Snackbar.make(view, "Ingrese correo", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if(contra.getText().length() == 0){
                        Snackbar.make(view, "Ingrese Contraseña", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else{
                    actualizarPerfil(view);
                }

            }
        });

        btn_icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para cerrar el Fragment
                ((HomeMedico) requireActivity()).setBottomNavigationVisible(true);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        obtenerInfo();
        return view;
    }


    private void closeFragment() {
        // Obtiene el FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Remueve este Fragment de la pila
        fragmentManager.beginTransaction().remove(this).commit();

        // Opcionalmente, puedes agregar una transición de animación para cerrar el Fragment
        // fragmentManager.beginTransaction().remove(this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
    }

    public void guardarFoto(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String ip = localStorage.getString("ip", null);

        String url = "http://"+ip+":3000/subir-foto";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(getView(), "Error al Subir La Foto", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                String nameImage = jsonObject.getString("data");
                                nameUploaded = nameImage;


                                Snackbar.make(getView(), "Foto Subida", Snackbar.LENGTH_LONG)
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
                    jsonBody.put("url_fotos","data:image/jpg;base64,"+base64Image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }
        };

        queue.add(request);
    }



    public void actualizarPerfil(View view){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Procesando Datos...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences localStorage = getActivity().getSharedPreferences("localstorage", MODE_PRIVATE);
        String idPefil = localStorage.getString("idPerfil", null);
        String ip = localStorage.getString("ip", null);

        String url = "http://"+ip+":3000/perfil/actualizar/"+idPefil;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if(status == "false"){
                                Snackbar.make(view, "Error al Actualizar Perfil Verifique sus Campos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else{
                                Snackbar.make(view, "Perfil Actualizado", Snackbar.LENGTH_LONG)
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
                    jsonBody.put("nameImg",nameUploaded);
                    jsonBody.put("nombre", name.getText().toString());
                    jsonBody.put("correo", correo.getText().toString());
                    jsonBody.put("contra", contra.getText().toString());
                    jsonBody.put("apellidos", apellidos.getText().toString());

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
        String idPerfil = localStorage.getString("idPerfil", "");
        String ip = localStorage.getString("ip", "");

        String url = "http://"+ip+":3000/usuarios/usuario/"+idPerfil;

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

                                    name.setText(objeto.getString("name"));
                                    correo.setText(objeto.getString("correo"));
                                    contra.setText(objeto.getString("contrasena"));
                                    apellidos.setText(objeto.getString("apellido_p"));

                                    String url_foto = objeto.getString("photo");

                                    String imageUrl =  "http://"+ip+":3000/imagen/"+url_foto;
                                    Glide.with(getActivity())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(iv_profile);



                            }else{
                                Snackbar.make(getView(), "Error al Obtener Tus Datos", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getView(), "Se produjo tus Datos", Snackbar.LENGTH_LONG)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && data != null && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);


            iv_profile.setImageBitmap(photo);
            guardarFoto();
        }


        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    iv_profile.setImageBitmap(decodedBitmap);
                    guardarFoto();
                }
            });
        }


    }

}