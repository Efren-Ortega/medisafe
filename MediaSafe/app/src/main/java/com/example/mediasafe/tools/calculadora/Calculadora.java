package com.example.mediasafe.tools.calculadora;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediasafe.R;

import java.util.regex.Pattern;

public class Calculadora extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_sumar, btn_restar, btn_multiplicar, btn_dividir, btn_potencia, btn_punto, btn_parentesisApertura, btn_parentesisiCierre, btn_clear, btn_Calcular, btn_del;
    private TextView tv_valores;
    private Object Analizador;

    public Calculadora() {
        // Required empty public constructor
    }

    public static Calculadora newInstance(String param1, String param2) {
        Calculadora fragment = new Calculadora();
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

        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);

        btn_1 = (Button) view.findViewById(R.id.btn_1);
        btn_2=(Button) view.findViewById(R.id.btn_2);
        btn_3 = (Button) view.findViewById(R.id.btn_3);
        btn_4=(Button) view.findViewById(R.id.btn_4);
        btn_5 = (Button) view.findViewById(R.id.btn_5);
        btn_6=(Button) view.findViewById(R.id.btn_6);
        btn_7 = (Button) view.findViewById(R.id.btn_7);
        btn_8=(Button) view.findViewById(R.id.btn_8);
        btn_9=(Button) view.findViewById(R.id.btn_9);
        btn_0=(Button) view.findViewById(R.id.btn_0);

        btn_sumar=(Button) view.findViewById(R.id.btn_sumar);
        btn_restar=(Button) view.findViewById(R.id.btn_restar);
        btn_multiplicar=(Button) view.findViewById(R.id.btn_multiplicar);
        btn_dividir=(Button) view.findViewById(R.id.btn_dividir);
        btn_potencia = (Button) view.findViewById(R.id.btn_potencia);
        btn_parentesisApertura=(Button) view.findViewById(R.id.btn_parentesis_apertura);
        btn_parentesisiCierre=(Button) view.findViewById(R.id.btn_parentesis_cierre);
        btn_punto=(Button) view.findViewById(R.id.btn_punto);
        btn_clear = (Button) view.findViewById(R.id.btn_limpiar);
        btn_Calcular = (Button) view.findViewById(R.id.btn_calcular);
        btn_del = (Button) view.findViewById(R.id.btn_DEL);

        tv_valores = (TextView) view.findViewById(R.id.tv_valores);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);


        btn_sumar.setOnClickListener(this);
        btn_restar.setOnClickListener(this);
        btn_dividir.setOnClickListener(this);
        btn_multiplicar.setOnClickListener(this);
        btn_potencia.setOnClickListener(this);

        btn_parentesisApertura.setOnClickListener(this);
        btn_parentesisiCierre.setOnClickListener(this);
        btn_punto.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_Calcular.setOnClickListener(this);
        btn_del.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_0:
                tv_valores.setText(tv_valores.getText() + "0");
                break;

            case R.id.btn_1:
                tv_valores.setText(tv_valores.getText() + "1");
                break;

            case R.id.btn_2:
                tv_valores.setText(tv_valores.getText() + "2");
                break;

            case R.id.btn_3:
                tv_valores.setText(tv_valores.getText() + "3");
                break;

            case R.id.btn_4:
                tv_valores.setText(tv_valores.getText() + "4");
                break;

            case R.id.btn_5:
                tv_valores.setText(tv_valores.getText() + "5");
                break;

            case R.id.btn_6:
                tv_valores.setText(tv_valores.getText() + "6");
                break;

            case R.id.btn_7:
                tv_valores.setText(tv_valores.getText() + "7");
                break;

            case R.id.btn_8:
                tv_valores.setText(tv_valores.getText() + "8");
                break;

            case R.id.btn_9:
                tv_valores.setText(tv_valores.getText() + "9");
                break;

            case R.id.btn_sumar:
                tv_valores.setText(tv_valores.getText() + "+");
                break;

            case R.id.btn_restar:
                tv_valores.setText(tv_valores.getText() + "-");
                break;

            case R.id.btn_dividir:
                tv_valores.setText(tv_valores.getText() + "/");
                break;

            case R.id.btn_potencia:
                tv_valores.setText(tv_valores.getText() + "^");
                break;

            case R.id.btn_multiplicar:
                tv_valores.setText(tv_valores.getText() + "*");
                break;

            case R.id.btn_parentesis_apertura:
                tv_valores.setText(tv_valores.getText() + "(");
                break;

            case R.id.btn_parentesis_cierre:
                tv_valores.setText(tv_valores.getText() + ")");
                break;

            case R.id.btn_punto:
                tv_valores.setText(tv_valores.getText() + ".");
                break;

            case R.id.btn_limpiar:
                tv_valores.setText(null);
                break;

            case R.id.btn_DEL:

                if (tv_valores.getText().length()>0){
                    tv_valores.setText(tv_valores.getText().toString().substring(0, tv_valores.getText().length()-1));
                }
                break;

            case R.id.btn_calcular:

                if(!validarExpresión()) return;

                Analizador analizador = new Analizador();
                analizador.setExpresion(tv_valores.getText().toString());
                String resultado = Float.toString(analizador.evaluar());
                tv_valores.setText(resultado);
                break;
        }
    }

    public boolean validarExpresión(){

        try {
            String regex = "";

            //Validar que no haiga parentesis vacios
            regex = "\\(\\)";
            if(Pattern.matches(regex, tv_valores.getText())) throw new Exception();

            if(!this.EvaluarParentesis()) throw new Exception("Error Desconocido");

            String Expresion = tv_valores.getText().toString();
            String NuevaExpresion = Expresion.replace("(", "").replace(")", "");

            //Antes de Calcular verifico que la expresión ingresada este correcta siguiendo el siguiente patron:
            //( (num{1,}(.num*)) (operador){0,1} ((num{1,}.num*)parentesis*){1,} )*
            regex = "^((([0-9]{1,}(\\.[0-9]{1,}){0,1}))(([\\+|\\-|\\*|\\/|\\^]){1}(([0-9]{1,}(\\.[0-9]{1,}){0,1}))){0,}){1,}$";

            if(!Pattern.matches(regex, NuevaExpresion)) throw new Exception();

            return true;

        }catch (Exception e){
            Toast.makeText(getActivity(), "Verifique que su Expresión sea Correcta", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    //Función la cual verifica que los parentesis esten abiertos y cerrados correctamente
    public boolean EvaluarParentesis(){

        String Expresion  = tv_valores.getText().toString();
        int numParentesisApertura = 0;
        int numParentesisCierre = 0;

        for(int i=0; i<Expresion.length(); i++){
            char caracterActual = Expresion.charAt(i);
            if(caracterActual == '('){
                numParentesisApertura++;
            }else if(caracterActual == ')'){
                numParentesisCierre++;
            }
        }

        if(numParentesisApertura == numParentesisCierre) return true;

        return false;

    }
}