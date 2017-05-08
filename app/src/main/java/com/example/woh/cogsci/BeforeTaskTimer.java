package com.example.woh.cogsci;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by woh on 07/05/17.
 */

public class BeforeTaskTimer extends CountDownTimer {
    private MainActivity mainActivity;
    private TextView countDownLabel;

    public BeforeTaskTimer(int seconds, MainActivity mainActivity) {
        super(seconds*1000,1000);
        this.mainActivity = mainActivity;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        countDownLabel = (TextView) mainActivity.findViewById(R.id.countDownLabel);
        countDownLabel.setText(((int)(millisUntilFinished / 1000)+1) + "...");
    }

    @Override
    public void onFinish() {
        countDownLabel.setText("Go!");
        mainActivity.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            mainActivity.setupTaskDisplayWord();
            }
        }, 1000);
    }
}
