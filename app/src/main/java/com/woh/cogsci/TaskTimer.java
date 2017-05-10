package com.woh.cogsci;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by woh on 07/05/17.
 */

public class TaskTimer extends CountDownTimer {

    private MainActivity mainActivity;

    public TaskTimer(int second, MainActivity mainActivity) {
        super((second+1)*1000, 500);
        this.mainActivity = mainActivity;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        TextView timer = (TextView) mainActivity.findViewById(R.id.timerLabel);
        timer.setText((millisUntilFinished/1000)+"");
    }

    @Override
    public void onFinish() {
        mainActivity.setupTaskPromptInput();
    }
}
