package com.helisur.helisurapp.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.helisur.helisurapp.R;
import com.helisur.helisurapp.domain.util.SessionUserManager;

public class LogoutDialog extends DialogFragment {

  //  RelativeLayout btnCerrar;
    String message;
    RelativeLayout btnNo,btnSi;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_logout, new LinearLayout(getActivity()), false);
        btnSi = (RelativeLayout) view.findViewById(R.id.btnSi);
        btnNo = (RelativeLayout) view.findViewById(R.id.btnNo);
        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        return builder;
    }


    void cerrarSesion()
    {
        SessionUserManager sessionManager = new SessionUserManager(getActivity());
        sessionManager.saveUser("");
        sessionManager.savePass("");
        sessionManager.saveUserId("");
        sessionManager.saveUserNombres("");
        sessionManager.saveUserApellidoPaterno("");
        sessionManager.saveUserApellidoMaterno("");
        sessionManager.saveUserRol("");
        sessionManager.saveUserLogged(false);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        dismiss();

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

}

