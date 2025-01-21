package com.helisur.helisurapp.domain.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.helisur.helisurapp.R;

public class TransparentProgressDialog extends Dialog {

    private ImageView iv,iv2;

    // String icName="logo_m";
    String icName="ic_loading_services";
    // String icName="pruebasaaa";
    String icNamerrr="loadingtwo";


    public TransparentProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        //  LinearLayout.LayoutParams parassms = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        wlmp.gravity = Gravity.CENTER;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        //  LinearLayout layout = new LinearLayout(context);
        RelativeLayout layout = new RelativeLayout(context);
        // layout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        iv = new ImageView(context);
        params.gravity=Gravity.CENTER;
        iv.setImageResource(context.getResources().getIdentifier(icName, "drawable", context.getPackageName()));
        layout.addView(iv, params);
        addContentView(layout, params);

      /*  //para poner una imagen abajo del circlo moviendose
        //cambiar arriba tmb tipo de layout a relative sin orientacion - RelativeLayout layout = new RelativeLayout(context)
        iv2 = new ImageView(context);
        iv2.setImageResource(context.getResources().getIdentifier(icNamerrr, "drawable", context.getPackageName()));
        layout.addView(iv2, params);
        if(layout.getParent() != null) {
            ((ViewGroup)layout.getParent()).removeView(layout); // <- fix
        }
        addContentView(layout, params);
        */
    }

    public TransparentProgressDialog(Context context, int resourceIdOfImage) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv = new ImageView(context);
        iv.setImageResource(resourceIdOfImage);
        layout.addView(iv, params);
        addContentView(layout, params);
    }

    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);
        iv.setAnimation(anim);
        iv.startAnimation(anim);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

