package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by VNPrk on 27.04.2017.
 */
//заменить на Train
public class ActivityTrainList extends AppCompatActivity implements ActionMode.Callback {

    public final static int RESULT_CODE_TRAIN = 111;

	RecyclerView rvTrainings;
	//Button btnEndTraining;
    TrainingRecyclerAdapter adapter;
	List<ClassTraining> trainings;
    ActionMode actionMode;
	DataResCompound dataRes = null;
    View imTransitionView = null;

	public static void openActivity(Context context){
        Intent intent = new Intent(context, ActivityTrainList.class);
        /*if (!(context instanceof Activity))
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);*/
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

    }
	
	private void initData(){
        trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		//trainings = new Select().from(ClassTraining.class).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		if(trainings.size()==0){
			initStartData();
			trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		}

		for (ClassTraining train : trainings) {

            train.setNeedAttempts(dataRes.getTextRes(train.getIdAttempts())/*getString(getResources().getIdentifier(train.getIdAttempts(),"string",getApplicationContext().getPackageName()))*/);
            train.setMyAttempts();
		}
    }

    private void refreshData(){
		trainings = new Select().from(ClassTraining.class).where(ClassTraining_Table.lastTraining.is(0)).orderBy(ClassTraining_Table.typeTrening, true).queryList();
		for (ClassTraining train : trainings) {
			train.setNeedAttempts(dataRes.getTextRes(train.getIdAttempts())/*getString(getResources().getIdentifier(train.getIdAttempts(),"string",getApplicationContext().getPackageName()))*/);
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
        /*LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.s);
        rvTrainings.setLayoutAnimation(animation);*/
        //rvTrainings.addOnItemTouchListener(this);
    }
	
	private TrainingRecyclerAdapter.TrainingClickListener rvClickListener = new TrainingRecyclerAdapter.TrainingClickListener() {
		@Override
		public void onTrainingClick(View trainImage, int position){
            if (actionMode != null) {
                myToggleSelection(position,trainImage);
                return;
            }
			goToTraining(trainings.get(position), trainImage);
            /*elementView(notes.get(idx),noteImage);*/
		}
		
		@Override
		public void onTrainingLongClick(View trainImage, int position){
            if (actionMode != null) {
                return;
            }
            actionMode = startActionMode(ActivityTrainList.this);
            myToggleSelection(position,trainImage);
		}
	};
	
	private void goToTraining(ClassTraining _training, View trainImage){
		Date nowDate = new Date();
		if(!compareTrainingDates(_training) || _training.getLastTraining()==0){
			Intent intent = new Intent(ActivityTrainList.this, ActivityTrainNow.class);
			intent.putExtra(ClassTraining.KEY_TRAINING, _training);
			startActivityTransition(intent, RESULT_CODE_TRAIN, trainImage);
		}
		else
		{
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_list),"Сегодня вы уже делали данное упражнение, попробуйте завтра", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            View snackbarView = snackbar.getView();
            TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            snackTextView.setTextColor(ContextCompat.getColor(this,R.color.colorIcons));
            snackbar.setDuration(4000); // 8 секунд
            snackbar.show();
			//Toast.makeText(this, "Сегодня вы уже делали данное упражнение, попробуйте завтра", Toast.LENGTH_LONG).show();
		}
		//поставить else
    }

    private void startActivityTransition(Intent intent, int requestCode, View trainImage){
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, trainImage, getString(R.string.activity_image_trans));
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
		if(requestCode==RESULT_CODE_TRAIN && resultCode==RESULT_OK)
			refreshData();
		//super.onActivityResult(requestCode, resultCode, data);
	}

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        /*editItem = menu.findItem(R.id.menu_edit);
        deleteItem = menu.findItem(R.id.menu_delete);
        fab.setVisibility(View.GONE);*/
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
                //editElementView(notes.get(selectedPositions.get(0)));
                actionMode.finish();
                return true;
            case R.id.menu_info:
                goToInfo(adapter.getSelection());
                //deleteElement(selectedPositions);
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
        //fab.setVisibility(View.VISIBLE);
    }

    private void myToggleSelection(int idx, View imageView) {
        adapter.toggleSelection(idx);
        imTransitionView = imageView;
    }

    private void goToStat(int idx){
        ActivityStatistics.openActivity(this, trainings.get(idx).getTypeTrening());
    }

    private void goToInfo(int idx){
        Intent intent = new Intent(ActivityTrainList.this, ActivityTrainInfo.class);
        intent.putExtra(ClassTraining.KEY_TR_TYPE, trainings.get(idx).getTypeTrening());
        intent.putExtra(ClassTraining.KEY_TR_LVL, trainings.get(idx).getLvlTrening());
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        Pair.create((View)imTransitionView, getString(R.string.activity_image_trans)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }
        else
            startActivity(intent);
    }
}
