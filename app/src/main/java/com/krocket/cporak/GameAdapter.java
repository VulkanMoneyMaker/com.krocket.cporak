package com.krocket.cporak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.krocket.cporak.wheel.src.kankan.wheel.widget.adapters.AbstractWheelAdapter;

import java.util.ArrayList;


public class GameAdapter extends AbstractWheelAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> mListImages;
    private Context mContext;

    @SuppressLint("WrongConstant")
    public GameAdapter(Context mContext, ArrayList<Integer> list) {
        this.mContext = mContext;
        this.mListImages = list;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService("layout_inflater");
    }

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        return super.getEmptyItem(convertView, parent);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    protected void notifyDataChangedEvent() {
        super.notifyDataChangedEvent();
    }

    @Override
    public int getItemsCount() {
        return mListImages == null ? 0 : mListImages.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        View view = this.mLayoutInflater.inflate(R.layout.item_list, parent, false);
        ((ImageView) view.findViewById(R.id.image)).setImageDrawable(
                ContextCompat.getDrawable(this.mContext, this.mListImages.get(index))
        );
        return view;
    }
}
