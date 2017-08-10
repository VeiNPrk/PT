package com.example.vnprk.prisontrainings;

import android.content.Intent;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by VNPrk on 25.04.2017.
 */
@Table(database = AppDataBase.class)
public class ClassTraining extends BaseModel implements Serializable {
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    private int typeTrening;
    @Column
    private int lvlTrening;
    @Column
    private Date dateTraining;
    @Column(defaultValue = "0")
    private int lastTraining;
    @Column
	private String myStrAttempts = "";
    @Column
    private String oldStrAttempts = "";
	
    ArrayList<Integer> myAttempts = new ArrayList<Integer>();
    ArrayList<Integer> needAttempts= new ArrayList<Integer>();

    String needStrAttempts = "";
    

    public static final String STR_DESCRIPTION = "trening_description_";
    public static final String STR_NAME = "trening_name_";
    public static final String STR_ATTEMPTS = "trening_attempts_";
    public static final String STR_IMAGE = "trening_image_";
    public static final String KEY_TRAINING = "key_training";
    public static final String KEY_TR_TYPE = "key_training_type";
    public static final String KEY_TR_LVL = "key_training_level";

    public ClassTraining(){
	
    }

    public ClassTraining(int _type, int _lvl){
        typeTrening = _type;
        lvlTrening = _lvl;
    }

    public void setTypeTrening(int type){
        typeTrening=type;
    }

    public void setLvlTrening(int lvl){
        lvlTrening=lvl;
    }

    public void setLastTraining(int last){
        lastTraining = last;
    }

    public void setLastTrain(){
        lastTraining = 1;
    }

    public void setDateTraining(Date date){
        dateTraining = date;
    }

    public void setLastDateTraining(){
        dateTraining = new Date();
    }

    public void setNeedAttempts(String _need){
        if(_need != ""){
            needStrAttempts=_need;
            for (String attempts : needStrAttempts.split("-")) {
                try{
                    needAttempts.add(Integer.parseInt(attempts));
                }
                catch (Exception ex){
                    needAttempts.add(0);
                }
            }
        }
    }

	public void setMyAttempts(){
        for (String attempts : myStrAttempts.split("-")) {
            myAttempts.add(Integer.parseInt(attempts));
        }
    }
	
    public void addMyAttempts(int _count){
        myAttempts.add(_count);
        setStrMyAttempts();
    }

    public void setOldStrAttempts(String old){
        oldStrAttempts=old;
    }

    public void setMyStrAttempts(String my){
        myStrAttempts=my;
    }

    public void setStrMyAttempts(){
        if(myAttempts.size() > 0){
            myStrAttempts = "";
            for (int attempts : myAttempts) {
                myStrAttempts = myStrAttempts + attempts + "-";
            }
            myStrAttempts=myStrAttempts.substring(0, myStrAttempts.length()-1);
        }
    }

    public String getIdDescription(){
        return STR_DESCRIPTION + typeTrening + "_" + lvlTrening;
    }

    public String getIdName(){
        return STR_NAME + typeTrening + "_" + lvlTrening;
    }

    public String getIdAttempts(){
        return STR_ATTEMPTS + typeTrening + "_" + lvlTrening;
    }

    public String getIdImage(){
        return STR_IMAGE + typeTrening + "_" + lvlTrening;
    }

	public int getTypeTrening(){
        return typeTrening;
    }
	
    public int getLvlTrening(){
        return lvlTrening;
    }

    public int getLastTraining(){ return lastTraining; }
	
	public Date getDateTraining(){
        return dateTraining;
    }

	public String getStrNeedAttempts(){
		return needStrAttempts;
	}
	
	public String getMyStrAttempts(){
		return myStrAttempts;
	}

    public String getOldStrAttempts(){
        return oldStrAttempts;
    }
    
	public boolean checkAttempts(){
        boolean tf = false;
        /*int result = needStrAttempts.compareTo(myStrAttempts);
        if(result >= 0)
            tf = true;
        else{*/
            if(equalAttempts())
                tf=true;
        //}
        return tf;
    }

    private boolean equalAttempts(){
        int i = 0, k=0;
        boolean tf=false;
        if(myAttempts.size()>=needAttempts.size()) {
            for (int attempts : needAttempts) {
                //myStrAttempts = attempts + "-";
                if(myAttempts.get(i)>=attempts)
                    k++;
                i++;
            }
        }
        if(k>=needAttempts.size())
            tf=true;

        return tf;
    }
}
