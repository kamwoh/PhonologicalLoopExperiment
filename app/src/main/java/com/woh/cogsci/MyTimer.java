package com.woh.cogsci;

import android.os.Handler;

/**
 * Created by woh on 10/05/17.
 */

public abstract class MyTimer {

    private long millisInFuture;
    private long countDownInterval;
    public MyTimer(long pMillisInFuture, long pCountDownInterval) {
        this.millisInFuture = pMillisInFuture*1000;
        this.countDownInterval = pCountDownInterval;
    }

    public abstract void onTick(long millisInFuture);
    public abstract void onFinish();

    public void start()
    {
        final Handler handler = new Handler();
        final Runnable counter = new Runnable(){

            public void run(){
                if(millisInFuture <= 0) {
                    onFinish();
                } else {
                    millisInFuture -= countDownInterval;
                    onTick(millisInFuture);
                    handler.postDelayed(this, countDownInterval);
                }
            }
        };

        handler.postDelayed(counter, countDownInterval);
    }
}
