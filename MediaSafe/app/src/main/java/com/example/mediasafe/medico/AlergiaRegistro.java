package com.example.mediasafe.medico;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediasafe.MyBottomSheetDialogFragment;
import com.example.mediasafe.R;

public class AlergiaRegistro  extends AppCompatActivity {

    private ImageButton btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alergia_componente);

        btn_edit = (ImageButton) findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Editar", Toast.LENGTH_SHORT).show();

                MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        /*Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);*/


    }
}
