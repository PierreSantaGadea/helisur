package com.helisur.helisurapp.ui.mantenimiento.formatos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.helisur.helisurapp.R;

public class FilterFormatoAeronaveDialog extends DialogFragment {

    RelativeLayout btnCerrar;
    String message;
    TextView tvMensaje;
    String className = "FilterFormatoAeronaveDialog";
    public OnFilterListener onFilterListener;

    public interface OnFilterListener {
        void filter(String idFlete);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.error_message_dialog, new LinearLayout(getActivity()), false);
        btnCerrar = (RelativeLayout) view.findViewById(R.id.btnCerrar);
        tvMensaje = (TextView) view.findViewById(R.id.tvMensaje);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        init();
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        return builder;
    }

    void init() {
        Bundle bundle = getArguments();
        message = (String) bundle.getSerializable("errorMessage");
        tvMensaje.setText(message);
    }


    void clickListener()
    {
        /*
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilterListener.filter(idFlete);
                getDialog().dismiss();

                dismiss();
            }
        });

         */
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().getAttributes().alpha = 1f;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFilterListener = (OnFilterListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.d(className, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

}

