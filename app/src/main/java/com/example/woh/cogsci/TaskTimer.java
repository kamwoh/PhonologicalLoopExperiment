package com.example.woh.cogsci;

import android.os.CountDownTimer;

/**
 * Created by woh on 07/05/17.
 */

public class TaskTimer extends CountDownTimer {

    private MainActivity mainActivity;

    public TaskTimer(int second, MainActivity mainActivity) {
        super(second*1000, 1000);
        this.mainActivity = mainActivity;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        mainActivity.setupTaskPromptInput();
    }
}
