package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.AlertDialog;
 import android.content.Context;
 import android.view.ViewGroup;
 import android.widget.FrameLayout;
 import android.widget.Toast;



import com.google.android.material.tabs.TabLayout;
import com.helisur.helisurapp.R;
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter;
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaTareaCloudParameter;
import com.helisur.helisurapp.domain.util.ViewPagerNoSwipeable;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina15;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina16;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina17;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina19;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina20;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina21;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina22;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina23;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina24;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina25;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina26;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina31;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina33;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina37;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina38;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina39;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina41;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina43;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina45;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina49;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina50;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina51;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina52;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina53;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina54;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina55;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina56;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina57;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina58;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina59;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina60;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina61;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina62;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina7;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina10;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina11;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina12;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina14;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.paginas.Pagina9;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.IAHelper;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.VoiceRecognitionHelper;
import com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas.SimpleVoiceTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Inspecion100horasTabs extends Fragment {
    public static TabLayout tabLayout;
    // @BindView(R.id.viewpager)
    public static ViewPagerNoSwipeable viewPager;
    public static int int_items = 39;
    public static Context generalContext;
    public static String FRAGMENT = "";
    public View view = null;

    public static GuardaFormatoCloudParameter formatoParameter;

    public static Bitmap firmaResponsable = null;
    public static ArrayList<GuardaTareaCloudParameter> tareasParameter;

    public static String idUsuario;

    public LinearLayout llBack;

    public TextView tituloFDormato;

    public static TextView tvPaginaFormato;
    ImageView btnPdf;

    ImageView btnPregintaAI;
    
    // Helper para reconocimiento de voz
    private VoiceRecognitionHelper voiceHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.tabs_inspeccion_100horas, null);
        llBack = (LinearLayout) x.findViewById(R.id.llBack);
        tituloFDormato = (TextView) x.findViewById(R.id.tvTituloFormato);
        btnPdf = (ImageView) x.findViewById(R.id.btnPdf);
        initUI(x);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 4000);

              */
                //   getActivity().finish();
                showDialog();
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlll = "https://firebasestorage.googleapis.com/v0/b/autoservicio-87532.appspot.com/o/00.-%20Insp.%20100%20horas%20(Mi-171)%20rev.%208%20COMPLETO%20CON%20PND%20%2B%20PM-318.pdf?alt=media&token=18b36c7f-f79a-45ae-ab74-d8201a88c7f6";
                showPdfWithProgressPopup(getContext(),urlll);
            }
        });





        return x;
    }


    public static void showPdfWithProgressPopup(final Context context, final String pdfUrl) {
        // Contenedor principal (programático)
        final LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (8 * context.getResources().getDisplayMetrics().density);
        container.setPadding(pad, pad, pad, pad);

        // Barra y texto de progreso (porcentaje)
        final LinearLayout progressContainer = new LinearLayout(context);
        progressContainer.setOrientation(LinearLayout.HORIZONTAL);
        progressContainer.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams pcParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressContainer.setLayoutParams(pcParams);

        final ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams pbParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        progressBar.setLayoutParams(pbParams);
        progressBar.setMax(100);
        progressBar.setIndeterminate(false);

        final TextView tvProgress = new TextView(context);
        tvProgress.setText("0%");
        tvProgress.setPadding(pad, 0, 0, 0);

        progressContainer.addView(progressBar);
        progressContainer.addView(tvProgress);
        container.addView(progressContainer);

        // ImageView para mostrar la página renderizada (SIN ZOOM)
        final ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (context.getResources().getDisplayMetrics().heightPixels * 0.7f));
        imgParams.topMargin = pad;
        imageView.setLayoutParams(imgParams);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // sin zoom, ajusta centrado
        container.addView(imageView, imgParams);

        // Controles de navegación y texto de página
        LinearLayout controls = new LinearLayout(context);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        controls.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams ctrlParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ctrlParams.topMargin = pad;

        final Button btnPrev = new Button(context);
        btnPrev.setText("Anterior");
        final TextView tvPage = new TextView(context);
        tvPage.setText("0 / 0");
        tvPage.setPadding(pad, 0, pad, 0);
        final Button btnNext = new Button(context);
        btnNext.setText("Siguiente");

        controls.addView(btnPrev);
        controls.addView(tvPage);
        controls.addView(btnNext);
        container.addView(controls, ctrlParams);

        // Dialogo
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(container)
                .setCancelable(true)
                .create();
        dialog.show();

        // AsyncTask para descargar con progreso y luego abrir con PdfRenderer
        new AsyncTask<Void, Integer, File>() {
            Exception error = null;

            @Override
            protected void onPreExecute() {
                progressBar.setProgress(0);
                tvProgress.setText("0%");
                progressBar.setIndeterminate(false);
            }

            @Override
            protected File doInBackground(Void... voids) {
                File output = new File(context.getCacheDir(), "tmp_downloaded_pdf.pdf");
                InputStream input = null;
                FileOutputStream fos = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(pdfUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(20000);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int code = conn.getResponseCode();
                    if (code != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + code);
                    }

                    int contentLength = conn.getContentLength(); // puede ser -1 si desconocido
                    input = conn.getInputStream();
                    if (output.exists()) output.delete();
                    fos = new FileOutputStream(output);
                    byte[] buffer = new byte[4096];
                    int len;
                    long total = 0;
                    while ((len = input.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        total += len;
                        if (contentLength > 0) {
                            int percent = (int) (total * 100 / contentLength);
                            publishProgress(percent);
                        }
                    }
                    fos.flush();
                    return output;
                } catch (Exception e) {
                    error = e;
                    if (output.exists()) output.delete();
                    return null;
                } finally {
                    try { if (input != null) input.close(); } catch (IOException ignored) {}
                    try { if (fos != null) fos.close(); } catch (IOException ignored) {}
                    if (conn != null) conn.disconnect();
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                int p = values.length > 0 ? values[0] : 0;
                progressBar.setProgress(p);
                tvProgress.setText(p + "%");
            }

            @Override
            protected void onPostExecute(final File file) {
                // Ocultar indicadores de progreso tras terminar
                progressBar.setProgress(100);
                tvProgress.setText("100%");
                progressBar.setVisibility(View.GONE);
                tvProgress.setVisibility(View.GONE);

                if (file == null) {
                    Toast.makeText(context, "Error al descargar PDF: " + (error != null ? error.getMessage() : "desconocido"), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }

                try {
                    final ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                    final PdfRenderer renderer = new PdfRenderer(pfd);
                    final int pageCount = renderer.getPageCount();
                    final int[] current = new int[]{0}; // índice de página actual

                    // renderiza la página actual y la muestra en imageView
                    final Runnable renderPage = new Runnable() {
                        @Override
                        public void run() {
                            PdfRenderer.Page page = null;
                            try {
                                page = renderer.openPage(current[0]);
                                DisplayMetrics metrics = context.getResources().getDisplayMetrics();

                                // ancho objetivo = ancho de pantalla * 0.9
                                int targetW = (int) (metrics.widthPixels * 0.9f);
                                float scale = (float) targetW / (float) page.getWidth();
                                int targetH = Math.max(1, (int) (page.getHeight() * scale));

                                Bitmap bmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
                                bmp.eraseColor(Color.WHITE);
                                page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                                imageView.setImageBitmap(bmp);
                                tvPage.setText((current[0] + 1) + " / " + pageCount);
                                btnPrev.setEnabled(current[0] > 0);
                                btnNext.setEnabled(current[0] < pageCount - 1);
                            } catch (Exception e) {
                                Toast.makeText(context, "Error al renderizar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } finally {
                                if (page != null) page.close();
                            }
                        }
                    };

                    // listeners de botones
                    btnPrev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (current[0] > 0) {
                                current[0]--;
                                renderPage.run();
                            }
                        }
                    });

                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (current[0] < pageCount - 1) {
                                current[0]++;
                                renderPage.run();
                            }
                        }
                    });

                    // render inicial
                    renderPage.run();

                    // liberar recursos al cerrar el diálogo
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface d) {
                            renderer.close();
                            try { pfd.close(); } catch (IOException ignored) {}
                            if (file.exists()) file.delete();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(context, "No se puede abrir PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        }.execute();
    }
    private void initUI(View x) {

        //  SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES.FORMATO, Context.MODE_PRIVATE);
        //  tituloFDormato.setText(preferences.getString(Constants.SHARED_PREFERENCES.NOMBRE_FORMATO, ""));
        //   tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPagerNoSwipeable) x.findViewById(R.id.viewpager);
        tvPaginaFormato = (TextView) x.findViewById(R.id.tvPaginaFormato);
        view = x;
        generalContext = getContext();
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        viewPager.setEnableSwipe(true);

        //  int limit = (mSectionsPagerAdapter.getCount() > 1 ? mSectionsPagerAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(4);

    }


    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return new Pagina7();
                }
                case 1: {
                    return new Pagina9();
                }
                case 2: {
                    return new Pagina10();
                }
                case 3: {
                    return new Pagina11();
                }
                case 4: {
                    return new Pagina12();
                }
                case 5: {
                    return new Pagina14();
                }
                case 6: {
                    return new Pagina15();
                }
                case 7: {
                    return new Pagina16();
                }
                case 8: {
                    return new Pagina17();
                }
                case 9: {
                    return new Pagina19();
                }
                case 10: {
                    return new Pagina20();
                }
                case 11: {
                    return new Pagina21();
                }
                case 12: {
                    return new Pagina22();
                }
                case 13: {
                    return new Pagina23();
                }
                case 14: {
                    return new Pagina24();
                }
                case 15: {
                    return new Pagina25();
                }
                case 16: {
                    return new Pagina26();
                }
                case 17: {
                    return new Pagina31();
                }
                case 18: {
                    return new Pagina33();
                }
                case 19: {
                    return new Pagina37();
                }
                case 20: {
                    return new Pagina38();
                }
                case 21: {
                    return new Pagina39();
                }
                case 22: {
                    return new Pagina41();
                }
                case 23: {
                    return new Pagina43();
                }
                case 24: {
                    return new Pagina45();
                }
                case 25: {
                    return new Pagina49();
                }
                case 26: {
                    return new Pagina50();
                }
                case 27: {
                    return new Pagina51();
                }
                case 28: {
                    return new Pagina52();
                }
                case 29: {
                    return new Pagina53();
                }
                case 30: {
                    return new Pagina54();
                }
                case 31: {
                    return new Pagina55();
                }
                case 32: {
                    return new Pagina56();
                }
                case 33: {
                    return new Pagina57();
                }
                case 34: {
                    return new Pagina58();
                }
                case 35: {
                    return new Pagina59();
                }
                case 36: {
                    return new Pagina60();
                }
                case 37: {
                    return new Pagina61();
                }
                case 38: {
                    return new Pagina62();
                }

            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }


    private void showDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_cerrar_formato);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().getAttributes().alpha = 1f;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        RelativeLayout yesBtn = dialog.findViewById(R.id.btnSi);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.dismiss();
                requireActivity().finish();
                //   espera();
                //   requireActivity().finish();
                //dialog.dismiss();

                //         Intent intent = new Intent(getActivity(), ListaPrevuelosRealizadosActivity.class);
                //         startActivity(intent);
            }
        });

        RelativeLayout noBtn = dialog.findViewById(R.id.btnNo);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }





}
