package com.digitalminds.dmssevent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;

import static com.digitalminds.dmssevent.NominationsAwardsActivity.formalAwards;

/**
 * Created by Jaya.Krishna on 27-02-2018.
 */
public class GuideLinesActivity extends AppCompatActivity {

    LinearLayout guideLinesDetailsLayout, introductionDetailsLayout;
    DmsEventsAppController appController;
    Toolbar toolbar;
    TextView toolbar_title;
    TextView tv_eligibility_header;
    TextView tv_header;
    TextView tv_procedure_sub;
    TextView tv_eligibility_points;
    TextView tv_awards_criteria;

    String textFormalHeader = "Milestone 2020 is the annual event celebrated at Digital Minds. Celebrated in March, it is a gathering to applaud and cheer ourselves for the efforts we put each day.\n" +
            "In order to honour outstanding employees, Digital Minds has introduced the Milestone Awards.";

    String textInformalHeader = "Milestone 2020 is the annual event celebrated at Digital\nMinds in month of March. Informal Awards are Fun\nAwards to recognize few of our collegues with their\nregular habits";

    String textInformalAwards= "The following awards and criteria descriptions are\noffered as guidlines for nominations.";
    String textformalAwards = "The basis for each award selection will be determined by meeting most or all of the criteria for each award. The following awards and criteria descriptions are offered as guidelines for nominations";


    String formalProcedureText = "\\u2022  Nominations once submitted, cannot for  any reason be withdrawn.\\n\\u2022  Nominations that either do not meet the award criteria, or are not in compliance with the procedures listed above will be considered INVALID.\\n\\u2022 Nominees for the awards will be judged on the basis of their performance in advancing the organisation toward its goals.\\n\\u2022 The decision taken by the nominated Panel prevails under any given scenario.";


    String informalProcedureText = "\\u2022  Nominations once submitted, cannot for  any reason be withdrawn.\\n\\u2022  Nominations that either do not meet the award criteria, or are not in compliance with the procedures listed above will be considered INVALID.";

    String formalEligibilityText ="The awards are open to employees of Digital Minds with the below exceptions:";
    String informalEligibilityText = "The awards are open to employees of Digital Minds";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_lines);
        appController = (DmsEventsAppController) getApplicationContext();
        //appController.setIntroductionReadTrueOrFalse(true);
        DmsSharedPreferences.saveIntoductionReadOrNot(GuideLinesActivity.this,true);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        guideLinesDetailsLayout = (LinearLayout) findViewById(R.id.guideLinesDetailsLayout);
        introductionDetailsLayout = (LinearLayout) findViewById(R.id.introductionDetailsLayout);
        tv_awards_criteria = findViewById(R.id.tv_awards_criteria);
        tv_eligibility_header = findViewById(R.id.tv_eligibility_header);
        tv_header = findViewById(R.id.tv_header);
        tv_procedure_sub = findViewById(R.id.tv_procedure_sub);
        tv_eligibility_points = findViewById(R.id.tv_eligibility_points);


        if (formalAwards){
            tv_header.setText(getString(R.string.txt_formal));
            tv_procedure_sub.setText(getString(R.string.txt_formal_procedure));
            tv_eligibility_header.setText(formalEligibilityText);
            tv_eligibility_points.setVisibility(View.VISIBLE);
            tv_awards_criteria.setText(textformalAwards);
        }else {
            tv_header.setText(getString(R.string.txt_informal));
            tv_procedure_sub.setText(getString(R.string.txt_informal_procedure));
            tv_eligibility_header.setText(informalEligibilityText);
            tv_eligibility_points.setVisibility(View.GONE);
            tv_awards_criteria.setText(textInformalAwards);

        }





        if (appController.isGuideLines()) {
            guideLinesDetailsLayout.setVisibility(View.VISIBLE);
            introductionDetailsLayout.setVisibility(View.GONE);
            toolbar_title.setText("Introduction");
        } else {
            guideLinesDetailsLayout.setVisibility(View.GONE);
            introductionDetailsLayout.setVisibility(View.VISIBLE);
            toolbar_title.setText("Introduction");
        }
    }

    /**
     * Toolbar widgets on-click actions.
     *
     * @param item A variable of type MenuItem.
     **/
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
