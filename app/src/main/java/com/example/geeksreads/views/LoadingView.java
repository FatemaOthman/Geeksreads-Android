package com.example.geeksreads.views;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.geeksreads.R;

public class LoadingView {
    TextView[] allControls;
    FrameLayout FindViewByIdForProgressBarHolder;
    TextView FindViewByIdFromProgressName;
    boolean Initialized = false;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    public LoadingView(TextView[]ObjList, FrameLayout _FindViewByIdForProgressBarHolder, TextView _FindViewByIdFromProgressName)
    {
        allControls = ObjList;
        FindViewByIdForProgressBarHolder = _FindViewByIdForProgressBarHolder;
        FindViewByIdFromProgressName = _FindViewByIdFromProgressName;
        progressBarHolder = FindViewByIdForProgressBarHolder;
        Initialized = true;
    }
    public void Start(String LoadingText)
    {
        for (int i=0; i<allControls.length; i++)
        {
            allControls[i].setEnabled(false);
        }
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        TextView pName = FindViewByIdFromProgressName;
        pName.setText(LoadingText);
    }

    public void Stop ()
    {
        if (!Initialized) return;
        for (int i=0; i < allControls.length; i++) {
            allControls[i].setEnabled(true);
        }
        outAnimation=new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        TextView pName = FindViewByIdFromProgressName;//findViewById(R.id.ProgressName);
        pName.setText("");
    }
}
