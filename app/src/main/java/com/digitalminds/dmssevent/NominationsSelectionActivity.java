package com.digitalminds.dmssevent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NominationsSelectionActivity extends AppCompatActivity {

    Button btn_formal;
    Button btn_informal;
    Toolbar toolbar;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominations_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Award Nominations");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


         btn_formal = findViewById(R.id.btn_formal);
        btn_informal = findViewById(R.id.btn_informal);

        btn_formal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NominationsSelectionActivity.this, NominationsAwardsActivity.class);
                NominationsAwardsActivity.formalAwards = true;
                startActivity(i);
            }
        });

        btn_informal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NominationsSelectionActivity.this, NominationsAwardsActivity.class);
                NominationsAwardsActivity.formalAwards = false;
                startActivity(i);
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //finishing activity up on click of back arrow button
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
