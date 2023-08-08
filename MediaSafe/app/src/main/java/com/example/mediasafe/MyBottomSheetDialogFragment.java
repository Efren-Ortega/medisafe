package com.example.mediasafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public interface OnSaveClickListener {
        void onSaveClicked(String inputText, String id); // Puedes modificar los parámetros según tus necesidades
    }

    public interface OnCancelClickListener {
        void onCancelClicked(String inputText); // Puedes modificar los parámetros según tus necesidades
    }

    private OnSaveClickListener onSaveClickListener;
    private OnCancelClickListener OnCancelClickListener;
    private String id;

    public void setOnSaveClickListener(OnSaveClickListener onSaveClickListener, String id) {
        this.onSaveClickListener = onSaveClickListener;
        this.id = id;
    }

    public void setOnCancelClickListener(OnSaveClickListener onSaveClickListener) {
        this.onSaveClickListener = onSaveClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_dialog, container, false);

        // Obtener referencias a los elementos del layout
        EditText editText = view.findViewById(R.id.editText);
        Button buttonSave = view.findViewById(R.id.buttonSave);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);

        // Acciones al hacer clic en los botones
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes obtener el texto ingresado en el EditText y realizar acciones con él
                String inputText = editText.getText().toString();
                String id = getArguments().getString("id");


                    // Puedes cerrar el modal aquí si es necesario
                    if (onSaveClickListener != null) {
                        onSaveClickListener.onSaveClicked(inputText, id);
                    }

                dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes realizar acciones adicionales si el usuario cancela
                // Puedes cerrar el modal aquí si es necesario
                dismiss();
            }
        });

        return view;
    }
}