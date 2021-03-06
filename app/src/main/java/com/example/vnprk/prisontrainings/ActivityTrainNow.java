package com.example.vnprk.prisontrainings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by VNPrk on 27.04.2017.
 */

public class ActivityTrainNow extends AppCompatActivity implements MyDialogFragment.MyDialogListener, MyDialogTwoButtonFragment.TwoDialogListener {
	
	public static final String SAVED_TRAINING_KEY="saved_tr_key";
	public static final String CASH_STR_TRAINING_KEY="saved_cash_str_key";
	public static final String CASH_DATE_TRAINING_KEY="saved_cash_date_key";
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
	Button btnClearAttempts;
	NumberPicker numAttempt;
	MyDialogFragment dialog;
	MyDialogTwoButtonFragment twoDialog;
	
	int countAttempts = 1;
	
	String cashStrMyAttempts = "";
	
	Date cashDateTrening = null;
	
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
		checkLevelTraining();
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
		btnClearAttempts = (Button)findViewById(R.id.btn_clear_result);
		numAttempt = (NumberPicker)findViewById(R.id.np_num_attempts);
		numAttempt.setMaxValue(100);
		numAttempt.setMinValue(0);
		dialog = new MyDialogFragment();
		twoDialog = new MyDialogTwoButtonFragment();
		Bundle args = new Bundle();
		args.putString(MyDialogTwoButtonFragment.STR_DIALOG_TITTLE, getString(R.string.dialog_clear_tittle));
		args.putString(MyDialogTwoButtonFragment.STR_DIALOG_MESSAGE, getString(R.string.dialog_clear_message));
		args.putString(MyDialogTwoButtonFragment.STR_DIALOG_YESBUT, getString(R.string.dialog_clear_yes));
		args.putString(MyDialogTwoButtonFragment.STR_DIALOG_NOBUT, getString(R.string.dialog_clear_no));
		twoDialog.setArguments(args);
    }
	
	private void initData(){
		countAttempts=training.getMyAttempts().size();
		countAttempts++;
		cashStrMyAttempts = training.getMyStrAttempts();
		cashDateTrening = training.getDateTraining();
		imTraining.setImageDrawable(dataRes.getDrawableRes(training.getIdImage())/*ResourcesCompat.getDrawable(getResources(),
				getResources().getIdentifier(training.getIdImage(),"drawable",getApplicationContext().getPackageName()),null)*/);
		tvNameTraining.setText(dataRes.getTextRes(training.getIdName())/*getString(getResources().getIdentifier(training.getIdName(),"string",getApplicationContext().getPackageName()))*/);
		tvLevelTraining.setText(getString(R.string.text_level)+" "+training.getLvlTrening());
		tvNeedAtempts.setText(training.getStrNeedAttempts());
		tvMyAttempts.setText(training.getMyStrAttempts());
		tvNowAttempts.setText(getString(R.string.text_now_attempt)+" "+countAttempts);

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
		btnClearAttempts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				twoDialog.show(getSupportFragmentManager(), "MyDialog");
				//clearResult();
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
		outState.putSerializable(CASH_STR_TRAINING_KEY, cashStrMyAttempts);
		outState.putSerializable(CASH_DATE_TRAINING_KEY, cashDateTrening);
		outState.putInt(COUNT_TRAINING_KEY, numAttempt.getValue());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		training = (ClassTraining) savedInstanceState.getSerializable(SAVED_TRAINING_KEY);
		tvMyAttempts.setText(training.getMyStrAttempts());
		numAttempt.setValue(savedInstanceState.getInt(COUNT_TRAINING_KEY,0));
		cashStrMyAttempts = savedInstanceState.getString(CASH_STR_TRAINING_KEY);
		cashDateTrening = (Date) savedInstanceState.getSerializable(CASH_DATE_TRAINING_KEY);
		checkLevelTraining();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("dest","Destroy");
    }

    private void goToInfo() {
		//Intent intent = new Intent(ActivityTrainNow.this, ActivityTrainInfo.class);
		Intent intent = new Intent(ActivityTrainNow.this, ActivityTrainInfo.class);
		intent.putExtra(ClassTraining.KEY_TR_TYPE, training.getTypeTrening());
        intent.putExtra(ClassTraining.KEY_TR_LVL, training.getLvlTrening());
		startInfoActivityTransition(intent);
	}

	private void nextAttempt(){
		if(numAttempt.getValue()>0){
			if(countAttempts+1<11){
				training.addMyAttempts(numAttempt.getValue());
				training.setLastDateTraining();
				tvMyAttempts.setText(training.getMyStrAttempts());
				checkLevelTraining();
				numAttempt.setValue(0);
				countAttempts++;
				tvNowAttempts.setText(getString(R.string.text_now_attempt)+" "+countAttempts);
			    training.save();
			}
			else{
				showSnackBar(getString(R.string.snack_more_attempt),2700);
			}
		}
		else{
			showSnackBar(getString(R.string.snack_null_attempt),2700);
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
		newTraining.setLastTraining(2);
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

	private void clearResult() {
		training.clearAttempts();
		training.save();
		cashStrMyAttempts="";
		tvMyAttempts.setText("");
		countAttempts=1;
		tvNowAttempts.setText(getString(R.string.text_now_attempt) +" "+countAttempts);
        checkLevelTraining();
	}
	
	private void checkLevelTraining(){
		if(training.checkAttempts()) {
			imCheck.setVisibility(View.VISIBLE);
			showSnackBar(getString(R.string.snack_next_level),2700);
		}
        else
            imCheck.setVisibility(View.INVISIBLE);
	}

	private void startInfoActivityTransition(Intent intent){
		/*ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        Pair.create((View)imTraining, getString(R.string.activity_image_trans)),
						Pair.create((View)tvNameTraining, getString(R.string.activity_name_trans)),
						Pair.create((View)tvLevelTraining, getString(R.string.activity_level_trans)));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			startActivity(intent, options.toBundle());
		}
		else*/
			startActivity(intent);
	}
	
	@Override
	public void onBackPressed()
	{
		if(cashStrMyAttempts.length()!=training.getMyStrAttempts().length())
			dialog.show(getSupportFragmentManager(), "MyDialog");
		else
			super.onBackPressed();
	}

	@Override
	public void onYesClicked(DialogFragment dialog) {
		Log.d("123", "Dialog onYesClicked");
		setResult(RESULT_OK, null);
		super.onBackPressed();
	}

	@Override
	public void onNoClicked(DialogFragment dialog) {
		Log.d("123", "Dialog onNoClicked");
		training.setMyStrAttempts(cashStrMyAttempts);
		training.setMyAttempts();
		training.save();
		setResult(RESULT_OK, null);
		super.onBackPressed();
	}

	@Override
	public void yesClicked(DialogFragment dialog) {
		clearResult();
	}

	@Override
	public void noClicked(DialogFragment dialog) {

	}

	private void showSnackBar(String text, int time)	{
		Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_train_now),text, Snackbar.LENGTH_LONG)
				.setAction("Action", null);
		View snackbarView = snackbar.getView();
		TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
		snackTextView.setTextColor(ContextCompat.getColor(this,R.color.colorIcons));
		snackbar.setDuration(time); // 8 секунд
		snackbar.show();
	}
}
