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

public class Peso extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] items = new String[]{"Tonelada", "Kilogramo", "Gramo", "Onza", "Libra"};
    private Spinner pesosEntrada;
    private Spinner pesosSalida;
    private EditText et_entrada;
    private TextView et_salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peso);
        setTitle("Peso");

        pesosEntrada = (Spinner) findViewById(R.id.spinner_TempEntrada);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Peso.this,
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pesosEntrada.setAdapter(adapter);
        pesosEntrada.setOnItemSelectedListener(this);

        pesosSalida = (Spinner) findViewById(R.id.spinner_tempSalida);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Peso.this,
                android.R.layout.simple_spinner_item, items);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pesosSalida.setAdapter(adapter2);
        pesosSalida.setOnItemSelectedListener(this);

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

    public boolean validarCampos() {
        if (et_entrada.getText().toString().matches("")) {
            Toast.makeText(this, "Ingrese un Valor a Convertir", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_entrada.getText().toString().matches("\\.")) {
            Toast.makeText(this, "Ingrese un Valor Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void Calcular() {

        double Kg = 0;
        double res = 0;

        if (!validarCampos()) return;

        if (pesosEntrada.getSelectedItem().toString() == pesosSalida.getSelectedItem().toString()) {
            Toast.makeText(this, "Selecciona unidades diferentes", Toast.LENGTH_SHORT).show();
            return;
        }

        //Detectamos la Unidad de Peso a convertir para convertilo a Kilogramos y con esa base convertirlos a la Unidad de peso de salida seleccionada
        switch (pesosEntrada.getSelectedItem().toString()) {
            case "Tonelada":
                Kg = Float.parseFloat(et_entrada.getText().toString()) * 1000;
                break;

            case "Kilogramo":
                Kg = Float.parseFloat(et_entrada.getText().toString());
                break;

            case "Gramo":
                Kg = Float.parseFloat(et_entrada.getText().toString()) / 1000;
                break;

            case "Onza":
                Kg = Float.parseFloat(et_entrada.getText().toString()) * 6.35;
                break;

            case "Libra":
                Kg = Float.parseFloat(et_entrada.getText().toString()) / 2.205;
                break;
        }

        //Con la conversión anterior a Kg ahora convertimos ese valor en Kilogramo a la unidad de peso seleccionada.
        switch (pesosSalida.getSelectedItem().toString()) {
            case "Tonelada":
                res = Kg / 1000;
                break;

            case "Kilogramo":
                res = Kg;
                break;

            case "Gramo":
                res = Kg * 1000;
                break;

            case "Onza":
                res = Kg / 6.35;
                break;

            case "Libra":
                res = Kg * 2.205;
                break;
        }

        et_salida.setText(Double.toString(res));
    }
}