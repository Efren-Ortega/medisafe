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

public class Volumen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] items = new String[]{"Litro", "Mililitro", "Metro Cubico", "Galon"};
    private Spinner volumenEntrada;
    private Spinner volumenSalida;
    private EditText et_entrada;
    private TextView et_salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volumen);
        setTitle("Volumen");

        volumenEntrada = (Spinner)findViewById(R.id.spinner_TempEntrada);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Volumen.this,
                android.R.layout.simple_spinner_item,items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        volumenEntrada.setAdapter(adapter);
        volumenEntrada.setOnItemSelectedListener(this);

        volumenSalida = (Spinner)findViewById(R.id.spinner_tempSalida);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(Volumen.this,
                android.R.layout.simple_spinner_item,items);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        volumenSalida.setAdapter(adapter2);
        volumenSalida.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        Calcular();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

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

    public void Calcular(){
        double Lt = 0;
        double res = 0;

        if(!validarCampos()) return;

        if(volumenEntrada.getSelectedItem().toString() == volumenSalida.getSelectedItem().toString()){
            Toast.makeText(this, "Selecciona unidades diferentes", Toast.LENGTH_SHORT).show();
            return;
        }

        //Detectamos la Unidad de Volumen a convertir para convertilo a Litros y con esa base convertirlos a la Unidad de Volumen de salida seleccionada
        switch (volumenEntrada.getSelectedItem().toString()){
            case "Litro":
                Lt = Float.parseFloat(et_entrada.getText().toString());
                break;

            case "Mililitro":
                Lt = Float.parseFloat(et_entrada.getText().toString()) / 1000;
                break;

            case "Metro Cubico":
                Lt = Float.parseFloat(et_entrada.getText().toString()) * 1000;
                break;

            case "Galon":
                Lt = Float.parseFloat(et_entrada.getText().toString()) * 3.785;
                break;
        }

        //Con la conversión anterior a Lt ahora convertimos ese valor en Litros a la unidad de volumen seleccionada.
        switch (volumenSalida.getSelectedItem().toString()){
            case "Litro":
                res = Lt;
                break;

            case "Mililitro":
                res = Lt * 1000;
                break;

            case "Metro Cubico":
                res = Lt / 1000;
                break;

            case "Galon":
                res = Lt / 3.785;
                break;
        }

        et_salida.setText(Double.toString(res));
    }
}