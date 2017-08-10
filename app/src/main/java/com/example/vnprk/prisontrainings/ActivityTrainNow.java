package com.example.vnprk.prisontrainings;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;

/**
 * Created by VNPrk on 27.04.2017.
 */

public class ActivityTrainNow extends AppCompatActivity {
	
	public static final String SAVED_TRAINING_KEY="saved_tr_key";
	public static final String COUNT_TRAINING_KEY="saved_count_key";
	
	ClassTraining training;
	DataResCompound dataRes = null;
	TextView tvNameTraining;
	TextView tvLevelTraining;
	TextView tvNeedAtempts;
	TextView tvMyAttempts;
	TextView tvNowAttempts;
	ImageView imTraining;
	ImageView imCheck;
	Button btnNextAttempt;
	Button btnEndAttempts;
	NumberPicker numAttempt;
	
	int countAttempts = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treining_now);
		if (getIntent().hasExtra(ClassTraining.KEY_TRAINING)) {
			training = (ClassTraining) getIntent().getSerializableExtra(ClassTraining.KEY_TRAINING);
			/*int nowLevel = training.getLvlTrening();
			training = new ClassTraining(training.getTypeTrening(), nowLevel);
			training.setLastDateTraining();
			training.save();*/
		}
		dataRes = DataResCompound.getInstance();
		initViews();
		initData();
		setListeners();
    }
	
	private void initViews(){
		imTraining = (ImageView)findViewById(R.id.im_training);
		imCheck = (ImageView)findViewById(R.id.im_check_training);
		//imCheck.setTint(context.getResources().getColor(R.color.colorAccent));
		tvNameTraining = (TextView)findViewById(R.id.tv_tr_name);
		tvLevelTraining = (TextView)findViewById(R.id.tv_tr_level);
		tvNeedAtempts = (TextView)findViewById(R.id.tv_need_attempts);
		tvMyAttempts = (TextView)findViewById(R.id.tv_my_attempts);
		tvNowAttempts = (TextView)findViewById(R.id.tv_now_attempts);
		btnNextAttempt = (Button)findViewById(R.id.btn_next_attempt);
        btnEndAttempts = (Button)findViewById(R.id.btn_end_attempts);
		numAttempt = (NumberPicker)findViewById(R.id.np_num_attempts);
		numAttempt.setMaxValue(100);
		numAttempt.setMinValue(0);
    }
	
	private void initData(){
		imTraining.setImageDrawable(dataRes.getDrawableRes(training.getIdImage())/*ResourcesCompat.getDrawable(getResources(),
				getResources().getIdentifier(training.getIdImage(),"drawable",getApplicationContext().getPackageName()),null)*/);
		tvNameTraining.setText(dataRes.getTextRes(training.getIdName())/*getString(getResources().getIdentifier(training.getIdName(),"string",getApplicationContext().getPackageName()))*/);
		tvLevelTraining.setText("Уровень "+training.getLvlTrening());
		tvNeedAtempts.setText(training.getStrNeedAttempts());
		//tvMyAttempts.setText(training.getMyStrAttempts());
		tvNowAttempts.setText("Текущий подход "+countAttempts);

	}
	
	private void setListeners(){
		btnNextAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAttempt();
            }
        });
		btnEndAttempts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTrening();
            }
        });
		imTraining.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToInfo();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVED_TRAINING_KEY, training);
		outState.putInt(COUNT_TRAINING_KEY, numAttempt.getValue());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		training = (ClassTraining) savedInstanceState.getSerializable(SAVED_TRAINING_KEY);
		tvMyAttempts.setText(training.getMyStrAttempts());
		numAttempt.setValue(savedInstanceState.getInt(COUNT_TRAINING_KEY,0));
		checkLevelTraining();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("dest","Destroy");
    }

    private void goToInfo() {
		Intent intent = new Intent(ActivityTrainNow.this, ActivityTrainInfo.class);
		intent.putExtra(ClassTraining.KEY_TR_TYPE, training.getTypeTrening());
        intent.putExtra(ClassTraining.KEY_TR_LVL, training.getLvlTrening());
		startInfoActivityTransition(intent);
	}

	private void nextAttempt(){
		if(numAttempt.getValue()>0){
			if(countAttempts+1<11){
				training.addMyAttempts(numAttempt.getValue());
				tvMyAttempts.setText(training.getMyStrAttempts());
				checkLevelTraining();
				numAttempt.setValue(0);
				countAttempts++;
				tvNowAttempts.setText("Текущий подход "+countAttempts);
			    training.save();
			}
			else{
				Toast.makeText(this,"Слишком много подходов",Toast.LENGTH_LONG).show();
			}
		}
		else{
			//может заменить на диалог
			Toast.makeText(this,"Вы не сделали ни одного повтореня в текущем подходе",Toast.LENGTH_LONG).show();
		}
	}
	
	private void endTrening(){
		int nowLevel = training.getLvlTrening();
		String strMyAtt = "";
		if(numAttempt.getValue()>0){
			training.addMyAttempts(numAttempt.getValue());
			tvMyAttempts.setText(training.getMyStrAttempts());
		}
		if(training.checkAttempts()){
			if(nowLevel<10)
				nowLevel++;
		}
		else
			strMyAtt=training.getMyStrAttempts();
		training.setLastTrain();
		training.setLastDateTraining();
		training.save();
		ClassTraining newTraining = new ClassTraining(training.getTypeTrening(), nowLevel);
		newTraining.setLastDateTraining();
		newTraining.setOldStrAttempts(strMyAtt);
		newTraining.save();
        //Intent intent = new Intent();
        setResult(RESULT_OK, null);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition();
        else
            finish();
	}
	
	private void checkLevelTraining(){
		if(training.checkAttempts())
			imCheck.setVisibility(View.VISIBLE);
	}

	private void startInfoActivityTransition(Intent intent){
		ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        Pair.create((View)imTraining, getString(R.string.activity_image_trans)),
						Pair.create((View)tvNameTraining, getString(R.string.activity_name_trans)),
						Pair.create((View)tvLevelTraining, getString(R.string.activity_level_trans)));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			startActivity(intent, options.toBundle());
		}
		else
			startActivity(intent);
	}
}