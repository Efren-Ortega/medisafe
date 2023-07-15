package com.example.mediasafe.tools.listTareas;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediasafe.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListTareas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListTareas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btn_add;
    private ViewGroup layout;
    private ScrollView scrollView;
    private EditText tv_tarea;

    public ListTareas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListTareas.
     */
    // TODO: Rename and change types and number of parameters
    public static ListTareas newInstance(String param1, String param2) {
        ListTareas fragment = new ListTareas();
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
        View view =  inflater.inflate(R.layout.fragment_list_tareas, container, false);


        layout = (ViewGroup) view.findViewById(R.id.content);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        tv_tarea = (EditText) view.findViewById(R.id.et_tarea);

        btn_add = (Button) view.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarTarea(view);
            }
        });

        return view;
    }

    public void agregarTarea(View view){
        if(tv_tarea.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Ingrese la Tarea a Agregar", Toast.LENGTH_SHORT).show();
            return;
        }
        agregar();
        tv_tarea.setText("");
        //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void agregar(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int id = R.layout.task;
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        TextView textView = (TextView) relativeLayout.findViewById(R.id.tv_tarea);
        textView.setText(tv_tarea.getText());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setPadding(5, 0, 5, 10);
        relativeLayout.setLayoutParams(params);
        layout.addView(relativeLayout);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

}