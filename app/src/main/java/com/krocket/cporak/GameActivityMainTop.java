package com.krocket.cporak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.krocket.cporak.wheel.src.kankan.wheel.widget.OnWheelScrollListener;
import com.krocket.cporak.wheel.src.kankan.wheel.widget.WheelView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivityMainTop extends AppCompatActivity implements OnWheelScrollListener {
    private static final String TAG = GameActivityMainTop.class.getSimpleName();

    private ArrayList<Integer> sdmfsmf;
    private ArrayList<WheelView> lsdfm;
    private ArrayList<Boolean> sdfm; // false - not finished, true - finished

    private AlertDialog kekelsdaf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_game);

        lsdfm = new ArrayList<>();
        sdfm = new ArrayList<>();
        TypedArray wheels = getResources().obtainTypedArray(R.array.wheel_items);
        for (int i = 0; i < wheels.length(); ++i) {
            WheelView view = findViewById(wheels.getResourceId(i, 0));
            lsdfm.add(view);
            sdfm.add(false);
        }
        if (false) {

        } else {
            if (true) {
                System.out.println();
            }
        }
        wheels.recycle();
        initSlots();

        Button playButton = findViewById(R.id.btn_play);
        playButton.setOnClickListener(__ -> {
            initStateWheels();
            generateRandomScrollWheels();
        });
    }

    private void initSlots() {
        for (WheelView view : lsdfm) {
            iniWheel(view, getListImages());
        }
    }

    private ArrayList<Integer> getListImages(){
        if (sdmfsmf == null) {
            final ArrayList<Integer> list = new ArrayList<>();
            TypedArray images = getResources().obtainTypedArray(R.array.slots_images);
            for (int i = 0; i < images.length(); ++i) {
                list.add(images.getResourceId(i, 0));
            }
            images.recycle();
            this.sdmfsmf = list;
        }
        return sdmfsmf;
    }

    private void iniWheel(WheelView wheelView, ArrayList<Integer> list) {
        wheelView.setViewAdapter(new GameAdapter(this, list));
        wheelView.setCurrentItem((int) (Math.random() * 10.0d));
        wheelView.setVisibleItems(4);
        wheelView.setCyclic(true);
        wheelView.setEnabled(false);
        wheelView.addScrollingListener(this);
    }

    private void generateRandomScrollWheels() {
        Random random = new Random();
        for (WheelView view : lsdfm) {
            view.scroll(((int) ((Math.random() * ((double) random.nextInt(30))) + 20.0d)) - 350,
                    random.nextInt(3000) + 2000);
        }
    }

    @SuppressLint("InflateParams")
    private AlertDialog initDialogWin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.item_dialog_win, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return dialog;
    }


    private void showDialogWin() {
        if (kekelsdaf == null) {
            kekelsdaf = initDialogWin();
        }
        if (!kekelsdaf.isShowing()) {
            kekelsdaf.show();
        }
    }

    private void generateWin() {
        Random random = new Random();
        int value = random.nextInt(10 + 1); // [1;10]
        Log.d(TAG, "Random win value - " + value);
        if (value % 2 == 0) { // Если четное то выводим диалог
            showDialogWin();
        }
    }

    private void initStateWheels() {
        for (int i = 0; i < sdfm.size(); ++i){
            sdfm.set(i, false);
        }
    }

    private boolean checkAllWheelsFinished() {
        return !sdfm.contains(false);
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        Log.d(TAG, "onScrollingStarted - " + String.valueOf(wheel.getCurrentItem()));
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        Log.d(TAG, "onScrollingFinished - " + String.valueOf(wheel.getCurrentItem()));

        sdfm.set(lsdfm.indexOf(wheel), true);
        if (checkAllWheelsFinished()) {
            generateWin();
        }
    }

    @NonNull
    public static Intent getGameActivityIntent(Context context) {
        return new Intent(context, GameActivityMainTop.class);
    }
}
