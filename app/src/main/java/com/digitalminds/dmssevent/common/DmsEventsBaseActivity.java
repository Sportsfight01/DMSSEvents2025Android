package com.digitalminds.dmssevent.common;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.digitalminds.dmssevent.R;

/**
 * DmsEventsBaseActivity.java -
 *
 * @author Jaya Krishna
 * @version 1.0
 * @since 10-03-2017.
 */
public abstract class DmsEventsBaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmss_events_base);
    }

    abstract public void initializeUIElements();

    @Override
    abstract public void onClick(View view);

    abstract public void actionBarSettings();
}
        /*AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmss_events_base);
    }
}*/
