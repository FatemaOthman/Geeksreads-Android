package com.example.geeksreads.views;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.geeksreads.R;

public class LoadingView {
    private TextView[] allControls;
    private FrameLayout FindViewByIdForProgressBarHolder;
    private TextView FindViewByIdFromProgressName;
    private boolean Initialized = false;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private FrameLayout progressBarHolder;

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
        if (!Initialized) return;
        if (allControls != null)
        {
            for (int i=0; i<allControls.length; i++)
            {
                allControls[i].setEnabled(false);
            }
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
        if (allControls != null)
        {
            for (int i=0; i < allControls.length; i++) {
                allControls[i].setEnabled(true);
            }
        }
        outAnimation=new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        TextView pName = FindViewByIdFromProgressName;//findViewById(R.id.ProgressName);
        pName.setText("");
    }
}
