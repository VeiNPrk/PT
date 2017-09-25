package com.example.vnprk.prisontrainings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    AnimationClass animation;
    DataResCompound dataRes=null;
    ConstraintLayout clInfo;
    int isStart=0;
    int typeTrening = 1;
    int lvlTrening = 1;
	static final int PAGE_COUNT = 10;

    Spinner spnTrain;
	ViewPager pager;
	PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_treining_info);
        if (getIntent().hasExtra(ClassTraining.KEY_TR_TYPE)) {
            typeTrening = getIntent().getIntExtra(ClassTraining.KEY_TR_TYPE, 1);
            lvlTrening = getIntent().getIntExtra(ClassTraining.KEY_TR_LVL, 1);
        }
        animation = new AnimationClass(this);
        dataRes=DataResCompound.getInstance();
        clInfo = (ConstraintLayout)findViewById(R.id.cl_info);
		initPagerAdapter();
        initSpinner();
    }

	private void initPagerAdapter()	{
		pager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
	 
		pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	 
		@Override
		public void onPageSelected(int position) {
			lvlTrening = position+1;
		}
	 
		@Override
		public void onPageScrolled(int position, float positionOffset,
			  int positionOffsetPixels) {
		}
	 
		  @Override
		  public void onPageScrollStateChanged(int state) {
		  }
		});
        pager.setCurrentItem(lvlTrening-1);
	}

	private void initSpinner() {
        final int[] size1 = {0};
        spnTrain = (Spinner)findViewById(R.id.spn_train);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.trainings, android.R.layout.simple_spinner_dropdown_item);
        spnTrain.setAdapter(adapter);
        spnTrain.setSelection(typeTrening-1);
        spnTrain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isStart>0) {
                    lvlTrening = 1;
                    typeTrening=position+1;
                    //initPagerAdapter();
                    //size1[0] =getSupportFragmentManager().getFragments().size();
                    getSupportFragmentManager().getFragments().clear();
                    pagerAdapter.notifyDataSetChanged();
                    pager.setCurrentItem(lvlTrening-1);
                    //alphaViewTraining();
					//alphaViewTraining();
                }
                isStart++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TYPE_TRENING_KEY, typeTrening);
        outState.putInt(LVL_TRENING_KEY, lvlTrening);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        typeTrening = savedInstanceState.getInt(TYPE_TRENING_KEY, 1);
        lvlTrening = savedInstanceState.getInt(LVL_TRENING_KEY, 1);
        pager.setCurrentItem(lvlTrening-1);
        spnTrain.setSelection(typeTrening - 1);
    }

    private void alphaViewTraining(){
        clInfo.startAnimation(animation.getAlphaAnimation(pager, pagerAdapter, clInfo, 2, lvlTrening-1));
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

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentPageTrainInfo.newInstance(typeTrening, position+1);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int lvl=position+1;
            return "Уровень " + lvl;
        }

    }
}


