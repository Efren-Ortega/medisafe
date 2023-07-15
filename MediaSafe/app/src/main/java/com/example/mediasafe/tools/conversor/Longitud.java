package com.example.mediasafe.tools.conversor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediasafe.R;

public class Longitud extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    String[] items = new String[]{"Kilómetro", "Metro", "Centímetro", "Milímetro", "Milla", "Pie", "Yarda", "Pulgada"};
    private Spinner longitudesEntrada;
    private Spinner longitudesSalida;
    private EditText et_entrada;
    private TextView et_salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longitud);
        setTitle("Longitud");

        //SPINNER 1
        longitudesEntrada = (Spinner)findViewById(R.id.spinner_TempEntrada);

        //Cargamos los datos del Array items en el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Longitud.this,
                android.R.layout.simple_spinner_item,items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Cargamos el ArrayAdapter en el spinner
        longitudesEntrada.setAdapter(adapter);

        //Se le asigna un Escuchador
        longitudesEntrada.setOnItemSelectedListener(this);


        //SPINNER 2
        longitudesSalida = (Spinner)findViewById(R.id.spinner_tempSalida);

        //Cargamos los datos del Array items en el Spinner
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(Longitud.this,
                android.R.layout.simple_spinner_item, items);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Cargamos el ArrayAdapter en el spinner
        longitudesSalida.setAdapter(adapter2);

        //Se le asigna un Escuchador
        longitudesSalida.setOnItemSelectedListener(this);

        et_entrada = (EditText) findViewById(R.id.et_entrada);
        et_salida = (TextView) findViewById(R.id.et_salida);
        et_entrada.setText("1");
        et_salida.setFocusable(false);


        et_entrada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Al cambiar el EditText ejecutamos un nuevo calculo
                Calcular();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    //Función que se ejecuta cuando se selecciona un Item del Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        this.Calcular();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    //Valimos que el campo no este vacio y que no inicie con un punto
    public boolean validarCampos(){
        if(et_entrada.getText().toString().matches("")){
            Toast.makeText(this, "Ingrese un Valor a Convertir", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(et_entrada.getText().toString().matches("\\.")){
            Toast.makeText(this, "Ingrese un Valor Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //Realizamos los calculos según el tipo de unidad a convertir
    public void Calcular(){

        double cm = 0;
        double res = 0;

        if(!validarCampos()) return;

        if(longitudesEntrada.getSelectedItem().toString() == longitudesSalida.getSelectedItem().toString()){
            Toast.makeText(this, "Selecciona unidades diferentes", Toast.LENGTH_SHORT).show();
            return;
        }

        //Detectamos la Unidad de Longitud a convertir para convertilo a centimetros y con esa base convertirlos a la Unidad de Longitud de salida seleccionada
        switch (longitudesEntrada.getSelectedItem().toString()){
            case "Kilómetro":
                cm = Float.parseFloat(et_entrada.getText().toString()) * 100000;
                break;

            case "Metro":
                cm = Float.parseFloat(et_entrada.getText().toString()) * 100;
                break;

            case "Centímetro":
                cm = Float.parseFloat(et_entrada.getText().toString());
                break;

            case "Milímetro":
                cm = Float.parseFloat(et_entrada.getText().toString()) / 10;
                break;

            case "Milla":
                cm = Float.parseFloat(et_entrada.getText().toString()) * 160900;
                break;

            case "Pie":
                cm = Float.parseFloat(et_entrada.getText().toString()) * 30.48;
                break;

            case "Yarda":
                cm = Float.parseFloat(et_entrada.getText().toString()) * 91.44;
                break;

            case "Pulgada":
                cm = Float.parseFloat(et_entrada.getText().toString()) * 2.54;
                break;
        }

        //Con la conversión anterior a CM ahora convertimos ese valor en Centimetro al la unidad de longitud seleccionada.
        switch (longitudesSalida.getSelectedItem().toString()){
            case "Kilómetro":
                res = cm / 100000;
                break;

            case "Metro":
                res = cm / 100;
                break;

            case "Centímetro":
                res = cm;
                break;

            case "Milímetro":
                res = cm * 10;
                break;

            case "Milla":
                res = cm / 160900;
                break;

            case "Pie":
                res = cm / 30.48;
                break;

            case "Yarda":
                res = cm / 91.44;
                break;

            case "Pulgada":
                res = cm / 2.54;
                break;
        }

        et_salida.setText(Double.toString(res));
    }
}