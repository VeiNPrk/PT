package com.example.vnprk.prisontrainings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
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

public class FragmentPageTrainInfo extends Fragment /*implements ViewSwitcher.ViewFactory*/{

    public static final String TYPE_TRENING_KEY="type_key";
    public static final String LVL_TRENING_KEY="lvl_key";
    DataResCompound dataRes=null;
    ImageView ivTrainIcon;
    TextView tvName;
    TextView tvDescript;
	View view;
    int typeTrening = 1;
    int lvlTrening = 1;
	
	static FragmentPageTrainInfo newInstance(int typeTrening, int lvlTrening) {
        FragmentPageTrainInfo pageFragment = new FragmentPageTrainInfo();
		Bundle arguments = new Bundle();
		arguments.putInt(ClassTraining.KEY_TR_TYPE, typeTrening);
		arguments.putInt(ClassTraining.KEY_TR_LVL, lvlTrening);
		pageFragment.setArguments(arguments);
		return pageFragment;
	 }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            typeTrening = getArguments().getInt(ClassTraining.KEY_TR_TYPE, 1);
            lvlTrening = getArguments().getInt(ClassTraining.KEY_TR_LVL, 1);
        dataRes=DataResCompound.getInstance();
        
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_treining_info, null);
        setRetainInstance(true);
		initViews();
        initData();
		return view;
	}
	
    private void initViews() {
        ivTrainIcon = (ImageView) view.findViewById(R.id.iv_icon_train);
        tvName = (TextView) view.findViewById(R.id.tv_tr_name);
        tvDescript = (TextView) view.findViewById(R.id.tv_tr_descr);
        tvDescript.setMovementMethod(new ScrollingMovementMethod());
    }

    private void initData(){
		if(lvlTrening!=-1)
			viewTraining();
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

	private void viewTraining(){
        tvDescript.scrollTo(0,0);
            ivTrainIcon.setImageDrawable(dataRes.getDrawableRes(getStringId(1)));
        tvName.setText(dataRes.getTextRes(getStringId(2)));
        //tvLevel.setText(getString(R.string.text_level)+" " + lvlTrening);
        tvDescript.setText(dataRes.getTextRes(getStringId(3)));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(tvDescript!=null && !isVisibleToUser){
            tvDescript.scrollTo(0,0);
            // Your fragment is visible
        }
    }
}
