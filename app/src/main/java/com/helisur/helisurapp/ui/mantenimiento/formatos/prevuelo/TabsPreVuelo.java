package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.helisur.helisurapp.R;
import com.helisur.helisurapp.domain.util.Constants;

public class TabsPreVuelo extends Fragment {
    public static TabLayout tabLayout;
    // @BindView(R.id.viewpager)
    public static ViewPager viewPager;
    public static int int_items = 4;
    public static Context generalContext;
    public static String FRAGMENT = "";
    public View view = null;

    public LinearLayout llBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_tabs_prevuelo, null);
        llBack = (LinearLayout) x.findViewById(R.id.llBack);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        initUI(x);
        return x;
    }

    private void initUI(View x) {
        //   tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        view = x;
        generalContext = getContext();
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        //  final BottomNavigationView navigation = (BottomNavigationView) x.findViewById(R.id.bottom_navigation);
        //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //  SessionUserManager sesion = new SessionUserManager(generalContext);
        //  viewPager.setCurrentItem(sesion.getUserCurrentTabPosition());
        //  viewPager.setEnableSwipe(false);
    }


    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case Constants.TABS_PRE_VUELO.AERONAVE_ANTECEDENTE_REQUERIMIENTO: {
                    return new DatosAeronaveFragment();
                }
                case Constants.TABS_PRE_VUELO.SISTEMAS: {
                    return new TareasFragment();
                }
                case Constants.TABS_PRE_VUELO.ANOTACIONES: {
                    return new PreVueloResponsableFragment();
                }
                case Constants.TABS_PRE_VUELO.FIRMA_RESPONSABLE: {
                    return new PreVueloFirmasFragment();
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


}
