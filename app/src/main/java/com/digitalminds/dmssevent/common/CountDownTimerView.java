package com.digitalminds.dmssevent.common;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;

/**
 * Created by Sandeep.Kumar on 19-02-2018.
 */

public class CountDownTimerView extends LinearLayout {
    Handler handler;
    CountRunnable countRunnable;
    String endtime;
    boolean endtimeoverflag = false;
    Context mContext;
    int position;
    private transient TextView tvCountDays;
    private transient TextView tvCounthours;
    private transient TextView tvCountMinute;
    private transient TextView tvCountSeconds;
    private transient TextView daysTextView;
    private transient TextView hoursTextView;

    public CountDownTimerView(Context context) {
        super(context);
    }

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvCountDays = (TextView ) findViewById(R.id.tvCountDays);
        tvCounthours = (TextView ) findViewById(R.id.tvCounthours);
        tvCountMinute = (TextView ) findViewById(R.id.tvCountMinute);
        tvCountSeconds = (TextView ) findViewById(R.id.tvCountSeconds);
        daysTextView = (TextView ) findViewById(R.id.daysTextView);
        hoursTextView = (TextView ) findViewById(R.id.hoursTextView);
        countRunnable = new CountRunnable();
        handler = new Handler();
    }

    public void setCountDownData() {
        handler.postDelayed(countRunnable, 1000);
    }

    public void setEndTime(String endtime) {
        this.endtime = endtime;
        setCountDownData();
    }

    public boolean getendtimeflag() {
        return endtimeoverflag;
    }



    public void setContext(Context context,int position) {
        this.mContext = context;
        this.position=position;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(countRunnable);
    }

    class CountRunnable implements Runnable {

        @Override
        public void run() {
            String[] countdowntimearray = Utils.getCounterTime(endtime);
            if (Integer.parseInt(countdowntimearray[0])  < 02) {
                daysTextView.setText("Day");
            } else {
                daysTextView.setText("Days");
            }
            if (Integer.parseInt(countdowntimearray[1]) < 02) {
               hoursTextView.setText("Hour");
            } else {
                hoursTextView.setText("Hours");
            }
            if (Integer.parseInt(countdowntimearray[0]) <= 0) {
                tvCountDays.setText("0");
                endtimeoverflag = false;
            } else {
                tvCountDays.setText(countdowntimearray[0]);
                endtimeoverflag = true;
            }

            if (Integer.parseInt(countdowntimearray[1]) <= 0) {
                tvCounthours.setText("0");
                endtimeoverflag = false;
            } else {
                tvCounthours.setText(countdowntimearray[1]);
                endtimeoverflag = true;
            }

            if (Integer.parseInt(countdowntimearray[2]) <= 0) {
                tvCountMinute.setText("0");
                endtimeoverflag = false;
            } else {
                tvCountMinute.setText(countdowntimearray[2]);
                endtimeoverflag = true;
            }

            if (Integer.parseInt(countdowntimearray[3]) <= 0) {
                tvCountSeconds.setText("0");
                endtimeoverflag = false;
            } else {
                tvCountSeconds.setText(countdowntimearray[3]);
                endtimeoverflag = true;
            }

            setCountDownData();
        }
    }
}

