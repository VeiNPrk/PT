package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by VNPrk on 27.04.2017.
 */
//заменить на Train
public class ActivityTrainList extends AppCompatActivity implements ActionMode.Callback, MyDialogTwoButtonFragment.TwoDialogListener {

    public final static int RESULT_CODE_TRAIN = 111;

	RecyclerView rvTrainings;
	//Button btnEndTraining;
    TrainingRecyclerAdapter adapter;
	List<ClassTraining> trainings;
    ActionMode actionMode;
    ClassTraining currentTraining;
    View vCurrentImage;
    View vCurrentName;
    View vCurrentLevel;
    View vCurrentNeed;
	DataResCompound dataRes = null;
    View imTransitionView = null;
    View tvTransitionName = null;
    View tvTransitionLevel = null;
    MyDialogTwoButtonFragment twoDialog;

	public static void openActivity(Context context){
        Intent intent = new Intent(context, ActivityTrainList.class);
        context.startActivity(intent);
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinings_list);
		trainings = new ArrayList<ClassTraining>();
        dataRes = DataResCompound.getInstance();
		initViews();
        initData();
        setRecyclerView();
    }

    private void initViews(){
		//btnEndTraining = (Button)findViewById(R.id.btn_end_train);
        rvTrainings = (RecyclerView) findViewById(R.id.rv_trainings);
        twoDialog = new MyDialogTwoButtonFragment();
        Bundle args = new Bundle();
        args.putString(MyDialogTwoButtonFragment.STR_DIALOG_TITTLE, "Повторить тренировку?");
        args.putString(MyDialogTwoButtonFragment.STR_DIALOG_MESSAGE, "Вы уверены что хотите проделать одну тренировку дважды за день, может лучше отдохнуть?");
        args.putString(MyDialogTwoButtonFragment.STR_DIALOG_YESBUT, "ПОВТОРИТЬ");
        args.putString(MyDialogTwoButtonFragment.STR_DIALOG_NOBUT, "ОТДОХНУТЬ");
        twoDialog.setArguments(args);

    }
	
	private void initData(){
        trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).or(ClassTraining_Table.lastTraining.is(2)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		if(trainings.size()==0){
			initStartData();
			trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		}

		for (ClassTraining train : trainings) {

            train.setNeedAttempts(dataRes.getTextRes(train.getIdAttempts()));
            train.setMyAttempts();
		}
    }

    private void refreshData(){
		trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).or(ClassTraining_Table.lastTraining.is(2)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		for (ClassTraining train : trainings) {
			train.setNeedAttempts(dataRes.getTextRes(train.getIdAttempts())/*getString(getResources().getIdentifier(train.getIdAttempts(),"string",getApplicationContext().getPackageName()))*/);
            train.setMyAttempts();
		}
		adapter.setData(trainings);
	}
	
	private void setRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new TrainingRecyclerAdapter(this, trainings, rvClickListener);
        rvTrainings.setLayoutManager(layoutManager);
        rvTrainings.setHasFixedSize(true);
        rvTrainings.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTrainings.addItemDecoration(itemDecoration);
        rvTrainings.setItemAnimator(new DefaultItemAnimator());
    }
	
	private TrainingRecyclerAdapter.TrainingClickListener rvClickListener = new TrainingRecyclerAdapter.TrainingClickListener() {
		@Override
		public void onTrainingClick(View trainImage, View trainName, View trainLevel, View trainNeed, int position){
            if (actionMode != null) {
                myToggleSelection(position,trainImage, trainName, trainLevel);
                return;
            }
			goToTraining(trainings.get(position), trainImage, trainName, trainLevel, trainNeed);
		}
		
		@Override
		public void onTrainingLongClick(View trainImage, View trainName, View trainLevel, int position){
            if (actionMode != null) {
                return;
            }
            actionMode = startActionMode(ActivityTrainList.this);
            myToggleSelection(position,trainImage, trainName, trainLevel);
		}
	};
	
	private void goToTraining(ClassTraining _training, View trainImage, View trainName, View trainLevel, View trainNeed){
		if(!compareTrainingDates(_training) || _training.getLastTraining()==0){
			Intent intent = new Intent(ActivityTrainList.this, ActivityTrainNow.class);
			intent.putExtra(ClassTraining.KEY_TRAINING, _training);
			startActivityTransition(intent, RESULT_CODE_TRAIN, trainImage, trainName, trainLevel, trainNeed);
		}
		else {
            currentTraining=_training;
            vCurrentImage=trainImage;
            vCurrentName=trainName;
            vCurrentLevel=trainLevel;
            vCurrentNeed = trainNeed;
            twoDialog.show(getSupportFragmentManager(), "MyDialog");
		}
    }

    private void startActivityTransition(Intent intent, int requestCode, View trainImage, View trainName, View trainLevel, View trainNeed){
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        Pair.create(trainImage, getString(R.string.activity_image_trans)),
                        Pair.create(trainName, getString(R.string.activity_name_trans)),
                        Pair.create(trainLevel, getString(R.string.activity_level_trans)),
                        Pair.create(trainNeed, getString(R.string.activity_needat_trans)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(intent, requestCode, options.toBundle());
        }
        else
            startActivityForResult(intent, requestCode);
    }
	
	private boolean compareTrainingDates(ClassTraining training){
		boolean tf = false;
		Calendar nowDate = Calendar.getInstance();
		Calendar trainingDate = Calendar.getInstance();
		nowDate.setTime(new Date());
		if(training.getDateTraining()!=null) {
			trainingDate.setTime(training.getDateTraining());
			tf = nowDate.get(Calendar.YEAR) == trainingDate.get(Calendar.YEAR) &&
					nowDate.get(Calendar.DAY_OF_YEAR) == trainingDate.get(Calendar.DAY_OF_YEAR);
		}
		return tf;
        //return false;
	}
	
	private void initStartData(){
		ClassTraining t1 = new ClassTraining(1,1);
		t1.save();
		ClassTraining t2 = new ClassTraining(2,1);
		t2.save();
		ClassTraining t3 = new ClassTraining(3,1);
		t3.save();
		ClassTraining t4 = new ClassTraining(4,1);
		t4.save();
		ClassTraining t5 = new ClassTraining(5,1);
		t5.save();
		ClassTraining t6 = new ClassTraining(6,1);
		t6.save();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==RESULT_CODE_TRAIN /*&& resultCode==RESULT_OK*/)
			refreshData();
		//super.onActivityResult(requestCode, resultCode, data);
	}

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_stat:
                goToStat(adapter.getSelection());
                actionMode.finish();
                return true;
            case R.id.menu_info:
                goToInfo(adapter.getSelection());
                actionMode.finish();
                return true;
            default:
                actionMode.finish();
                return true;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        adapter.clearSelections();
    }

    private void myToggleSelection(int idx, View trainImage, View trainName, View trainLevel) {
        adapter.toggleSelection(idx);
        imTransitionView = trainImage;
        tvTransitionName = trainName;
        tvTransitionLevel = trainLevel;
    }

    private void goToStat(int idx){
        ActivityStatistics.openActivity(this, trainings.get(idx).getTypeTrening());
    }

    private void goToInfo(int idx){
        Intent intent = new Intent(ActivityTrainList.this, ActivityTrainInfo.class);
        intent.putExtra(ClassTraining.KEY_TR_TYPE, trainings.get(idx).getTypeTrening());
        intent.putExtra(ClassTraining.KEY_TR_LVL, trainings.get(idx).getLvlTrening());
        startActivity(intent);
    }

    @Override
    public void yesClicked(DialogFragment dialog) {
        currentTraining.setLastTraining(0);
        currentTraining.save();
        goToTraining(currentTraining, vCurrentImage, vCurrentName, vCurrentLevel, vCurrentNeed);
    }

    @Override
    public void noClicked(DialogFragment dialog) {

    }
}
