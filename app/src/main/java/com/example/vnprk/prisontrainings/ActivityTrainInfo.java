package com.example.vnprk.prisontrainings;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
//import fr.ganfra.materialspinner.MaterialSpinner;

//import com.jaredrummler.materialspinner.MaterialSpinner;

/**
 * Created by VNPrk on 27.04.2017.
 */

public class ActivityTrainInfo extends AppCompatActivity /*implements ViewSwitcher.ViewFactory*/{

    public static final String TYPE_TRENING_KEY="type_key";
    public static final String LVL_TRENING_KEY="lvl_key";
    Animation in;
    Animation out;
    AnimationClass animation;
    AnimationClass animation1;
    AnimationClass animation2;
    AnimationClass animation3;
    DataResCompound dataRes=null;
    ImageView ivTrainIcon;
    TextView tvName;
    TextView tvLevel;
    TextView tvDescript;
    ConstraintLayout clInfo;
    //private TextSwitcher tvDescript;
    //ClassTraining training;
    int isStart=0;
    int typeTrening = -1;
    int lvlTrening = -1;

    //MaterialSpinner spnTrain;
    Spinner spnTrain;

    TextView tv;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treining_info);
        if (getIntent().hasExtra(ClassTraining.KEY_TR_TYPE)) {
            typeTrening = getIntent().getIntExtra(ClassTraining.KEY_TR_TYPE, -1);
            lvlTrening = getIntent().getIntExtra(ClassTraining.KEY_TR_LVL, -1);

            //training = (ClassTraining) getIntent().getSerializableExtra(ClassTraining.KEY_TRAINING);
        }
        dataRes=DataResCompound.getInstance();
        initViews();
        initData();
        setSpinner();
    }

    private void initViews() {
        ivTrainIcon = (ImageView) findViewById(R.id.iv_icon_train);
        tvName = (TextView) findViewById(R.id.tv_tr_name);
        tvLevel = (TextView) findViewById(R.id.tv_tr_level);
        tvDescript = (TextView) findViewById(R.id.tv_tr_descr);
        spnTrain = (Spinner)findViewById(R.id.spn_train);
        //tvDescript.setFactory(this);
        animation = new AnimationClass(this);
        animation1 = new AnimationClass(this);
        animation2 = new AnimationClass(this);
        animation3 = new AnimationClass(this);
        tvDescript.setMovementMethod(new ScrollingMovementMethod());
/*
        in = AnimationUtils.loadAnimation(this,  android.R.anim.slide_in_left);
        out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        tvDescript.setInAnimation(in);
        tvDescript.setOutAnimation(out);*/
        clInfo = (ConstraintLayout)findViewById(R.id.cl_info);
        clInfo.setOnTouchListener(new OnSwipeTouchListener(ActivityTrainInfo.this) {
            @Override
            public void onSwipeLeft() {
                //Toast.makeText(ActivityTrainInfo.this, "LEFT", Toast.LENGTH_LONG).show();
                //super.onSwipeLeft();
				nextLevel();

            }

            @Override
            public void onSwipeRight() {
                //super.onSwipeRight();
                //Toast.makeText(ActivityTrainInfo.this, "RIGHT", Toast.LENGTH_LONG).show();
                // Put your logic here for text visibility and for timer like progress bar for 5 second and setText
				prevLevel();
            }
        });
    }

    private void setSpinner() {
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.trainings, R.layout.spiner_layout);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.trainings, android.R.layout.simple_spinner_dropdown_item);
        spnTrain.setAdapter(adapter);
        //spnTrain.setSelectedIndex(typeTrening-1);
        spnTrain.setSelection(typeTrening-1);
        /*spnTrain.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(isStart>0) {
                    lvlTrening = 1;
                    typeTrening=position+1;
                    alphaViewTraining();
                }
                isStart++;
            }
        });*/
        spnTrain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isStart>0) {
                    lvlTrening = 1;
                    typeTrening=position+1;
                    alphaViewTraining();
                }
                isStart++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData(){
		if(lvlTrening!=-1)
			viewTraining();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TYPE_TRENING_KEY, typeTrening);
        outState.putInt(LVL_TRENING_KEY, lvlTrening);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        typeTrening = savedInstanceState.getInt(TYPE_TRENING_KEY, 1);
        lvlTrening = savedInstanceState.getInt(LVL_TRENING_KEY, -1);
        viewTraining();
        //spnTrain.setSelectedIndex(typeTrening-1);
        spnTrain.setSelection(typeTrening - 1);
    }

    private String getStringId(int type){
        String str = "";
        switch (type) {
            case 1:
                str=ClassTraining.STR_IMAGE;
                break;
            case 2:
                str=ClassTraining.STR_NAME;
                break;
            case 3:
                str=ClassTraining.STR_DESCRIPTION;
                break;
            default:
                str="";
                break;
                //return "";
        }
        str = str + typeTrening + "_" + lvlTrening;
        return str;
    }

    private void nextLevel(){
		if(lvlTrening+1<11){
            lvlTrening++;
            //tvName.startAnimation(AnimationUtils.loadAnimation(this,  R.anim.slide_out_left));
            //setSlideAnimation(tvDescript,1);
            //slideAnimation.SetTextView(tvName);
            //tvName.startAnimation(slideAnimation.getSlideAnimation(1));
			viewTraining(1);
            //tvName.startAnimation(AnimationUtils.loadAnimation(this,  R.anim.slide_in_right));
		}
    }

    private void prevLevel(){
		if(lvlTrening-1>0) {
            lvlTrening--;
            //tvName.startAnimation(AnimationUtils.loadAnimation(this,  R.anim.slide_out_right));
            //setSlideAnimation(tvDescript,0);
            //tvName.startAnimation(slideAnimation.getSlideAnimation(tv0));
            viewTraining(0);
            //tvName.startAnimation(AnimationUtils.loadAnimation(this,  R.anim.slide_in_left));
        }
    }
	
	private void viewTraining(int k){
		//try {
            ivTrainIcon.startAnimation(animation.getSlideAnimation(ivTrainIcon,k,1,dataRes.getDrawableRes(getStringId(1))
                    /*ResourcesCompat.getDrawable(getResources(),
                    getResources().getIdentifier(getStringId(1), "drawable", getApplicationContext().getPackageName()), null)*/));
            /*ivTrainIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                getResources().getIdentifier(getStringId(1), "drawable", getApplicationContext().getPackageName()), null));*/
        /*}
        catch(Exception ex){}*/
        tvName.startAnimation(animation1.getSlideAnimation(tvName,k,0,dataRes.getTextRes(getStringId(2))
                /*getString(getResources().getIdentifier(getStringId(2),"string",getApplicationContext().getPackageName()))*/));
        //tvName.setText();
        tvLevel.startAnimation(animation2.getSlideAnimation(tvLevel,k,0,"Уровень " + lvlTrening));
        tvDescript.startAnimation(animation3.getSlideAnimation(tvDescript,k,0,dataRes.getTextRes(getStringId(3))/*getString(getResources().getIdentifier(getStringId(3),"string",getApplicationContext().getPackageName()))*/));
    }

    private void viewTraining(){
        /*try {*/
            ivTrainIcon.setImageDrawable(dataRes.getDrawableRes(getStringId(1))/*ResourcesCompat.getDrawable(getResources(),
                    getResources().getIdentifier(getStringId(1), "drawable", getApplicationContext().getPackageName()), null)*/);
        /*}
        catch(Exception ex){}*/
        tvName.setText(dataRes.getTextRes(getStringId(2))/*getString(getResources().getIdentifier(getStringId(2),"string",getApplicationContext().getPackageName()))*/);
        tvLevel.setText("Уровень " + lvlTrening);
        tvDescript.setText(dataRes.getTextRes(getStringId(3))/*getString(getResources().getIdentifier(getStringId(3),"string",getApplicationContext().getPackageName()))*/);
    }

    private void alphaViewTraining(){
        //try {
            ivTrainIcon.startAnimation(animation.getAlphaAnimation(ivTrainIcon,1,dataRes.getDrawableRes(getStringId(1))/*ResourcesCompat.getDrawable(getResources(),
                    getResources().getIdentifier(getStringId(1), "drawable", getApplicationContext().getPackageName()), null)*/));
            /*ivTrainIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    getResources().getIdentifier(getStringId(1), "drawable", getApplicationContext().getPackageName()), null));*/
        /*}
        catch(Exception ex){}*/
        tvName.startAnimation(animation1.getAlphaAnimation(tvName,0,dataRes.getTextRes(getStringId(2))
                /*getString(getResources().getIdentifier(getStringId(2),"string",getApplicationContext().getPackageName()))*/));
        tvLevel.startAnimation(animation2.getAlphaAnimation(tvLevel,0,"Уровень " + lvlTrening));
        tvDescript.startAnimation(animation3.getAlphaAnimation(tvDescript,0,dataRes.getTextRes(getStringId(3))/*getString(getResources().getIdentifier(getStringId(3),"string",getApplicationContext().getPackageName()))*/));

        //tvName.setText(getString(getResources().getIdentifier(getStringId(2),"string",getApplicationContext().getPackageName())));
        //tvLevel.setText("Уровень " + lvlTrening);
        //tvDescript.setText(getString(getResources().getIdentifier(getStringId(3),"string",getApplicationContext().getPackageName())));
    }
/*
    private void setSlideAnimation(TextSwitcher switcher, int l_r){
        if(l_r==0){
            in = AnimationUtils.loadAnimation(this,  R.anim.slide_in_left);
            out = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            switcher.setInAnimation(in);
            switcher.setOutAnimation(out);
        }
        else{
            in = AnimationUtils.loadAnimation(this,  R.anim.slide_in_right);
            out = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            switcher.setInAnimation(in);
            switcher.setOutAnimation(out);
        }
    }
    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(70);
        t.setTextColor(Color.RED);
        return t;
    }
*/

    public Animation getSlideAnimation(TextView _tv, int i, String _text){
        tv=_tv;
        text=_text;
        if(i==0){
            in = AnimationUtils.loadAnimation(this,  R.anim.slide_in_left);
            out = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        }
        else{
            in = AnimationUtils.loadAnimation(this,  R.anim.slide_in_right);
            out = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        }

        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setText(text);
                tv.startAnimation(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return out;
    }
}
