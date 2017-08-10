package com.example.vnprk.prisontrainings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends AppCompatActivity {

	Button btnStartTraining;
	Button btnStatistic;
	Button btnAbout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		initViews();
    }
	
	private void initViews(){
		btnStartTraining = (Button)findViewById(R.id.btn_start_training);
        btnStatistic = (Button)findViewById(R.id.btn_statistic);
		btnAbout = (Button)findViewById(R.id.btn_about);
		setListeners();
    }
	
	private void setListeners() {
        btnStartTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTraining();
            }
        });
        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStatistic();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAbout();
            }
        });
    }
	
	private void startTraining(){
		ActivityTrainList.openActivity(this);
	}
	
	private void toStatistic(){
        ActivityStatistics.openActivity(this);
	}
	
	private void toAbout(){
	}
}
