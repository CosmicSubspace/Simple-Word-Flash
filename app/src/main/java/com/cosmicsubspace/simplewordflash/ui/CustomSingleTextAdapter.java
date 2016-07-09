package com.cosmicsubspace.simplewordflash.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cosmicsubspace.simplewordflash.R;
import com.cosmicsubspace.simplewordflash.internals.Word;
import com.cosmicsubspace.simplewordflash.internals.WordsManager;

/**
 * Created by Chan on 7/7/2016.
 */
public class CustomSingleTextAdapter extends BaseAdapter{

    private LayoutInflater mInflater;

    String[] dataset;

    public CustomSingleTextAdapter(Context c, String[] data) {
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataset=data;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return dataset.length;
    }

    @Override
    public String getItem(int position) {
        return dataset[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void replaceData(String[] dataset){
        this.dataset=dataset;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.single_textview, null);
        ((TextView)convertView.findViewById(R.id.single_tv)).setText(dataset[position]);
        return convertView;
    }


}
