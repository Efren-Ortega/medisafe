package com.example.mediasafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mediasafe.tools.calculadora.Calculadora;
import com.example.mediasafe.tools.conversor.Conversor;
import com.example.mediasafe.tools.listTareas.ListTareas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView, bottomNavigationView1;
    private DrawerLayout drawerLayout;
    private NavigationView nav_view;

    private TextView tv_name, tv_user;
    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        bottomNavigationView1 = findViewById(R.id.bottomNavigationView1);
        bottomNavigationView1.setVisibility(View.GONE);

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        loadFragment(new Inicio());
                        return true;

                    case R.id.expediente:
                        loadFragment(new Expediente());
                        return true;

                    case R.id.diagnosticos:
                        loadFragment(new Diagnosticos());
                        return true;

                    case R.id.medicamentos:
                        loadFragment(new Medicamentos());
                        return true;

                    case R.id.tools:
                        bottomNavigationView1.setVisibility(View.VISIBLE);
                        bottomNavigationView.setVisibility(View.GONE);
                        return true;

                    case R.id.perfil:
                        loadFragment(new Perfil());
                        return true;

                    case R.id.exit:
                        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
                        SharedPreferences.Editor editor = localStorage.edit();
                        editor.remove("token");
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finishAffinity();
                        return true;

                    default:
                        return false;
                }
            }

        });


        SharedPreferences localStorage = getSharedPreferences("localstorage", MODE_PRIVATE);
        String idPerfil = localStorage.getString("idPerfil", null);

        View headerView = nav_view.getHeaderView(0);
        iv_profile = headerView.findViewById(R.id.iv_profile);
        tv_name = headerView.findViewById(R.id.tv_name);
        tv_user = headerView.findViewById(R.id.tv_user);
        ImageButton imageButton = headerView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Perfil());
            }
        });

        System.out.println("\n\n" + idPerfil + "\n\n");
        if(idPerfil != null){
            getDataUser(idPerfil);
        }

        drawerLayout = findViewById(R.id.drawer_layout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Manejar la selección del elemento del menú
                switch (item.getItemId()) {
                    case R.id.inicio:
                        loadFragment(new Inicio());
                        return true;
                    case R.id.expediente:
                        loadFragment(new Expediente());
                        return true;
                    case R.id.diagnosticos:
                        loadFragment(new Diagnosticos());
                        return true;
                    case R.id.medicamentos:
                        loadFragment(new Medicamentos());
                        return true;


                    case R.id.menu:
                        drawerLayout.openDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Manejar la selección del elemento del menú
                switch (item.getItemId()) {
                    case R.id.calculadora:
                        loadFragment(new Calculadora());
                        return true;
                    case R.id.listTareas:
                        loadFragment(new ListTareas());
                        return true;
                    case R.id.conversor:
                        loadFragment(new Conversor());
                        return true;
                }
                return false;
            }
        });

        loadFragment(new Inicio());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void getDataUser(String idPerfil){
        String ip = "192.168.137.23";

        String url = "http://"+ip+":3000/usuarios/usuario/"+idPerfil;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);//Crear JSON con la respuesta
                            String status = jsonObject.getString("status");//Obtener valores del JSON

                            if(status == "false"){//Usuario No Autorizado
                                Log.e("TAG", "Error Al onbtener los datos ");
                            }else{

                                JSONArray resArray = jsonObject.getJSONArray("res");
                                JSONObject firstObject = resArray.getJSONObject(0);

                                String name = firstObject.getString("name");
                                String apellido_p = firstObject.getString("apellido_p");
                                String apellido_m = firstObject.getString("apellido_m");
                                String usuario = firstObject.getString("usuario");
                                String photo = firstObject.getString("photo");

                                if(photo != ""){
                                    String imageUrl =  "http://"+ip+":3000/imagenes/"+photo;
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .override(200, 200)
                                            .into(iv_profile);
                                }

                                tv_name.setText(name +" "+ apellido_p +" "+ apellido_m);
                                tv_user.setText(usuario);
                            }

                        } catch (JSONException e) {//Error al ejecutar algo en la respuesta
                            e.printStackTrace();
                            Snackbar.make(getWindow().getDecorView(), "Error al Manejar la Respuesta", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Ocurrió un error al realizar la petición
                        Log.e("TAG", "Error: " + error.getMessage());
                        Snackbar.make(getWindow().getDecorView(), "Error al Obtener Datos", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

        requestQueue.add(stringRequest);
    }

}