package com.helisur.helisurapp.ui.mantenimiento.formatos.prevuelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.helisur.helisurapp.R;

public class SpinenrItemAeronave extends ArrayAdapter<String> {

    private Context ctx;
    private String[] contentArray;
    private Integer[] imageArray;

    public SpinenrItemAeronave(Context context, int resource, String[] objects,
                          Integer[] imageArray) {
        super(context,  R.layout.spinner_item_aeronave, R.id.lapinga, objects);
        this.ctx = context;
        this.contentArray = objects;
        this.imageArray = imageArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomViewFirst(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item_aeronave, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.lapinga);
        textView.setText(contentArray[position]);

        ImageView imageView = (ImageView)row.findViewById(R.id.aeronaveImagen);
        imageView.setImageResource(imageArray[position]);

        return row;
    }

    public View getCustomViewFirst(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item_first, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.textt);
        textView.setText(contentArray[position]);

        return row;
    }
}