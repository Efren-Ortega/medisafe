package com.example.mediasafe.medico;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mediasafe.Login;
import com.example.mediasafe.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpedientePaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpedientePaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout btn_info_personal;
    private ImageView iv_profile;
    private TextView tv_nombre, tv_edad, tv_nss;

    public ExpedientePaciente() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpedientePaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpedientePaciente newInstance(String param1, String param2) {
        ExpedientePaciente fragment = new ExpedientePaciente();
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
        View view = inflater.inflate(R.layout.fragment_expediente_paciente, container, false);

        btn_info_personal = view.findViewById(R.id.btn_info_personal);

        iv_profile = view.findViewById(R.id.iv_profile);
        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_edad = view.findViewById(R.id.tv_edad);
        tv_nss = view.findViewById(R.id.tv_nss);

        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString("nombre");
            String edad = args.getString("edad");
            String nss = args.getString("nss");
            String photo = args.getString("photo");

            tv_nombre.setText(nombre);
            tv_edad.setText(edad);
            tv_nss.setText(nss);
            Glide.with(getActivity())
                    .load(photo)
                    .override(200, 200)
                    .into(iv_profile);
        }


        btn_info_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_info_personal.setBackgroundResource(R.drawable.bg_card);
                abrirInfoPaciente();
            }
        });

        return view;
    }

    private void abrirInfoPaciente() {
        InfoPaciente fragmentoDestino = new InfoPaciente();
        Bundle args = new Bundle();
        args.putString("nss", tv_nss.getText().toString());


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
}